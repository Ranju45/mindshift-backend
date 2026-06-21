package com.mindshift.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Goal {
    CALM, PEAK, SUPERHUMAN;

    @JsonCreator
    public static Goal fromJson(String value) {
        return Goal.valueOf(value.trim().toUpperCase());
    }
}