package com.BloggingPlatform.ByteBlog.Security;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.BloggingPlatform.ByteBlog.Entity.User;

public class OAuth2UserDetails implements OAuth2User {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2UserDetails.class);

    private final User user;
    private final String jwtToken;

    public OAuth2UserDetails(User user, String jwtToken) {
        this.user = user;
        this.jwtToken = jwtToken;
        logger.info("OAuth2UserDetails created for user: {}", user.getUserEmail());
    }

    @Override
    public Map<String, Object> getAttributes() {
        logger.debug("Fetching attributes for user: {}", user.getUserEmail());
        return Map.of("name", user.getUserName(), "email", user.getUserEmail());
    }

    @Override
    public String getName() {
        logger.debug("Fetching name for user: {}", user.getUserEmail());
        return user.getUserEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        logger.debug("Fetching authorities for user: {}", user.getUserEmail());
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    public String getJwtToken() {
        logger.debug("Fetching JWT token for user: {}", user.getUserEmail());
        return jwtToken;
    }
}
