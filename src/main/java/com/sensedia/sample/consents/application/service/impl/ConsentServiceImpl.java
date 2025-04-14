package com.sensedia.sample.consents.application.service.impl;

import com.sensedia.sample.consents.application.service.ConsentService;
import com.sensedia.sample.consents.domain.enums.ConsentStatus;
import com.sensedia.sample.consents.domain.model.Consent;
import com.sensedia.sample.consents.domain.model.ConsentHistory;
import com.sensedia.sample.consents.domain.repository.ConsentHistoryRepository;
import com.sensedia.sample.consents.domain.repository.ConsentRepository;
import com.sensedia.sample.consents.infra.client.GitHubClient;
import com.sensedia.sample.consents.rest.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.rest.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.rest.dto.ConsentUpdateDTO;
import com.sensedia.sample.consents.rest.dto.PageResponseDTO;
import com.sensedia.sample.consents.rest.exception.ConsentNotFoundException;
import com.sensedia.sample.consents.rest.exception.DuplicateCpfException;
import com.sensedia.sample.consents.rest.mapper.ConsentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sensedia.sample.consents.infra.util.LogUtils.maskCpf;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsentServiceImpl implements ConsentService {

    private static final String OPERATION_DELETED = "DELETED";
    private static final String OPERATION_UPDATED = "UPDATED";

    private final ConsentRepository repository;
    private final ConsentMapper mapper;
    private final ConsentHistoryRepository historyRepository;
    private final GitHubClient gitHubClient;

    @Override
    public ConsentResponseDTO createConsent(ConsentRequestDTO request) {
        log.info("Iniciando criação de consentimento para CPF: {}", maskCpf(request.cpf()));
        validateDuplicateCpf(request.cpf());

        Consent consent = mapper.toEntity(request);
        consent.setCreationDateTime(LocalDateTime.now());

        fillAdditionalInfoIfMissing(consent);

        Consent saved = repository.save(consent);
        log.info("Consentimento criado com sucesso. ID: {}", saved.getId());

        return mapper.toResponseDTO(saved);
    }

    @Override
    public List<ConsentResponseDTO> getAllConsents() {
        log.debug("Buscando todos os consentimentos...");
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponseDTO<ConsentResponseDTO> getAllConsentsPaged(int page, int size) {
        log.debug("Buscando consentimentos paginados. Página: {}, Tamanho: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Consent> consentPage = repository.findAll(pageable);

        List<ConsentResponseDTO> content = consentPage.getContent().stream()
                .map(mapper::toResponseDTO)
                .toList();

        return new PageResponseDTO<>(
                content,
                consentPage.getNumber(),
                consentPage.getSize(),
                consentPage.getTotalElements(),
                consentPage.getTotalPages()
        );
    }

    @Override
    public ConsentResponseDTO getConsentById(UUID id) {
        log.debug("Buscando consentimento por ID: {}", id);
        Consent consent = verifyIfConsentExists(id);
        return mapper.toResponseDTO(consent);
    }

    @Override
    public ConsentResponseDTO updateConsent(UUID id, ConsentUpdateDTO request) {
        log.info("Atualizando consentimento ID: {}", id);
        Consent existing = verifyIfConsentExists(id);
        mapper.updateEntityFromDto(request, existing);

        Consent saved = repository.save(existing);
        saveHistory(saved, OPERATION_UPDATED);
        log.info("Consentimento atualizado com sucesso. ID: {}", saved.getId());

        return mapper.toResponseDTO(saved);
    }

    @Override
    public void deleteConsent(UUID id) {
        log.info("Revogando consentimento ID: {}", id);
        Consent existing = verifyIfConsentExists(id);
        saveHistory(existing, OPERATION_DELETED);
        repository.deleteById(id);
        log.info("Consentimento revogado com sucesso. ID: {}", id);
    }

    private void validateDuplicateCpf(String cpf) {
        if (repository.existsByCpf(cpf)) {
            log.warn("Tentativa de criação de consentimento com CPF duplicado: {}", maskCpf(cpf));
            throw new DuplicateCpfException("Já existe um consentimento com este CPF: " + cpf);
        }
    }

    private Consent verifyIfConsentExists(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Consentimento não encontrado para o ID: {}", id);
                    return new ConsentNotFoundException("Consentimento não encontrado para o ID: " + id);
                });
    }

    private void saveHistory(Consent consent, String operation) {
        ConsentStatus statusToSave = operation.equalsIgnoreCase(OPERATION_DELETED)
                ? ConsentStatus.REVOKED
                : consent.getStatus();

        log.debug("Registrando histórico de operação '{}'. ID: {}", operation, consent.getId());

        historyRepository.save(ConsentHistory.builder()
                .consentId(consent.getId())
                .cpf(consent.getCpf())
                .status(statusToSave)
                .expirationDateTime(consent.getExpirationDateTime())
                .additionalInfo(consent.getAdditionalInfo())
                .operation(operation)
                .timestamp(LocalDateTime.now())
                .build());
    }

    private void fillAdditionalInfoIfMissing(Consent consent) {
        if (consent.getAdditionalInfo() == null || consent.getAdditionalInfo().isBlank()) {
            log.info("Campo 'additionalInfo' está vazio. Buscando bio via API do GitHub...");
            consent.setAdditionalInfo(gitHubClient.getUserBioOrDefault("fsousa1987"));
        }
    }

}
