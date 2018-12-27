package demo.person;

import demo.elasticsearch.ElasticsearchTemplate;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zacconding
 * @Date 2018-12-27
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Service
public class PersonService {

    private RestHighLevelClient client;
    private ElasticsearchTemplate elasticsearchIndexHelper;

    @Autowired
    public PersonService(RestHighLevelClient client, ElasticsearchTemplate elasticsearchIndexHelper) {
        this.client = client;
        this.elasticsearchIndexHelper = elasticsearchIndexHelper;
    }

    /*public Person save(Person person) {

    }*/

    // save

    // update

    // get

    // search

    // delete
}