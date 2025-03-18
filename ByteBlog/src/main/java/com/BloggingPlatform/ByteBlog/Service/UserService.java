package com.BloggingPlatform.ByteBlog.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.BloggingPlatform.ByteBlog.Repo.UserRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Cacheable(value = "userEmails", key = "#authorId")
    public String getUserEmail(Long authorId) {
        logger.info("Fetching email for user with ID: {}", authorId);
        String email = userRepository.findEmailByUserId(authorId);
        if (email == null) {
            logger.warn("No email found for user ID: {}", authorId);
        } else {
            logger.info("Email retrieved successfully for user ID: {}", authorId);
        }
        return email;
    }
}
