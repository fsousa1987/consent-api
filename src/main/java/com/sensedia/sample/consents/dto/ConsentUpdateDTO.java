package com.sensedia.sample.consents.dto;

import com.sensedia.sample.consents.domain.ConsentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ConsentUpdateDTO(

        @NotNull(message = "O status é obrigatório")
        ConsentStatus status,

        @NotNull(message = "A data de expiração é obrigatória")
        LocalDateTime expirationDateTime,

        @NotNull(message = "O campo 'additionalInfo' é obrigatório")
        @Size(min = 1, max = 50, message = "O campo deve ter entre 1 e 50 caracteres")
        String additionalInfo

) {
}
