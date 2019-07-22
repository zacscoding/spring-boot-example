package demo.learn;

import static org.assertj.core.api.Assertions.assertThat;

import demo.dto.Pair;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.ReadOptions;
import org.iq80.leveldb.WriteBatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * https://github.com/fusesource/leveldbjni
 */
public class LearnLevelDBTests {

    DB db;

    @Before
    public void setUp() throws Exception {
        File dir = new File("testdb/leveldb");
        FileUtils.deleteDirectory(dir);
        Files.createDirectories(dir.toPath());

        Options options = new Options();
        options.createIfMissing(true);
        db = JniDBFactory.factory.open(new File("testdb/leveldb"), options);
    }

    @After
    public void tearDown() throws Exception {
        if (db != null) {
            db.close();
        }
    }

    @Test
    public void temp() {
        System.out.println(4 << 20);
    }

    @Test
    public void testPuttingGettingDeleting() throws Exception {
        // putting , getting, deleting key/values
        db.put(bytes("key1"), bytes("rocks"));
        byte[] read = db.get(bytes("key1"));
        assertThat(new String(read)).isEqualTo("rocks");
        db.delete(bytes("key1"));
        byte[] read2 = db.get(bytes("key1"));
        assertThat(read2).isNull();
    }

    @Test
    public void testBatchBulkAtomicUpdates() throws Exception {
        db.put(bytes("key0"), bytes("value0"));
        WriteBatch batch = db.createWriteBatch();
        List<Pair<byte[], byte[]>> keyValues = Arrays.asList(
            Pair.newInstance(bytes("key1"), bytes("value1"))
            , Pair.newInstance(bytes("key2"), bytes("value2"))
            , Pair.newInstance(bytes("key3"), bytes("value3"))
        );

        try {
            for (Pair<byte[], byte[]> keyValue : keyValues) {
                batch.put(keyValue.getFirst(), keyValue.getSecond());
            }
            batch.delete(bytes("key0"));
            db.write(batch);
        } finally {
            batch.close();
        }

        assertThat(db.get(bytes("key0"))).isNull();
        assertThat(db.get(bytes("key1"))).isNotNull();
        assertThat(db.get(bytes("key2"))).isNotNull();
        assertThat(db.get(bytes("key3"))).isNotNull();
    }

    @Test
    public void testIteratingKeyValues() throws Exception {
        for (int i = 1; i <= 3; i++) {
            db.put(bytes("key" + i), bytes("value" + i));
        }

        DBIterator iterator = db.iterator();
        try {
            for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                String key = asString(iterator.peekNext().getKey());
                String value = asString(iterator.peekNext().getValue());
                System.out.println(key + " = " + value);
            }
        } finally {
            // Make sure you close the iterator to avoid resource leaks.
            iterator.close();
        }
    }

    @Test
    public void testSnapshot() throws Exception {
        for (int i = 1; i <= 5; i++) {
            db.put(bytes("key" + i), bytes("value" + i));
        }

        ReadOptions readOptions = new ReadOptions();
        readOptions.snapshot(db.getSnapshot());

        try {
            db.put(bytes("key6"), bytes("value6"));
            db.put(bytes("key7"), bytes("value7"));

            for (int i = 1; i <= 5; i++) {
                assertThat(db.get(bytes("key" + i), readOptions)).isNotNull();
            }

            for (int i = 6; i <= 7; i++) {
                assertThat(db.get(bytes("key" + i), readOptions)).isNull();
            }
        } finally {
            readOptions.snapshot().close();
        }
    }

    private String asString(byte[] bytes) {
        return new String(bytes);
    }

    private byte[] bytes(String value) {
        return value.getBytes();
    }
}
