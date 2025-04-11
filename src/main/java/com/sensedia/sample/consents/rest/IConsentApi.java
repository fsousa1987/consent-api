package com.sensedia.sample.consents.rest;

import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/consents")
public interface IConsentApi {

	@PostMapping
	ResponseEntity<ConsentResponseDTO> createConsent(@RequestBody @Valid ConsentRequestDTO dto);

	@GetMapping
	ResponseEntity<List<ConsentResponseDTO>> getAllConsents();

	@GetMapping("/{id}")
	ResponseEntity<ConsentResponseDTO> getConsentById(@PathVariable UUID id);

	@PutMapping("/{id}")
	ResponseEntity<ConsentResponseDTO> updateConsent(@PathVariable UUID id, @RequestBody @Valid ConsentRequestDTO dto);

	@DeleteMapping("/{id}")
	ResponseEntity<Void> deleteConsent(@PathVariable UUID id);

}
