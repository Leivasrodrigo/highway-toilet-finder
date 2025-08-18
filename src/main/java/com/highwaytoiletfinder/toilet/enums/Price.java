package com.highwaytoiletfinder.toilet.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Price {
    FREE,
    PAID;

    @JsonCreator
    public static Price fromString(String value) {
        if (value == null) return null;
        return Price.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
