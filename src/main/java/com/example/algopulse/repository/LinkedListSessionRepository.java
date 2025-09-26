package com.example.algopulse.repository;

import com.example.algopulse.model.LinkedListSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LinkedListSessionRepository extends MongoRepository<LinkedListSession, String> {
    Optional<LinkedListSession> findBySessionIdAndUserId(String sessionId, String userId);
    List<LinkedListSession> findByUserId(String userId);
    List<LinkedListSession> findByUserIdAndType(String userId, String type);
    void deleteBySessionIdAndUserId(String sessionId, String userId);
}