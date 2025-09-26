package com.example.algopulse.repository;

import com.example.algopulse.model.QueueSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface QueueSessionRepository extends MongoRepository<QueueSession, String> {
    Optional<QueueSession> findBySessionIdAndUserId(String sessionId, String userId);
    List<QueueSession> findByUserId(String userId);
    List<QueueSession> findByUserIdAndType(String userId, String type);
    void deleteBySessionIdAndUserId(String sessionId, String userId);
}