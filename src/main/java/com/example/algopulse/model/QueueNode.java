package com.example.algopulse.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "queue_nodes")
public class QueueNode {
    @Id
    private String id;
    private String sessionId;
    private String userId;
    private int data;
    private String nextNodeId; // For node-based queue implementation
    private boolean isFront;
    private boolean isRear;
    private int position; // Position from front (0 = front)
    private long timestamp;
    
    public QueueNode(String sessionId, String userId, int data) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
        this.isFront = false;
        this.isRear = false;
        this.position = 0;
    }
}