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

import com.BloggingPlatform.ByteBlog.Dto.BlogDto;
import com.BloggingPlatform.ByteBlog.Entity.Blog;
import com.BloggingPlatform.ByteBlog.Exception.EntityNotFoundException;
import com.BloggingPlatform.ByteBlog.Service.BlogServiceImpl;

@RestController
@RequestMapping("/blogs")
public class BlogController {

    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);
    
    @Autowired
    private BlogServiceImpl blogService;

    @GetMapping
    public ResponseEntity<List<BlogDto>> getAllBlogs() {
        logger.info("Fetching all blogs");
        List<BlogDto> blogDTOs = blogService.getAllBlogs();
        return ResponseEntity.ok(blogDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogDto> getBlogById(@PathVariable Long id) {
        logger.info("Fetching blog with ID: {}", id);
        BlogDto blogDto = blogService.getBlogById(id);
        return ResponseEntity.ok(blogDto);
    }
    
    @PostMapping
    public ResponseEntity<BlogDto> createBlog(@RequestBody Blog blog, Authentication authentication) {
        String loggedInUsername = authentication.getName();
        logger.info("User {} is creating a new blog", loggedInUsername);
        Blog createdBlog = blogService.createBlog(blog, loggedInUsername);
        BlogDto blogDto = new BlogDto(createdBlog);
        logger.info("Blog created successfully with ID: {}", createdBlog.getBlogId());
        return ResponseEntity.ok(blogDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogDto> updateBlog(@PathVariable Long id,
                                             @RequestBody Blog updatedBlog,
                                             Authentication authentication) {
        String loggedInUsername = authentication.getName();
        logger.info("User {} attempting to update blog with ID: {}", loggedInUsername, id);

        BlogDto existingBlog;
        try {
            existingBlog = blogService.getBlogById(id);
        } catch (EntityNotFoundException e) {
            logger.warn("Blog with ID: {} not found for update", id);
            return ResponseEntity.notFound().build();
        }

        if (!existingBlog.getAuthorUsername().equals(loggedInUsername)) {
            logger.error("User {} is not the author of blog ID: {}. Update denied.", loggedInUsername, id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        updatedBlog.setBlogId(id);
        updatedBlog.setAuthorUsername(existingBlog.getAuthorUsername());
        blogService.updateBlog(updatedBlog);
        logger.info("Blog with ID: {} updated successfully", id);
        return ResponseEntity.ok(new BlogDto(updatedBlog));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id, Authentication authentication) {
        String loggedInUsername = authentication.getName();
        logger.info("User {} attempting to delete blog with ID: {}", loggedInUsername, id);

        boolean isAdmin = authentication.getAuthorities().stream()
                              .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        BlogDto existingBlog;
        try {
            existingBlog = blogService.getBlogById(id);
        } catch (EntityNotFoundException e) {
            logger.error("Blog with ID: {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }

        if (!isAdmin && !existingBlog.getAuthorUsername().equals(loggedInUsername)) {
            logger.error("User {} is not authorized to delete blog ID: {}", loggedInUsername, id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        blogService.deleteBlog(id);
        logger.info("Blog with ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}
