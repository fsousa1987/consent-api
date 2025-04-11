package com.sensedia.sample.consents.integration;

import com.sensedia.sample.consents.ConsentsApplication;
import com.sensedia.sample.consents.domain.Consent;
import com.sensedia.sample.consents.domain.ConsentStatus;
import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.repository.ConsentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = ConsentsApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Import(TestcontainersConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConsentApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ConsentRepository repository;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/consents";
    }

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    @Order(1)
    void shouldCreateConsentSuccessfully() {
        var request = new ConsentRequestDTO(
                "123.456.789-00",
                ConsentStatus.ACTIVE,
                LocalDateTime.of(2026, 12, 31, 23, 59, 59),
                "Termo de aceite digital"
        );

        var response = restTemplate.postForEntity(getBaseUrl(), request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    @Order(2)
    void shouldGetConsentById() {

        var saved = repository.save(Consent.builder()
                .cpf("999.999.999-99")
                .status(ConsentStatus.ACTIVE)
                .creationDateTime(LocalDateTime.now())
                .additionalInfo("Aceite via e-mail")
                .build());

        var response = restTemplate.getForEntity(getBaseUrl() + "/" + saved.getId(), String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("999.999.999-99");
    }

    @Test
    @Order(3)
    void shouldListAllConsents() {
        repository.saveAll(List.of(
                Consent.builder()
                        .cpf("111.111.111-11")
                        .status(ConsentStatus.ACTIVE)
                        .creationDateTime(LocalDateTime.now())
                        .additionalInfo("Primeiro")
                        .build(),
                Consent.builder()
                        .cpf("222.222.222-22")
                        .status(ConsentStatus.REVOKED)
                        .creationDateTime(LocalDateTime.now())
                        .additionalInfo("Segundo")
                        .build()
        ));

        var response = restTemplate.getForEntity(getBaseUrl(), String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("111.111.111-11", "222.222.222-22");
    }

    @Test
    @Order(4)
    void shouldReturnPagedConsents() {
        repository.saveAll(List.of(
                Consent.builder().cpf("333.333.333-33").status(ConsentStatus.ACTIVE)
                        .creationDateTime(LocalDateTime.now()).build(),
                Consent.builder().cpf("444.444.444-44").status(ConsentStatus.ACTIVE)
                        .creationDateTime(LocalDateTime.now()).build()
        ));

        var response = restTemplate.getForEntity(getBaseUrl() + "/paged?page=0&size=2", String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("333.333.333-33", "444.444.444-44");
    }

}
