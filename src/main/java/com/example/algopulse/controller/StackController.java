package com.example.algopulse.controller;

import com.example.algopulse.dto.DataStructureRequest;
import com.example.algopulse.service.StackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/stack")
@CrossOrigin(origins = "*")
public class StackController {

    @Autowired
    private StackService stackService;

    // Create new stack session
    @PostMapping("/{type}")
    public ResponseEntity<Map<String, Object>> createStack(
            @PathVariable String type,
            @RequestAttribute("userId") String userId,
            @RequestBody DataStructureRequest request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate type
            if (!"static".equals(type) && !"dynamic".equals(type)) {
                response.put("success", false);
                response.put("message", "Invalid stack type. Use 'static' or 'dynamic'");
                return ResponseEntity.badRequest().body(response);
            }
            
            String sessionId = request.getSessionId() != null ? 
                              request.getSessionId() : UUID.randomUUID().toString();
            
            int maxSize = "static".equals(type) ? 
                         (request.getMaxSize() > 0 ? request.getMaxSize() : 10) : 
                         Integer.MAX_VALUE;
            
            var session = stackService.createStack(sessionId, userId, type, maxSize);
            
            response.put("success", true);
            response.put("message", type + " stack created successfully");
            response.put("data", session);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Push operation
    @PostMapping("/push")
    public ResponseEntity<Map<String, Object>> push(
            @RequestAttribute("userId") String userId,
            @RequestBody DataStructureRequest request) {
        
        try {
            Map<String, Object> result = stackService.push(
                request.getSessionId(), userId, request.getData());
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Pop operation
    @PostMapping("/pop")
    public ResponseEntity<Map<String, Object>> pop(
            @RequestAttribute("userId") String userId,
            @RequestBody DataStructureRequest request) {
        
        try {
            Map<String, Object> result = stackService.pop(request.getSessionId(), userId);
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Peek operation
    @GetMapping("/peek")
    public ResponseEntity<Map<String, Object>> peek(
            @RequestAttribute("userId") String userId,
            @RequestParam String sessionId) {
        
        try {
            Map<String, Object> result = stackService.peek(sessionId, userId);
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // View entire stack
    @GetMapping("/view")
    public ResponseEntity<Map<String, Object>> viewStack(
            @RequestAttribute("userId") String userId,
            @RequestParam String sessionId) {
        
        try {
            Map<String, Object> result = stackService.viewStack(sessionId, userId);
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get all user sessions
    @GetMapping("/sessions")
    public ResponseEntity<Map<String, Object>> getUserSessions(
            @RequestAttribute("userId") String userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            var sessions = stackService.getUserSessions(userId);
            response.put("success", true);
            response.put("data", sessions);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}