package com.sensedia.sample.consents.service.impl;

import com.sensedia.sample.consents.domain.Consent;
import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.dto.ConsentUpdateDTO;
import com.sensedia.sample.consents.exception.ConsentNotFoundException;
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
        Consent consent = verifyIfConsentExists(id);
        return mapper.toResponseDTO(consent);
    }

    @Override
    public ConsentResponseDTO updateConsent(UUID id, ConsentUpdateDTO request) {
        Consent existing = verifyIfConsentExists(id);

        mapper.updateEntityFromDto(request, existing);
        return mapper.toResponseDTO(repository.save(existing));
    }

    @Override
    public void deleteConsent(UUID id) {
        verifyIfConsentExists(id);
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

}
