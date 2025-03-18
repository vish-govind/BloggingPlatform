package com.BloggingPlatform.ByteBlog.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.BloggingPlatform.ByteBlog.Config.JwtUtil;
import com.BloggingPlatform.ByteBlog.Entity.User;
import com.BloggingPlatform.ByteBlog.Repo.UserRepository;
import com.BloggingPlatform.ByteBlog.Security.OAuth2UserDetails;

@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Cacheable(value = "oauthUsers", key = "#result.attributes['email']")
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        logger.info("Loading OAuth2 user from provider: {}", userRequest.getClientRegistration().getRegistrationId());

        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String username = oAuth2User.getAttribute("name");
        String provider = userRequest.getClientRegistration().getRegistrationId();

        logger.info("OAuth2 user details retrieved - Email: {}, Username: {}, Provider: {}", email, username, provider);

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            logger.info("User with email {} not found. Creating new user.", email);
            User newUser = new User();
            newUser.setUserEmail(email);
            newUser.setUserName(username);
            newUser.setPassword(null);
            newUser.setProvider(provider);
            User savedUser = userRepository.save(newUser);
            logger.info("New user created with email: {}", savedUser.getUserEmail());
            return savedUser;
        });

        String jwtToken = jwtUtil.generateToken(new UsernamePasswordAuthenticationToken(user.getUserEmail(), null));
        logger.info("Generated JWT token for user: {}", user.getUserEmail());

        return new OAuth2UserDetails(user, jwtToken);
    }

    @CachePut(value = "oauthUsers", key = "#user.userEmail")
    public User updateUser(User user) {
        logger.info("Updating OAuth2 user with email: {}", user.getUserEmail());
        User updatedUser = userRepository.save(user);
        logger.info("User with email {} updated successfully", user.getUserEmail());
        return updatedUser;
    }
}
