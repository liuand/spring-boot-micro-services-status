package org.microservices.reporter.notify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.microservices.reporter.domain.Application;
import org.microservices.reporter.domain.StatusInfo;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@ToString
public class AttachmentMessage extends BasicMessage {
    private List<Attachment> attachments = new ArrayList<>();

    public AttachmentMessage() {
        setText(null);
    }

    public AttachmentMessage(List<Application> apps) {
        this();

        List<AttachmentMessage.Attachment> attachments = apps.stream()
            .map(AttachmentMessage.Attachment::new)
            .collect(toList());

        getAttachments().addAll(attachments);
    }

    @AllArgsConstructor
    @Getter
    @ToString
    static class Attachment {
        private String text;
        private String title;
        @JsonProperty("title_link")
        private String titleLink;
        private String color;

        public Attachment(Application app) {
            StatusInfo statusInfo = app.getStatusInfo();
            this.text = statusInfo.getName();
            this.color = statusInfo.getColor();

            this.title = app.getName();
            this.titleLink = app.getHealthStatusUrl();
        }
    }
}
