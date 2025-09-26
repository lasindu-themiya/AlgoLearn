package com.example.algopulse.repository;

import com.example.algopulse.model.SinglyLinkedListNode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SinglyLinkedListNodeRepository extends MongoRepository<SinglyLinkedListNode, String> {
    List<SinglyLinkedListNode> findBySessionIdAndUserIdOrderByPosition(String sessionId, String userId);
    List<SinglyLinkedListNode> findBySessionIdOrderByPosition(String sessionId);
    Optional<SinglyLinkedListNode> findBySessionIdAndUserIdAndIsHeadTrue(String sessionId, String userId);
    Optional<SinglyLinkedListNode> findBySessionIdAndUserIdAndData(String sessionId, String userId, int data);
    void deleteBySessionIdAndUserId(String sessionId, String userId);
    long countBySessionIdAndUserId(String sessionId, String userId);
}