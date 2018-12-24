## Hateos demo  

> #### Basic usage  

```aidl
@Test
public void basic() {
  String url1 = "http://localhost:8080/somthing";
  Link link = new Link(url1);
  assertThat(link.getHref()).isEqualTo(url1);
  assertThat(link.getRel()).isEqualTo(Link.REL_SELF);

  String rel = "my-rel";
  Link link2 = new Link(url1, rel);
  assertThat(link2.getHref()).isEqualTo(url1);
  assertThat(link2.getRel()).isEqualTo(rel);
}
```  

> #### Person  

```aidl
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Builder
@Entity
public class Person {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int age;
    @ElementCollection
    private List<String> hobbies;
}
```  

> #### PersonResource  

```aidl
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class PersonResourceV1 extends Resource<Person> {

    public PersonResourceV1(Person person, Link... links) {
        super(person, links);
    }
}
```  

> #### PersonController  

```aidl
@Slf4j
@RestController
@RequestMapping("/api/v1/person")
public class PersonControllerV1 {

    ...

    @GetMapping("/{id}")
    public ResponseEntity getPerson(@PathVariable("id") Long id) {
        Person person = personRepository.findOneById(id);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }

        PersonResourceV1 resource = new PersonResourceV1(person);
        // adds links
        resource.add(linkTo(methodOn(PersonControllerV1.class).getPerson(person.getId())).withSelfRel());
        resource.add(linkTo(methodOn(PersonControllerV1.class).updatePerson(person.getId(), person)).withRel("update"));
        resource.add(linkTo(methodOn(PersonControllerV1.class).deletePerson(person.getId())).withRel("delete"));

        return ResponseEntity.ok(resource);
    }
```  

> #### get person response  

```aidl
{
  "id": 1,
  "name": "Hiva1",
  "age": 11,
  "hobbies": [
    "coding",
    "movie"
  ],
  "_links": {
    "self": {
      "href": "http://localhost/api/v1/person/1"
    },
    "update": {
      "href": "http://localhost/api/v1/person/update/1"
    },
    "delete": {
      "href": "http://localhost/api/v1/person/1"
    }
  }
}
```  

> #### PersonControllerTests  

```aidl
    @Test
    public void getPerson() throws Exception {
        Person expectedPerson = Person.builder()
            .id(1L)
            .name("Hiva1")
            .age(11)
            .hobbies(Arrays.asList("coding", "movie"))
            .build();
        when(personRepository.findOneById(1L)).thenReturn(expectedPerson);

        mockMvc.perform(get("/api/v1/person/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(expectedPerson.getId()))
            .andExpect(jsonPath("name").value(expectedPerson.getName()))
            .andExpect(jsonPath("age").value(expectedPerson.getAge()))
            .andExpect(jsonPath("hobbies[0]").value(expectedPerson.getHobbies().get(0)))
            .andExpect(jsonPath("hobbies[1]").value(expectedPerson.getHobbies().get(1)))
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.update").exists())
            .andExpect(jsonPath("_links.delete").exists());
    }
```

