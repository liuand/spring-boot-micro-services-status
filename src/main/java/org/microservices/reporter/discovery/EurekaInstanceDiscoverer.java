package org.microservices.reporter.discovery;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.microservices.reporter.registry.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class EurekaInstanceDiscoverer {

    private DiscoveryClient discoveryClient;
    private ServiceRegistry registry;

    public void discover() {
        log.debug("Discovering services");

        Applications applications = discoveryClient.getApplications();
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
