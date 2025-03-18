package com.BloggingPlatform.ByteBlog.Controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BloggingPlatform.ByteBlog.Config.JwtUtil;
import com.BloggingPlatform.ByteBlog.Dto.AuthResponse;
import com.BloggingPlatform.ByteBlog.Dto.LoginRequest;
import com.BloggingPlatform.ByteBlog.Dto.RegisterRequest;
import com.BloggingPlatform.ByteBlog.Entity.User;
import com.BloggingPlatform.ByteBlog.Repo.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt for identifier: {}", loginRequest.getIdentifier());
        
        String identifier = loginRequest.getIdentifier(); 
        String password = loginRequest.getPassword();
        logger.info(identifier +" , "+ password);
        
        Optional<User> userOptional = userRepository.findByEmail(identifier);

        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByUserName(identifier);
        }

        User user = userOptional.orElseThrow(() -> {
            logger.error("User not found for identifier: {}", identifier);
            return new RuntimeException("User not found");
        });

        Authentication authentication =
        	    authenticationManager.authenticate(
        	        new UsernamePasswordAuthenticationToken(identifier, password));

        String token = jwtUtil.generateToken(authentication);
        logger.info("User {} logged in successfully", user.getUserEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }
    
    @GetMapping("/hello")
    public String test()
    {
    	return "Hello Vishali";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        logger.info("Registration attempt for email: {}", request.getEmail());
        
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.error("Registration failed: Email {} already in use", request.getEmail());
            return ResponseEntity.badRequest().body("Email already in use!");
        }

        User user = new User();
        user.setUserEmail(request.getEmail());
        user.setUserName(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProvider("manual"); 

        userRepository.save(user);
        logger.info("User registered successfully with email: {}", request.getEmail());
        return ResponseEntity.ok("User registered successfully!");
    }

}
