package com.sdgp.MediPass.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getServletPath();

        // Skip JWT validation for these endpoints as they do not require authentication
        if (requestURI.equals("/medipass/auth/register/adult") || requestURI.equals("/medipass/auth/register/child") ||
        requestURI.equals("/medipass/otp/sendOTP") || requestURI.equals("/medipass/otp/verifyOTP") || requestURI.equals("/medipass/otp/reset-password")) {
            filterChain.doFilter(request, response);
            return;
        }


        // Retrieve the Authorization header
            String token = request.getHeader("Authorization");

            // Check if the Authorization header contains the token and starts with "Bearer "
            if (token != null && token.startsWith("Bearer ")) {
                // Extract the token by removing "Bearer " prefix
                String jwtToken = token.substring(7);

                // Call the JwtUtil to validate the token and retrieve the user information (e.g., mediId)
                String mediId = JwtUtil.validateToken(jwtToken);

                // If token is invalid, send unauthorized response
                if (mediId == null) {
                    response.sendError(javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                }

                // Create an authentication object and set it in the Security Context
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(mediId, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // If the token is valid, set the username as a request attribute
                request.setAttribute("user", mediId);
            }

            // Continue with the next filter in the chain
            filterChain.doFilter(request, response);
    }
}