package server.api.service;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import server.api.Person;
import server.api.exception.DuplicateValueException;
import server.util.GsonUtil;

/**
 * @author zacconding
 * @Date 2018-12-03
 * @GitHub : https://github.com/zacscoding
 */
@Service
public class InmemoryPersonService implements PersonService {

    private List<Person> persons;

    private ReadWriteLock lock;
    private Gson gson;

    @PostConstruct
    private void setUp() {
        this.persons = new LinkedList<>();
        this.lock = new ReentrantReadWriteLock();
        this.gson = GsonUtil.GsonFactory.createDefaultGson();
        save(new Person(10, "1", "hiva1", Arrays.asList("coding", "test")));
        save(new Person(15, "2", "hiva2", Arrays.asList("coding", "movie")));
    }

    @Override
    public String save(Person person) {
        if (person == null) {
            return null;
        }

        try {
            lock.writeLock().lock();

            List<Integer> indices = find(p -> p.getId().equals(person.getId()));
            if (!CollectionUtils.isEmpty(indices)) {
                throw new DuplicateValueException(String.format("Duplicate id : %s", person.getId()));
            }

            persons.add(GsonUtil.clone(gson, person));

            return person.getId();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Person findOneById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }

        try {
            lock.readLock().lock();

            List<Integer> indices = find(p -> p.getId().equals(id));
            if (CollectionUtils.isEmpty(indices)) {
                return null;
            }

            return GsonUtil.clone(gson, persons.get(indices.get(0)));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Person> findAll() {
        return GsonUtil.clone(gson, persons);
    }

    @Override
    public int deleteById(String id) {
        if (StringUtils.isEmpty(id)) {
            return 0;
        }

        try {
            lock.readLock().lock();

            List<Integer> indices = find(p -> p.getId().equals(id));
            if (CollectionUtils.isEmpty(indices)) {
                return 0;
            }

            persons.remove(indices.get(0));
            return 1;
        } finally {
            lock.readLock().unlock();
        }
    }

    private List<Integer> find(Predicate<Person> filter) {
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < persons.size(); i++) {
            if (filter.test(persons.get(i))) {
                indices.add(i);
            }
        }

        return indices;
    }
}
