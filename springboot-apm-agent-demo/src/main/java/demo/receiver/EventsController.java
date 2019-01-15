package demo.receiver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.InflaterInputStream;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zacconding
 * @Date 2019-01-15
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j(topic = "receiver")
@RestController
public class EventsController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping("/intake/**")
    public ResponseEntity requestAll(HttpServletRequest request, @RequestBody(required = false) byte[] body) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("Request uri : ").append(request.getRequestURI()).append("\n")
                .append("Query string : ").append(request.getQueryString()).append("\n")
                .append("Display body :: ").append("\n");

            List<JsonNode> nodes = readBody(body);
            for (JsonNode node : nodes) {
                builder.append(node.toString()).append("\n");
            }

            log.info("// ---------------------------------------\n{}\n----------------------------------------- //",
                builder);
        } catch (Exception e) {
            log.error("Exception occur", e);
        }

        return ResponseEntity.ok().build();
    }

    private List<JsonNode> readBody(byte[] body) {
        return new BufferedReader(new InputStreamReader(new InflaterInputStream(new ByteArrayInputStream(body))))
            .lines()
            .map(this::readTree)
            .collect(Collectors.toList());
    }

    private JsonNode readTree(String line) {
        try {
            return objectMapper.readTree(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
