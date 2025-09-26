package com.example.algopulse.repository;

import com.example.algopulse.model.DoublyLinkedListNode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoublyLinkedListNodeRepository extends MongoRepository<DoublyLinkedListNode, String> {
    List<DoublyLinkedListNode> findBySessionIdAndUserIdOrderByPosition(String sessionId, String userId);
    List<DoublyLinkedListNode> findBySessionIdOrderByPosition(String sessionId);
    Optional<DoublyLinkedListNode> findBySessionIdAndUserIdAndIsHeadTrue(String sessionId, String userId);
    Optional<DoublyLinkedListNode> findBySessionIdAndUserIdAndIsTailTrue(String sessionId, String userId);
    Optional<DoublyLinkedListNode> findBySessionIdAndUserIdAndData(String sessionId, String userId, int data);
    void deleteBySessionIdAndUserId(String sessionId, String userId);
    long countBySessionIdAndUserId(String sessionId, String userId);
}