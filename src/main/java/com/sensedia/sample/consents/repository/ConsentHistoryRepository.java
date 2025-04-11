package com.sensedia.sample.consents.repository;

import com.sensedia.sample.consents.domain.model.ConsentHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConsentHistoryRepository extends MongoRepository<ConsentHistory, UUID> {
}
