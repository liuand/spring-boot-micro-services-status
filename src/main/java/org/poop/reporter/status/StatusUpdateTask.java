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

        mapStore.findAll().stream()
                .forEach(this::updateApplication);
    }

    private void updateApplication(Application originalApp) {
        log.trace("Check for {}", originalApp);

        StatusInfo oldStatus = originalApp.getStatusInfo();
        StatusInfo newStatus = statusInfo.getStatusInfo(originalApp);
        if (!newStatus.equals(oldStatus)) {
            log.info("App has changed status: {} -> {}", oldStatus, newStatus);
            Application appRefreshed = Application.builder()
                    .name(originalApp.getName())
                    .uri(originalApp.getUri())
                    .statusPageUrl(originalApp.getStatusPageUrl())
                    .healthStatusUrl(originalApp.getHealthStatusUrl())
                    .statusInfo(newStatus)
                    .build();
            mapStore.put(appRefreshed);

            ApplicationStatusChangedEvent applicationStatusChangedEvent = new ApplicationStatusChangedEvent(this, appRefreshed,
                    oldStatus, newStatus);
            eventPublisher.publishEvent(applicationStatusChangedEvent);
        }
    }

}
