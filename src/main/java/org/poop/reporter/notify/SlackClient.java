package org.poop.reporter.notify;

import lombok.extern.slf4j.Slf4j;
import org.poop.reporter.rest.JacksonRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class SlackClient {

    @Autowired
    private SlackProperties properties;

    private RestTemplate template = new JacksonRestTemplate();

    public void postSimpleMessage(String text) {
        log.trace("Posting text={} with config={}", text, properties);
        BasicMessage message = new BasicMessage(properties, text);

        postMessage(message);
    }

    public void postAttachmentMessage(AttachmentMessage attachmentMessage) {
        log.trace("Posting attachmnent message={}", attachmentMessage);

        postMessage(attachmentMessage);
    }

    private void postMessage(Object object) {
        template.postForObject(properties.getWebHookUrl(), object, String.class);
    }
}
