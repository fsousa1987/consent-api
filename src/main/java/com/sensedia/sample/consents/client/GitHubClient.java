package com.sensedia.sample.consents.client;

import com.sensedia.sample.consents.client.response.GitHubUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Component
@RequiredArgsConstructor
public class GitHubClient {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.github.com/users")
            .build();

    public String fetchBioOrDefault() {
        try {
            GitHubUserResponse response = webClient
                    .get()
                    .uri("/martinfowler")
                    .retrieve()
                    .bodyToMono(GitHubUserResponse.class)
                    .block();

            return response != null && response.bio() != null
                    ? response.bio()
                    : "Informação externa indisponível";

        } catch (WebClientResponseException ex) {
            log.warn("Erro ao consumir API externa do GitHub: {}", ex.getStatusCode());
            return "Informação externa indisponível";
        } catch (Exception ex) {
            log.error("Erro inesperado ao chamar API externa do GitHub", ex);
            return "Informação externa indisponível";
        }
    }

}
