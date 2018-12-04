package client.api.person;

import client.api.ResponseDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author zacconding
 * @Date 2018-12-04
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
public class PersonRestTemplate {

    private RestTemplate restTemplate;

    @Autowired
    private PersonRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseDTO<Person> getPersonById(String accessToken, String id) {
        HttpEntity request = new HttpEntity<>(createAuthHeaders(accessToken));
        try {
            ResponseEntity<ResponseDTO<Person>> entity = restTemplate.exchange(
                PersonApiUrl.getPersonURI(id), HttpMethod.GET, request, new ParameterizedTypeReference<ResponseDTO<Person>>() {});
            log.debug("getPersonById() >> status code : " + entity.getStatusCodeValue());
            return entity.getBody();
        } catch(Exception e) {
            log.warn("Failed to get person.", e);
            throw new RuntimeException(e);
        }
    }

    public ResponseDTO<Integer> deletePersonById(String accessToken, String id) {
        HttpEntity request = new HttpEntity<>(createAuthHeaders(accessToken));
        try {
            ResponseEntity<ResponseDTO<Integer>> entity = restTemplate.exchange(
                PersonApiUrl.deletePersonURI(id), HttpMethod.DELETE, request, new ParameterizedTypeReference<ResponseDTO<Integer>>() {});

            log.debug("deletePersonById() >> status code : " + entity.getStatusCodeValue());
            return entity.getBody();
        } catch(Exception e) {
            log.warn("Failed to get person.", e);
            throw new RuntimeException(e);
        }
    }

    public ResponseDTO<List<Person>> getPersonAll(String accessToken) {
        HttpEntity request = new HttpEntity<>(createAuthHeaders(accessToken));
        try {
            ResponseEntity<ResponseDTO<List<Person>>> entity = restTemplate.exchange(
                PersonApiUrl.getPersonAllURI(), HttpMethod.GET, request, new ParameterizedTypeReference<ResponseDTO<List<Person>>>() {});

            log.debug("getPersonAll() >> status code : " + entity.getStatusCodeValue());
            return entity.getBody();
        } catch(Exception e) {
            log.warn("Failed to get person.", e);
            throw new RuntimeException(e);
        }
    }

    public ResponseDTO<String> savePerson(String accessToken, Person person) {
        HttpEntity request = new HttpEntity<>(person, createAuthHeaders(accessToken));
        try {
            ResponseEntity<ResponseDTO<String>> entity = restTemplate.exchange(
                PersonApiUrl.savePersonURI(), HttpMethod.POST, request, new ParameterizedTypeReference<ResponseDTO<String>>() {});

            log.debug("savePerson() >> status code : " + entity.getStatusCodeValue());
            return entity.getBody();
        } catch(Exception e) {
            log.warn("Failed to get person.", e);
            throw new RuntimeException(e);
        }
    }

    private HttpHeaders createAuthHeaders(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders() {
        };
        httpHeaders.set("AUTH_TOKEN", accessToken);
        return httpHeaders;
    }
}
