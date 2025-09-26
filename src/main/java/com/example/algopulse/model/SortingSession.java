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
@Document(collection = "sorting_sessions")
public class SortingSession {
    @Id
    private String id;
    private String sessionId;
    private String userId;
    private String algorithm; // "bubble", "insertion", "selection", "min", "optimized_bubble"
    private List<Integer> originalArray;
    private List<Integer> currentArray;
    private boolean completed;
    private int comparisons;
    private int swaps;
    private List<SortStep> steps;
    private List<OperationHistory> operationHistory;
    private long createdAt;
    private long updatedAt;
    
    public SortingSession(String sessionId, String userId, String algorithm, List<Integer> array) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.algorithm = algorithm;
        this.originalArray = new ArrayList<>(array);
        this.currentArray = new ArrayList<>(array);
        this.completed = false;
        this.comparisons = 0;
        this.swaps = 0;
        this.steps = new ArrayList<>();
        this.operationHistory = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SortStep {
        private List<Integer> array; // Snapshot of array at this step
        private int compareIndex1; // Index being compared
        private int compareIndex2; // Index being compared with
        private boolean swapped; // Whether a swap occurred
        private String description; // Step description
        private long timestamp;
        
        public SortStep(List<Integer> array, int compareIndex1, int compareIndex2, boolean swapped, String description) {
            this.array = new ArrayList<>(array);
            this.compareIndex1 = compareIndex1;
            this.compareIndex2 = compareIndex2;
            this.swapped = swapped;
            this.description = description;
            this.timestamp = System.currentTimeMillis();
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperationHistory {
        private String operation; // "sort"
        private String algorithm;
        private int arraySize;
        private boolean success;
        private String message;
        private int totalComparisons;
        private int totalSwaps;
        private long duration; // milliseconds
        private long timestamp;
        
        public OperationHistory(String operation, String algorithm, int arraySize, boolean success, String message, int totalComparisons, int totalSwaps, long duration) {
            this.operation = operation;
            this.algorithm = algorithm;
            this.arraySize = arraySize;
            this.success = success;
            this.message = message;
            this.totalComparisons = totalComparisons;
            this.totalSwaps = totalSwaps;
            this.duration = duration;
            this.timestamp = System.currentTimeMillis();
        }
    }
}