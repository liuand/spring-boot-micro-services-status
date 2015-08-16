package org.poop.reporter.notify;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "poop.reporter.lifecycle", name = "notify", matchIfMissing = true)
public class HelloAndGoobyeNotifier implements SmartLifecycle {

    private static final Random RANDOM = new Random();

    private AtomicBoolean running = new AtomicBoolean();

    @Autowired
    private LifecycleMessages messages;

    @Autowired
    private SlackClient client;

    @Override
    public void start() {
        log.info("Poop at report!");

        running.set(true);

        sendMessage(messages.getHelloMessages());
    }

    @Override
    public void stop() {
        log.info("Poop will stop report!");

        sendMessage(messages.getGoodbyeMessages());
    }

    private void sendMessage(List<String> messages) {
        Optional<String> maybeStartMessage = getMessage(messages);
        if (maybeStartMessage.isPresent()) {
            client.postSimpleMessage(maybeStartMessage.get());
        }
    }

    private Optional<String> getMessage(List<String> messages) {
        if (CollectionUtils.isEmpty(messages)) {
            return Optional.empty();
        }

        int messagesSize = messages.size();
        int index = RANDOM.nextInt(messagesSize);
        log.trace("Messages size={}, RandomValue={}", messagesSize, index);
        String text = messages.get(index);

        return Optional.of(text);
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public int getPhase() {
        return 0;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable runnable) {
        stop();
        runnable.run();
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "poop.reporter.lifecycle")
    static class LifecycleMessages {
        private List<String> helloMessages;
        private List<String> goodbyeMessages;
    }
}
