package com.example.algopulse.controller;

import com.example.algopulse.dto.DataStructureRequest;
import com.example.algopulse.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/queue")
@CrossOrigin(origins = "*")
public class QueueController {

    @Autowired
    private QueueService queueService;

    // Create new queue session
    @PostMapping("/{type}")
    public ResponseEntity<Map<String, Object>> createQueue(
            @PathVariable String type,
            @RequestAttribute("userId") String userId,
            @RequestBody DataStructureRequest request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate type
            if (!"static".equals(type) && !"dynamic".equals(type)) {
                response.put("success", false);
                response.put("message", "Invalid queue type. Use 'static' or 'dynamic'");
                return ResponseEntity.badRequest().body(response);
            }
            
            String sessionId = request.getSessionId() != null ? 
                              request.getSessionId() : UUID.randomUUID().toString();
            
            int maxSize = "static".equals(type) ? 
                         (request.getMaxSize() > 0 ? request.getMaxSize() : 10) : 
                         Integer.MAX_VALUE;
            
            var session = queueService.createQueue(sessionId, userId, type, maxSize);
            
            response.put("success", true);
            response.put("message", type + " queue created successfully");
            response.put("session", session);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Enqueue operation
    @PostMapping("/enqueue")
    public ResponseEntity<Map<String, Object>> enqueue(
            @RequestAttribute("userId") String userId,
            @RequestBody DataStructureRequest request) {
        
        try {
            Map<String, Object> result = queueService.enqueue(
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

    // Dequeue operation
    @PostMapping("/dequeue")
    public ResponseEntity<Map<String, Object>> dequeue(
            @RequestAttribute("userId") String userId,
            @RequestBody DataStructureRequest request) {
        
        try {
            Map<String, Object> result = queueService.dequeue(request.getSessionId(), userId);
            
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
            Map<String, Object> result = queueService.peek(sessionId, userId);
            
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

    // View entire queue
    @GetMapping("/view")
    public ResponseEntity<Map<String, Object>> viewQueue(
            @RequestAttribute("userId") String userId,
            @RequestParam String sessionId) {
        
        try {
            Map<String, Object> result = queueService.viewQueue(sessionId, userId);
            
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
            var sessions = queueService.getUserSessions(userId);
            response.put("success", true);
            response.put("sessions", sessions);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}