package com.BloggingPlatform.ByteBlog.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BloggingPlatform.ByteBlog.Dto.CommentDto;
import com.BloggingPlatform.ByteBlog.Entity.Comment;
import com.BloggingPlatform.ByteBlog.Service.CommentServiceImpl;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    
    @Autowired
    private CommentServiceImpl commentService;

    @PostMapping("/{blogId}")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long blogId, 
                                                  @RequestBody Comment comment,
                                                  Authentication authentication) {
        String loggedInUser = authentication.getName();
        logger.info("User {} is adding a comment to blog with ID {}", loggedInUser, blogId);
        CommentDto savedComment = commentService.addComment(blogId, comment, loggedInUser);
        logger.info("Comment added successfully with ID {}", savedComment.getCommentId());
        return ResponseEntity.ok(savedComment);
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<List<CommentDto>> getCommentsByBlog(@PathVariable Long blogId) {
        logger.info("Fetching comments for blog ID {}", blogId);
        List<CommentDto> comments = commentService.getCommentsByBlogId(blogId);
        logger.info("Found {} comments for blog ID {}", comments.size(), blogId);
        return ResponseEntity.ok(comments);
    }
    
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long commentId,
                                                    @RequestBody Comment updatedComment,
                                                    Authentication authentication) {
        String loggedInUser = authentication.getName();
        logger.info("User {} is attempting to update comment ID {}", loggedInUser, commentId);

        
        CommentDto existingComment = commentService.getCommentById(commentId);

        if (!existingComment.getAuthorUsername().equals(loggedInUser)) {
            logger.error("User {} is not authorized to update comment ID {}", loggedInUser, commentId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        updatedComment.setCommentId(commentId);
        updatedComment.setAuthorName(existingComment.getAuthorUsername());

        CommentDto updatedCommentResponse = commentService.updateComment(updatedComment);
        logger.info("Comment ID {} updated successfully", commentId);
        return ResponseEntity.ok(updatedCommentResponse);
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, Authentication authentication) {
        String loggedInUsername = authentication.getName();
        logger.info("User {} is attempting to delete comment ID {}", loggedInUsername, commentId);
        
        boolean isAdmin = authentication.getAuthorities().stream()
                                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        CommentDto existingComment = commentService.getCommentById(commentId);

        if (!isAdmin && !existingComment.getAuthorUsername().equals(loggedInUsername)) {
            logger.error("User {} is not authorized to delete comment ID {}", loggedInUsername, commentId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        commentService.deleteComment(commentId);
        logger.info("Comment ID {} deleted successfully", commentId);
        return ResponseEntity.noContent().build();
    }
}
