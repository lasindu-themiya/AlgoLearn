package com.example.algopulse.repository;

import com.example.algopulse.model.StackSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StackSessionRepository extends MongoRepository<StackSession, String> {
    Optional<StackSession> findBySessionIdAndUserId(String sessionId, String userId);
    List<StackSession> findByUserId(String userId);
    List<StackSession> findByUserIdAndType(String userId, String type);
    void deleteBySessionIdAndUserId(String sessionId, String userId);
}