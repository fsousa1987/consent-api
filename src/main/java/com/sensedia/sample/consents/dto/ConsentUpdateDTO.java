package com.sensedia.sample.consents.dto;

import com.sensedia.sample.consents.domain.ConsentStatus;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ConsentUpdateDTO(

        ConsentStatus status,
        LocalDateTime expirationDateTime,
        @Size(min = 1, max = 50, message = "O campo deve ter entre 1 e 50 caracteres")
        String additionalInfo

) {
}
