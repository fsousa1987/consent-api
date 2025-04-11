package com.sensedia.sample.consents.rest;

import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.dto.ConsentUpdateDTO;
import com.sensedia.sample.consents.exception.handler.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/consents")
public interface IConsentApi {

	@Operation(
			summary = "Criar um novo consentimento",
			description = "Cria um novo registro de consentimento com CPF, status, data de expiração e informações adicionais."
	)
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Consentimento criado com sucesso"),
			@ApiResponse(
					responseCode = "400",
					description = "Requisição inválida",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ApiErrorResponse.class))
			),
			@ApiResponse(
					responseCode = "409",
					description = "CPF já cadastrado",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ApiErrorResponse.class))
			)
	})
	@PostMapping
	ResponseEntity<ConsentResponseDTO> createConsent(@RequestBody @Valid ConsentRequestDTO dto);

	@Operation(
			summary = "Listar todos os consentimentos",
			description = "Retorna todos os consentimentos cadastrados no sistema."
	)
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
	})
	@GetMapping
	ResponseEntity<List<ConsentResponseDTO>> getAllConsents();

	@Operation(
			summary = "Buscar consentimento por ID",
			description = "Retorna os dados de um consentimento específico pelo seu identificador único (UUID)."
	)
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Consentimento encontrado"),
			@ApiResponse(
					responseCode = "404",
					description = "Consentimento não encontrado",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ApiErrorResponse.class))
			)
	})
	@GetMapping("/{id}")
	ResponseEntity<ConsentResponseDTO> getConsentById(@PathVariable UUID id);

	@Operation(
			summary = "Atualizar consentimento existente",
			description = "Atualiza completamente os dados de um consentimento existente, substituindo os campos atuais pelos novos valores enviados."
	)
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Consentimento atualizado com sucesso"),
			@ApiResponse(
					responseCode = "400",
					description = "Dados inválidos na requisição",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ApiErrorResponse.class))
			),
			@ApiResponse(
					responseCode = "404",
					description = "Consentimento não encontrado",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ApiErrorResponse.class))
			)
	})
	@PutMapping("/{id}")
	ResponseEntity<ConsentResponseDTO> updateConsent(@PathVariable UUID id, @RequestBody @Valid ConsentUpdateDTO dto);

	@Operation(
			summary = "Revogar ou excluir consentimento",
			description = "Remove um consentimento do sistema com base no seu identificador único (UUID)."
	)
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Consentimento removido com sucesso"),
			@ApiResponse(
					responseCode = "404",
					description = "Consentimento não encontrado",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ApiErrorResponse.class))
			)
	})
	@DeleteMapping("/{id}")
	ResponseEntity<Void> deleteConsent(@PathVariable UUID id);

}
