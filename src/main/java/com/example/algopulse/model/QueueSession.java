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
@Document(collection = "queues")
public class QueueSession {
    @Id
    private String id;
    private String sessionId;
    private String userId;
    private String type; // "static" or "dynamic"
    private int maxSize; // For static queue
    private int currentSize;
    private int front; // For static queue
    private int rear; // For static queue
    private String frontNodeId; // For dynamic queue
    private String rearNodeId; // For dynamic queue
    private List<Integer> elements; // For static queue
    private List<String> nodeIds; // For dynamic queue (ordered from front to rear)
    private List<OperationHistory> operationHistory;
    private long createdAt;
    private long updatedAt;
    
    public QueueSession(String sessionId, String userId, String type, int maxSize) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.type = type;
        this.maxSize = maxSize;
        this.currentSize = 0;
        this.front = -1;
        this.rear = -1;
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
        private String operation; // "enqueue", "dequeue", "peek"
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