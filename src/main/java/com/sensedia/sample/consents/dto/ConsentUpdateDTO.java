package com.sensedia.sample.consents.dto;

import com.sensedia.sample.consents.domain.ConsentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ConsentUpdateDTO(

        @Schema(description = "Status do consentimento", example = "REVOKED")
        @NotNull(message = "O status é obrigatório")
        ConsentStatus status,

        @Schema(description = "Data e hora de expiração do consentimento", example = "2026-12-31T23:59:59")
        @NotNull(message = "A data de expiração é obrigatória")
        LocalDateTime expirationDateTime,

        @Schema(description = "Informações adicionais sobre o consentimento", example = "Revogado via aplicativo")
        @NotNull(message = "O campo 'additionalInfo' é obrigatório")
        @Size(min = 1, max = 50, message = "O campo deve ter entre 1 e 50 caracteres")
        String additionalInfo

) {
}
