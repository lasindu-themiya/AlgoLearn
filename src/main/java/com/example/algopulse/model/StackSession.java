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
@Document(collection = "stacks")
public class StackSession {
    @Id
    private String id;
    private String sessionId;
    private String userId;
    private String type; // "static" or "dynamic"
    private int maxSize; // For static stack
    private int currentSize;
    private String topNodeId; // For dynamic stack
    private List<Integer> elements; // For static stack
    private List<String> nodeIds; // For dynamic stack (ordered from bottom to top)
    private List<OperationHistory> operationHistory;
    private long createdAt;
    private long updatedAt;
    
    public StackSession(String sessionId, String userId, String type, int maxSize) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.type = type;
        this.maxSize = maxSize;
        this.currentSize = 0;
        this.elements = new ArrayList<>();
        this.nodeIds = new ArrayList<>();
        this.operationHistory = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperationHistory {
        private String operation; // "push", "pop", "peek"
        private int data;
        private boolean success;
        private String message;
        private long timestamp;
        
        public OperationHistory(String operation, int data, boolean success, String message) {
            this.operation = operation;
            this.data = data;
            this.success = success;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }
    }
}