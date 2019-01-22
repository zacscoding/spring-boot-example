package crawler;

import java.nio.file.Files;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 *
 */
@Slf4j
public class ParserTest {

    String html;

    @Before
    public void setUp() throws Exception {
        html = Files.readAllLines(new ClassPathResource("samplehtml.txt").getFile().toPath())
            .stream()
            .collect(Collectors.joining(""));
    }

    @Test
    public void temp() {
        Document document = Jsoup.parse(html);
        System.out.println(document.body().toString());

        /*String[] classes = {"Card-RQot", "efjktk"};
        for (String className : classes) {
            Elements elts = document.body().getElementsByClass(className);
            logger.info("Check : {} >> {}", className, elts);
        }*/
    }
}
