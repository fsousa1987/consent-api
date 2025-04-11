package com.sensedia.sample.consents.domain.model;

import com.sensedia.sample.consents.domain.enums.ConsentStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "consents")
public class Consent {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Indexed(unique = true)
    private String cpf;

    private ConsentStatus status;

    private LocalDateTime creationDateTime;

    private LocalDateTime expirationDateTime;

    private String additionalInfo;

}
