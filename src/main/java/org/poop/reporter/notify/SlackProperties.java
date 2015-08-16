package org.poop.reporter.notify;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "poop.reporter.slack")
@Data
@ToString
public class SlackProperties {
    private String webHookUrl;
    private String channel = "#general";
    private String userName = "poop-reporter";
    private String iconEmoji = ":poop:";
}
