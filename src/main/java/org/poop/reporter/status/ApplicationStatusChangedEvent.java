package org.poop.reporter.status;

import lombok.Getter;
import lombok.ToString;
import org.poop.reporter.domain.Application;
import org.poop.reporter.domain.Status;
import org.poop.reporter.domain.StatusInfo;
import org.springframework.context.ApplicationEvent;

@Getter
@ToString
public class ApplicationStatusChangedEvent extends ApplicationEvent {

    private Application application;
    private StatusInfo oldStatusInfo;
    private StatusInfo newStatusInfo;

    public ApplicationStatusChangedEvent(Object source, Application application, StatusInfo oldStatusInfo, StatusInfo newStatusInfo) {
        super(source);

        this.application = application;
        this.oldStatusInfo = oldStatusInfo;
        this.newStatusInfo = newStatusInfo;
    }

    public Status getNewStatus() {
        return newStatusInfo.getStatus();
    }
}
