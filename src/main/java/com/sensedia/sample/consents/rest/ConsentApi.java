package com.sensedia.sample.consents.rest;

import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.dto.ConsentUpdateDTO;
import com.sensedia.sample.consents.dto.PageResponseDTO;
import com.sensedia.sample.consents.service.ConsentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ConsentApi implements IConsentApi {

    private final ConsentService service;

    @Override
    public ResponseEntity<ConsentResponseDTO> createConsent(ConsentRequestDTO dto) {
        return ResponseEntity.ok(service.createConsent(dto));
    }

    @Override
    public ResponseEntity<List<ConsentResponseDTO>> getAllConsents() {
        return ResponseEntity.ok(service.getAllConsents());
    }

    @Override
    public ResponseEntity<PageResponseDTO<ConsentResponseDTO>> getAllConsentsPaged(int page, int size) {
        return ResponseEntity.ok(service.getAllConsentsPaged(page, size));
    }

    @Override
    public ResponseEntity<ConsentResponseDTO> getConsentById(UUID id) {
        return ResponseEntity.ok(service.getConsentById(id));
    }

    @Override
    public ResponseEntity<ConsentResponseDTO> updateConsent(UUID id, ConsentUpdateDTO dto) {
        return ResponseEntity.ok(service.updateConsent(id, dto));
    }

    @Override
    public ResponseEntity<Void> deleteConsent(UUID id) {
        service.deleteConsent(id);
        return ResponseEntity.noContent().build();
    }

}
