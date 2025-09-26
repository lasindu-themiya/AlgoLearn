package com.example.algopulse.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataStructureRequest {
    private String sessionId;
    private String type; // "singly", "doubly", "static", "dynamic"
    private int maxSize; // For static structures
    private int data; // For operations
    private int position; // For specific position operations
}