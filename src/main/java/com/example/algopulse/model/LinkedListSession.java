package com.example.algopulse.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "linked_lists")
public class LinkedListSession {
    @Id
    private String id;
    private String sessionId;
    private String userId;
    private String type; // "singly" or "doubly"
    private String headNodeId;
    private String tailNodeId; // For doubly linked list
    private int size;
    private List<String> nodeIds; // Ordered list of node IDs
    private List<OperationHistory> operationHistory;
    private long createdAt;
    private long updatedAt;
    
    public LinkedListSession(String sessionId, String userId, String type) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.type = type;
        this.size = 0;
        this.nodeIds = new ArrayList<>();
        this.operationHistory = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperationHistory {
        private String operation; // "insert", "delete", "search"
        private int data;
        private int position;
        private boolean success;
        private String message;
        private long timestamp;
        
        public OperationHistory(String operation, int data, int position, boolean success, String message) {
            this.operation = operation;
            this.data = data;
            this.position = position;
            this.success = success;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }
    }
}