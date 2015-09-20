package org.microservices.reporter.domain;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@EqualsAndHashCode(of = "healthStatusUrl")
@ToString
public class Application implements Serializable {
    private String name;
    private String uri;
    private String healthStatusUrl;
    private String statusPageUrl;
    private StatusInfo statusInfo;
}
