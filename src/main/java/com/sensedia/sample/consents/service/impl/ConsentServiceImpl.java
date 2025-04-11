package com.sensedia.sample.consents.service.impl;

import com.sensedia.sample.consents.domain.Consent;
import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.exception.DuplicateCpfException;
import com.sensedia.sample.consents.mapper.ConsentMapper;
import com.sensedia.sample.consents.repository.ConsentRepository;
import com.sensedia.sample.consents.service.ConsentService;
import lombok.RequiredArgsConstructor;
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

    @Override
    public ConsentResponseDTO createConsent(ConsentRequestDTO request) {
        validateDuplicateCpf(request.cpf());

        Consent consent = mapper.toEntity(request);
        consent.setCreationDateTime(LocalDateTime.now());
        return mapper.toResponseDTO(repository.save(consent));
    }

    @Override
    public List<ConsentResponseDTO> getAllConsents() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ConsentResponseDTO getConsentById(UUID id) {
        Consent consent = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consentimento não encontrado"));
        return mapper.toResponseDTO(consent);
    }

    @Override
    public ConsentResponseDTO updateConsent(UUID id, ConsentRequestDTO request) {
        Consent existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consentimento não encontrado"));

        Consent updated = mapper.toEntity(request);
        updated.setId(existing.getId());
        updated.setCreationDateTime(existing.getCreationDateTime());

        return mapper.toResponseDTO(repository.save(updated));
    }

    @Override
    public void deleteConsent(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Consentimento não encontrado");
        }
        repository.deleteById(id);
    }

    private void validateDuplicateCpf(String cpf) {
        if (repository.existsByCpf(cpf)) {
            throw new DuplicateCpfException("Já existe um consentimento com este CPF: " + cpf);
        }
    }

}
