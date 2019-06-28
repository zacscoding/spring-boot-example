package demo.demo3;

import java.util.Arrays;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class BidirectionTest {

    @Autowired
    OneNodeRepository repository;
    @Autowired
    TwoNodeRepository twoNodeRepository;

    @Test
    public void temp() {
        TwoNode twoNode1 = TwoNode.builder()
            .name("node11")
            .build();

        TwoNode twoNode2 = TwoNode.builder()
            .name("node12")
            .build();

        OneNode oneNode = OneNode.builder()
            .name("node1")
            .twoNodes(
                Arrays.asList(twoNode1, twoNode2)
            )
            .build();

        twoNode1.setOneNode(oneNode);
        twoNode2.setOneNode(oneNode);

        OneNode save = repository.save(oneNode);

        Optional<TwoNode> foundOptional = twoNodeRepository.findById(twoNode1.getId());
        System.out.println(
            foundOptional.isPresent()
        );

        TwoNode found = foundOptional.get();

        System.out.println(
            foundOptional.get().getOneNode().getName()
        );
    }
}
