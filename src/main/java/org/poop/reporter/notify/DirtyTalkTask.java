package org.poop.reporter.notify;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "poop.reporter.dirty", name = "enable", matchIfMissing = true)
@EnableScheduling
public class DirtyTalkTask {

    private SlackClient client;
    private ShuffleSequence messagesSequence;

    @Autowired
    public DirtyTalkTask(SlackClient client, DirtyTalkProperties properties) {
        this.client = client;
        this.messagesSequence = new ShuffleSequence(properties.getMessages());
    }

    @Scheduled(initialDelayString = "${poop.reporter.dirty.initialDelay:20000}", fixedDelayString = "${poop.reporter.dirty.delay:1800000}")
    public void dirtyTalk() {
        if (!messagesSequence.isEmpty()) {
            doDirtyTak();
        }
    }

    private void doDirtyTak() {
        log.debug("Let's do dirty talk!");

        client.postSimpleMessage(messagesSequence.getNext());
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "poop.reporter.dirty")
    static class DirtyTalkProperties {
        private List<String> messages;
    }

    static class ShuffleSequence {
        private AtomicInteger index = new AtomicInteger(0);
        private List<String> messages;

        public ShuffleSequence(List<String> messages) {
            Collections.shuffle(messages);

            this.messages = messages;
        }

        public String getNext() {
            if (index.get() == messages.size()) {
                index.set(0);
                Collections.shuffle(messages);
            }

            return messages.get(index.getAndIncrement());
        }

        public boolean isEmpty() {
            return CollectionUtils.isEmpty(messages);
        }
    }
}
