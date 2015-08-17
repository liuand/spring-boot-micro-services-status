package org.poop.reporter.status;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.poop.reporter.domain.Application;
import org.poop.reporter.domain.StatusInfo;
import org.poop.reporter.registry.MapStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@EnableScheduling
@Service
public class StatusUpdateTask {

    private MapStore mapStore;
    private GetServiceStatus statusInfo;
    private ApplicationEventPublisher eventPublisher;

    @Scheduled(initialDelayString = "${poop.reporter.initialDelay:20000}", fixedRateString = "${poop.reporter.rate:10000}")
    public void updateForAllApplications() {
        log.debug("Check status for applications");

        List<Application> appsWithNewStatus = mapStore.findAll().stream()
            .map(this::getMaybeNewStatusApplication)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toList());

        sendEvent(appsWithNewStatus);
    }

    private Optional<Application> getMaybeNewStatusApplication(Application originalApp) {
        log.trace("Check for {}", originalApp);

        StatusInfo oldStatus = originalApp.getStatusInfo();
        StatusInfo newStatus = statusInfo.getStatusInfo(originalApp);
        if (newStatus.equals(oldStatus)) {
            return Optional.empty();
        }

        log.info("App has changed status: {} -> {}", oldStatus, newStatus);
        Application appRefreshed = Application.builder()
            .name(originalApp.getName())
            .uri(originalApp.getUri())
            .statusPageUrl(originalApp.getStatusPageUrl())
            .healthStatusUrl(originalApp.getHealthStatusUrl())
            .statusInfo(newStatus)
            .build();
        mapStore.put(appRefreshed);

        return Optional.of(appRefreshed);
    }

    private void sendEvent(List<Application> appsWithNewStatus) {
        if (!appsWithNewStatus.isEmpty()) {
            ApplicationsStatusChangedEvent applicationsStatusChangedEvent = new ApplicationsStatusChangedEvent(this, appsWithNewStatus);
            eventPublisher.publishEvent(applicationsStatusChangedEvent);
        }
    }

}
