package com.sensedia.sample.consents.client.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubUserResponse(

        @JsonProperty("bio")
        String bio

) {
}
