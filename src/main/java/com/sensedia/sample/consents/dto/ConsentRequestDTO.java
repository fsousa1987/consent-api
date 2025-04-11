package com.sensedia.sample.consents.dto;

import com.sensedia.sample.consents.domain.ConsentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ConsentRequestDTO(
        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato ###.###.###-##")
        String cpf,

        @NotNull(message = "Status é obrigatório")
        ConsentStatus status,

        LocalDateTime expirationDateTime,

        @Size(min = 1, max = 50, message = "O campo deve ter entre 1 e 50 caracteres")
        String additionalInfo
) {
}
