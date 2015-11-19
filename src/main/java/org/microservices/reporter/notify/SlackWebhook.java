package org.microservices.reporter.notify;

public interface SlackWebhook {
    String getChannel();
    String getUsername();
    String getIconEmoji();
}
