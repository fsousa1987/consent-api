package com.sensedia.sample.consents.repository;

import com.sensedia.sample.consents.domain.Consent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConsentRepository extends MongoRepository<Consent, UUID> {

    boolean existsByCpf(String cpf);

}
