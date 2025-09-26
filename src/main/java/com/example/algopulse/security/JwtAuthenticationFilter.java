package com.example.algopulse.security;

import com.example.algopulse.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        logger.info("Processing request: " + requestURI);
        
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String userId = authService.extractUserIdFromToken(authHeader);
                
                if (userId != null && authService.validateToken(authHeader)) {
                    // Create authentication token
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                        userId, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    
                    // Add userId to request attributes for easy access in controllers
                    request.setAttribute("userId", userId);
                }
            } catch (Exception e) {
                // Invalid token, continue without authentication
                logger.error("JWT token validation failed: " + e.getMessage());
            }
        }
        
        filterChain.doFilter(request, response);
    }
}