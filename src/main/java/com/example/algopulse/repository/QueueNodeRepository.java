package com.example.algopulse.repository;

import com.example.algopulse.model.QueueNode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface QueueNodeRepository extends MongoRepository<QueueNode, String> {
    List<QueueNode> findBySessionIdAndUserIdOrderByPosition(String sessionId, String userId);
    Optional<QueueNode> findBySessionIdAndUserIdAndIsFrontTrue(String sessionId, String userId);
    Optional<QueueNode> findBySessionIdAndUserIdAndIsRearTrue(String sessionId, String userId);
    void deleteBySessionIdAndUserId(String sessionId, String userId);
    long countBySessionIdAndUserId(String sessionId, String userId);
}