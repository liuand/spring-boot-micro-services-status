package org.microservices.reporter.notify;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.microservices.reporter.domain.Application;
import org.microservices.reporter.web.rest.JacksonRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class SlackClient {
    private SlackProperties properties;

    private final RestTemplate template = new JacksonRestTemplate();

    public void postSimpleMessage(String text) {
        log.trace("Posting text={} with config={}", text, properties);
        BasicMessage message = new BasicMessage(properties, text);

        postMessage(message);
    }

    public void postAttachmentMessage(List<Application> apps) {
        AttachmentMessage attachmentMessage = new AttachmentMessage(properties, apps);
        log.trace("Posting attachmnent message={}", attachmentMessage);

        postMessage(attachmentMessage);
    }

    private void postMessage(SlackWebhook slackWebhook) {
        try {
            template.postForObject(properties.getWebHookUrl(), slackWebhook, String.class);
        } catch (RestClientException e) {
            log.error("Failed to send the webhook message: {}", slackWebhook, e);
        }
    }
}
