package demo.database.leveldb;

import static org.fusesource.leveldbjni.JniDBFactory.factory;

import demo.config.properties.LevelDbProperties;
import demo.database.DbSource;
import demo.exception.DatabaseException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.extern.slf4j.Slf4j;
import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

/**
 * Level db data source
 *
 * modified from ethereumj
 * https://github.com/ethereum/ethereumj/blob/develop/ethereumj-core/src/main/java/org/ethereum/datasource/leveldb/LevelDbDataSource
 */
@Slf4j
public class LevelDbDataSource implements DbSource<byte[]> {

    private String name;
    private DB db;
    private boolean alive;

    private ReadWriteLock resetDbLock = new ReentrantReadWriteLock();
    private LevelDbProperties properties;

    public LevelDbDataSource(LevelDbProperties properties) {
        this.properties = properties;
        this.name = properties.getName();
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
                    db = JniDBFactory.factory.open(dbPath.toFile(), options);
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
        return null;
    }

    @Override
    public void put(byte[] key, byte[] value) {
        try {
            resetDbLock.writeLock().lock();
            if (logger.isTraceEnabled()) {
                logger.trace("Try to put into the {} >> {} - {}", name);
            }
            db.put(key, value);
        } finally {
            resetDbLock.writeLock().unlock();
        }
    }

    @Override
    public byte[] get(byte[] key) {
        return new byte[0];
    }

    @Override
    public void delete(byte[] key) {

    }

    @Override
    public void reset() {

    }
}
