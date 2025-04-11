package com.sensedia.sample.consents.application.service.impl;

import com.sensedia.sample.consents.client.GitHubClient;
import com.sensedia.sample.consents.domain.model.Consent;
import com.sensedia.sample.consents.domain.model.ConsentHistory;
import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.dto.ConsentUpdateDTO;
import com.sensedia.sample.consents.dto.PageResponseDTO;
import com.sensedia.sample.consents.exception.ConsentNotFoundException;
import com.sensedia.sample.consents.exception.DuplicateCpfException;
import com.sensedia.sample.consents.mapper.ConsentMapper;
import com.sensedia.sample.consents.domain.repository.ConsentHistoryRepository;
import com.sensedia.sample.consents.domain.repository.ConsentRepository;
import com.sensedia.sample.consents.application.service.ConsentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsentServiceImpl implements ConsentService {

    private final ConsentRepository repository;
    private final ConsentMapper mapper;
    private final ConsentHistoryRepository historyRepository;
    private final GitHubClient gitHubClient;

    @Override
    public ConsentResponseDTO createConsent(ConsentRequestDTO request) {
        validateDuplicateCpf(request.cpf());

        Consent consent = mapper.toEntity(request);
        consent.setCreationDateTime(LocalDateTime.now());

        fillAdditionalInfoIfMissing(consent);

        return mapper.toResponseDTO(repository.save(consent));
    }

    @Override
    public List<ConsentResponseDTO> getAllConsents() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponseDTO<ConsentResponseDTO> getAllConsentsPaged(int page, int size) {
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
        Consent consent = verifyIfConsentExists(id);
        return mapper.toResponseDTO(consent);
    }

    @Override
    public ConsentResponseDTO updateConsent(UUID id, ConsentUpdateDTO request) {
        Consent existing = verifyIfConsentExists(id);
        mapper.updateEntityFromDto(request, existing);
        saveHistory(existing, "UPDATED");
        return mapper.toResponseDTO(repository.save(existing));
    }

    @Override
    public void deleteConsent(UUID id) {
        Consent existing = verifyIfConsentExists(id);
        saveHistory(existing, "DELETED");
        repository.deleteById(id);
    }

    private void validateDuplicateCpf(String cpf) {
        if (repository.existsByCpf(cpf)) {
            throw new DuplicateCpfException("Já existe um consentimento com este CPF: " + cpf);
        }
    }

    private Consent verifyIfConsentExists(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ConsentNotFoundException("Consentimento não encontrado para o ID: " + id));
    }

    private void saveHistory(Consent consent, String operation) {
        historyRepository.save(ConsentHistory.builder()
                .consentId(consent.getId())
                .cpf(consent.getCpf())
                .status(consent.getStatus())
                .expirationDateTime(consent.getExpirationDateTime())
                .additionalInfo(consent.getAdditionalInfo())
                .operation(operation)
                .timestamp(LocalDateTime.now())
                .build());
    }

    private void fillAdditionalInfoIfMissing(Consent consent) {
        if (consent.getAdditionalInfo() == null || consent.getAdditionalInfo().isBlank()) {
            consent.setAdditionalInfo(gitHubClient.getUserBioOrDefault("fsousa1987"));
        }
    }

}
