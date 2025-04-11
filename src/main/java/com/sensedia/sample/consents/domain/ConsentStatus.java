package com.sensedia.sample.consents.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;
import java.util.stream.Collectors;

@Schema(description = "Status de um consentimento")
public enum ConsentStatus {

    @Schema(description = "Consentimento está ativo e válido")
    ACTIVE,

    @Schema(description = "Consentimento foi revogado")
    REVOKED,

    @Schema(description = "Consentimento expirou automaticamente")
    EXPIRED;

    public static String valuesList() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }

}
