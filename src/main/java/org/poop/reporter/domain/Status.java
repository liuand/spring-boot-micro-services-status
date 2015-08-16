package org.poop.reporter.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    UNKNOWN("#DE9E31"),
    UP("#2FA44F"),
    DOWN("#D50200"),
    OFFLINE("#000")
    ;

    private String color;
}
