# Spring integration tests with maven and docker compose  

this projects is demo for integration tests.  

<a href="#docker-compose-rule-junit4">first tests</a> is using docker-compose-rule-junit4 but dead projects  
(https://github.com/palantir/docker-compose-rule/issues/255)  

<a href="#docker-compose-rule-junit4">second tests</a> is using testcontainers library.

---  

## Integration tests with testcontainers  


   

---  

<div id="docker-compose-rule-junit4"></div>

## Integration tests with docker-compose-rule-junit4

> ## Getting started  

```aidl
$ ./mvnw -B verify
```  

> ## pom.xml  

```aidl
...
    <repositories>
        <repository>
            <id>compose</id>
            <name>compose-rule</name>
            <url>https://dl.bintray.com/palantir/releases</url>
        </repository>
    </repositories>
...

    <dependencies>
        <!-- compose rule -->
        <dependency>
            <groupId>com.palantir.docker.compose</groupId>
            <artifactId>docker-compose-rule-junit4</artifactId>
            <version>0.31.1</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
...
```  

> apply tests  

```aidl
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PersonControllerTest {

    @ClassRule
    public static DockerComposeRule DOCKER_COMPOSE_RULE = DockerComposeRule.builder()
        .file("src/test/resources/compose/postgresql-compose.yaml")
        .waitingForService("postgres_test_db", HealthChecks.toHaveAllPortsOpen())
        .build();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    ObjectMapper objectMapper;
    Random random = new Random();

    @Before
    public void setUp() {
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        personRepository.deleteAll();
    }

    @Test
    public void test_savePerson() throws Exception {
        Person person = Person.builder()
            .name("zaccoding")
            .age(20)
            .hobbies(Arrays.asList("coding", "math"))
            .build();

        mockMvc.perform(post("/api/person")
            .content(objectMapper.writeValueAsString(person))
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
        // TODO : assertion of response
        ;
    }    
    ...
```  
