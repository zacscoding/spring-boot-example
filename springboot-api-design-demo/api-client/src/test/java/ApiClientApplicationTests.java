import client.ApiClientApplication;
import client.api.ResponseDTO;
import client.api.person.Person;
import client.api.person.PersonRestTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zacconding
 * @Date 2018-12-04
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiClientApplication.class)
public class ApiClientApplicationTests {

    @Autowired
    private PersonRestTemplate restTemplate;
    private String accessToken = "aa";

    @Test
    public void temp() {
        ResponseDTO<Person> result = restTemplate.getPersonById(accessToken, "1");
        System.out.println(result.getData());
    }
}
