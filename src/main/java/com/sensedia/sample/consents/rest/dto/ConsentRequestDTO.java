package com.sensedia.sample.consents.rest.dto;

import com.sensedia.sample.consents.domain.enums.ConsentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(description = "Dados de entrada para criar um novo consentimento")
public record ConsentRequestDTO(

        @Schema(description = "CPF do usuário", example = "123.456.789-00")
        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato ###.###.###-##")
        String cpf,

        @Schema(description = "Status do consentimento", example = "ACTIVE")
        @NotNull(message = "Status é obrigatório")
        ConsentStatus status,

        @Schema(description = "Data e hora de expiração do consentimento", example = "2026-12-31T23:59:59")
        LocalDateTime expirationDateTime,

        @Schema(description = "Informações adicionais sobre o consentimento", example = "Termo de aceite digital")
        @Size(min = 1, max = 50, message = "O campo deve ter entre 1 e 50 caracteres")
        String additionalInfo
) {
}
