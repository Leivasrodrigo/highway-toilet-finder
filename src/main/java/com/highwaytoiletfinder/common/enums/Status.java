package com.highwaytoiletfinder.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    PENDING,
    APPROVED,
    VALIDATED,
    REJECTED;

    @JsonCreator
    public static Status fromString(String value) {
        if (value == null) return null;
        return Status.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
