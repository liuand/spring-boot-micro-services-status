package org.poop.reporter.status;

import lombok.Getter;
import lombok.ToString;
import org.poop.reporter.domain.Application;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
@ToString
public class ApplicationsStatusChangedEvent extends ApplicationEvent {

    private List<Application> applications;

    public ApplicationsStatusChangedEvent(StatusUpdateTask statusUpdateTask, List<Application> applications) {
        super(statusUpdateTask);

        this.applications = applications;
    }

}
