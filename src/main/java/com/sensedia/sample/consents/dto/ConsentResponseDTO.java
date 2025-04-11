package com.sensedia.sample.consents.dto;

import com.sensedia.sample.consents.domain.enums.ConsentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados retornados de um consentimento")
public record ConsentResponseDTO(

        @Schema(description = "Identificador único do consentimento", example = "df1d3ef5-b253-4e7b-bf2c-70a0c5df3ae0")
        UUID id,

        @Schema(description = "CPF do usuário", example = "123.456.789-00")
        String cpf,

        @Schema(description = "Status atual do consentimento", example = "ACTIVE")
        ConsentStatus status,

        @Schema(description = "Data e hora da criação do consentimento", example = "2025-04-11T12:00:00")
        LocalDateTime creationDateTime,

        @Schema(description = "Data e hora de expiração do consentimento", example = "2026-12-31T23:59:59")
        LocalDateTime expirationDateTime,

        @Schema(description = "Informações adicionais fornecidas pelo usuário", example = "Termo de aceite digital")
        String additionalInfo
) {
}
