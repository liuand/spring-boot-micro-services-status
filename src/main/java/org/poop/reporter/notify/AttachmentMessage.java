package org.poop.reporter.notify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AttachmentMessage extends BasicMessage {
    private List<Attachment> attachments = new ArrayList<>();

    public AttachmentMessage(SlackProperties slackProperties, String text, String title, String titleLink, String color) {
        super(slackProperties, null);

        this.attachments.add(new Attachment(text, title, titleLink, color));
    }

    @AllArgsConstructor
    @Getter
    static class Attachment {
        private String text;
        private String title;
        @JsonProperty("title_link")
        private String titleLink;
        private String color;
    }
}
