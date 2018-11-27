package org.zaccoding.document;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.zaccoding.AbstractTestRunner;

/**
 * @author zacconding
 * @Date 2018-03-01
 * @GitHub : https://github.com/zacscoding
 */
public class HashesTest extends AbstractTestRunner {

    static final String personKey = "person";

    @Resource(name = "redisTemplate")
    HashOperations<String, byte[], byte[]> hashOperations;
    HashMapper<Object, byte[], byte[]> mapper = new ObjectHashMapper();
    HashesTestPerson savedPerson;

    @Before
    public void setUp() {
        savedPerson = new HashesTestPerson("zac", "coding", 19, 0.95D);
        Map<byte[], byte[]> mappedHash = mapper.toHash(savedPerson);
        hashOperations.putAll(personKey, mappedHash);
    }

    @Test
    public void load() {
        HashesTestPerson person = loadHash();
        System.out.println(person);
        assertThat(savedPerson, is(person));
    }

    @Test
    public void hasKey() {
        String[] exists = {"name", "hobby", "age", "score"};
        String[] notExists = {"name2", "hobbies"};

        for (String exist : exists) {
            assertTrue(hashOperations.hasKey(personKey, exist.getBytes()));
        }

        for (String notExist : notExists) {
            assertFalse(hashOperations.hasKey(personKey, notExist.getBytes()));
        }
    }

    @Test
    public void keys() {
        Set<byte[]> keys = hashOperations.keys(personKey);
        Iterator<byte[]> itr = keys.iterator();
        while (itr.hasNext()) {
            System.out.println(new String(itr.next()));
        }
    }

    @Test
    public void values() {
        List<byte[]> values = hashOperations.values(personKey);
        for (byte[] value : values) {
            System.out.println(redisTemplate.getValueSerializer().deserialize(value));
        }
    }

    private HashesTestPerson loadHash() {
        Map<byte[], byte[]> loadedHash = hashOperations.entries(personKey);
        return (HashesTestPerson) mapper.fromHash(loadedHash);
    }
}

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class HashesTestPerson {

    private String name;
    private String hobby;
    private int age;
    private double score;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HashesTestPerson)) {
            return false;
        }
        HashesTestPerson that = (HashesTestPerson) o;
        return this.age == that.age && Double.compare(this.score, that.score) == 0 && Objects.equals(this.name, that.name) && Objects
            .equals(this.hobby, that.hobby);
    }
}
