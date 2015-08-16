package org.poop.reporter.notify;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.poop.reporter.domain.Application;
import org.poop.reporter.domain.Status;
import org.poop.reporter.status.ApplicationStatusChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Service
public class SlackNotifier implements ApplicationListener<ApplicationStatusChangedEvent> {

    private SlackClient client;

    @Override
    public void onApplicationEvent(ApplicationStatusChangedEvent event) {
        log.info("Receive app status changed event: {}", event);

        Application app = event.getApplication();
        Status newStatus = event.getNewStatus();

        client.postAttachedMessage(format("Status is %s", newStatus.name()), app.getName(), app.getHealthStatusUrl(), newStatus.getColor());
    }
}
