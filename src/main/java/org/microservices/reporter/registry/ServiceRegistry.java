package org.microservices.reporter.registry;

import com.netflix.appinfo.InstanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.microservices.reporter.domain.Application;
import org.microservices.reporter.domain.StatusInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

import static java.lang.String.format;

@Slf4j
@Service
public class ServiceRegistry implements EnvironmentAware {

    private String appName;

    @Autowired
    private MapStore mapStore;

    @Override
    public void setEnvironment(Environment environment) {
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment, "spring.application.");
        this.appName = propertyResolver.getProperty("name");
    }

    public void register(InstanceInfo instanceInfo) {
        log.debug("Instance info={}", instanceInfo);
        if (shouldRegister(instanceInfo)) {
            doRegister(instanceInfo);
        }

    }

    private boolean shouldRegister(InstanceInfo instanceInfo) {
        return !this.appName.toLowerCase().equals(instanceInfo.getAppName().toLowerCase());
    }

    private void doRegister(InstanceInfo instanceInfo) {
        Application originalApp = createApp(instanceInfo);

        Application app = mapStore.find(originalApp);
        if (app == null) {
            log.info("New app registered: {}", originalApp);
            mapStore.put(originalApp);
        } else {
            log.debug("App refreshed: {}", originalApp);
        }
    }

    private Application createApp(InstanceInfo instanceInfo) {
        return Application.builder()
                .name(instanceInfo.getAppName())
                .uri(generateUri(instanceInfo))
                .statusPageUrl(instanceInfo.getStatusPageUrl())
                .healthStatusUrl(extractFirstHealthCheckUrl(instanceInfo))
                .statusInfo(StatusInfo.unknown())
                .build();
    }

    private String generateUri(InstanceInfo instanceInfo) {
        return format("%s:%s", instanceInfo.getHostName(), instanceInfo.getPort());
    }

    private static String extractFirstHealthCheckUrl(InstanceInfo info) {
        Set<String> healthCheckUrls = info.getHealthCheckUrls();
        if (CollectionUtils.isEmpty(healthCheckUrls)) {
            return null;
        }

        return healthCheckUrls.iterator().next();
    }
}
