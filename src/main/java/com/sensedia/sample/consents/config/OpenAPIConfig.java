package com.sensedia.sample.consents.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Consent API",
                version = "v1.0",
                description = "API para gerenciamento de consentimentos de usuários"
        ),
        servers = @Server(url = "http://localhost:8099", description = "Servidor local de desenvolvimento")
)
@Configuration
public class OpenAPIConfig {
}
