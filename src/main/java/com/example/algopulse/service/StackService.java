package com.example.algopulse.service;

import com.example.algopulse.model.*;
import com.example.algopulse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class StackService {

    @Autowired
    private StackSessionRepository sessionRepository;
    
    @Autowired
    private StackNodeRepository nodeRepository;

    // Create new stack session
    public StackSession createStack(String sessionId, String userId, String type, int maxSize) {
        // Check if session already exists
        Optional<StackSession> existingSession = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (existingSession.isPresent()) {
            throw new RuntimeException("Session with ID '" + sessionId + "' already exists for this user");
        }
        
        StackSession session = new StackSession(sessionId, userId, type, maxSize);
        return sessionRepository.save(session);
    }

    // Push operation
    public Map<String, Object> push(String sessionId, String userId, int data) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<StackSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        StackSession session = sessionOpt.get();
        
        if ("static".equals(session.getType())) {
            return pushStatic(session, data);
        } else {
            return pushDynamic(session, data);
        }
    }

    private Map<String, Object> pushStatic(StackSession session, int data) {
        Map<String, Object> result = new HashMap<>();
        
        // Check if stack is full
        if (session.getCurrentSize() >= session.getMaxSize()) {
            result.put("success", false);
            result.put("message", "Stack overflow - maximum size reached");
            session.getOperationHistory().add(
                new StackSession.OperationHistory("push", data, false, "Stack overflow")
            );
            sessionRepository.save(session);
            return result;
        }
        
        // Add element to static array
        session.getElements().add(data);
        session.setCurrentSize(session.getCurrentSize() + 1);
        
        // Add to operation history
        session.getOperationHistory().add(
            new StackSession.OperationHistory("push", data, true, "Element pushed successfully")
        );
        session.setUpdatedAt(System.currentTimeMillis());
        sessionRepository.save(session);
        
        result.put("success", true);
        result.put("message", "Element pushed successfully");
        result.put("size", session.getCurrentSize());
        result.put("data", data);
        
        return result;
    }

    private Map<String, Object> pushDynamic(StackSession session, int data) {
        Map<String, Object> result = new HashMap<>();
        
        // Create new stack node
        StackNode newNode = new StackNode(session.getSessionId(), session.getUserId(), data);
        newNode.setPosition(session.getCurrentSize()); // Position from bottom
        
        // Get current top node if exists
        Optional<StackNode> currentTopOpt = 
            nodeRepository.findBySessionIdAndUserIdAndIsTopTrue(session.getSessionId(), session.getUserId());
        
        if (currentTopOpt.isPresent()) {
            // Update current top node
            StackNode currentTop = currentTopOpt.get();
            currentTop.setTop(false);
            nodeRepository.save(currentTop);
            
            // Link new node to current top
            newNode.setNextNodeId(currentTop.getId());
        }
        
        newNode.setTop(true);
        StackNode savedNode = nodeRepository.save(newNode);
        
        // Update session
        session.setTopNodeId(savedNode.getId());
        session.setCurrentSize(session.getCurrentSize() + 1);
        session.getNodeIds().add(savedNode.getId());
        session.getOperationHistory().add(
            new StackSession.OperationHistory("push", data, true, "Element pushed successfully")
        );
        session.setUpdatedAt(System.currentTimeMillis());
        sessionRepository.save(session);
        
        result.put("success", true);
        result.put("message", "Element pushed successfully");
        result.put("size", session.getCurrentSize());
        result.put("nodeId", savedNode.getId());
        result.put("data", data);
        
        return result;
    }

    // Pop operation
    public Map<String, Object> pop(String sessionId, String userId) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<StackSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        StackSession session = sessionOpt.get();
        
        if ("static".equals(session.getType())) {
            return popStatic(session);
        } else {
            return popDynamic(session);
        }
    }

    private Map<String, Object> popStatic(StackSession session) {
        Map<String, Object> result = new HashMap<>();
        
        // Check if stack is empty
        if (session.getCurrentSize() == 0) {
            result.put("success", false);
            result.put("message", "Stack underflow - stack is empty");
            session.getOperationHistory().add(
                new StackSession.OperationHistory("pop", 0, false, "Stack underflow")
            );
            sessionRepository.save(session);
            return result;
        }
        
        // Remove top element
        int poppedData = session.getElements().remove(session.getCurrentSize() - 1);
        session.setCurrentSize(session.getCurrentSize() - 1);
        
        // Add to operation history
        session.getOperationHistory().add(
            new StackSession.OperationHistory("pop", poppedData, true, "Element popped successfully")
        );
        session.setUpdatedAt(System.currentTimeMillis());
        sessionRepository.save(session);
        
        result.put("success", true);
        result.put("message", "Element popped successfully");
        result.put("size", session.getCurrentSize());
        result.put("data", poppedData);
        
        return result;
    }

    private Map<String, Object> popDynamic(StackSession session) {
        Map<String, Object> result = new HashMap<>();
        
        // Check if stack is empty
        if (session.getCurrentSize() == 0 || session.getTopNodeId() == null) {
            result.put("success", false);
            result.put("message", "Stack underflow - stack is empty");
            session.getOperationHistory().add(
                new StackSession.OperationHistory("pop", 0, false, "Stack underflow")
            );
            sessionRepository.save(session);
            return result;
        }
        
        // Get current top node
        Optional<StackNode> topNodeOpt = nodeRepository.findById(session.getTopNodeId());
        if (topNodeOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Stack corruption - top node not found");
            return result;
        }
        
        StackNode topNode = topNodeOpt.get();
        int poppedData = topNode.getData();
        
        // Update new top node
        if (topNode.getNextNodeId() != null) {
            Optional<StackNode> nextNodeOpt = nodeRepository.findById(topNode.getNextNodeId());
            if (nextNodeOpt.isPresent()) {
                StackNode nextNode = nextNodeOpt.get();
                nextNode.setTop(true);
                nodeRepository.save(nextNode);
                session.setTopNodeId(nextNode.getId());
            }
        } else {
            session.setTopNodeId(null);
        }
        
        // Delete the popped node
        nodeRepository.delete(topNode);
        
        // Update session
        session.setCurrentSize(session.getCurrentSize() - 1);
        session.getNodeIds().remove(topNode.getId());
        session.getOperationHistory().add(
            new StackSession.OperationHistory("pop", poppedData, true, "Element popped successfully")
        );
        session.setUpdatedAt(System.currentTimeMillis());
        sessionRepository.save(session);
        
        result.put("success", true);
        result.put("message", "Element popped successfully");
        result.put("size", session.getCurrentSize());
        result.put("data", poppedData);
        
        return result;
    }

    // Peek operation (view top element without removing)
    public Map<String, Object> peek(String sessionId, String userId) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<StackSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        StackSession session = sessionOpt.get();
        
        // Check if stack is empty
        if (session.getCurrentSize() == 0) {
            result.put("success", false);
            result.put("message", "Stack is empty");
            return result;
        }
        
        if ("static".equals(session.getType())) {
            int topData = session.getElements().get(session.getCurrentSize() - 1);
            result.put("success", true);
            result.put("data", topData);
            result.put("message", "Top element: " + topData);
        } else {
            Optional<StackNode> topNodeOpt = nodeRepository.findById(session.getTopNodeId());
            if (topNodeOpt.isPresent()) {
                StackNode topNode = topNodeOpt.get();
                result.put("success", true);
                result.put("data", topNode.getData());
                result.put("nodeId", topNode.getId());
                result.put("message", "Top element: " + topNode.getData());
            } else {
                result.put("success", false);
                result.put("message", "Stack corruption - top node not found");
            }
        }
        
        return result;
    }

    // View entire stack
    public Map<String, Object> viewStack(String sessionId, String userId) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<StackSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        StackSession session = sessionOpt.get();
        List<Map<String, Object>> stackView = new ArrayList<>();
        
        if ("static".equals(session.getType())) {
            // For static stack, show elements from top to bottom
            for (int i = session.getElements().size() - 1; i >= 0; i--) {
                Map<String, Object> element = new HashMap<>();
                element.put("position", i);
                element.put("data", session.getElements().get(i));
                element.put("isTop", i == session.getElements().size() - 1);
                stackView.add(element);
            }
        } else {
            // For dynamic stack, traverse from top to bottom
            List<StackNode> nodes = 
                nodeRepository.findBySessionIdAndUserIdOrderByPositionDesc(session.getSessionId(), session.getUserId());
            
            for (StackNode node : nodes) {
                Map<String, Object> nodeInfo = new HashMap<>();
                nodeInfo.put("id", node.getId());
                nodeInfo.put("data", node.getData());
                nodeInfo.put("position", node.getPosition());
                nodeInfo.put("isTop", node.isTop());
                nodeInfo.put("nextNodeId", node.getNextNodeId());
                stackView.add(nodeInfo);
            }
        }
        
        result.put("success", true);
        result.put("session", session);
        result.put("stack", stackView);
        result.put("size", session.getCurrentSize());
        result.put("maxSize", session.getMaxSize());
        
        return result;
    }

    // Get all sessions for user
    public List<StackSession> getUserSessions(String userId) {
        return sessionRepository.findByUserId(userId);
    }
}