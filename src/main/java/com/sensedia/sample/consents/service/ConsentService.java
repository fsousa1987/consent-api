package com.sensedia.sample.consents.service;

import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.dto.ConsentUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface ConsentService {

    ConsentResponseDTO createConsent(ConsentRequestDTO request);

    List<ConsentResponseDTO> getAllConsents();

    ConsentResponseDTO getConsentById(UUID id);

    ConsentResponseDTO updateConsent(UUID id, ConsentUpdateDTO request);

    void deleteConsent(UUID id);

}
