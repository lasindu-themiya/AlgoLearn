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
@Document(collection = "search_sessions")
public class SearchSession {
    @Id
    private String id;
    private String sessionId;
    private String userId;
    private String algorithm; // "binary", "linear"
    private List<Integer> array;
    private int target;
    private boolean found;
    private int foundIndex;
    private int comparisons;
    private List<SearchStep> steps;
    private List<OperationHistory> operationHistory;
    private long createdAt;
    private long updatedAt;
    
    public SearchSession(String sessionId, String userId, String algorithm, List<Integer> array, int target) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.algorithm = algorithm;
        this.array = new ArrayList<>(array);
        this.target = target;
        this.found = false;
        this.foundIndex = -1;
        this.comparisons = 0;
        this.steps = new ArrayList<>();
        this.operationHistory = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchStep {
        private int currentIndex; // Current index being checked
        private int value; // Value at current index
        private boolean match; // Whether current value matches target
        private int left; // For binary search - left boundary
        private int right; // For binary search - right boundary
        private int mid; // For binary search - middle index
        private String description; // Step description
        private long timestamp;
        
        public SearchStep(int currentIndex, int value, boolean match, String description) {
            this.currentIndex = currentIndex;
            this.value = value;
            this.match = match;
            this.left = -1;
            this.right = -1;
            this.mid = -1;
            this.description = description;
            this.timestamp = System.currentTimeMillis();
        }
        
        public SearchStep(int currentIndex, int value, boolean match, int left, int right, int mid, String description) {
            this.currentIndex = currentIndex;
            this.value = value;
            this.match = match;
            this.left = left;
            this.right = right;
            this.mid = mid;
            this.description = description;
            this.timestamp = System.currentTimeMillis();
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperationHistory {
        private String operation; // "search"
        private String algorithm;
        private int arraySize;
        private int target;
        private boolean found;
        private int foundIndex;
        private boolean success;
        private String message;
        private int totalComparisons;
        private long duration; // milliseconds
        private long timestamp;
        
        public OperationHistory(String operation, String algorithm, int arraySize, int target, boolean found, int foundIndex, boolean success, String message, int totalComparisons, long duration) {
            this.operation = operation;
            this.algorithm = algorithm;
            this.arraySize = arraySize;
            this.target = target;
            this.found = found;
            this.foundIndex = foundIndex;
            this.success = success;
            this.message = message;
            this.totalComparisons = totalComparisons;
            this.duration = duration;
            this.timestamp = System.currentTimeMillis();
        }
    }
}