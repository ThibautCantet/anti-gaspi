package com.soat.anti_gaspi.model;

import java.util.Arrays;

public enum Status {
    UNPUBLISHED("non publiée"),
    PUBLISHED("publiée");

    private String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Status from(String value) {
        return Arrays.stream(Status.values())
                .filter(status -> status.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }
}
