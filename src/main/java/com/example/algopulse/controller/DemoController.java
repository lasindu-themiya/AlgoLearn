package com.example.algopulse.controller;

import com.example.algopulse.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/demo")
@CrossOrigin(origins = "*")
public class DemoController {

    @Autowired
    private AuthService authService;

    /**
     * Example secured endpoint demonstrating how to access authenticated user's ID from JWT
     * This endpoint shows user-specific data scoped to the authenticated user
     */
    @GetMapping("/user-info")
    public ResponseEntity<Map<String, Object>> getUserInfo(@RequestAttribute("userId") String userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Get the authenticated user's details using their ID from JWT
            var user = authService.getUserById(userId);
            
            // Create user-specific response (without sensitive data)
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("email", user.getEmail());
            userInfo.put("role", user.getRole());
            userInfo.put("enabled", user.isEnabled());
            userInfo.put("createdAt", user.getCreatedAt());
            
            response.put("success", true);
            response.put("message", "User information retrieved successfully");
            response.put("user", userInfo);
            
            // Example of user-scoped operation
            response.put("welcomeMessage", "Welcome back, " + user.getUsername() + "!");
            response.put("userScope", "This data is specific to user ID: " + userId);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Another example showing how to create user-scoped data
     */
    @PostMapping("/create-user-data")
    public ResponseEntity<Map<String, Object>> createUserData(
            @RequestAttribute("userId") String userId,
            @RequestBody Map<String, Object> requestData) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // This is how you would create data scoped to the authenticated user
            // The userId from JWT ensures each user can only access their own data
            
            String dataType = (String) requestData.get("dataType");
            Object data = requestData.get("data");
            
            // Example: Create a user-specific record
            Map<String, Object> userRecord = new HashMap<>();
            userRecord.put("userId", userId); // Always scope to authenticated user
            userRecord.put("dataType", dataType);
            userRecord.put("data", data);
            userRecord.put("createdAt", System.currentTimeMillis());
            
            response.put("success", true);
            response.put("message", "User data created successfully");
            response.put("record", userRecord);
            response.put("note", "This record is automatically scoped to user ID: " + userId);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}