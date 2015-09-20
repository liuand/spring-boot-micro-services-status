package org.microservices.reporter.notify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BasicMessage {
    private String channel = "#general";
    private String username = "microservices-reporter";
    private String text = "Hello world!";
    @JsonProperty("icon_emoji")
    private String iconEmoji = ":poop:";

    public BasicMessage() {
    }

    public BasicMessage(SlackProperties slackProperties, String text) {
        this.channel = slackProperties.getChannel();
        this.iconEmoji = slackProperties.getIconEmoji();
        this.username = slackProperties.getUserName();
        this.text = text;
    }
}
