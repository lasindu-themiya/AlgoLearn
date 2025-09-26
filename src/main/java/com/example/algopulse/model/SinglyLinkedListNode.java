package com.example.algopulse.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "singly_linked_list_nodes")
public class SinglyLinkedListNode {
    @Id
    private String id;
    private String sessionId;
    private String userId;
    private int data;
    private String nextNodeId; // Reference to next node ID
    private boolean isHead;
    private int position; // Position in the list
    private long timestamp;
    
    public SinglyLinkedListNode(String sessionId, String userId, int data) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
        this.isHead = false;
        this.position = 0;
    }
}