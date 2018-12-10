package client.api.v1.person;

import client.api.v1.ResponseDTOV1;
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
public class PersonRestTemplateV1 {

    private RestTemplate restTemplate;

    @Autowired
    private PersonRestTemplateV1(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseDTOV1<PersonV1> getPersonById(String accessToken, String id) {
        HttpEntity request = new HttpEntity<>(createAuthHeaders(accessToken));
        try {
            ResponseEntity<ResponseDTOV1<PersonV1>> entity = restTemplate.exchange(
                PersonApiUrlV1.getPersonURI(id), HttpMethod.GET, request, new ParameterizedTypeReference<ResponseDTOV1<PersonV1>>() {});
            log.debug("getPersonById() >> status code : " + entity.getStatusCodeValue());
            return entity.getBody();
        } catch(Exception e) {
            log.warn("Failed to get person.", e);
            throw new RuntimeException(e);
        }
    }

    public ResponseDTOV1<Integer> deletePersonById(String accessToken, String id) {
        HttpEntity request = new HttpEntity<>(createAuthHeaders(accessToken));
        try {
            ResponseEntity<ResponseDTOV1<Integer>> entity = restTemplate.exchange(
                PersonApiUrlV1.deletePersonURI(id), HttpMethod.DELETE, request, new ParameterizedTypeReference<ResponseDTOV1<Integer>>() {});

            log.debug("deletePersonById() >> status code : " + entity.getStatusCodeValue());
            return entity.getBody();
        } catch(Exception e) {
            log.warn("Failed to get person.", e);
            throw new RuntimeException(e);
        }
    }

    public ResponseDTOV1<List<PersonV1>> getPersonAll(String accessToken) {
        HttpEntity request = new HttpEntity<>(createAuthHeaders(accessToken));
        try {
            ResponseEntity<ResponseDTOV1<List<PersonV1>>> entity = restTemplate.exchange(
                PersonApiUrlV1.getPersonAllURI(), HttpMethod.GET, request, new ParameterizedTypeReference<ResponseDTOV1<List<PersonV1>>>() {});

            log.debug("getPersonAll() >> status code : " + entity.getStatusCodeValue());
            return entity.getBody();
        } catch(Exception e) {
            log.warn("Failed to get person.", e);
            throw new RuntimeException(e);
        }
    }

    public ResponseDTOV1<String> savePerson(String accessToken, PersonV1 person) {
        HttpEntity request = new HttpEntity<>(person, createAuthHeaders(accessToken));
        try {
            ResponseEntity<ResponseDTOV1<String>> entity = restTemplate.exchange(
                PersonApiUrlV1.savePersonURI(), HttpMethod.POST, request, new ParameterizedTypeReference<ResponseDTOV1<String>>() {});

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
