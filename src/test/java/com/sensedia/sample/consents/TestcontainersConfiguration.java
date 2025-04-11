package com.sensedia.sample.consents;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    public MongoDBContainer mongoContainer() {
        return new MongoDBContainer(DockerImageName.parse("mongo:latest"));
    }

}
