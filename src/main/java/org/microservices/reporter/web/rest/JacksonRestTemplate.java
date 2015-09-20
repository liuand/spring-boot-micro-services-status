package org.microservices.reporter.web.rest;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class JacksonRestTemplate  extends RestTemplate {

    public JacksonRestTemplate() {
        getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }
}
