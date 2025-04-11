package com.sensedia.sample.consents.application.service;

import com.sensedia.sample.consents.rest.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.rest.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.rest.dto.ConsentUpdateDTO;
import com.sensedia.sample.consents.rest.dto.PageResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ConsentService {

    ConsentResponseDTO createConsent(ConsentRequestDTO request);

    List<ConsentResponseDTO> getAllConsents();

    PageResponseDTO<ConsentResponseDTO> getAllConsentsPaged(int page, int size);

    ConsentResponseDTO getConsentById(UUID id);

    ConsentResponseDTO updateConsent(UUID id, ConsentUpdateDTO request);

    void deleteConsent(UUID id);

}
