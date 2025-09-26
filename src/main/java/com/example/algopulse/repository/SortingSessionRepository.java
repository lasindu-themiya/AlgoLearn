package com.example.algopulse.repository;

import com.example.algopulse.model.SortingSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SortingSessionRepository extends MongoRepository<SortingSession, String> {
    Optional<SortingSession> findBySessionIdAndUserId(String sessionId, String userId);
    List<SortingSession> findByUserId(String userId);
    List<SortingSession> findByUserIdAndAlgorithm(String userId, String algorithm);
    void deleteBySessionIdAndUserId(String sessionId, String userId);
}