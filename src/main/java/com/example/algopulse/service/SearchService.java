package com.example.algopulse.service;

import com.example.algopulse.model.SearchSession;
import com.example.algopulse.repository.SearchSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchService {

    @Autowired
    private SearchSessionRepository sessionRepository;

    // Linear Search Implementation
    public Map<String, Object> linearSearch(String sessionId, String userId, List<Integer> array, int target) {
        long startTime = System.currentTimeMillis();
        Map<String, Object> result = new HashMap<>();
        
        try {
            SearchSession session = new SearchSession(sessionId, userId, "linear", array, target);
            
            // Add initial state
            session.getSteps().add(new SearchSession.SearchStep(-1, -1, false, 
                String.format("Starting linear search for target %d in array %s", target, array.toString())));
            
            for (int i = 0; i < array.size(); i++) {
                session.setComparisons(session.getComparisons() + 1);
                int currentValue = array.get(i);
                boolean match = currentValue == target;
                
                // Add search step
                session.getSteps().add(new SearchSession.SearchStep(i, currentValue, match, 
                    String.format("Checking arr[%d] = %d %s target %d", 
                        i, currentValue, match ? "== (MATCH!)" : "!=", target)));
                
                if (match) {
                    session.setFound(true);
                    session.setFoundIndex(i);
                    
                    session.getSteps().add(new SearchSession.SearchStep(i, currentValue, true, 
                        String.format("Target %d found at index %d!", target, i)));
                    break;
                }
            }
            
            if (!session.isFound()) {
                session.getSteps().add(new SearchSession.SearchStep(-1, -1, false, 
                    String.format("Target %d not found in the array", target)));
            }
            
            session.setUpdatedAt(System.currentTimeMillis());
            
            long duration = System.currentTimeMillis() - startTime;
            session.getOperationHistory().add(new SearchSession.OperationHistory(
                "search", "linear", array.size(), target, session.isFound(), session.getFoundIndex(),
                true, "Linear search completed successfully", session.getComparisons(), duration));
            
            sessionRepository.save(session);
            
            result.put("success", true);
            result.put("message", session.isFound() ? 
                String.format("Target %d found at index %d", target, session.getFoundIndex()) :
                String.format("Target %d not found", target));
            result.put("session", session);
            result.put("found", session.isFound());
            result.put("foundIndex", session.getFoundIndex());
            result.put("comparisons", session.getComparisons());
            result.put("steps", session.getSteps());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error during linear search: " + e.getMessage());
        }
        
        return result;
    }

    // Binary Search Implementation
    public Map<String, Object> binarySearch(String sessionId, String userId, List<Integer> array, int target) {
        long startTime = System.currentTimeMillis();
        Map<String, Object> result = new HashMap<>();
        
        try {
            SearchSession session = new SearchSession(sessionId, userId, "binary", array, target);
            
            // Check if array is sorted
            boolean isSorted = true;
            for (int i = 0; i < array.size() - 1; i++) {
                if (array.get(i) > array.get(i + 1)) {
                    isSorted = false;
                    break;
                }
            }
            
            if (!isSorted) {
                result.put("success", false);
                result.put("message", "Binary search requires a sorted array. Please sort the array first.");
                return result;
            }
            
            // Add initial state
            session.getSteps().add(new SearchSession.SearchStep(-1, -1, false, 
                String.format("Starting binary search for target %d in sorted array %s", target, array.toString())));
            
            int left = 0;
            int right = array.size() - 1;
            
            while (left <= right) {
                session.setComparisons(session.getComparisons() + 1);
                int mid = left + (right - left) / 2;
                int midValue = array.get(mid);
                
                // Add search step
                session.getSteps().add(new SearchSession.SearchStep(mid, midValue, false, left, right, mid,
                    String.format("Searching in range [%d, %d]. Mid = %d, arr[%d] = %d", 
                        left, right, mid, mid, midValue)));
                
                if (midValue == target) {
                    session.setFound(true);
                    session.setFoundIndex(mid);
                    
                    session.getSteps().add(new SearchSession.SearchStep(mid, midValue, true, left, right, mid,
                        String.format("Target %d found at index %d!", target, mid)));
                    break;
                } else if (midValue < target) {
                    session.getSteps().add(new SearchSession.SearchStep(mid, midValue, false, left, right, mid,
                        String.format("arr[%d] = %d < target %d. Searching right half [%d, %d]", 
                            mid, midValue, target, mid + 1, right)));
                    left = mid + 1;
                } else {
                    session.getSteps().add(new SearchSession.SearchStep(mid, midValue, false, left, right, mid,
                        String.format("arr[%d] = %d > target %d. Searching left half [%d, %d]", 
                            mid, midValue, target, left, mid - 1)));
                    right = mid - 1;
                }
            }
            
            if (!session.isFound()) {
                session.getSteps().add(new SearchSession.SearchStep(-1, -1, false, left, right, -1,
                    String.format("Search space exhausted. Target %d not found in the array", target)));
            }
            
            session.setUpdatedAt(System.currentTimeMillis());
            
            long duration = System.currentTimeMillis() - startTime;
            session.getOperationHistory().add(new SearchSession.OperationHistory(
                "search", "binary", array.size(), target, session.isFound(), session.getFoundIndex(),
                true, "Binary search completed successfully", session.getComparisons(), duration));
            
            sessionRepository.save(session);
            
            result.put("success", true);
            result.put("message", session.isFound() ? 
                String.format("Target %d found at index %d", target, session.getFoundIndex()) :
                String.format("Target %d not found", target));
            result.put("session", session);
            result.put("found", session.isFound());
            result.put("foundIndex", session.getFoundIndex());
            result.put("comparisons", session.getComparisons());
            result.put("steps", session.getSteps());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error during binary search: " + e.getMessage());
        }
        
        return result;
    }

    // Get session by sessionId and userId
    public Map<String, Object> getSession(String sessionId, String userId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Optional<SearchSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
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
            List<SearchSession> sessions = sessionRepository.findByUserId(userId);
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
            Optional<SearchSession> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
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