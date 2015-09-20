package org.microservices.reporter.status;

import lombok.extern.slf4j.Slf4j;
import org.microservices.reporter.domain.Application;
import org.microservices.reporter.domain.StatusInfo;
import org.microservices.reporter.web.rest.JacksonRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class GetServiceStatus {

    RestTemplate template = new JacksonRestTemplate();

    public StatusInfo getStatusInfo(Application application) {
        log.trace("Getting status for {}", application);

        try {

            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, String>> response = template.getForEntity(application.getHealthStatusUrl(), (Class<Map<String, String>>) (Class<?>) Map.class);
            log.debug("/health for {} responded with {}", application, response);

            if (response.hasBody() && response.getBody().get("status") != null) {
                return  StatusInfo.valueOf(response.getBody().get("status"));
            }

            if (response.getStatusCode().is2xxSuccessful()) {
                return StatusInfo.up();
            }

            return StatusInfo.down();
        } catch (RestClientException ex) {
            log.warn("Couldn't retrieve status for {}", application, ex);

            return StatusInfo.offline();
        }
    }

}
