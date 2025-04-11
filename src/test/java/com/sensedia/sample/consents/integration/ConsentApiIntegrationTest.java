package com.sensedia.sample.consents.integration;

import com.sensedia.sample.consents.ConsentsApplication;
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

}
