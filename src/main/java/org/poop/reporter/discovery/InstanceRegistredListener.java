package org.poop.reporter.discovery;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class InstanceRegistredListener implements ApplicationListener<InstanceRegisteredEvent> {

    private EurekaInstanceDiscoverer discoverer;

    @Override
    public void onApplicationEvent(InstanceRegisteredEvent instanceRegisteredEvent) {
        discoverer.discover();
    }

}
