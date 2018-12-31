package demo.person;

import com.google.gson.Gson;
import demo.elasticsearch.ElasticsearchTemplate;
import demo.util.GsonUtil;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
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
    private ElasticsearchTemplate template;
    private Gson gson;

    @Autowired
    public PersonService(RestHighLevelClient client, ElasticsearchTemplate template) {
        this.client = client;
        this.template = template;
        this.gson = GsonUtil.GsonFactory.createDefaultGson();
    }

    public Person save(Person person) {
        String personJson = gson.toJson(person);

        IndexRequest request = new IndexRequest();
        request.index(template.getPersonsIndex());
        request.type(template.getPersonsType());
        request.source(personJson, XContentType.JSON);

        try {
            String id = client.index(request).getId();
            person.setId(id);
            return person;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*public Person save(Person person) {

    }*/

    // save

    // update

    // get

    // search

    // delete
}
