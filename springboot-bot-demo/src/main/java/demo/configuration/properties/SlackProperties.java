package demo.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Slack properties
 *
 * @author zacconding
 * @Date 2019-01-16
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Getter
@Setter
@ToString
@Component
@ConditionalOnProperty(name = "slack.enabled", havingValue = "true")
public class SlackProperties {

    private String slackApi;
    private String slackBotToken;
    private String slashCommandToken;
    private String slackIncomingWebhookUrl;

    @Autowired
    public SlackProperties(@Value("${slackApi}") String slackApi,
        @Value("${slackBotToken}") String slackBotToken,
        @Value("${slashCommandToken}") String slashCommandToken,
        @Value("${slackIncomingWebhookUrl}") String slackIncomingWebhookUrl) {

        this.slackApi = slackApi;
        this.slackBotToken = slackBotToken;
        this.slashCommandToken = slashCommandToken;
        this.slackIncomingWebhookUrl = slackIncomingWebhookUrl;
        log.info("## Slack :: {}", this);
    }
}
