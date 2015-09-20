package org.microservices.reporter.notify;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.microservices.reporter.domain.Application;
import org.microservices.reporter.status.ApplicationsStatusChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Service
public class SlackNotifier implements ApplicationListener<ApplicationsStatusChangedEvent> {

    private SlackClient client;

    @Override
    public void onApplicationEvent(ApplicationsStatusChangedEvent event) {
        log.info("Receive app status changed event: {}", event);

        List<Application> apps = event.getApplications();
        AttachmentMessage attachmentMessage = new AttachmentMessage(apps);

        client.postAttachmentMessage(attachmentMessage);
    }
}
