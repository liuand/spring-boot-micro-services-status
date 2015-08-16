package org.poop.reporter.notify;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "poop.reporter.dirty", name = "enable", matchIfMissing = true)
@EnableScheduling
public class DirtyTalkTask {

    private static final Random RANDOM = new Random();

    @Autowired
    private SlackClient client;

    @Autowired
    private DirtyTalkProperties properties;

    @Scheduled(initialDelayString = "${poop.reporter.dirty.initialDelay:20000}", fixedDelayString = "${poop.reporter.dirty.delay:1800000}")
    public void dirtyTalk() {
        if (!CollectionUtils.isEmpty(properties.getMessages())) {
            doDirtyTak();
        }
    }

    private void doDirtyTak() {
        log.debug("Let's do dirty talk!");
        List<String> messages = properties.getMessages();

        int index = RANDOM.nextInt(messages.size());
        String message = messages.get(index);

        client.postSimpleMessage(message);
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "poop.reporter.dirty")
    static class DirtyTalkProperties {
        private List<String> messages;
    }
}
