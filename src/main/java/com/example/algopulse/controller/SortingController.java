package com.example.algopulse.controller;

import com.example.algopulse.dto.SortingRequest;
import com.example.algopulse.service.SortingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/sort")
@CrossOrigin(origins = "*")
public class SortingController {

    @Autowired
    private SortingService sortingService;

    // Bubble Sort Endpoint
    @PostMapping("/bubble")
    public ResponseEntity<Map<String, Object>> bubbleSort(
            @RequestAttribute("userId") String userId,
            @RequestBody SortingRequest request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate input
            if (request.getArray() == null || request.getArray().isEmpty()) {
                response.put("success", false);
                response.put("message", "Array cannot be null or empty");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (request.getArray().size() > 100) {
                response.put("success", false);
                response.put("message", "Array size cannot exceed 100 elements");
                return ResponseEntity.badRequest().body(response);
            }
            
            String sessionId = request.getSessionId() != null ? 
                              request.getSessionId() : UUID.randomUUID().toString();
            
            Map<String, Object> result = sortingService.bubbleSort(sessionId, userId, request.getArray());
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error performing bubble sort: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Insertion Sort Endpoint
    @PostMapping("/insertion")
    public ResponseEntity<Map<String, Object>> insertionSort(
            @RequestAttribute("userId") String userId,
            @RequestBody SortingRequest request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate input
            if (request.getArray() == null || request.getArray().isEmpty()) {
                response.put("success", false);
                response.put("message", "Array cannot be null or empty");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (request.getArray().size() > 100) {
                response.put("success", false);
                response.put("message", "Array size cannot exceed 100 elements");
                return ResponseEntity.badRequest().body(response);
            }
            
            String sessionId = request.getSessionId() != null ? 
                              request.getSessionId() : UUID.randomUUID().toString();
            
            Map<String, Object> result = sortingService.insertionSort(sessionId, userId, request.getArray());
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error performing insertion sort: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Selection Sort Endpoint
    @PostMapping("/selection")
    public ResponseEntity<Map<String, Object>> selectionSort(
            @RequestAttribute("userId") String userId,
            @RequestBody SortingRequest request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate input
            if (request.getArray() == null || request.getArray().isEmpty()) {
                response.put("success", false);
                response.put("message", "Array cannot be null or empty");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (request.getArray().size() > 100) {
                response.put("success", false);
                response.put("message", "Array size cannot exceed 100 elements");
                return ResponseEntity.badRequest().body(response);
            }
            
            String sessionId = request.getSessionId() != null ? 
                              request.getSessionId() : UUID.randomUUID().toString();
            
            Map<String, Object> result = sortingService.selectionSort(sessionId, userId, request.getArray());
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error performing selection sort: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Min Sort Endpoint
    @PostMapping("/min")
    public ResponseEntity<Map<String, Object>> minSort(
            @RequestAttribute("userId") String userId,
            @RequestBody SortingRequest request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate input
            if (request.getArray() == null || request.getArray().isEmpty()) {
                response.put("success", false);
                response.put("message", "Array cannot be null or empty");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (request.getArray().size() > 100) {
                response.put("success", false);
                response.put("message", "Array size cannot exceed 100 elements");
                return ResponseEntity.badRequest().body(response);
            }
            
            String sessionId = request.getSessionId() != null ? 
                              request.getSessionId() : UUID.randomUUID().toString();
            
            Map<String, Object> result = sortingService.minSort(sessionId, userId, request.getArray());
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error performing min sort: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Optimized Bubble Sort Endpoint
    @PostMapping("/optimized-bubble")
    public ResponseEntity<Map<String, Object>> optimizedBubbleSort(
            @RequestAttribute("userId") String userId,
            @RequestBody SortingRequest request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate input
            if (request.getArray() == null || request.getArray().isEmpty()) {
                response.put("success", false);
                response.put("message", "Array cannot be null or empty");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (request.getArray().size() > 100) {
                response.put("success", false);
                response.put("message", "Array size cannot exceed 100 elements");
                return ResponseEntity.badRequest().body(response);
            }
            
            String sessionId = request.getSessionId() != null ? 
                              request.getSessionId() : UUID.randomUUID().toString();
            
            Map<String, Object> result = sortingService.optimizedBubbleSort(sessionId, userId, request.getArray());
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error performing optimized bubble sort: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Get Sorting Session
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<Map<String, Object>> getSession(
            @PathVariable String sessionId,
            @RequestAttribute("userId") String userId) {
        
        try {
            Map<String, Object> result = sortingService.getSession(sessionId, userId);
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error retrieving session: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Get All User Sessions
    @GetMapping("/sessions")
    public ResponseEntity<Map<String, Object>> getUserSessions(
            @RequestAttribute("userId") String userId) {
        
        try {
            Map<String, Object> result = sortingService.getUserSessions(userId);
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error retrieving user sessions: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Delete Session
    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<Map<String, Object>> deleteSession(
            @PathVariable String sessionId,
            @RequestAttribute("userId") String userId) {
        
        try {
            Map<String, Object> result = sortingService.deleteSession(sessionId, userId);
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error deleting session: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}