package com.sensedia.sample.consents.service.impl;

import com.sensedia.sample.consents.client.GitHubClient;
import com.sensedia.sample.consents.domain.model.Consent;
import com.sensedia.sample.consents.domain.enums.ConsentStatus;
import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.dto.ConsentUpdateDTO;
import com.sensedia.sample.consents.exception.ConsentNotFoundException;
import com.sensedia.sample.consents.exception.DuplicateCpfException;
import com.sensedia.sample.consents.mapper.ConsentMapper;
import com.sensedia.sample.consents.domain.repository.ConsentHistoryRepository;
import com.sensedia.sample.consents.domain.repository.ConsentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ConsentServiceImplTest {

    private AutoCloseable openMocks;

    @InjectMocks
    private ConsentServiceImpl service;

    @Mock
    private ConsentRepository repository;

    @Mock
    private ConsentMapper mapper;

    @Mock
    private ConsentHistoryRepository historyRepository;

    @Mock
    private GitHubClient gitHubClient;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void shouldCreateConsentWithAdditionalInfoFromGitHubWhenMissing() {

        var request = new ConsentRequestDTO("123.456.789-00", ConsentStatus.ACTIVE, LocalDateTime.now().plusDays(1),
                null);

        var entity = Consent.builder()
                .cpf(request.cpf())
                .creationDateTime(LocalDateTime.now())
                .build();

        var saved = Consent.builder()
                .id(entity.getId())
                .cpf(entity.getCpf())
                .creationDateTime(entity.getCreationDateTime())
                .additionalInfo("GitHub Bio")
                .build();

        when(repository.existsByCpf(request.cpf())).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(entity);
        when(gitHubClient.getUserBioOrDefault("fsousa1987")).thenReturn("GitHub Bio");
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toResponseDTO(saved)).thenReturn(new ConsentResponseDTO(saved.getId(), saved.getCpf(), null,
                saved.getCreationDateTime(), null, saved.getAdditionalInfo()));

        var response = service.createConsent(request);

        assertThat(response.additionalInfo()).isEqualTo("GitHub Bio");
        verify(repository).save(entity);
    }

    @Test
    void shouldThrowExceptionWhenCpfAlreadyExists() {
        ConsentRequestDTO request = new ConsentRequestDTO("123.456.789-00", null,
                LocalDateTime.now().plusDays(1), null);

        when(repository.existsByCpf(request.cpf())).thenReturn(true);

        assertThatThrownBy(() -> service.createConsent(request))
                .isInstanceOf(DuplicateCpfException.class)
                .hasMessageContaining("Já existe um consentimento com este CPF");
    }

    @Test
    void shouldReturnConsentWhenIdExists() {

        UUID id = UUID.randomUUID();

        var entity = Consent.builder()
                .id(id)
                .cpf("123.456.789-00")
                .creationDateTime(LocalDateTime.now())
                .build();

        var responseDTO = new ConsentResponseDTO(
                id, entity.getCpf(), entity.getStatus(), entity.getCreationDateTime(), null, "Info"
        );

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toResponseDTO(entity)).thenReturn(responseDTO);

        var result = service.getConsentById(id);

        assertThat(result.id()).isEqualTo(id);
    }

    @Test
    void shouldThrowExceptionWhenIdNotFound() {

        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> service.getConsentById(id))
                .isInstanceOf(com.sensedia.sample.consents.exception.ConsentNotFoundException.class)
                .hasMessageContaining("Consentimento não encontrado");
    }

    @Test
    void shouldReturnAllConsents() {

        var consent = Consent.builder()
                .cpf("123.456.789-00")
                .creationDateTime(LocalDateTime.now())
                .build();

        when(repository.findAll()).thenReturn(List.of(consent));
        when(mapper.toResponseDTO(consent)).thenReturn(
                new ConsentResponseDTO(consent.getId(), consent.getCpf(), consent.getStatus(),
                        consent.getCreationDateTime(), null, consent.getAdditionalInfo())
        );

        var result = service.getAllConsents();

        assertThat(result).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    void shouldReturnPagedConsents() {

        int page = 0, size = 1;
        var consent = Consent.builder()
                .cpf("999.999.999-99")
                .creationDateTime(LocalDateTime.now())
                .build();

        var pageResult = new PageImpl<>(List.of(consent), PageRequest.of(page, size), 1);

        when(repository.findAll(any(Pageable.class))).thenReturn(pageResult);
        when(mapper.toResponseDTO(consent)).thenReturn(
                new ConsentResponseDTO(consent.getId(), consent.getCpf(), consent.getStatus(),
                        consent.getCreationDateTime(), null, consent.getAdditionalInfo())
        );

        var result = service.getAllConsentsPaged(page, size);

        assertThat(result.content()).hasSize(1);
        assertThat(result.totalPages()).isEqualTo(1);
    }

    @Test
    void shouldUpdateConsentSuccessfully() {

        UUID id = UUID.randomUUID();

        var existing = Consent.builder()
                .id(id)
                .cpf("123.456.789-00")
                .creationDateTime(LocalDateTime.now())
                .status(ConsentStatus.ACTIVE)
                .additionalInfo("Old Info")
                .build();

        var request = new ConsentUpdateDTO(ConsentStatus.REVOKED, LocalDateTime.now().plusDays(2), "New Info");

        doAnswer(invocation -> {
            ConsentUpdateDTO dto = invocation.getArgument(0);
            Consent entity = invocation.getArgument(1);
            entity.setStatus(dto.status());
            entity.setExpirationDateTime(dto.expirationDateTime());
            entity.setAdditionalInfo(dto.additionalInfo());
            return null;
        }).when(mapper).updateEntityFromDto(request, existing);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);
        when(mapper.toResponseDTO(existing)).thenReturn(new ConsentResponseDTO(id, existing.getCpf(), request.status(),
                existing.getCreationDateTime(), request.expirationDateTime(), request.additionalInfo()));

        var response = service.updateConsent(id, request);

        assertThat(response.status()).isEqualTo(request.status());
        assertThat(response.additionalInfo()).isEqualTo("New Info");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingConsent() {
        UUID id = UUID.randomUUID();
        var request = new ConsentUpdateDTO(ConsentStatus.REVOKED, LocalDateTime.now().plusDays(2), "Info");

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateConsent(id, request))
                .isInstanceOf(ConsentNotFoundException.class);
    }

    @Test
    void shouldDeleteConsentSuccessfully() {

        UUID id = UUID.randomUUID();

        var entity = Consent.builder()
                .id(id)
                .cpf("123.456.789-00")
                .creationDateTime(LocalDateTime.now())
                .status(ConsentStatus.ACTIVE)
                .additionalInfo("info")
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        service.deleteConsent(id);

        verify(repository).deleteById(id);
        verify(historyRepository).save(argThat(history ->
                history.getConsentId().equals(id) &&
                        history.getOperation().equals("DELETED")
        ));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingConsent() {

        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> service.deleteConsent(id))
                .isInstanceOf(com.sensedia.sample.consents.exception.ConsentNotFoundException.class)
                .hasMessageContaining("Consentimento não encontrado");
    }

}
