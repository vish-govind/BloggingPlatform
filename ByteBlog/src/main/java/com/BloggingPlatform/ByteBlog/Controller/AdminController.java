package com.BloggingPlatform.ByteBlog.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BloggingPlatform.ByteBlog.Service.BlogServiceImpl;
import com.BloggingPlatform.ByteBlog.Service.CommentServiceImpl;
import com.BloggingPlatform.ByteBlog.Service.EmailService;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    
    @Autowired
    private BlogServiceImpl blogService;

    @Autowired
    private CommentServiceImpl commentService;

    @DeleteMapping("/blogs/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        logger.info("Admin request to delete blog with ID: {}", id);
        String userEmail = blogService.getBlogAuthorEmail(id);
        
        if (userEmail != null) {
            logger.info("Notifying user ({}) about blog deletion.", userEmail);
            EmailService.sendEmail(userEmail, "Blog Deletion Notice",
                "Your blog was deleted by an admin due to inappropriate content.");
        }
        
        blogService.deleteBlog(id);
        logger.info("Blog with ID: {} deleted successfully.", id);
        
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        logger.info("Admin request to delete comment with ID: {}", id);
        String userEmail = commentService.getCommentAuthorEmail(id);
        
        if (userEmail != null) {
            logger.info("Notifying user ({}) about comment deletion.", userEmail);
            EmailService.sendEmail(userEmail, "Comment Deletion Notice",
                "Your comment was deleted by an admin due to inappropriate content.");
        }
        
        commentService.deleteComment(id);
        logger.info("Comment with ID: {} deleted successfully.", id);
        
        return ResponseEntity.noContent().build();
    }
}
