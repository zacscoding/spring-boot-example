package demo.database.leveldb;

import static org.fusesource.leveldbjni.JniDBFactory.factory;

import demo.config.properties.LevelDbProperties;
import demo.database.DbSource;
import demo.exception.DatabaseException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBException;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.WriteBatch;

/**
 * Level db data source
 *
 * modified from ethereumj
 * https://github.com/ethereum/ethereumj/blob/develop/ethereumj-core/src/main/java/org/ethereum/datasource/leveldb/LevelDbDataSource.java
 */
@Slf4j
public class LevelDbDataSource implements DbSource<byte[]> {

    private final boolean isTraceEnabled;
    private final ReadWriteLock resetDbLock;

    private String name;
    private LevelDbProperties properties;
    private DB db;
    private boolean alive;

    public LevelDbDataSource(LevelDbProperties properties) {
        this.properties = properties;
        name = properties.getName();
        isTraceEnabled = logger.isTraceEnabled();
        resetDbLock = new ReentrantReadWriteLock();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void init() {
        logger.debug("Initialize level db");

        if (properties == null) {
            throw new IllegalStateException("LevelDbProperties must be not null");
        }

        try {
            resetDbLock.writeLock().lock();
            if (isAlive()) {
                logger.debug("skip to initialize level db because already initialized");
                return;
            }

            Options options = new Options();
            options.createIfMissing(true);
            options.compressionType(CompressionType.NONE);
            options.paranoidChecks(true);
            options.verifyChecksums(true);

            options.blockSize(properties.getBlockSize());
            options.writeBufferSize(properties.getWriteBufferSize());
            options.cacheSize(properties.getCacheSize());
            options.maxOpenFiles(properties.getMaxOpenFiles());

            final Path dbPath = Paths.get(properties.getDir());

            try {
                if (!Files.isSymbolicLink(dbPath.getParent())) {
                    Files.createDirectories(dbPath.getParent());
                }

                try {
                    db = factory.open(dbPath.toFile(), options);
                } catch (IOException e) {
                    // database could be corrupted
                    // exception in std out may look:
                    // org.fusesource.leveldbjni.internal.NativeDB$DBException: Corruption: 16 missing files; e.g.: /Users/stan/ethereumj/database-test/block/000026.ldb
                    // org.fusesource.leveldbjni.internal.NativeDB$DBException: Corruption: checksum mismatch
                    if (e.getMessage().contains("Corruption:")) {
                        logger.warn("Problem initializing database.", e);
                        logger.info("LevelDB database must be corrupted. Trying to repair. Could take some time.");
                        factory.repair(dbPath.toFile(), options);
                        logger.info("Repair finished. Opening database again.");
                        db = factory.open(dbPath.toFile(), options);
                    } else {
                        // must be db lock
                        // org.fusesource.leveldbjni.internal.NativeDB$DBException: IO error: lock /Users/stan/ethereumj/database-test/state/LOCK: Resource temporarily unavailable
                        alive = false;
                        throw e;
                    }
                }
                alive = true;
                logger.debug("Success to initialize level db. database dir : {}", properties.getDir());
            } catch (IOException e) {
                logger.warn("IOException occur while create dir : " + properties.getDir());
                alive = false;
                throw new DatabaseException("Cannot initialize database", e);
            }
        } finally {
            resetDbLock.writeLock().unlock();
        }
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public Set<byte[]> keys() throws DatabaseException {
        try {
            resetDbLock.readLock().lock();

            if (isTraceEnabled) {
                logger.trace("~> LevelDbDataSource.keys(): {}", name);
            }

            try (DBIterator iterator = db.iterator()) {
                Set<byte[]> result = new HashSet<>();

                for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                    result.add(iterator.peekNext().getKey());
                }

                if (isTraceEnabled) {
                    logger.trace("<~ LevelDbDataSource.keys(): {}, {}", name, result.size());
                }

                return result;
            } catch (IOException e) {
                logger.error("Unexpected", e);
                throw new RuntimeException(e);
            }
        } finally {
            resetDbLock.readLock().unlock();
        }
    }

    @Override
    public void put(byte[] key, byte[] value) {
        try {
            resetDbLock.writeLock().lock();

            if (isTraceEnabled) {
                logger.trace("Try to put into the {} >> {} - {}", name);
            }

            db.put(key, value);
        } finally {
            resetDbLock.writeLock().unlock();
        }
    }

    @Override
    public void updateBatch(Map<byte[], byte[]> rows) {
        resetDbLock.readLock().lock();
        try {
            if (isTraceEnabled) {
                logger.trace("~> LevelDbDataSource.updateBatch(): {}, {}", name, rows.size());
            }

            try {
                updateBatchInternal(rows);
                if (isTraceEnabled) {
                    logger.trace("<~ LevelDbDataSource.updateBatch(): {}, {}", name, rows.size());
                }
            } catch (Exception e) {
                logger.error("Error, retrying one more time...", e);
                // try one more time
                try {
                    updateBatchInternal(rows);
                    if (isTraceEnabled) {
                        logger.trace("<~ LevelDbDataSource.updateBatch(): {}, {}", name, rows.size());
                    }
                } catch (Exception e1) {
                    logger.error("Error", e);
                    throw new RuntimeException(e);
                }
            }
        } finally {
            resetDbLock.readLock().unlock();
        }
    }

    @Override
    public byte[] get(byte[] key) {
        try {
            resetDbLock.readLock().lock();
            return db.get(key);
        } finally {
            resetDbLock.readLock().unlock();
        }
    }

    @Override
    public void delete(byte[] key) {
        try {
            resetDbLock.writeLock().lock();
            db.delete(key);
        } finally {
            resetDbLock.writeLock().unlock();
        }
    }

    @Override
    public void reset() {
        close();
        try {
            FileUtils.deleteDirectory(Paths.get(properties.getDir()).toFile());
        } catch (IOException e) {
            throw new DBException(e);
        }
        init();
    }

    private void updateBatchInternal(Map<byte[], byte[]> rows) throws IOException {
        try (WriteBatch batch = db.createWriteBatch()) {
            for (Map.Entry<byte[], byte[]> entry : rows.entrySet()) {
                if (entry.getValue() == null) {
                    batch.delete(entry.getKey());
                } else {
                    batch.put(entry.getKey(), entry.getValue());
                }
            }
            db.write(batch);
        }
    }

    @Override
    public void close() {
        resetDbLock.writeLock().lock();
        try {
            if (!isAlive()) {
                return;
            }

            try {
                logger.debug("Close db: {}", name);
                db.close();

                alive = false;
            } catch (IOException e) {
                logger.error("Failed to find the db file on the close: {} ", name);
            }
        } finally {
            resetDbLock.writeLock().unlock();
        }
    }
}
