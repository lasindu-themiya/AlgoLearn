package com.example.algopulse.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stack_nodes")
public class StackNode {
    @Id
    private String id;
    private String sessionId;
    private String userId;
    private int data;
    private String nextNodeId; // For node-based stack implementation
    private boolean isTop;
    private int position; // Position from top (0 = top)
    private long timestamp;
    
    public StackNode(String sessionId, String userId, int data) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
        this.isTop = false;
        this.position = 0;
    }
}