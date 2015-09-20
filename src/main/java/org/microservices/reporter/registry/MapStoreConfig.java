package org.microservices.reporter.registry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapStoreConfig {

    @Bean
    public MapStore memoryStore() {
        return new MapStore();
    }

}
