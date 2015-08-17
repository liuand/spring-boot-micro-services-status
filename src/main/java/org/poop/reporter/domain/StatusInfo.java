package org.poop.reporter.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode(of = "status")
@ToString(of = "status")
public class StatusInfo implements Serializable {
    private Status status;
    private long timestamp;

    public String getName() {
        return format("Status is %s", status.name());
    }

    public String getColor() {
        return status.getColor();
    }

    public static StatusInfo up() {
        return of(Status.UP);
    }

    public static StatusInfo down() {
        return of(Status.DOWN);
    }

    public static StatusInfo offline() {
        return of(Status.DOWN);
    }

    public static StatusInfo valueOf(String statusAsString) {
        Status status = Status.valueOf(statusAsString);

        return of(status);
    }

    public static StatusInfo unknown() {
        return of(Status.UNKNOWN);
    }

    private static StatusInfo of(Status status) {
        return of(status, currentTimeMillis());
    }
}
