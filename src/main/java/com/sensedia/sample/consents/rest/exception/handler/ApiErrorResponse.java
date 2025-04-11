package com.sensedia.sample.consents.rest.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ApiErrorResponse(

        @Schema(description = "Código HTTP da resposta", example = "400")
        int status,

        @Schema(description = "Descrição do erro HTTP", example = "Bad Request")
        String error,

        @Schema(description = "Mensagem explicando o erro ocorrido", example = "Erro de validação nos dados da requisição")
        String message,

        @Schema(description = "Data e hora do erro", example = "2025-04-11T11:57:19.973Z")
        LocalDateTime timestamp,

        @Schema(
                description = "Lista de mensagens detalhadas sobre os erros encontrados",
                example = "[\"cpf: formato inválido\", \"status: valor obrigatório\"]"
        )
        List<String> details
) {

}
