package com.BloggingPlatform.ByteBlog.Repo;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.BloggingPlatform.ByteBlog.Entity.Role;
import com.BloggingPlatform.ByteBlog.Entity.User;

@Repository
public class UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE user_email = ?";
        logger.info("Executing query: {} with email: {}", sql, email);
        
        List<User> users = jdbcTemplate.query(sql, 
            ps -> ps.setString(1, email),
            (rs, rowNum) -> new User(
                rs.getLong("user_id"),
                rs.getString("user_name"),
                rs.getString("user_email"),
                rs.getString("password"),
                rs.getString("provider"),
                Role.valueOf(rs.getString("role") != null ? rs.getString("role") : "USER")
            )
        );
        
        if (users.isEmpty()) {
            logger.warn("No user found with email: {}", email);
        } else {
            logger.info("User found with email: {}", email);
        }
        
        return users.stream().findFirst();
    }

    public Optional<User> findByUserName(String username) {
        String sql = "SELECT * FROM users WHERE user_name = ?";
        logger.info("Executing query: {} with username: {}", sql, username);

        List<User> users = jdbcTemplate.query(sql, 
            ps -> ps.setString(1, username),
            (rs, rowNum) -> new User(
                rs.getLong("user_id"),
                rs.getString("user_name"),
                rs.getString("user_email"),
                rs.getString("password"),
                rs.getString("provider"),
                Role.valueOf(rs.getString("role") != null ? rs.getString("role") : "USER")
            )
        );

        if (users.isEmpty()) {
            logger.warn("No user found with username: {}", username);
        } else {
            logger.info("User found with username: {}", username);
        }

        return users.stream().findFirst();
    }

    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        logger.info("Executing query: {} with userId: {}", sql, id);

        List<User> users = jdbcTemplate.query(sql,
            ps -> ps.setLong(1, id),
            (rs, rowNum) -> new User(
                rs.getLong("user_id"),
                rs.getString("user_name"),
                rs.getString("user_email"),
                rs.getString("password"),
                rs.getString("provider"),
                Role.valueOf(rs.getString("role")) 
            )
        );

        if (users.isEmpty()) {
            logger.warn("No user found with userId: {}", id);
        } else {
            logger.info("User found with userId: {}", id);
        }

        return users.stream().findFirst();
    }

    public User save(User user) {
        String sql = "INSERT INTO users (user_name, user_email, password, provider, role) VALUES (?, ?, ?, ?, ?)";
        logger.info("Inserting user: {} into database", user.getUserEmail());

        jdbcTemplate.update(sql, 
            user.getUserName(), 
            user.getUserEmail(), 
            user.getPassword(),
            user.getProvider(), 
            user.getRole().name() 
        );

        logger.info("User inserted successfully: {}", user.getUserEmail());
        return user;
    }

    public Long findUserIdByUsername(String username) {
        String sql = "SELECT user_id FROM users WHERE user_name = ?";
        logger.info("Executing query: {} with username: {}", sql, username);

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("user_id"), username);
        } catch (Exception e) {
            logger.warn("User ID not found for username: {}", username);
            return null;
        }
    }

    public String findUsernameByUserId(Long userId) {
        String sql = "SELECT user_name FROM users WHERE user_id = ?";
        logger.info("Executing query: {} with userId: {}", sql, userId);

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getString("user_name"), userId);
        } catch (Exception e) {
            logger.warn("Username not found for userId: {}", userId);
            return null;
        }
    }

    public String findEmailByUserId(Long userId) {
        String sql = "SELECT user_email FROM users WHERE user_id = ?";
        logger.info("Executing query: {} with userId: {}", sql, userId);

        List<String> emails = jdbcTemplate.query(sql, ps -> ps.setLong(1, userId),
                (rs, rowNum) -> rs.getString("user_email"));

        if (emails.isEmpty()) {
            logger.warn("No email found for userId: {}", userId);
            return null;
        }

        logger.info("Email retrieved for userId: {}", userId);
        return emails.get(0);
    }
}
