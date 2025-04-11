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

    private final WebClient.Builder webClientBuilder;

    private static final String BASE_URL = "https://api.github.com/users";
    private static final String DEFAULT_BIO = "Informação externa indisponível";

    public String getUserBioOrDefault(String username) {
        try {
            WebClient client = webClientBuilder
                    .baseUrl(BASE_URL)
                    .build();

            GitHubUserResponse response = client
                    .get()
                    .uri("/{username}", username)
                    .retrieve()
                    .bodyToMono(GitHubUserResponse.class)
                    .block();

            return response != null && response.bio() != null
                    ? response.bio()
                    : DEFAULT_BIO;

        } catch (WebClientResponseException ex) {
            log.warn("Erro ao consumir API do GitHub ({}): {}", username, ex.getStatusCode());
            return DEFAULT_BIO;
        } catch (Exception ex) {
            log.error("Erro inesperado ao chamar API do GitHub para '{}'", username, ex);
            return DEFAULT_BIO;
        }
    }

}
