package com.example.algopulse.controller;

import com.example.algopulse.dto.SearchRequest;
import com.example.algopulse.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*")
public class SearchController {

    @Autowired
    private SearchService searchService;

    // Linear Search Endpoint
    @PostMapping("/linear")
    public ResponseEntity<Map<String, Object>> linearSearch(
            @RequestAttribute("userId") String userId,
            @RequestBody SearchRequest request) {
        
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
            
            Map<String, Object> result = searchService.linearSearch(
                sessionId, userId, request.getArray(), request.getTarget());
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error performing linear search: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Binary Search Endpoint
    @PostMapping("/binary")
    public ResponseEntity<Map<String, Object>> binarySearch(
            @RequestAttribute("userId") String userId,
            @RequestBody SearchRequest request) {
        
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
            
            Map<String, Object> result = searchService.binarySearch(
                sessionId, userId, request.getArray(), request.getTarget());
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error performing binary search: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Get Search Session
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<Map<String, Object>> getSession(
            @PathVariable String sessionId,
            @RequestAttribute("userId") String userId) {
        
        try {
            Map<String, Object> result = searchService.getSession(sessionId, userId);
            
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
            Map<String, Object> result = searchService.getUserSessions(userId);
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
            Map<String, Object> result = searchService.deleteSession(sessionId, userId);
            
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