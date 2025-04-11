package com.sensedia.sample.consents.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Registro de histórico de alterações de consentimentos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "consent_history")
public class ConsentHistory {

    @Id
    @Schema(description = "Identificador único do histórico", example = "e12c8a4b-0e1c-49d0-bac8-4e6123a934dd")
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Schema(description = "ID do consentimento original", example = "df1d3ef5-b253-4e7b-bf2c-70a0c5df3ae0")
    private UUID consentId;

    @Schema(description = "CPF do consentimento", example = "123.456.789-00")
    private String cpf;

    @Schema(description = "Status do consentimento no momento da alteração", example = "REVOKED")
    private ConsentStatus status;

    @Schema(description = "Data de expiração registrada na alteração", example = "2026-12-31T23:59:59")
    private LocalDateTime expirationDateTime;

    @Schema(description = "Informações adicionais registradas", example = "Revogado manualmente")
    private String additionalInfo;

    @Schema(description = "Tipo da operação realizada", example = "UPDATED")
    private String operation;

    @Schema(description = "Data e hora da operação", example = "2025-04-11T15:30:00")
    private LocalDateTime timestamp;

}
