package com.example.algopulse.service;

import com.example.algopulse.model.SortingSession;
import com.example.algopulse.repository.SortingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SortingService {

    @Autowired
    private SortingSessionRepository sessionRepository;

    // Bubble Sort Implementation
    public Map<String, Object> bubbleSort(String sessionId, String userId, List<Integer> array) {
        long startTime = System.currentTimeMillis();
        Map<String, Object> result = new HashMap<>();
        
        try {
            SortingSession session = new SortingSession(sessionId, userId, "bubble", array);
            List<Integer> arr = new ArrayList<>(session.getCurrentArray());
            int n = arr.size();
            
            // Add initial state
            session.getSteps().add(new SortingSession.SortStep(arr, -1, -1, false, "Initial array"));
            
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    session.setComparisons(session.getComparisons() + 1);
                    
                    // Add comparison step
                    session.getSteps().add(new SortingSession.SortStep(arr, j, j + 1, false, 
                        String.format("Comparing arr[%d]=%d with arr[%d]=%d", j, arr.get(j), j + 1, arr.get(j + 1))));
                    
                    if (arr.get(j) > arr.get(j + 1)) {
                        // Swap elements
                        Collections.swap(arr, j, j + 1);
                        session.setSwaps(session.getSwaps() + 1);
                        
                        // Add swap step
                        session.getSteps().add(new SortingSession.SortStep(arr, j, j + 1, true, 
                            String.format("Swapped arr[%d] and arr[%d]", j, j + 1)));
                    }
                }
                // Add pass completion step
                session.getSteps().add(new SortingSession.SortStep(arr, -1, -1, false, 
                    String.format("Pass %d completed. Element %d is in its final position", i + 1, arr.get(n - i - 1))));
            }
            
            session.setCurrentArray(arr);
            session.setCompleted(true);
            session.setUpdatedAt(System.currentTimeMillis());
            
            long duration = System.currentTimeMillis() - startTime;
            session.getOperationHistory().add(new SortingSession.OperationHistory(
                "sort", "bubble", array.size(), true, "Bubble sort completed successfully", 
                session.getComparisons(), session.getSwaps(), duration));
            
            sessionRepository.save(session);
            
            result.put("success", true);
            result.put("message", "Bubble sort completed successfully");
            result.put("session", session);
            result.put("sortedArray", arr);
            result.put("comparisons", session.getComparisons());
            result.put("swaps", session.getSwaps());
            result.put("steps", session.getSteps());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error during bubble sort: " + e.getMessage());
        }
        
        return result;
    }

    // Insertion Sort Implementation
    public Map<String, Object> insertionSort(String sessionId, String userId, List<Integer> array) {
        long startTime = System.currentTimeMillis();
        Map<String, Object> result = new HashMap<>();
        
        try {
            SortingSession session = new SortingSession(sessionId, userId, "insertion", array);
            List<Integer> arr = new ArrayList<>(session.getCurrentArray());
            int n = arr.size();
            
            // Add initial state
            session.getSteps().add(new SortingSession.SortStep(arr, -1, -1, false, "Initial array"));
            
            for (int i = 1; i < n; i++) {
                int key = arr.get(i);
                int j = i - 1;
                
                session.getSteps().add(new SortingSession.SortStep(arr, i, -1, false, 
                    String.format("Taking key = arr[%d] = %d", i, key)));
                
                while (j >= 0 && arr.get(j) > key) {
                    session.setComparisons(session.getComparisons() + 1);
                    
                    session.getSteps().add(new SortingSession.SortStep(arr, j, i, false, 
                        String.format("Comparing arr[%d]=%d with key=%d", j, arr.get(j), key)));
                    
                    arr.set(j + 1, arr.get(j));
                    session.setSwaps(session.getSwaps() + 1);
                    
                    session.getSteps().add(new SortingSession.SortStep(arr, j, j + 1, true, 
                        String.format("Shifted arr[%d]=%d to position %d", j, arr.get(j + 1), j + 1)));
                    
                    j--;
                }
                
                if (j >= 0) {
                    session.setComparisons(session.getComparisons() + 1);
                }
                
                arr.set(j + 1, key);
                session.getSteps().add(new SortingSession.SortStep(arr, j + 1, -1, false, 
                    String.format("Inserted key=%d at position %d", key, j + 1)));
            }
            
            session.setCurrentArray(arr);
            session.setCompleted(true);
            session.setUpdatedAt(System.currentTimeMillis());
            
            long duration = System.currentTimeMillis() - startTime;
            session.getOperationHistory().add(new SortingSession.OperationHistory(
                "sort", "insertion", array.size(), true, "Insertion sort completed successfully", 
                session.getComparisons(), session.getSwaps(), duration));
            
            sessionRepository.save(session);
            
            result.put("success", true);
            result.put("message", "Insertion sort completed successfully");
            result.put("session", session);
            result.put("sortedArray", arr);
            result.put("comparisons", session.getComparisons());
            result.put("swaps", session.getSwaps());
            result.put("steps", session.getSteps());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error during insertion sort: " + e.getMessage());
        }
        
        return result;
    }

    // Selection Sort Implementation
    public Map<String, Object> selectionSort(String sessionId, String userId, List<Integer> array) {
        long startTime = System.currentTimeMillis();
        Map<String, Object> result = new HashMap<>();
        
        try {
            SortingSession session = new SortingSession(sessionId, userId, "selection", array);
            List<Integer> arr = new ArrayList<>(session.getCurrentArray());
            int n = arr.size();
            
            // Add initial state
            session.getSteps().add(new SortingSession.SortStep(arr, -1, -1, false, "Initial array"));
            
            for (int i = 0; i < n - 1; i++) {
                int minIndex = i;
                
                session.getSteps().add(new SortingSession.SortStep(arr, i, -1, false, 
                    String.format("Finding minimum from position %d to %d", i, n - 1)));
                
                for (int j = i + 1; j < n; j++) {
                    session.setComparisons(session.getComparisons() + 1);
                    
                    session.getSteps().add(new SortingSession.SortStep(arr, minIndex, j, false, 
                        String.format("Comparing arr[%d]=%d with arr[%d]=%d", minIndex, arr.get(minIndex), j, arr.get(j))));
                    
                    if (arr.get(j) < arr.get(minIndex)) {
                        minIndex = j;
                        session.getSteps().add(new SortingSession.SortStep(arr, minIndex, -1, false, 
                            String.format("New minimum found at index %d with value %d", minIndex, arr.get(minIndex))));
                    }
                }
                
                if (minIndex != i) {
                    Collections.swap(arr, i, minIndex);
                    session.setSwaps(session.getSwaps() + 1);
                    
                    session.getSteps().add(new SortingSession.SortStep(arr, i, minIndex, true, 
                        String.format("Swapped arr[%d] and arr[%d]", i, minIndex)));
                }
                
                session.getSteps().add(new SortingSession.SortStep(arr, -1, -1, false, 
                    String.format("Position %d sorted with value %d", i, arr.get(i))));
            }
            
            session.setCurrentArray(arr);
            session.setCompleted(true);
            session.setUpdatedAt(System.currentTimeMillis());
            
            long duration = System.currentTimeMillis() - startTime;
            session.getOperationHistory().add(new SortingSession.OperationHistory(
                "sort", "selection", array.size(), true, "Selection sort completed successfully", 
                session.getComparisons(), session.getSwaps(), duration));
            
            sessionRepository.save(session);
            
            result.put("success", true);
            result.put("message", "Selection sort completed successfully");
            result.put("session", session);
            result.put("sortedArray", arr);
            result.put("comparisons", session.getComparisons());
            result.put("swaps", session.getSwaps());
            result.put("steps", session.getSteps());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error during selection sort: " + e.getMessage());
        }
        
        return result;
    }

    // Min Sort Implementation (similar to selection sort, but finds minimum and places it at the beginning)
    public Map<String, Object> minSort(String sessionId, String userId, List<Integer> array) {
        long startTime = System.currentTimeMillis();
        Map<String, Object> result = new HashMap<>();
        
        try {
            SortingSession session = new SortingSession(sessionId, userId, "min", array);
            List<Integer> arr = new ArrayList<>(session.getCurrentArray());
            int n = arr.size();
            
            // Add initial state
            session.getSteps().add(new SortingSession.SortStep(arr, -1, -1, false, "Initial array"));
            
            for (int i = 0; i < n - 1; i++) {
                int minIndex = i;
                int minValue = arr.get(i);
                
                session.getSteps().add(new SortingSession.SortStep(arr, i, -1, false, 
                    String.format("Pass %d: Finding minimum from remaining elements", i + 1)));
                
                // Find the minimum element in the remaining array
                for (int j = i + 1; j < n; j++) {
                    session.setComparisons(session.getComparisons() + 1);
                    
                    session.getSteps().add(new SortingSession.SortStep(arr, minIndex, j, false, 
                        String.format("Comparing current min arr[%d]=%d with arr[%d]=%d", minIndex, minValue, j, arr.get(j))));
                    
                    if (arr.get(j) < minValue) {
                        minIndex = j;
                        minValue = arr.get(j);
                        session.getSteps().add(new SortingSession.SortStep(arr, minIndex, -1, false, 
                            String.format("New minimum found: arr[%d]=%d", minIndex, minValue)));
                    }
                }
                
                // Swap if needed
                if (minIndex != i) {
                    Collections.swap(arr, i, minIndex);
                    session.setSwaps(session.getSwaps() + 1);
                    
                    session.getSteps().add(new SortingSession.SortStep(arr, i, minIndex, true, 
                        String.format("Swapped minimum value %d to position %d", minValue, i)));
                }
                
                session.getSteps().add(new SortingSession.SortStep(arr, -1, -1, false, 
                    String.format("Minimum %d placed at position %d", arr.get(i), i)));
            }
            
            session.setCurrentArray(arr);
            session.setCompleted(true);
            session.setUpdatedAt(System.currentTimeMillis());
            
            long duration = System.currentTimeMillis() - startTime;
            session.getOperationHistory().add(new SortingSession.OperationHistory(
                "sort", "min", array.size(), true, "Min sort completed successfully", 
                session.getComparisons(), session.getSwaps(), duration));
            
            sessionRepository.save(session);
            
            result.put("success", true);
            result.put("message", "Min sort completed successfully");
            result.put("session", session);
            result.put("sortedArray", arr);
            result.put("comparisons", session.getComparisons());
            result.put("swaps", session.getSwaps());
            result.put("steps", session.getSteps());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error during min sort: " + e.getMessage());
        }
        
        return result;
    }

    // Optimized Bubble Sort Implementation
    public Map<String, Object> optimizedBubbleSort(String sessionId, String userId, List<Integer> array) {
        long startTime = System.currentTimeMillis();
        Map<String, Object> result = new HashMap<>();
        
        try {
            SortingSession session = new SortingSession(sessionId, userId, "optimized_bubble", array);
            List<Integer> arr = new ArrayList<>(session.getCurrentArray());
            int n = arr.size();
            
            // Add initial state
            session.getSteps().add(new SortingSession.SortStep(arr, -1, -1, false, "Initial array"));
            
            for (int i = 0; i < n - 1; i++) {
                boolean swapped = false;
                
                session.getSteps().add(new SortingSession.SortStep(arr, -1, -1, false, 
                    String.format("Pass %d: Checking for swaps", i + 1)));
                
                for (int j = 0; j < n - i - 1; j++) {
                    session.setComparisons(session.getComparisons() + 1);
                    
                    // Add comparison step
                    session.getSteps().add(new SortingSession.SortStep(arr, j, j + 1, false, 
                        String.format("Comparing arr[%d]=%d with arr[%d]=%d", j, arr.get(j), j + 1, arr.get(j + 1))));
                    
                    if (arr.get(j) > arr.get(j + 1)) {
                        // Swap elements
                        Collections.swap(arr, j, j + 1);
                        session.setSwaps(session.getSwaps() + 1);
                        swapped = true;
                        
                        // Add swap step
                        session.getSteps().add(new SortingSession.SortStep(arr, j, j + 1, true, 
                            String.format("Swapped arr[%d] and arr[%d]", j, j + 1)));
                    }
                }
                
                if (!swapped) {
                    session.getSteps().add(new SortingSession.SortStep(arr, -1, -1, false, 
                        "No swaps occurred in this pass. Array is already sorted!"));
                    break;
                }
                
                session.getSteps().add(new SortingSession.SortStep(arr, -1, -1, false, 
                    String.format("Pass %d completed. Element %d is in its final position", i + 1, arr.get(n - i - 1))));
            }
            
            session.setCurrentArray(arr);
            session.setCompleted(true);
            session.setUpdatedAt(System.currentTimeMillis());
            
            long duration = System.currentTimeMillis() - startTime;
            session.getOperationHistory().add(new SortingSession.OperationHistory(
                "sort", "optimized_bubble", array.size(), true, "Optimized bubble sort completed successfully", 
                session.getComparisons(), session.getSwaps(), duration));
            
            sessionRepository.save(session);
            
            result.put("success", true);
            result.put("message", "Optimized bubble sort completed successfully");
            result.put("session", session);
            result.put("sortedArray", arr);
            result.put("comparisons", session.getComparisons());
            result.put("swaps", session.getSwaps());
            result.put("steps", session.getSteps());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error during optimized bubble sort: " + e.getMessage());
        }
        
        return result;
    }

    // Get session by sessionId and userId
    public Map<String, Object> getSession(String sessionId, String userId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Optional<SortingSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
            if (sessionOpt.isEmpty()) {
                result.put("success", false);
                result.put("message", "Session not found");
                return result;
            }
            
            result.put("success", true);
            result.put("session", sessionOpt.get());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error retrieving session: " + e.getMessage());
        }
        
        return result;
    }

    // Get all sessions for a user
    public Map<String, Object> getUserSessions(String userId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<SortingSession> sessions = sessionRepository.findByUserId(userId);
            result.put("success", true);
            result.put("sessions", sessions);
            result.put("count", sessions.size());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error retrieving user sessions: " + e.getMessage());
        }
        
        return result;
    }

    // Delete session
    public Map<String, Object> deleteSession(String sessionId, String userId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Optional<SortingSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
            if (sessionOpt.isEmpty()) {
                result.put("success", false);
                result.put("message", "Session not found");
                return result;
            }
            
            sessionRepository.deleteBySessionIdAndUserId(sessionId, userId);
            result.put("success", true);
            result.put("message", "Session deleted successfully");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error deleting session: " + e.getMessage());
        }
        
        return result;
    }
}