package com.example.algopulse.repository;

import com.example.algopulse.model.StackNode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StackNodeRepository extends MongoRepository<StackNode, String> {
    List<StackNode> findBySessionIdAndUserIdOrderByPositionDesc(String sessionId, String userId);
    Optional<StackNode> findBySessionIdAndUserIdAndIsTopTrue(String sessionId, String userId);
    void deleteBySessionIdAndUserId(String sessionId, String userId);
    long countBySessionIdAndUserId(String sessionId, String userId);
}