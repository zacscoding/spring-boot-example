package demo.leveldb;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.common.MockBlock;
import demo.config.properties.LevelDbProperties;
import demo.database.DbSource;
import demo.database.leveldb.LevelDbDataSource;
import demo.dto.Pair;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class LevelDbDataSourceTest {

    ObjectMapper objectMapper;
    DbSource<byte[]> db;
    LevelDbProperties properties;

    @Before
    public void setUp() {
        properties = LevelDbProperties.builder()
            .name("blockchain")
            .dir(new File("src/test/resources/db/blockchain").getAbsolutePath())
            .build();
        db = new LevelDbDataSource(properties);
        db.init();
        assertThat(db.isAlive()).isTrue();
        objectMapper = new ObjectMapper();
    }

    @After
    public void tearDown() throws IOException {
        if (db.isAlive()) {
            db.close();
        }

        File workingDir = new File(properties.getDir());
        FileUtils.cleanDirectory(workingDir);
        workingDir.delete();
    }

    @Test
    public void testPutGetDelete() {
        Pair<byte[], byte[]> serialized = createMockBlockSerialized();
        MockBlock deserialized = deserializeValue(serialized.getSecond());

        byte[] value = db.get(serialized.getFirst());
        assertThat(value).isNull();

        db.put(serialized.getFirst(), serialized.getSecond());
        value = db.get(serialized.getFirst());
        assertThat(value).isNotNull();
        MockBlock readDeserialized = deserializeValue(value);
        assertThat(deserialized).isEqualTo(readDeserialized);

        db.delete(serialized.getFirst());
        assertThat(db.keys().isEmpty()).isTrue();
    }

    @Test
    public void testUpdateBatch() {
        assertThat(db.keys().isEmpty()).isTrue();

        String chainName = "mainnet";
        MockBlock[] blocks = {
            new MockBlock(1L, "AA"),
            new MockBlock(2L, "BB"),
            new MockBlock(3L, "CC"),
        };

        Map<byte[], byte[]> rows = new HashMap<>();

        for (MockBlock block : blocks) {
            Pair<byte[], byte[]> serialized = serializeBlock(chainName, block);
            rows.put(serialized.getFirst(), serialized.getSecond());
        }

        db.updateBatch(rows);

        assertThat(db.keys().size()).isEqualTo(blocks.length);
        for (MockBlock block : blocks) {
            byte[] key = serializeKey(chainName, block);
            byte[] value = db.get(key);
            MockBlock find = deserializeValue(value);
            assertThat(find).isEqualTo(block);
        }
    }

    @Test
    public void testReset() {
        assertThat(db.keys().isEmpty()).isTrue();

        Pair<byte[], byte[]> serialized = createMockBlockSerialized();
        db.put(serialized.getFirst(), serialized.getSecond());
        assertThat(db.keys().size()).isEqualTo(1);

        db.reset();

        assertThat(db.isAlive()).isTrue();
        assertThat(db.keys().isEmpty()).isTrue();
    }

    @Test
    public void testKeys() {
        Set<byte[]> keys = db.keys();
        assertThat(keys.isEmpty()).isTrue();

        Pair<byte[], byte[]> serialized = createMockBlockSerialized();
        db.put(serialized.getFirst(), serialized.getSecond());
        keys = db.keys();
        assertThat(keys.size()).isEqualTo(1);
        keys.contains(serialized.getFirst());
    }

    private Pair<byte[], byte[]> createMockBlockSerialized() {
        return serializeBlock("chain1",
            MockBlock.builder()
                .blockNumber(1L)
                .hash("AA")
                .build()
        );
    }

    private byte[] serializeKey(String chainName, MockBlock block) {
        String key = chainName + '.' + block.getBlockNumber() + '.' + block.getHash();
        return key.getBytes(StandardCharsets.UTF_8);
    }

    private byte[] serializeValue(MockBlock block) {
        try {
            return objectMapper.writeValueAsBytes(block);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MockBlock deserializeValue(byte[] value) {
        try {
            return objectMapper.readValue(value, MockBlock.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Pair<byte[], byte[]> serializeBlock(String chainName, MockBlock block) {
        try {
            byte[] key = serializeKey(chainName, block);
            byte[] value = serializeValue(block);

            return Pair.newInstance(key, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
