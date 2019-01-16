package demo.slack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.configuration.properties.SlackProperties;
import demo.server.Server;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import me.ramswaroop.jbot.core.slack.models.Attachment;
import me.ramswaroop.jbot.core.slack.models.RichMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * https://github.com/rampatra/jbot/blob/master/jbot-example/src/main/java/example/jbot/slack/SlackWebhooks.java
 */
@Slf4j
@Component
public class SlackWebhooks {

    private SlackProperties slackProperties;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    @Autowired
    public SlackWebhooks(SlackProperties slackProperties, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.slackProperties = slackProperties;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    private void setUp() {
        // invokeSlackWebhook();
        displayServerState();
    }

    public void invokeSlackWebhook() {
        RichMessage richMessage = new RichMessage("Webhook message : " + UUID.randomUUID().toString());
        // set attachments
        Attachment[] attachments = new Attachment[1];
        attachments[0] = new Attachment();
        attachments[0].setText("Some data relevant to your users.");
        richMessage.setAttachments(attachments);

        // For debugging purpose only
        try {
            log.debug("Reply (RichMessage): {}", objectMapper.writeValueAsString(richMessage));
        } catch (JsonProcessingException e) {
            log.debug("Error parsing RichMessage: ", e);
        }

        // Always remember to send the encoded message to Slack
        try {
            log.info("Try to send webhooks");
            restTemplate.postForEntity(
                slackProperties.getSlackIncomingWebhookUrl(),
                richMessage.encodedMessage(),
                String.class
            );
        } catch (RestClientException e) {
            log.error("Error posting to Slack Incoming Webhook: ", e);
        }
    }

    private void displayServerState() {
        RichMessage richMessage = new RichMessage(getServerMessage());
        try {
            log.info("Try to send webhooks");
            restTemplate.postForEntity(
                slackProperties.getSlackIncomingWebhookUrl(),
                richMessage.encodedMessage(),
                String.class
            );
        } catch (RestClientException e) {
            log.error("Error posting to Slack Incoming Webhook: ", e);
        }
    }

    public static void main(String[] args) {
        System.out.println(getServerMessage());
    }

    private static String getServerMessage() {
        List<Server> servers = Arrays.asList(
            Server.builder().serviceName("Service00001").working("ON").lastHeartbeat("2 minute ago").build(),
            Server.builder().serviceName("Service00002").working("OFF").lastHeartbeat("10 minute ago").build(),
            Server.builder().serviceName("Service00003").working("ON").lastHeartbeat("10 sec ago").build()
        );

        String format = "%-20s  /  %-20s  /  %-20s";
        String line = "-----------------------------------------------------------------\n";

        StringBuilder message = new StringBuilder(line);
        message.append(
            String.format(format, "Service name", "Working", "Last heartbeat")
        ).append("\n").append(line);

        for (Server server : servers) {
            message.append(
                String.format(format, server.getServiceName(), server.getWorking(), server.getLastHeartbeat())
            ).append("\n");
            message.append(line);
        }

        return message.toString();
    }
}
