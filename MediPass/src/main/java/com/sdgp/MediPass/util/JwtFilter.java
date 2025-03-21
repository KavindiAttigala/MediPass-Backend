package com.sdgp.MediPass.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            // Retrieve the Authorization header
            String token = request.getHeader("Authorization");

            // Check if the Authorization header contains the token and starts with "Bearer "
            if (token != null && token.startsWith("Bearer ")) {
                // Extract the token by removing "Bearer " prefix
                String jwtToken = token.replace("Bearer ", "");

                // Call the JwtUtil to validate the token and retrieve the user information (e.g., username)
                String username = JwtUtil.validateToken(jwtToken);

                // If token is invalid, send unauthorized response
                if (username == null) {
                    response.sendError(javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                }

                // If the token is valid, set the username as a request attribute
                request.setAttribute("user", username);
            }

            // Continue with the next filter in the chain
            filterChain.doFilter(request, response);
    }
}