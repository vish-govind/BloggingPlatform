package com.BloggingPlatform.ByteBlog.Repo;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.BloggingPlatform.ByteBlog.Entity.Comment;

@Repository
public class CommentRepository {

    private static final Logger logger = LoggerFactory.getLogger(CommentRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Comment> findByBlogId(Long blogId) {
        String sql = "SELECT * FROM comments WHERE blog_id = ?";
        logger.info("Executing query: {} with blogId: {}", sql, blogId);

        List<Comment> comments = jdbcTemplate.query(sql, 
                ps -> ps.setLong(1, blogId),
                new BeanPropertyRowMapper<>(Comment.class));

        if (comments.isEmpty()) {
            logger.warn("No comments found for blogId: {}", blogId);
        } else {
            logger.info("Retrieved {} comments for blogId: {}", comments.size(), blogId);
        }

        return comments;
    }

    public Optional<Comment> findById(Long id) {
        String sql = "SELECT * FROM comments WHERE id = ?";
        logger.info("Executing query: {} with commentId: {}", sql, id);

        List<Comment> comments = jdbcTemplate.query(sql, 
                ps -> ps.setLong(1, id),
                new BeanPropertyRowMapper<>(Comment.class));

        if (comments.isEmpty()) {
            logger.warn("No comment found with id: {}", id);
        } else {
            logger.info("Comment found with id: {}", id);
        }

        return comments.stream().findFirst();
    }

    public void save(Comment comment) {
        String sql = "INSERT INTO comments (blog_id, author_id, content) VALUES (?, ?, ?)";
        logger.info("Inserting comment for blogId: {}, authorId: {}", comment.getBlogId(), comment.getAuthorId());

        jdbcTemplate.update(sql, comment.getBlogId(), comment.getAuthorId(), comment.getComment());

        logger.info("Comment inserted successfully for blogId: {}", comment.getBlogId());
    }

    public void update(Comment comment) {
        String sql = "UPDATE comments SET content = ?, created_at = ? WHERE comment_id = ?";
        logger.info("Updating comment with id: {}", comment.getCommentId());

        int rowsAffected = jdbcTemplate.update(sql, comment.getComment(), comment.getCreatedAt(), comment.getCommentId());

        if (rowsAffected > 0) {
            logger.info("Comment updated successfully with id: {}", comment.getCommentId());
        } else {
            logger.warn("No comment found to update with id: {}", comment.getCommentId());
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM comments WHERE id = ?";
        logger.info("Deleting comment with id: {}", id);

        int rowsAffected = jdbcTemplate.update(sql, id);

        if (rowsAffected > 0) {
            logger.info("Comment deleted successfully with id: {}", id);
        } else {
            logger.warn("No comment found to delete with id: {}", id);
        }
    }
}
