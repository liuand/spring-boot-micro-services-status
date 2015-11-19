package org.microservices.reporter.notify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class BasicMessage implements SlackWebhook {
    private String channel;
    private String username;
    private String text;
    @JsonProperty("icon_emoji")
    private String iconEmoji;

    public BasicMessage(SlackProperties slackProperties, String text) {
        this.channel = slackProperties.getChannel();
        this.iconEmoji = slackProperties.getIconEmoji();
        this.username = slackProperties.getUserName();
        this.text = text;
    }
}
