package demo.database;

import demo.exception.DatabaseException;
import java.util.Map;
import java.util.Set;

/**
 * modified from ethereumj
 * https://github.com/ethereum/ethereumj/blob/develop/ethereumj-core/src/main/java/org/ethereum/datasource/DbSource.java
 */
public interface DbSource<V> {

    /**
     * Setting database name for table or file name
     */
    void setName(String name);

    /**
     * Getting database name
     */
    String getName();

    /**
     * initialize database
     */
    void init();

    /**
     * check alive or not
     */
    boolean isAlive();

    /**
     * getting all key sets
     */
    Set<byte[]> keys() throws DatabaseException;

    /**
     * put key value
     */
    void put(byte[] key, V value);

    /**
     * put multiple key values
     */
    void updateBatch(Map<byte[], V> rows);

    /**
     * getting value about key
     */
    V get(byte[] key);

    /**
     * deleting key
     */
    void delete(byte[] key);

    /**
     * reset database
     */
    void reset();

    /**
     * close database
     */
    void close();
}
