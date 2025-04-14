package com.sensedia.sample.consents.rest;

import com.sensedia.sample.consents.application.service.ConsentService;
import com.sensedia.sample.consents.rest.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.rest.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.rest.dto.ConsentUpdateDTO;
import com.sensedia.sample.consents.rest.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.sensedia.sample.consents.infra.util.LogUtils.maskCpf;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ConsentApi implements IConsentApi {

    private final ConsentService service;

    @Override
    public ResponseEntity<ConsentResponseDTO> createConsent(ConsentRequestDTO dto) {
        log.info("Requisição recebida para criar consentimento: CPF={}", maskCpf(dto.cpf()));
        ConsentResponseDTO response = service.createConsent(dto);
        URI location = URI.create("/consents/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @Override
    public ResponseEntity<List<ConsentResponseDTO>> getAllConsents() {
        log.info("Recebida requisição GET para listar todos os consentimentos");
        return ResponseEntity.ok(service.getAllConsents());
    }

    @Override
    public ResponseEntity<PageResponseDTO<ConsentResponseDTO>> getAllConsentsPaged(int page, int size) {
        log.info("Recebida requisição GET para listar consentimentos com paginação: página {}, tamanho {}", page, size);
        return ResponseEntity.ok(service.getAllConsentsPaged(page, size));
    }

    @Override
    public ResponseEntity<ConsentResponseDTO> getConsentById(UUID id) {
        log.info("Recebida requisição GET para buscar consentimento por ID: {}", id);
        return ResponseEntity.ok(service.getConsentById(id));
    }

    @Override
    public ResponseEntity<ConsentResponseDTO> updateConsent(UUID id, ConsentUpdateDTO dto) {
        log.info("Recebida requisição PUT para atualizar consentimento ID: {}", id);
        return ResponseEntity.ok(service.updateConsent(id, dto));
    }

    @Override
    public ResponseEntity<Void> deleteConsent(UUID id) {
        log.info("Requisição para deletar consentimento ID: {}", id);
        service.deleteConsent(id);
        return ResponseEntity.noContent().build();
    }

}
