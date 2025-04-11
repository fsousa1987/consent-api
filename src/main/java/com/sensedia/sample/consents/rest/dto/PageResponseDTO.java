package com.sensedia.sample.consents.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Resposta paginada padrão da API")
public record PageResponseDTO<T>(

        @Schema(description = "Lista de itens da página atual")
        List<T> content,

        @Schema(description = "Número da página atual (começa em 0)", example = "0")
        int page,

        @Schema(description = "Quantidade de elementos por página", example = "10")
        int size,

        @Schema(description = "Quantidade total de elementos disponíveis", example = "57")
        long totalElements,

        @Schema(description = "Quantidade total de páginas disponíveis", example = "6")
        int totalPages

) {
}
