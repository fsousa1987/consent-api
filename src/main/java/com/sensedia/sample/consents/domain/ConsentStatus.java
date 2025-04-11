package com.sensedia.sample.consents.domain;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ConsentStatus {

    ACTIVE,
    REVOKED,
    EXPIRED;

    public static String valuesList() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }

}
