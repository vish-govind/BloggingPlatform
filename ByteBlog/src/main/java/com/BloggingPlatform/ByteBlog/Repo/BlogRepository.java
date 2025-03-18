package com.BloggingPlatform.ByteBlog.Repo;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.BloggingPlatform.ByteBlog.Entity.Blog;

@Repository
public class BlogRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(BlogRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Blog> findAll() {
        String sql = "SELECT * FROM blogs";
        logger.info("Executing query: {}", sql);

        List<Blog> blogs = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Blog.class));

        if (blogs.isEmpty()) {
            logger.warn("No blogs found in the database.");
        } else {
            logger.info("Retrieved {} blogs from the database.", blogs.size());
        }

        return blogs;
    }

    public Optional<Blog> findById(Long id) {
        String sql = "SELECT * FROM blogs WHERE id = ?";
        logger.info("Executing query: {} with blogId: {}", sql, id);

        List<Blog> blogs = jdbcTemplate.query(sql,
                ps -> ps.setLong(1, id),
                new BeanPropertyRowMapper<>(Blog.class));

        if (blogs.isEmpty()) {
            logger.warn("No blog found with id: {}", id);
        } else {
            logger.info("Blog found with id: {}", id);
        }

        return blogs.stream().findFirst();
    }

    public void save(Blog blog) {
        String sql = "INSERT INTO blogs (title, content, author_id, author_username, created_at) VALUES (?, ?, ?, ?, ?)";
        logger.info("Inserting blog titled: '{}' by authorId: {}", blog.getTitle(), blog.getAuthorId());

        jdbcTemplate.update(sql, blog.getTitle(), blog.getContent(), blog.getAuthorId(), blog.getAuthorUsername(), blog.getCreatedAt());

        logger.info("Blog '{}' inserted successfully.", blog.getTitle());
    }

    public void update(Blog blog) {
        String sql = "UPDATE blogs SET title = ?, content = ? WHERE id = ?";
        logger.info("Updating blog with id: {}", blog.getBlogId());

        int rowsAffected = jdbcTemplate.update(sql, blog.getTitle(), blog.getContent(), blog.getBlogId());

        if (rowsAffected > 0) {
            logger.info("Blog updated successfully with id: {}", blog.getBlogId());
        } else {
            logger.warn("No blog found to update with id: {}", blog.getBlogId());
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM blogs WHERE id = ?";
        logger.info("Deleting blog with id: {}", id);

        int rowsAffected = jdbcTemplate.update(sql, id);

        if (rowsAffected > 0) {
            logger.info("Blog deleted successfully with id: {}", id);
        } else {
            logger.warn("No blog found to delete with id: {}", id);
        }
    }
}
