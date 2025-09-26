package com.example.algopulse.service;

import com.example.algopulse.model.*;
import com.example.algopulse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class QueueService {

    @Autowired
    private QueueSessionRepository sessionRepository;
    
    @Autowired
    private QueueNodeRepository nodeRepository;

    // Create new queue session
    public QueueSession createQueue(String sessionId, String userId, String type, int maxSize) {
        // Check if session already exists
        Optional<QueueSession> existingSession = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (existingSession.isPresent()) {
            throw new RuntimeException("Session with ID '" + sessionId + "' already exists for this user");
        }
        
        QueueSession session = new QueueSession(sessionId, userId, type, maxSize);
        return sessionRepository.save(session);
    }

    // Enqueue operation
    public Map<String, Object> enqueue(String sessionId, String userId, int data) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<QueueSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        QueueSession session = sessionOpt.get();
        
        if ("static".equals(session.getType())) {
            return enqueueStatic(session, data);
        } else {
            return enqueueDynamic(session, data);
        }
    }

    private Map<String, Object> enqueueStatic(QueueSession session, int data) {
        Map<String, Object> result = new HashMap<>();
        
        // Check if queue is full
        if (session.getCurrentSize() >= session.getMaxSize()) {
            result.put("success", false);
            result.put("message", "Queue overflow - maximum size reached");
            session.getOperationHistory().add(
                new QueueSession.OperationHistory("enqueue", data, false, "Queue overflow")
            );
            sessionRepository.save(session);
            return result;
        }
        
        // Initialize queue if empty
        if (session.getCurrentSize() == 0) {
            session.setFront(0);
            session.setRear(0);
        } else {
            // Circular queue implementation
            session.setRear((session.getRear() + 1) % session.getMaxSize());
        }
        
        // Ensure elements list has enough capacity
        while (session.getElements().size() <= session.getRear()) {
            session.getElements().add(0);
        }
        
        // Add element at rear
        session.getElements().set(session.getRear(), data);
        session.setCurrentSize(session.getCurrentSize() + 1);
        
        // Add to operation history
        session.getOperationHistory().add(
            new QueueSession.OperationHistory("enqueue", data, true, "Element enqueued successfully")
        );
        session.setUpdatedAt(System.currentTimeMillis());
        sessionRepository.save(session);
        
        result.put("success", true);
        result.put("message", "Element enqueued successfully");
        result.put("size", session.getCurrentSize());
        result.put("data", data);
        result.put("rear", session.getRear());
        
        return result;
    }

    private Map<String, Object> enqueueDynamic(QueueSession session, int data) {
        Map<String, Object> result = new HashMap<>();
        
        // Create new queue node
        QueueNode newNode = new QueueNode(session.getSessionId(), session.getUserId(), data);
        newNode.setPosition(session.getCurrentSize()); // Position from front
        
        if (session.getCurrentSize() == 0) {
            // First node is both front and rear
            newNode.setFront(true);
            newNode.setRear(true);
        } else {
            // Add to rear
            Optional<QueueNode> currentRearOpt = 
                nodeRepository.findBySessionIdAndUserIdAndIsRearTrue(session.getSessionId(), session.getUserId());
            
            if (currentRearOpt.isPresent()) {
                QueueNode currentRear = currentRearOpt.get();
                currentRear.setRear(false);
                nodeRepository.save(currentRear);
            }
            
            newNode.setRear(true);
        }
        
        QueueNode savedNode = nodeRepository.save(newNode);
        
        // Now set the IDs after saving
        if (session.getCurrentSize() == 0) {
            session.setFrontNodeId(savedNode.getId());
            session.setRearNodeId(savedNode.getId());
        } else {
            // Update the previous rear node to point to this new node
            Optional<QueueNode> currentRearOpt = 
                nodeRepository.findBySessionIdAndUserIdAndIsRearTrue(session.getSessionId(), session.getUserId());
            
            if (currentRearOpt.isPresent()) {
                QueueNode currentRear = currentRearOpt.get();
                currentRear.setNextNodeId(savedNode.getId());
                nodeRepository.save(currentRear);
            }
            
            session.setRearNodeId(savedNode.getId());
        }
        
        // Update session
        session.setCurrentSize(session.getCurrentSize() + 1);
        session.getNodeIds().add(savedNode.getId());
        session.getOperationHistory().add(
            new QueueSession.OperationHistory("enqueue", data, true, "Element enqueued successfully")
        );
        session.setUpdatedAt(System.currentTimeMillis());
        sessionRepository.save(session);
        
        result.put("success", true);
        result.put("message", "Element enqueued successfully");
        result.put("size", session.getCurrentSize());
        result.put("nodeId", savedNode.getId());
        result.put("data", data);
        
        return result;
    }

    // Dequeue operation
    public Map<String, Object> dequeue(String sessionId, String userId) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<QueueSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        QueueSession session = sessionOpt.get();
        
        if ("static".equals(session.getType())) {
            return dequeueStatic(session);
        } else {
            return dequeueDynamic(session);
        }
    }

    private Map<String, Object> dequeueStatic(QueueSession session) {
        Map<String, Object> result = new HashMap<>();
        
        // Check if queue is empty
        if (session.getCurrentSize() == 0) {
            result.put("success", false);
            result.put("message", "Queue underflow - queue is empty");
            session.getOperationHistory().add(
                new QueueSession.OperationHistory("dequeue", 0, false, "Queue underflow")
            );
            sessionRepository.save(session);
            return result;
        }
        
        // Remove front element
        int dequeuedData = session.getElements().get(session.getFront());
        session.setCurrentSize(session.getCurrentSize() - 1);
        
        if (session.getCurrentSize() == 0) {
            // Queue becomes empty
            session.setFront(-1);
            session.setRear(-1);
        } else {
            // Move front pointer (circular queue)
            session.setFront((session.getFront() + 1) % session.getMaxSize());
        }
        
        // Add to operation history
        session.getOperationHistory().add(
            new QueueSession.OperationHistory("dequeue", dequeuedData, true, "Element dequeued successfully")
        );
        session.setUpdatedAt(System.currentTimeMillis());
        sessionRepository.save(session);
        
        result.put("success", true);
        result.put("message", "Element dequeued successfully");
        result.put("size", session.getCurrentSize());
        result.put("data", dequeuedData);
        result.put("front", session.getFront());
        
        return result;
    }

    private Map<String, Object> dequeueDynamic(QueueSession session) {
        Map<String, Object> result = new HashMap<>();
        
        // Check if queue is empty
        if (session.getCurrentSize() == 0) {
            result.put("success", false);
            result.put("message", "Queue underflow - queue is empty");
            session.getOperationHistory().add(
                new QueueSession.OperationHistory("dequeue", 0, false, "Queue underflow")
            );
            sessionRepository.save(session);
            return result;
        }
        
        // Get current front node by finding the node marked as front
        Optional<QueueNode> frontNodeOpt = nodeRepository.findBySessionIdAndUserIdAndIsFrontTrue(session.getSessionId(), session.getUserId());
        if (frontNodeOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Queue corruption - front node not found");
            return result;
        }
        
        QueueNode frontNode = frontNodeOpt.get();
        int dequeuedData = frontNode.getData();
        
        // Update new front node
        if (frontNode.getNextNodeId() != null) {
            Optional<QueueNode> nextNodeOpt = nodeRepository.findById(frontNode.getNextNodeId());
            if (nextNodeOpt.isPresent()) {
                QueueNode nextNode = nextNodeOpt.get();
                nextNode.setFront(true);
                
                // Update positions of remaining nodes
                List<QueueNode> remainingNodes = 
                    nodeRepository.findBySessionIdAndUserIdOrderByPosition(session.getSessionId(), session.getUserId());
                
                for (QueueNode node : remainingNodes) {
                    if (!node.getId().equals(frontNode.getId())) {
                        node.setPosition(node.getPosition() - 1);
                    }
                }
                nodeRepository.saveAll(remainingNodes);
                nodeRepository.save(nextNode);
                session.setFrontNodeId(nextNode.getId());
            }
        } else {
            // Queue becomes empty
            session.setFrontNodeId(null);
            session.setRearNodeId(null);
        }
        
        // Delete the dequeued node
        nodeRepository.delete(frontNode);
        
        // Update session
        session.setCurrentSize(session.getCurrentSize() - 1);
        session.getNodeIds().remove(frontNode.getId());
        session.getOperationHistory().add(
            new QueueSession.OperationHistory("dequeue", dequeuedData, true, "Element dequeued successfully")
        );
        session.setUpdatedAt(System.currentTimeMillis());
        sessionRepository.save(session);
        
        result.put("success", true);
        result.put("message", "Element dequeued successfully");
        result.put("size", session.getCurrentSize());
        result.put("data", dequeuedData);
        
        return result;
    }

    // Peek operation (view front element without removing)
    public Map<String, Object> peek(String sessionId, String userId) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<QueueSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        QueueSession session = sessionOpt.get();
        
        // Check if queue is empty
        if (session.getCurrentSize() == 0) {
            result.put("success", false);
            result.put("message", "Queue is empty");
            return result;
        }
        
        if ("static".equals(session.getType())) {
            int frontData = session.getElements().get(session.getFront());
            result.put("success", true);
            result.put("data", frontData);
            result.put("message", "Front element: " + frontData);
        } else {
            // For dynamic queue, find front node
            Optional<QueueNode> frontNodeOpt = nodeRepository.findBySessionIdAndUserIdAndIsFrontTrue(session.getSessionId(), session.getUserId());
            if (frontNodeOpt.isPresent()) {
                QueueNode frontNode = frontNodeOpt.get();
                result.put("success", true);
                result.put("data", frontNode.getData());
                result.put("nodeId", frontNode.getId());
                result.put("message", "Front element: " + frontNode.getData());
            } else {
                result.put("success", false);
                result.put("message", "Queue corruption - front node not found");
            }
        }
        
        return result;
    }

    // View entire queue
    public Map<String, Object> viewQueue(String sessionId, String userId) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<QueueSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        QueueSession session = sessionOpt.get();
        List<Integer> elements = new ArrayList<>();
        
        if ("static".equals(session.getType())) {
            // For static queue, get elements from front to rear in order
            if (session.getCurrentSize() > 0) {
                int current = session.getFront();
                for (int i = 0; i < session.getCurrentSize(); i++) {
                    elements.add(session.getElements().get(current));
                    current = (current + 1) % session.getMaxSize();
                }
            }
        } else {
            // For dynamic queue, traverse nodes from front to rear in order
            if (session.getCurrentSize() > 0) {
                List<QueueNode> orderedNodes = nodeRepository.findBySessionIdAndUserIdOrderByPosition(session.getSessionId(), session.getUserId());
                for (QueueNode node : orderedNodes) {
                    elements.add(node.getData());
                }
            }
        }
        
        // Create response with session data and elements
        Map<String, Object> sessionData = new HashMap<>();
        sessionData.put("id", session.getId());
        sessionData.put("sessionId", session.getSessionId());
        sessionData.put("userId", session.getUserId());
        sessionData.put("name", session.getSessionId()); // Using sessionId as name for now
        sessionData.put("type", session.getType());
        sessionData.put("size", session.getCurrentSize());
        sessionData.put("maxSize", session.getMaxSize());
        sessionData.put("elements", elements);
        sessionData.put("createdAt", session.getCreatedAt());
        sessionData.put("updatedAt", session.getUpdatedAt());
        
        result.put("success", true);
        result.put("message", "Queue retrieved successfully");
        result.put("data", sessionData);
        return result;
    }

    // Get all sessions for user
    public List<QueueSession> getUserSessions(String userId) {
        return sessionRepository.findByUserId(userId);
    }
}