package com.example.algopulse.repository;

import com.example.algopulse.model.SearchSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchSessionRepository extends MongoRepository<SearchSession, String> {
    Optional<SearchSession> findBySessionIdAndUserId(String sessionId, String userId);
    List<SearchSession> findByUserId(String userId);
    List<SearchSession> findByUserIdAndAlgorithm(String userId, String algorithm);
    void deleteBySessionIdAndUserId(String sessionId, String userId);
}