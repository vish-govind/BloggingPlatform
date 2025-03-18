package com.BloggingPlatform.ByteBlog.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.BloggingPlatform.ByteBlog.Dto.CommentDto;
import com.BloggingPlatform.ByteBlog.Entity.Comment;
import com.BloggingPlatform.ByteBlog.Exception.EntityNotFoundException;
import com.BloggingPlatform.ByteBlog.Mapper.CommentMapper;
import com.BloggingPlatform.ByteBlog.Repo.CommentRepository;
import com.BloggingPlatform.ByteBlog.Repo.UserRepository;

@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    @Cacheable(value = "comments", key = "#blogId")
    public List<CommentDto> getCommentsByBlogId(Long blogId) {
        logger.info("Fetching comments for blog ID: {}", blogId);
        List<Comment> comments = commentRepository.findByBlogId(blogId);
        List<CommentDto> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            commentDTOs.add(commentMapper.toDTO(comment));
        }
        return commentDTOs;
    }

    @Override
    @Cacheable(value = "comments", key = "#id")
    public CommentDto getCommentById(Long id) {
        logger.info("Fetching comment with ID: {}", id);
        Optional<Comment> commentOptional = commentRepository.findById(id);
        
        if (commentOptional.isEmpty()) {
            logger.error("Comment with ID {} not found", id);
            throw new EntityNotFoundException("Comment with ID " + id + " not found");
        }

        Comment comment = commentOptional.get();
        return commentMapper.toDTO(comment);
    }

    
    @Override
    @CacheEvict(value = "comments", key = "#blogId", allEntries = true)
    public CommentDto addComment(Long blogId, Comment comment, String username) {
        logger.info("Adding a new comment for blog ID: {} by user: {}", blogId, username);
        Long userId = userRepository.findUserIdByUsername(username);
        if (userId == null) {
            logger.error("User not found: {}", username);
            throw new EntityNotFoundException("User not found: " + username);
        }

        comment.setAuthorId(userId);
        comment.setAuthorName(username);
        comment.setBlogId(blogId);
        commentRepository.save(comment);

        logger.info("Comment added successfully with ID: {}", comment.getCommentId());
        return commentMapper.toDTO(comment);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "comments", key = "#comment.blogId", allEntries = true), // Evicts all comments for the blog
        @CacheEvict(value = "comments", key = "#comment.id") // Evicts the specific comment
    })
    public CommentDto updateComment(Comment comment) {
        logger.info("Updating comment with ID: {}", comment.getCommentId());
        commentRepository.update(comment);
        return commentMapper.toDTO(comment);
    }

    @Override
    @CacheEvict(value = "comments", key = "#blogId")
    public void deleteComment(Long id) {
        logger.info("Deleting comment with ID: {}", id);
        commentRepository.deleteById(id);
    }

    @Override
    public String getCommentAuthorEmail(Long commentId) {
        logger.info("Fetching author email for comment ID: {}", commentId);
        
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        
        if (commentOptional.isEmpty()) {
            logger.error("Comment with ID {} not found while fetching author email", commentId);
            throw new EntityNotFoundException("Comment with ID " + commentId + " not found");
        }

        Comment comment = commentOptional.get();
        Long authorId = comment.getAuthorId();
        
        return userService.getUserEmail(authorId);
    }
}
