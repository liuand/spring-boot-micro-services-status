package org.microservices.reporter.discovery;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Service
public class DiscoveryHearbeatListener implements ApplicationListener<HeartbeatEvent> {

    private static final HeartbeatMonitor heartbeatMonitor = new HeartbeatMonitor();

    private EurekaInstanceDiscoverer discoverer;

    @Override
    public void onApplicationEvent(HeartbeatEvent event) {
        log.trace("Receive hearbeat event");

        tryToDiscover(event.getValue());
    }

    private void tryToDiscover(Object object) {
        if (heartbeatMonitor.update(object)) {
            discoverer.discover();
        }
    }

}
