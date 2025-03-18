package com.BloggingPlatform.ByteBlog.Config;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        logger.debug("Received Authorization header: {}", token);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            logger.debug("Extracted Bearer Token: {}", token);

            String username = jwtUtil.extractUsername(token);
            logger.info("Extracted username from token: {}", username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("User '{}' authenticated successfully.", username);
            } else {
                logger.error("Failed to authenticate user: {}.", username);
            }
        } else {
            logger.debug("No valid JWT token found in request.");
        }

        chain.doFilter(request, response);
    }
}
