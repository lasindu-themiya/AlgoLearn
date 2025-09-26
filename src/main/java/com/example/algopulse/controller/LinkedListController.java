package com.example.algopulse.controller;

import com.example.algopulse.dto.DataStructureRequest;
import com.example.algopulse.service.LinkedListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/linkedlist")
@CrossOrigin(origins = "*")
public class LinkedListController {

    @Autowired
    private LinkedListService linkedListService;

    // Create new linked list session
    @PostMapping("/{type}")
    public ResponseEntity<Map<String, Object>> createLinkedList(
            @PathVariable String type,
            @RequestAttribute("userId") String userId,
            @RequestBody(required = false) DataStructureRequest request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate type
            if (!"singly".equals(type) && !"doubly".equals(type)) {
                response.put("success", false);
                response.put("message", "Invalid linked list type. Use 'singly' or 'doubly'");
                return ResponseEntity.badRequest().body(response);
            }
            
            String sessionId = request != null && request.getSessionId() != null ? 
                              request.getSessionId() : UUID.randomUUID().toString();
            
            var session = linkedListService.createLinkedList(sessionId, userId, type);
            
            response.put("success", true);
            response.put("message", type + " linked list created successfully");
            response.put("data", session);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Insert at head (append front)
    @PostMapping("/insert-head")
    public ResponseEntity<Map<String, Object>> insertHead(
            @RequestAttribute("userId") String userId,
            @RequestBody DataStructureRequest request) {
        
        try {
            Map<String, Object> result = linkedListService.insertAtHead(
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

    // Insert at tail (normal append)
    @PostMapping("/insert-tail")
    public ResponseEntity<Map<String, Object>> insertTail(
            @RequestAttribute("userId") String userId,
            @RequestBody DataStructureRequest request) {
        
        try {
            Map<String, Object> result = linkedListService.insertAtTail(
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

    // Legacy insert endpoint (for backward compatibility - inserts at head)
    @PostMapping("/insert")
    public ResponseEntity<Map<String, Object>> insert(
            @RequestAttribute("userId") String userId,
            @RequestBody DataStructureRequest request) {
        
        try {
            Map<String, Object> result = linkedListService.insertAtHead(
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

    // Delete by value
    @PostMapping("/delete-value")
    public ResponseEntity<Map<String, Object>> deleteByValue(
            @RequestAttribute("userId") String userId,
            @RequestBody DataStructureRequest request) {
        
        try {
            Map<String, Object> result = linkedListService.deleteByValue(
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

    // Delete from head (delete first element)
    @PostMapping("/delete-head")
    public ResponseEntity<Map<String, Object>> deleteFromHead(
            @RequestAttribute("userId") String userId,
            @RequestBody DataStructureRequest request) {
        
        try {
            Map<String, Object> result = linkedListService.deleteFromHead(
                request.getSessionId(), userId);
            
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

    // Delete from tail (delete last element)
    @PostMapping("/delete-tail")
    public ResponseEntity<Map<String, Object>> deleteFromTail(
            @RequestAttribute("userId") String userId,
            @RequestBody DataStructureRequest request) {
        
        try {
            Map<String, Object> result = linkedListService.deleteFromTail(
                request.getSessionId(), userId);
            
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

    // Legacy delete endpoint (for backward compatibility - deletes by value)
    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> delete(
            @RequestAttribute("userId") String userId,
            @RequestBody DataStructureRequest request) {
        
        try {
            Map<String, Object> result = linkedListService.deleteByValue(
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

    // Search for value
    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> search(
            @RequestAttribute("userId") String userId,
            @RequestBody DataStructureRequest request) {
        
        try {
            Map<String, Object> result = linkedListService.search(
                request.getSessionId(), userId, request.getData());
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // View entire list
    @GetMapping("/view")
    public ResponseEntity<Map<String, Object>> viewList(
            @RequestAttribute("userId") String userId,
            @RequestParam String sessionId) {
        
        try {
            Map<String, Object> result = linkedListService.viewList(sessionId, userId);
            
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
            var sessions = linkedListService.getUserSessions(userId);
            response.put("success", true);
            response.put("data", sessions);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Insert at given index (for doubly linked list)
    @PostMapping("/insert-at-index")
    public ResponseEntity<Map<String, Object>> insertAtIndex(@RequestBody Map<String, Object> request,
                                                           @RequestAttribute("userId") String userId) {
        try {
            String sessionId = (String) request.get("sessionId");
            Object valueObj = request.get("value");
            Object indexObj = request.get("index");
            
            if (sessionId == null || valueObj == null || indexObj == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Missing required parameters: sessionId, value, and index");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Handle value conversion
            Integer value;
            if (valueObj instanceof String) {
                try {
                    value = Integer.parseInt((String) valueObj);
                } catch (NumberFormatException e) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Value must be a valid integer");
                    return ResponseEntity.badRequest().body(response);
                }
            } else if (valueObj instanceof Integer) {
                value = (Integer) valueObj;
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Value must be an integer");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Handle index conversion
            Integer index;
            if (indexObj instanceof String) {
                try {
                    index = Integer.parseInt((String) indexObj);
                } catch (NumberFormatException e) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Index must be a valid integer");
                    return ResponseEntity.badRequest().body(response);
                }
            } else if (indexObj instanceof Integer) {
                index = (Integer) indexObj;
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Index must be an integer");
                return ResponseEntity.badRequest().body(response);
            }
            
            Map<String, Object> result = linkedListService.insertAtGivenIndex(sessionId, userId, value, index);
            
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

    // Remove at given index (for doubly linked list)
    @PostMapping("/remove-at-index")
    public ResponseEntity<Map<String, Object>> removeAtIndex(@RequestBody Map<String, Object> request,
                                                           @RequestAttribute("userId") String userId) {
        try {
            String sessionId = (String) request.get("sessionId");
            Integer index = (Integer) request.get("index");
            
            if (sessionId == null || index == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Missing required parameters: sessionId and index");
                return ResponseEntity.badRequest().body(response);
            }
            
            Map<String, Object> result = linkedListService.removeAtGivenIndex(sessionId, userId, index);
            
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

    // Clear/Reset LinkedList (remove all elements)
    @PostMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearLinkedList(@RequestBody Map<String, Object> request,
                                                              @RequestAttribute("userId") String userId) {
        try {
            String sessionId = (String) request.get("sessionId");
            
            if (sessionId == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Missing required parameter: sessionId");
                return ResponseEntity.badRequest().body(response);
            }
            
            Map<String, Object> result = linkedListService.clearLinkedList(sessionId, userId);
            
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
}