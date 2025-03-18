package com.BloggingPlatform.ByteBlog.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BloggingPlatform.ByteBlog.Config.JwtUtil;
import com.BloggingPlatform.ByteBlog.Dto.AuthResponse;
import com.BloggingPlatform.ByteBlog.Security.OAuth2UserDetails;

@RestController
@RequestMapping("/auth/oauth2")
public class OAuth2Controller {
	
	 private static final Logger logger = LoggerFactory.getLogger(OAuth2Controller.class);
	
	 @Autowired
	    private JwtUtil jwtUtil;

	 @GetMapping("/token")
	    public ResponseEntity<?> getOAuth2Token(@AuthenticationPrincipal OAuth2UserDetails userDetails) {
	        if (userDetails == null) {
	            logger.error("OAuth2 token request failed: User not authenticated");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
	        }

	        logger.info("OAuth2 token requested for user: {}", userDetails.getName());

	        // Generate a new JWT if necessary
	        String token = jwtUtil.generateToken(
	                new UsernamePasswordAuthenticationToken(userDetails.getName(), null));

	        logger.info("OAuth2 token successfully generated for user: {}", userDetails.getName());

	        return ResponseEntity.ok(new AuthResponse(token));
	    }

}
