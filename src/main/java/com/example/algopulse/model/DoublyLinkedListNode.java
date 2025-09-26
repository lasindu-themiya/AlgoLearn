package com.example.algopulse.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "doubly_linked_list_nodes")
public class DoublyLinkedListNode {
    @Id
    private String id;
    private String sessionId;
    private String userId;
    private int data;
    private String nextNodeId; // Reference to next node ID
    private String prevNodeId; // Reference to previous node ID
    private boolean isHead;
    private boolean isTail;
    private int position; // Position in the list
    private long timestamp;
    
    public DoublyLinkedListNode(String sessionId, String userId, int data) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
        this.isHead = false;
        this.isTail = false;
        this.position = 0;
    }
}