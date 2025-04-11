package com.sensedia.sample.consents.service.impl;

import com.sensedia.sample.consents.client.GitHubClient;
import com.sensedia.sample.consents.domain.Consent;
import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.dto.ConsentUpdateDTO;
import com.sensedia.sample.consents.exception.DuplicateCpfException;
import com.sensedia.sample.consents.mapper.ConsentMapper;
import com.sensedia.sample.consents.repository.ConsentHistoryRepository;
import com.sensedia.sample.consents.repository.ConsentRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.*;

class ConsentServiceImplTest {

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
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateConsentWithAdditionalInfoFromGitHubWhenMissing() {

        ConsentRequestDTO request = new ConsentRequestDTO("123.456.789-00", null, LocalDateTime.now().plusDays(1), null);
        Consent entity = new Consent();
        entity.setId(UUID.randomUUID());
        entity.setCpf(request.cpf());
        entity.setCreationDateTime(LocalDateTime.now());

        Consent saved = new Consent();
        saved.setId(entity.getId());
        saved.setCpf(entity.getCpf());
        saved.setCreationDateTime(entity.getCreationDateTime());
        saved.setAdditionalInfo("GitHub Bio");

        when(repository.existsByCpf(request.cpf())).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(entity);
        when(gitHubClient.getUserBioOrDefault("fsousa1987")).thenReturn("GitHub Bio");
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toResponseDTO(saved)).thenReturn(new ConsentResponseDTO(saved.getId(), saved.getCpf(), null, saved.getCreationDateTime(), null, saved.getAdditionalInfo()));

        ConsentResponseDTO response = service.createConsent(request);

        AssertionsForClassTypes.assertThat(response.additionalInfo()).isEqualTo("GitHub Bio");
        verify(repository).save(entity);
    }

    @Test
    void shouldThrowExceptionWhenCpfAlreadyExists() {
        ConsentRequestDTO request = new ConsentRequestDTO("123.456.789-00", null, LocalDateTime.now().plusDays(1), null);
        when(repository.existsByCpf(request.cpf())).thenReturn(true);

        assertThatThrownBy(() -> service.createConsent(request))
                .isInstanceOf(DuplicateCpfException.class)
                .hasMessageContaining("Já existe um consentimento com este CPF");
    }

    @Test
    void shouldReturnConsentWhenIdExists() {

        UUID id = UUID.randomUUID();
        Consent entity = new Consent();
        entity.setId(id);
        entity.setCpf("123.456.789-00");
        entity.setCreationDateTime(LocalDateTime.now());

        ConsentResponseDTO responseDTO = new ConsentResponseDTO(
                id, "123.456.789-00", null, entity.getCreationDateTime(), null, "Info"
        );

        when(repository.findById(id)).thenReturn(java.util.Optional.of(entity));
        when(mapper.toResponseDTO(entity)).thenReturn(responseDTO);

        ConsentResponseDTO result = service.getConsentById(id);

        AssertionsForClassTypes.assertThat(result.id()).isEqualTo(id);
        verify(repository).findById(id);
        verify(mapper).toResponseDTO(entity);
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

        List<Consent> entities = List.of(
                new Consent(UUID.randomUUID(), "111.111.111-11", com.sensedia.sample.consents.domain.ConsentStatus.ACTIVE, LocalDateTime.now(), null, "info")
        );

        List<ConsentResponseDTO> dtos = List.of(
                new ConsentResponseDTO(entities.getFirst().getId(), entities.getFirst().getCpf(), entities.getFirst().getStatus(), entities.getFirst().getCreationDateTime(), null, "info")
        );

        when(repository.findAll()).thenReturn(entities);
        when(mapper.toResponseDTO(any())).thenReturn(dtos.getFirst());

        List<ConsentResponseDTO> result = service.getAllConsents();

        assertThat(result).hasSize(1);
        verify(repository).findAll();
        verify(mapper, times(1)).toResponseDTO(any());
    }

    @Test
    void shouldReturnPagedConsents() {

        int page = 0;
        int size = 2;

        List<Consent> content = List.of(
                new Consent(UUID.randomUUID(), "222.222.222-22", com.sensedia.sample.consents.domain.ConsentStatus.ACTIVE, LocalDateTime.now(), null, "info")
        );

        Page<Consent> consentPage = new PageImpl<>(content, PageRequest.of(page, size), 1);

        when(repository.findAll(any(Pageable.class))).thenReturn(consentPage);
        when(mapper.toResponseDTO(any())).thenReturn(
                new ConsentResponseDTO(content.getFirst().getId(), content.getFirst().getCpf(), content.getFirst().getStatus(), content.getFirst().getCreationDateTime(), null, "info")
        );

        var result = service.getAllConsentsPaged(page, size);

        assertThat(result.content()).hasSize(1);
        assertThat(result.page()).isEqualTo(page);
        assertThat(result.size()).isEqualTo(size);
        assertThat(result.totalPages()).isEqualTo(1);
        verify(repository).findAll(any(Pageable.class));
        verify(mapper).toResponseDTO(any());
    }

    @Test
    void shouldUpdateConsentSuccessfully() {

        UUID id = UUID.randomUUID();
        Consent existing = new Consent();
        existing.setId(id);
        existing.setCpf("123.456.789-00");
        existing.setCreationDateTime(LocalDateTime.now());
        existing.setStatus(com.sensedia.sample.consents.domain.ConsentStatus.ACTIVE);
        existing.setAdditionalInfo("Old Info");

        ConsentUpdateDTO request = new ConsentUpdateDTO(
                com.sensedia.sample.consents.domain.ConsentStatus.REVOKED,
                LocalDateTime.now().plusDays(2),
                "New Info"
        );

        Consent updatedEntity = new Consent();
        updatedEntity.setId(id);
        updatedEntity.setCpf(existing.getCpf());
        updatedEntity.setStatus(request.status());
        updatedEntity.setExpirationDateTime(request.expirationDateTime());
        updatedEntity.setAdditionalInfo(request.additionalInfo());
        updatedEntity.setCreationDateTime(existing.getCreationDateTime());

        when(repository.findById(id)).thenReturn(java.util.Optional.of(existing));
        doAnswer(invocation -> {
            ConsentUpdateDTO dto = invocation.getArgument(0);
            Consent entity = invocation.getArgument(1);
            entity.setStatus(dto.status());
            entity.setExpirationDateTime(dto.expirationDateTime());
            entity.setAdditionalInfo(dto.additionalInfo());
            return null;
        }).when(mapper).updateEntityFromDto(request, existing);

        when(repository.save(existing)).thenReturn(updatedEntity);
        when(mapper.toResponseDTO(updatedEntity)).thenReturn(
                new com.sensedia.sample.consents.dto.ConsentResponseDTO(
                        id,
                        existing.getCpf(),
                        request.status(),
                        existing.getCreationDateTime(),
                        request.expirationDateTime(),
                        request.additionalInfo()
                )
        );

        var response = service.updateConsent(id, request);

        AssertionsForClassTypes.assertThat(response.status()).isEqualTo(request.status());
        AssertionsForClassTypes.assertThat(response.additionalInfo()).isEqualTo("New Info");
        verify(mapper).updateEntityFromDto(request, existing);
        verify(repository).save(existing);
    }

    @Test
    void shouldDeleteConsentSuccessfully() {

        UUID id = UUID.randomUUID();
        Consent existing = new Consent();
        existing.setId(id);
        existing.setCpf("123.456.789-00");
        existing.setCreationDateTime(LocalDateTime.now());
        existing.setStatus(com.sensedia.sample.consents.domain.ConsentStatus.ACTIVE);
        existing.setAdditionalInfo("info");

        when(repository.findById(id)).thenReturn(java.util.Optional.of(existing));

        service.deleteConsent(id);

        verify(repository).deleteById(id);
        verify(historyRepository).save(argThat(history ->
                history.getConsentId().equals(id) &&
                        history.getOperation().equals("DELETED") &&
                        history.getCpf().equals(existing.getCpf())
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
