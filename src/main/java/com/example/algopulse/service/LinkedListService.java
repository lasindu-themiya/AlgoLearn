package com.example.algopulse.service;

import com.example.algopulse.model.*;
import com.example.algopulse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class LinkedListService {

    @Autowired
    private LinkedListSessionRepository sessionRepository;
    
    @Autowired
    private SinglyLinkedListNodeRepository singlyNodeRepository;
    
    @Autowired
    private DoublyLinkedListNodeRepository doublyNodeRepository;

    // Create new linked list session
    public LinkedListSession createLinkedList(String sessionId, String userId, String type) {
        // Check if session already exists
        Optional<LinkedListSession> existingSession = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (existingSession.isPresent()) {
            throw new RuntimeException("Session with ID '" + sessionId + "' already exists for this user");
        }
        
        LinkedListSession session = new LinkedListSession(sessionId, userId, type);
        return sessionRepository.save(session);
    }

    // Insert at tail (normal append) - matches your append() method
    public Map<String, Object> insertAtTail(String sessionId, String userId, int data) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<LinkedListSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        LinkedListSession session = sessionOpt.get();
        
        if ("singly".equals(session.getType())) {
            return appendSingly(session, data);
        } else {
            return insertDoubly(session, data);
        }
    }

    // Insert at head (append front) - matches your appendFront() method
    public Map<String, Object> insertAtHead(String sessionId, String userId, int data) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<LinkedListSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        LinkedListSession session = sessionOpt.get();
        
        if ("singly".equals(session.getType())) {
            return appendFrontSingly(session, data);
        } else {
            return insertAtFrontDoubly(session, data);
        }
    }

    // Delete from head - matches your removeFront() method
    public Map<String, Object> deleteFromHead(String sessionId, String userId) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<LinkedListSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        LinkedListSession session = sessionOpt.get();
        
        if (session.getSize() == 0) {
            result.put("success", false);
            result.put("message", "List is empty");
            return result;
        }
        
        if ("singly".equals(session.getType())) {
            return removeFrontSingly(session);
        } else {
            return removeAtFrontDoubly(session);
        }
    }

    // Delete by value
    public Map<String, Object> deleteByValue(String sessionId, String userId, int data) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<LinkedListSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        LinkedListSession session = sessionOpt.get();
        
        if (session.getSize() == 0) {
            result.put("success", false);
            result.put("message", "List is empty");
            return result;
        }
        
        if ("singly".equals(session.getType())) {
            return deleteByValueSingly(session, data);
        } else {
            return deleteByValueDoubly(session, data);
        }
    }

    // Delete from tail - matches your removeLast() method
    public Map<String, Object> deleteFromTail(String sessionId, String userId) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<LinkedListSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        LinkedListSession session = sessionOpt.get();
        
        if (session.getSize() == 0) {
            result.put("success", false);
            result.put("message", "List is empty");
            return result;
        }
        
        if ("singly".equals(session.getType())) {
            return removeLastSingly(session);
        } else {
            return removeAtTailDoubly(session);
        }
    }

    // SINGLY LINKED LIST IMPLEMENTATIONS

    // Singly: append(int data) - insert at tail
    private Map<String, Object> appendSingly(LinkedListSession session, int data) {
        Map<String, Object> result = new HashMap<>();
        
        SinglyLinkedListNode newNode = new SinglyLinkedListNode();
        newNode.setSessionId(session.getSessionId());
        newNode.setData(data);
        newNode.setPosition(session.getSize());
        newNode.setNextNodeId(null);
        
        SinglyLinkedListNode savedNode = singlyNodeRepository.save(newNode);
        
        if (session.getHeadNodeId() == null) {
            // List is empty - new node becomes head
            session.setHeadNodeId(savedNode.getId());
            session.setTailNodeId(savedNode.getId());
        } else {
            // Traverse to end and append
            Optional<SinglyLinkedListNode> headOpt = singlyNodeRepository.findById(session.getHeadNodeId());
            if (headOpt.isPresent()) {
                SinglyLinkedListNode current = headOpt.get();
                
                // Find the last node
                while (current.getNextNodeId() != null) {
                    Optional<SinglyLinkedListNode> nextOpt = singlyNodeRepository.findById(current.getNextNodeId());
                    if (nextOpt.isPresent()) {
                        current = nextOpt.get();
                    } else {
                        break;
                    }
                }
                
                // Link last node to new node
                current.setNextNodeId(savedNode.getId());
                singlyNodeRepository.save(current);
                session.setTailNodeId(savedNode.getId());
            }
        }
        
        session.setSize(session.getSize() + 1);
        sessionRepository.save(session);
        
        result.put("success", true);
        result.put("message", "Node appended successfully");
        result.put("nodeId", savedNode.getId());
        result.put("size", session.getSize());
        return result;
    }

    // Singly: appendFront(int data) - insert at head
    private Map<String, Object> appendFrontSingly(LinkedListSession session, int data) {
        Map<String, Object> result = new HashMap<>();
        
        SinglyLinkedListNode newNode = new SinglyLinkedListNode();
        newNode.setSessionId(session.getSessionId());
        newNode.setData(data);
        newNode.setPosition(0);
        
        SinglyLinkedListNode savedNode = singlyNodeRepository.save(newNode);
        
        if (session.getHeadNodeId() == null) {
            // List is empty
            session.setHeadNodeId(savedNode.getId());
            session.setTailNodeId(savedNode.getId());
            savedNode.setNextNodeId(null);
        } else {
            // Link new node to current head
            savedNode.setNextNodeId(session.getHeadNodeId());
            session.setHeadNodeId(savedNode.getId());
        }
        
        singlyNodeRepository.save(savedNode);
        session.setSize(session.getSize() + 1);
        sessionRepository.save(session);
        
        result.put("success", true);
        result.put("message", "Node inserted at front successfully");
        result.put("nodeId", savedNode.getId());
        result.put("size", session.getSize());
        return result;
    }

    // Singly: removeFront()
    private Map<String, Object> removeFrontSingly(LinkedListSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (session.getHeadNodeId() == null) {
            result.put("success", false);
            result.put("message", "List is empty");
            return result;
        }
        
        Optional<SinglyLinkedListNode> headOpt = singlyNodeRepository.findById(session.getHeadNodeId());
        if (headOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Head node not found");
            return result;
        }
        
        SinglyLinkedListNode head = headOpt.get();
        int deletedData = head.getData();
        
        if (head.getNextNodeId() != null) {
            // Move head to next node
            session.setHeadNodeId(head.getNextNodeId());
        } else {
            // List becomes empty
            session.setHeadNodeId(null);
            session.setTailNodeId(null);
        }
        
        singlyNodeRepository.delete(head);
        session.setSize(session.getSize() - 1);
        sessionRepository.save(session);
        
        result.put("success", true);
        result.put("message", "Node removed from front successfully");
        result.put("deletedData", deletedData);
        result.put("size", session.getSize());
        return result;
    }

    // Singly: delete by value
    private Map<String, Object> deleteByValueSingly(LinkedListSession session, int data) {
        Map<String, Object> result = new HashMap<>();
        
        if (session.getHeadNodeId() == null) {
            result.put("success", false);
            result.put("message", "List is empty");
            return result;
        }
        
        Optional<SinglyLinkedListNode> headOpt = singlyNodeRepository.findById(session.getHeadNodeId());
        if (headOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Head node not found");
            return result;
        }
        
        SinglyLinkedListNode head = headOpt.get();
        
        // If head contains the data
        if (head.getData() == data) {
            return removeFrontSingly(session);
        }
        
        // Search for the node to delete
        SinglyLinkedListNode current = head;
        while (current.getNextNodeId() != null) {
            Optional<SinglyLinkedListNode> nextOpt = singlyNodeRepository.findById(current.getNextNodeId());
            if (nextOpt.isEmpty()) break;
            
            SinglyLinkedListNode next = nextOpt.get();
            if (next.getData() == data) {
                // Found the node to delete
                current.setNextNodeId(next.getNextNodeId());
                singlyNodeRepository.save(current);
                
                // Update tail if necessary
                if (next.getId().equals(session.getTailNodeId())) {
                    session.setTailNodeId(current.getId());
                }
                
                singlyNodeRepository.delete(next);
                session.setSize(session.getSize() - 1);
                sessionRepository.save(session);
                
                result.put("success", true);
                result.put("message", "Node with value " + data + " deleted successfully");
                result.put("deletedData", data);
                result.put("size", session.getSize());
                return result;
            }
            current = next;
        }
        
        result.put("success", false);
        result.put("message", "Value " + data + " not found in the list");
        return result;
    }

    // Singly: removeLast() - your exact implementation
    private Map<String, Object> removeLastSingly(LinkedListSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (session.getHeadNodeId() == null) {
            result.put("success", false);
            result.put("message", "List is empty");
            return result;
        }
        
        Optional<SinglyLinkedListNode> headOpt = singlyNodeRepository.findById(session.getHeadNodeId());
        if (headOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Head node not found");
            return result;
        }
        
        SinglyLinkedListNode head = headOpt.get();
        int deletedData;
        
        if (head.getNextNodeId() == null) {
            // head.next == null - only one element
            deletedData = head.getData();
            session.setHeadNodeId(null);
            session.setTailNodeId(null);
            singlyNodeRepository.delete(head);
        } else {
            // Find node where current.next.next == null
            SinglyLinkedListNode current = head;
            while (current.getNextNodeId() != null) {
                Optional<SinglyLinkedListNode> nextOpt = singlyNodeRepository.findById(current.getNextNodeId());
                if (nextOpt.isEmpty()) break;
                
                SinglyLinkedListNode next = nextOpt.get();
                if (next.getNextNodeId() == null) {
                    // current.next.next == null, so current.next is the last node
                    deletedData = next.getData();
                    current.setNextNodeId(null);
                    singlyNodeRepository.save(current);
                    session.setTailNodeId(current.getId());
                    singlyNodeRepository.delete(next);
                    break;
                }
                current = next;
            }
            deletedData = current.getData(); // fallback
        }
        
        session.setSize(session.getSize() - 1);
        sessionRepository.save(session);
        
        result.put("success", true);
        result.put("message", "Last node removed successfully");
        result.put("deletedData", deletedData);
        result.put("size", session.getSize());
        return result;
    }

    // DOUBLY LINKED LIST IMPLEMENTATIONS

    // Doubly: insert(int data) - insert at tail
    private Map<String, Object> insertDoubly(LinkedListSession session, int data) {
        Map<String, Object> result = new HashMap<>();
        
        DoublyLinkedListNode newNode = new DoublyLinkedListNode();
        newNode.setSessionId(session.getSessionId());
        newNode.setData(data);
        newNode.setPosition(session.getSize());
        
        DoublyLinkedListNode savedNode = doublyNodeRepository.save(newNode);
        
        if (session.getHeadNodeId() == null) {
            // List is empty - new node becomes both head and tail
            session.setHeadNodeId(savedNode.getId());
            session.setTailNodeId(savedNode.getId());
            savedNode.setPrevNodeId(null);
            savedNode.setNextNodeId(null);
        } else {
            // Add to tail
            Optional<DoublyLinkedListNode> tailOpt = doublyNodeRepository.findById(session.getTailNodeId());
            if (tailOpt.isPresent()) {
                DoublyLinkedListNode tail = tailOpt.get();
                tail.setNextNodeId(savedNode.getId());
                savedNode.setPrevNodeId(tail.getId());
                savedNode.setNextNodeId(null);
                doublyNodeRepository.save(tail);
                session.setTailNodeId(savedNode.getId());
            }
        }
        
        doublyNodeRepository.save(savedNode);
        session.setSize(session.getSize() + 1);
        sessionRepository.save(session);
        
        result.put("success", true);
        result.put("message", "Node inserted successfully");
        result.put("nodeId", savedNode.getId());
        result.put("size", session.getSize());
        return result;
    }

    // Doubly: insertAtFront(int data)
    private Map<String, Object> insertAtFrontDoubly(LinkedListSession session, int data) {
        Map<String, Object> result = new HashMap<>();
        
        DoublyLinkedListNode newNode = new DoublyLinkedListNode();
        newNode.setSessionId(session.getSessionId());
        newNode.setData(data);
        newNode.setPosition(0);
        
        DoublyLinkedListNode savedNode = doublyNodeRepository.save(newNode);
        
        if (session.getHeadNodeId() == null) {
            // List is empty - new node becomes both head and tail
            session.setHeadNodeId(savedNode.getId());
            session.setTailNodeId(savedNode.getId());
            savedNode.setPrevNodeId(null);
            savedNode.setNextNodeId(null);
        } else {
            // Insert at front
            Optional<DoublyLinkedListNode> headOpt = doublyNodeRepository.findById(session.getHeadNodeId());
            if (headOpt.isPresent()) {
                DoublyLinkedListNode head = headOpt.get();
                savedNode.setNextNodeId(head.getId());
                head.setPrevNodeId(savedNode.getId());
                savedNode.setPrevNodeId(null);
                doublyNodeRepository.save(head);
                session.setHeadNodeId(savedNode.getId());
            }
        }
        
        doublyNodeRepository.save(savedNode);
        session.setSize(session.getSize() + 1);
        sessionRepository.save(session);
        
        result.put("success", true);
        result.put("message", "Node inserted at front successfully");
        result.put("nodeId", savedNode.getId());
        result.put("size", session.getSize());
        return result;
    }

    // Doubly: removeAtFront()
    private Map<String, Object> removeAtFrontDoubly(LinkedListSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (session.getHeadNodeId() == null) {
            result.put("success", false);
            result.put("message", "List is empty");
            return result;
        }
        
        Optional<DoublyLinkedListNode> headOpt = doublyNodeRepository.findById(session.getHeadNodeId());
        if (headOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Head node not found");
            return result;
        }
        
        DoublyLinkedListNode head = headOpt.get();
        int deletedData = head.getData();
        
        if (head.getNextNodeId() != null) {
            // Move head to next node
            Optional<DoublyLinkedListNode> nextHeadOpt = doublyNodeRepository.findById(head.getNextNodeId());
            if (nextHeadOpt.isPresent()) {
                DoublyLinkedListNode nextHead = nextHeadOpt.get();
                nextHead.setPrevNodeId(null);
                doublyNodeRepository.save(nextHead);
                session.setHeadNodeId(nextHead.getId());
            }
        } else {
            // List becomes empty
            session.setHeadNodeId(null);
            session.setTailNodeId(null);
        }
        
        doublyNodeRepository.delete(head);
        session.setSize(session.getSize() - 1);
        sessionRepository.save(session);
        
        result.put("success", true);
        result.put("message", "Node removed from front successfully");
        result.put("deletedData", deletedData);
        result.put("size", session.getSize());
        return result;
    }

    // Doubly: delete by value
    private Map<String, Object> deleteByValueDoubly(LinkedListSession session, int data) {
        Map<String, Object> result = new HashMap<>();
        
        if (session.getHeadNodeId() == null) {
            result.put("success", false);
            result.put("message", "List is empty");
            return result;
        }
        
        // Search for the node with the given data
        String currentNodeId = session.getHeadNodeId();
        while (currentNodeId != null) {
            Optional<DoublyLinkedListNode> currentOpt = doublyNodeRepository.findById(currentNodeId);
            if (currentOpt.isEmpty()) break;
            
            DoublyLinkedListNode current = currentOpt.get();
            if (current.getData() == data) {
                // Found the node to delete
                
                // Update next node's prev pointer
                if (current.getNextNodeId() != null) {
                    Optional<DoublyLinkedListNode> nextOpt = doublyNodeRepository.findById(current.getNextNodeId());
                    if (nextOpt.isPresent()) {
                        DoublyLinkedListNode next = nextOpt.get();
                        next.setPrevNodeId(current.getPrevNodeId());
                        doublyNodeRepository.save(next);
                    }
                }
                
                // Update previous node's next pointer
                if (current.getPrevNodeId() != null) {
                    Optional<DoublyLinkedListNode> prevOpt = doublyNodeRepository.findById(current.getPrevNodeId());
                    if (prevOpt.isPresent()) {
                        DoublyLinkedListNode prev = prevOpt.get();
                        prev.setNextNodeId(current.getNextNodeId());
                        doublyNodeRepository.save(prev);
                    }
                }
                
                // Update head if necessary
                if (current.getId().equals(session.getHeadNodeId())) {
                    session.setHeadNodeId(current.getNextNodeId());
                }
                
                // Update tail if necessary
                if (current.getId().equals(session.getTailNodeId())) {
                    session.setTailNodeId(current.getPrevNodeId());
                }
                
                doublyNodeRepository.delete(current);
                session.setSize(session.getSize() - 1);
                sessionRepository.save(session);
                
                result.put("success", true);
                result.put("message", "Node with value " + data + " deleted successfully");
                result.put("deletedData", data);
                result.put("size", session.getSize());
                return result;
            }
            
            currentNodeId = current.getNextNodeId();
        }
        
        result.put("success", false);
        result.put("message", "Value " + data + " not found in the list");
        return result;
    }

    // Doubly: removeAtTail() - remove from tail
    private Map<String, Object> removeAtTailDoubly(LinkedListSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (session.getTailNodeId() == null) {
            result.put("success", false);
            result.put("message", "List is empty");
            return result;
        }
        
        Optional<DoublyLinkedListNode> tailOpt = doublyNodeRepository.findById(session.getTailNodeId());
        if (tailOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Tail node not found");
            return result;
        }
        
        DoublyLinkedListNode tail = tailOpt.get();
        int deletedData = tail.getData();
        
        if (tail.getPrevNodeId() != null) {
            // Update previous node to be new tail
            Optional<DoublyLinkedListNode> prevOpt = doublyNodeRepository.findById(tail.getPrevNodeId());
            if (prevOpt.isPresent()) {
                DoublyLinkedListNode prev = prevOpt.get();
                prev.setNextNodeId(null);
                doublyNodeRepository.save(prev);
                session.setTailNodeId(prev.getId());
            }
        } else {
            // List becomes empty
            session.setHeadNodeId(null);
            session.setTailNodeId(null);
        }
        
        doublyNodeRepository.delete(tail);
        session.setSize(session.getSize() - 1);
        sessionRepository.save(session);
        
        result.put("success", true);
        result.put("message", "Node removed from tail successfully");
        result.put("deletedData", deletedData);
        result.put("size", session.getSize());
        return result;
    }

    // Search for value
    public Map<String, Object> search(String sessionId, String userId, int data) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<LinkedListSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        LinkedListSession session = sessionOpt.get();
        
        if (session.getSize() == 0) {
            result.put("success", false);
            result.put("message", "List is empty");
            result.put("found", false);
            return result;
        }
        
        if ("singly".equals(session.getType())) {
            return searchSingly(session, data);
        } else {
            return searchDoubly(session, data);
        }
    }

    private Map<String, Object> searchSingly(LinkedListSession session, int data) {
        Map<String, Object> result = new HashMap<>();
        
        if (session.getHeadNodeId() == null) {
            result.put("success", true);
            result.put("found", false);
            result.put("message", "Value " + data + " not found");
            return result;
        }
        
        String currentNodeId = session.getHeadNodeId();
        int position = 0;
        
        while (currentNodeId != null) {
            Optional<SinglyLinkedListNode> currentOpt = singlyNodeRepository.findById(currentNodeId);
            if (currentOpt.isEmpty()) break;
            
            SinglyLinkedListNode current = currentOpt.get();
            if (current.getData() == data) {
                result.put("success", true);
                result.put("found", true);
                result.put("message", "Value " + data + " found at position " + position);
                result.put("position", position);
                result.put("nodeId", current.getId());
                return result;
            }
            
            currentNodeId = current.getNextNodeId();
            position++;
        }
        
        result.put("success", true);
        result.put("found", false);
        result.put("message", "Value " + data + " not found");
        return result;
    }

    private Map<String, Object> searchDoubly(LinkedListSession session, int data) {
        Map<String, Object> result = new HashMap<>();
        
        if (session.getHeadNodeId() == null) {
            result.put("success", true);
            result.put("found", false);
            result.put("message", "Value " + data + " not found");
            return result;
        }
        
        String currentNodeId = session.getHeadNodeId();
        int position = 0;
        
        while (currentNodeId != null) {
            Optional<DoublyLinkedListNode> currentOpt = doublyNodeRepository.findById(currentNodeId);
            if (currentOpt.isEmpty()) break;
            
            DoublyLinkedListNode current = currentOpt.get();
            if (current.getData() == data) {
                result.put("success", true);
                result.put("found", true);
                result.put("message", "Value " + data + " found at position " + position);
                result.put("position", position);
                result.put("nodeId", current.getId());
                return result;
            }
            
            currentNodeId = current.getNextNodeId();
            position++;
        }
        
        result.put("success", true);
        result.put("found", false);
        result.put("message", "Value " + data + " not found");
        return result;
    }

    // View entire list (printList equivalent)
    public Map<String, Object> viewList(String sessionId, String userId) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<LinkedListSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        LinkedListSession session = sessionOpt.get();
        List<Integer> elements = new ArrayList<>();
        
        // Traverse the linked list to get all elements in order
        if (session.getHeadNodeId() != null) {
            if ("singly".equals(session.getType())) {
                // Traverse singly linked list
                Optional<SinglyLinkedListNode> currentOpt = singlyNodeRepository.findById(session.getHeadNodeId());
                while (currentOpt.isPresent()) {
                    SinglyLinkedListNode current = currentOpt.get();
                    elements.add(current.getData());
                    
                    if (current.getNextNodeId() != null) {
                        currentOpt = singlyNodeRepository.findById(current.getNextNodeId());
                    } else {
                        break;
                    }
                }
            } else {
                // Traverse doubly linked list
                Optional<DoublyLinkedListNode> currentOpt = doublyNodeRepository.findById(session.getHeadNodeId());
                while (currentOpt.isPresent()) {
                    DoublyLinkedListNode current = currentOpt.get();
                    elements.add(current.getData());
                    
                    if (current.getNextNodeId() != null) {
                        currentOpt = doublyNodeRepository.findById(current.getNextNodeId());
                    } else {
                        break;
                    }
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
        sessionData.put("size", session.getSize());
        sessionData.put("elements", elements);
        sessionData.put("createdAt", session.getCreatedAt());
        sessionData.put("updatedAt", session.getUpdatedAt());
        
        result.put("success", true);
        result.put("message", "List retrieved successfully");
        result.put("data", sessionData);
        return result;
    }

    private Map<String, Object> viewSinglyList(LinkedListSession session) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> listData = new ArrayList<>();
        
        if (session.getHeadNodeId() == null) {
            result.put("success", true);
            result.put("message", "List is empty");
            result.put("list", listData);
            result.put("size", 0);
            return result;
        }
        
        String currentNodeId = session.getHeadNodeId();
        int position = 0;
        
        while (currentNodeId != null) {
            Optional<SinglyLinkedListNode> currentOpt = singlyNodeRepository.findById(currentNodeId);
            if (currentOpt.isEmpty()) break;
            
            SinglyLinkedListNode current = currentOpt.get();
            Map<String, Object> nodeData = new HashMap<>();
            nodeData.put("data", current.getData());
            nodeData.put("position", position);
            nodeData.put("nodeId", current.getId());
            nodeData.put("nextNodeId", current.getNextNodeId());
            listData.add(nodeData);
            
            currentNodeId = current.getNextNodeId();
            position++;
        }
        
        result.put("success", true);
        result.put("message", "Singly linked list retrieved successfully");
        result.put("list", listData);
        result.put("size", session.getSize());
        result.put("type", "singly");
        return result;
    }

    private Map<String, Object> viewDoublyList(LinkedListSession session) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> listData = new ArrayList<>();
        
        if (session.getHeadNodeId() == null) {
            result.put("success", true);
            result.put("message", "List is empty");
            result.put("list", listData);
            result.put("size", 0);
            return result;
        }
        
        String currentNodeId = session.getHeadNodeId();
        int position = 0;
        
        while (currentNodeId != null) {
            Optional<DoublyLinkedListNode> currentOpt = doublyNodeRepository.findById(currentNodeId);
            if (currentOpt.isEmpty()) break;
            
            DoublyLinkedListNode current = currentOpt.get();
            Map<String, Object> nodeData = new HashMap<>();
            nodeData.put("data", current.getData());
            nodeData.put("position", position);
            nodeData.put("nodeId", current.getId());
            nodeData.put("nextNodeId", current.getNextNodeId());
            nodeData.put("prevNodeId", current.getPrevNodeId());
            listData.add(nodeData);
            
            currentNodeId = current.getNextNodeId();
            position++;
        }
        
        result.put("success", true);
        result.put("message", "Doubly linked list retrieved successfully");
        result.put("list", listData);
        result.put("size", session.getSize());
        result.put("type", "doubly");
        return result;
    }

    // Insert at given index (doubly linked list only)
    public Map<String, Object> insertAtGivenIndex(String sessionId, String userId, int data, int index) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<LinkedListSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        LinkedListSession session = sessionOpt.get();
        
        if (!"doubly".equals(session.getType())) {
            result.put("success", false);
            result.put("message", "Insert at given index is only supported for doubly linked lists");
            return result;
        }
        
        if (index < 0) {
            result.put("success", false);
            result.put("message", "Index cannot be negative");
            return result;
        }
        
        // If index is 0, insert at front
        if (index == 0) {
            return insertAtFrontDoubly(session, data);
        }
        
        // Find the node at index-1 position (following your algorithm exactly)
        String headNodeId = session.getHeadNodeId();
        if (headNodeId == null) {
            // Empty list, can only insert at index 0
            if (index == 0) {
                return insertAtFrontDoubly(session, data);
            } else {
                result.put("success", false);
                result.put("message", "Index " + index + " is out of bounds for empty list");
                return result;
            }
        }
        
        Optional<DoublyLinkedListNode> headOpt = doublyNodeRepository.findById(headNodeId);
        if (headOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Head node not found");
            return result;
        }
        
        DoublyLinkedListNode temp = headOpt.get();
        int count = 0;
        
        // Traverse to index-1 position
        while (temp != null && count < index - 1) {
            if (temp.getNextNodeId() != null) {
                Optional<DoublyLinkedListNode> nextOpt = doublyNodeRepository.findById(temp.getNextNodeId());
                if (nextOpt.isPresent()) {
                    temp = nextOpt.get();
                } else {
                    temp = null;
                }
            } else {
                temp = null;
            }
            count++;
        }
        
        // If temp is null or temp.next is null, insert at tail
        if (temp == null || temp.getNextNodeId() == null) {
            return insertDoubly(session, data);
        } else {
            // Insert in the middle (your algorithm: newNode.next = temp.next; newNode.prev = temp; temp.next.prev = newNode; temp.next = newNode;)
            DoublyLinkedListNode newNode = new DoublyLinkedListNode();
            newNode.setSessionId(session.getSessionId());
            newNode.setData(data);
            newNode.setPosition(index);
            
            // Get temp.next node
            Optional<DoublyLinkedListNode> tempNextOpt = doublyNodeRepository.findById(temp.getNextNodeId());
            if (tempNextOpt.isPresent()) {
                DoublyLinkedListNode tempNext = tempNextOpt.get();
                
                // Apply your algorithm exactly
                newNode.setNextNodeId(tempNext.getId());  // newNode.next = temp.next
                newNode.setPrevNodeId(temp.getId());      // newNode.prev = temp
                
                DoublyLinkedListNode savedNode = doublyNodeRepository.save(newNode);
                
                tempNext.setPrevNodeId(savedNode.getId()); // temp.next.prev = newNode
                temp.setNextNodeId(savedNode.getId());     // temp.next = newNode
                
                // Save all affected nodes
                doublyNodeRepository.save(tempNext);
                doublyNodeRepository.save(temp);
                
                session.setSize(session.getSize() + 1);
                sessionRepository.save(session);
                
                result.put("success", true);
                result.put("message", "Node inserted at index " + index + " successfully");
                result.put("nodeId", savedNode.getId());
                result.put("size", session.getSize());
                return result;
            } else {
                result.put("success", false);
                result.put("message", "Next node not found");
                return result;
            }
        }
    }
    
    // Remove at given index (doubly linked list only)
    public Map<String, Object> removeAtGivenIndex(String sessionId, String userId, int index) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<LinkedListSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        LinkedListSession session = sessionOpt.get();
        
        if (!"doubly".equals(session.getType())) {
            result.put("success", false);
            result.put("message", "Remove at given index is only supported for doubly linked lists");
            return result;
        }
        
        if (index < 0) {
            result.put("success", false);
            result.put("message", "Index cannot be negative");
            return result;
        }
        
        if (session.getHeadNodeId() == null) {
            result.put("success", false);
            result.put("message", "List is empty");
            return result;
        }
        
        // If index is 0, remove from front
        if (index == 0) {
            return removeAtFrontDoubly(session);
        }
        
        // Find the node at the given index (following your algorithm exactly)
        String headNodeId = session.getHeadNodeId();
        Optional<DoublyLinkedListNode> headOpt = doublyNodeRepository.findById(headNodeId);
        if (headOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Head node not found");
            return result;
        }
        
        DoublyLinkedListNode temp = headOpt.get();
        int count = 0;
        
        // Traverse to the index position
        while (temp != null && count < index) {
            if (temp.getNextNodeId() != null) {
                Optional<DoublyLinkedListNode> nextOpt = doublyNodeRepository.findById(temp.getNextNodeId());
                if (nextOpt.isPresent()) {
                    temp = nextOpt.get();
                } else {
                    temp = null;
                }
            } else {
                temp = null;
            }
            count++;
        }
        
        // If temp is null, index is out of bounds
        if (temp == null) {
            result.put("success", false);
            result.put("message", "Index " + index + " is out of bounds");
            return result;
        }
        
        int deletedData = temp.getData();
        
        // Apply your algorithm exactly:
        // if (temp.next != null) temp.next.prev = temp.prev;
        // if (temp.prev != null) temp.prev.next = temp.next;
        // if (temp == tail) tail = temp.prev;
        
        if (temp.getNextNodeId() != null) {
            Optional<DoublyLinkedListNode> nextOpt = doublyNodeRepository.findById(temp.getNextNodeId());
            if (nextOpt.isPresent()) {
                DoublyLinkedListNode next = nextOpt.get();
                next.setPrevNodeId(temp.getPrevNodeId()); // temp.next.prev = temp.prev
                doublyNodeRepository.save(next);
            }
        }
        
        if (temp.getPrevNodeId() != null) {
            Optional<DoublyLinkedListNode> prevOpt = doublyNodeRepository.findById(temp.getPrevNodeId());
            if (prevOpt.isPresent()) {
                DoublyLinkedListNode prev = prevOpt.get();
                prev.setNextNodeId(temp.getNextNodeId()); // temp.prev.next = temp.next
                doublyNodeRepository.save(prev);
            }
        }
        
        // If temp == tail, tail = temp.prev
        if (temp.getId().equals(session.getTailNodeId())) {
            session.setTailNodeId(temp.getPrevNodeId());
        }
        
        // Also handle head update (shouldn't happen for index > 0, but for safety)
        if (temp.getId().equals(session.getHeadNodeId())) {
            session.setHeadNodeId(temp.getNextNodeId());
        }
        
        doublyNodeRepository.delete(temp);
        session.setSize(session.getSize() - 1);
        sessionRepository.save(session);
        
        result.put("success", true);
        result.put("message", "Node at index " + index + " removed successfully");
        result.put("deletedData", deletedData);
        result.put("size", session.getSize());
        return result;
    }

    // Clear/Reset LinkedList (remove all elements)
    public Map<String, Object> clearLinkedList(String sessionId, String userId) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<LinkedListSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
        if (sessionOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "Session not found");
            return result;
        }
        
        LinkedListSession session = sessionOpt.get();
        
        try {
            if ("singly".equals(session.getType())) {
                // Clear all singly linked list nodes
                singlyNodeRepository.deleteBySessionId(sessionId);
            } else {
                // Clear all doubly linked list nodes
                doublyNodeRepository.deleteBySessionId(sessionId);
            }
            
            // Reset session properties
            session.setHeadNodeId(null);
            session.setTailNodeId(null);
            session.setSize(0);
            session.getNodeIds().clear();
            session.getOperationHistory().clear();
            session.setUpdatedAt(System.currentTimeMillis());
            
            sessionRepository.save(session);
            
            result.put("success", true);
            result.put("message", "LinkedList cleared successfully");
            result.put("size", 0);
            return result;
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error clearing LinkedList: " + e.getMessage());
            return result;
        }
    }

    // Get all user sessions
    public List<LinkedListSession> getUserSessions(String userId) {
        return sessionRepository.findByUserId(userId);
    }
}