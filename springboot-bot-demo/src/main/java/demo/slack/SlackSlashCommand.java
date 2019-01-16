package demo.slack;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.ramswaroop.jbot.core.slack.models.Attachment;
import me.ramswaroop.jbot.core.slack.models.RichMessage;
import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * https://github.com/rampatra/jbot/blob/master/jbot-example/src/main/java/example/jbot/slack/SlackSlashCommand.java
 */
@Slf4j
@RestController
public class SlackSlashCommand {

    /**
     * The token you get while creating a new Slash Command. You should paste the token in application.properties file.
     */
    @Value("${slashCommandToken}")
    private String slackToken;


    /**
     * Slash Command handler. When a user types for example "/app help" then slack sends a POST request to this
     * endpoint. So, this endpoint should match the url you set while creating the Slack Slash Command.
     */
    @RequestMapping(value = "/**",
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RichMessage onReceiveSlashCommand(
        @RequestParam("token") String token,
        @RequestParam("team_id") String teamId,
        @RequestParam("team_domain") String teamDomain,
        @RequestParam("channel_id") String channelId,
        @RequestParam("channel_name") String channelName,
        @RequestParam("user_id") String userId,
        @RequestParam("user_name") String userName,
        @RequestParam("command") String command,
        @RequestParam("text") String text,
        @RequestParam("response_url") String responseUrl) {

        log.info("token : {}\nteam_id : {}\nchannel_id : {}\nchannel_name : {}\nuser_id : {}\n"
                + "user_name : {}\ncommand : {} \ntext : {}\nresponse_url : {}\n"
            , token, teamId, teamDomain, channelId, channelName, userId, userName, command, text, responseUrl
        );

        // validate token
        if (!token.equals(slackToken)) {
            return new RichMessage("Sorry! You're not lucky enough to use our slack command.");
        }

        /** build response */
        RichMessage richMessage = new RichMessage("The is Slash Commander!");
        richMessage.setResponseType("in_channel");
        // set attachments
        Attachment[] attachments = new Attachment[1];
        attachments[0] = new Attachment();
        attachments[0].setText("I will perform all tasks for you.");
        richMessage.setAttachments(attachments);

        // For debugging purpose only
        if (log.isDebugEnabled()) {
            try {
                log.debug("Reply (RichMessage): {}", new ObjectMapper().writeValueAsString(richMessage));
            } catch (JsonProcessingException e) {
                log.debug("Error parsing RichMessage: ", e);
            }
        }

        return richMessage.encodedMessage(); // don't forget to send the encoded message to Slack
    }


}
