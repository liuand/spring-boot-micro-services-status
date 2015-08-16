package org.poop.reporter.discovery;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import lombok.extern.slf4j.Slf4j;
import org.poop.reporter.registry.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class DiscoveryHearbeatListener implements ApplicationListener<HeartbeatEvent> {

    private final HeartbeatMonitor heartbeatMonitor = new HeartbeatMonitor();

    private DiscoveryClient eurekaDiscoveryClient;
    private ServiceRegistry registry;

    @Autowired
    public DiscoveryHearbeatListener(DiscoveryClient eurekaDiscoveryClient, ServiceRegistry registry) {
        this.eurekaDiscoveryClient = eurekaDiscoveryClient;
        this.registry = registry;
    }

    @Override
    public void onApplicationEvent(HeartbeatEvent event) {
        tryToDiscover(event.getValue());
    }

    private void tryToDiscover(Object object) {
        if (heartbeatMonitor.update(object)) {
            discover();
        }
    }

    private void discover() {
        log.debug("Discovering services");

        Applications applications = eurekaDiscoveryClient.getApplications();
        List<Application> registeredApplications = applications.getRegisteredApplications();

        registeredApplications.stream()
                .map(Application::getInstances)
                .flatMap(Collection::stream)
                .forEach(this::register);
    }

    private void register(InstanceInfo instanceInfo) {
        registry.register(instanceInfo);
    }

}
