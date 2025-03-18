package com.BloggingPlatform.ByteBlog.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.BloggingPlatform.ByteBlog.Dto.BlogDto;
import com.BloggingPlatform.ByteBlog.Entity.Blog;
import com.BloggingPlatform.ByteBlog.Exception.EntityNotFoundException;
import com.BloggingPlatform.ByteBlog.Mapper.BlogMapper;
import com.BloggingPlatform.ByteBlog.Repo.BlogRepository;
import com.BloggingPlatform.ByteBlog.Repo.UserRepository;

@Service
public class BlogServiceImpl implements BlogService {

    private static final Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogMapper blogMapper;

    @Override
    @Cacheable(value = "allBlogs")
    public List<BlogDto> getAllBlogs() {
        logger.info("Fetching all blogs from the database");
        List<Blog> blogs = blogRepository.findAll();
        return blogMapper.toDTOList(blogs);
    }

    @Override
    @Cacheable(value = "blogs", key = "#id")
    public BlogDto getBlogById(Long id) {
        logger.info("Fetching blog with ID: {}", id);
        Optional<Blog> blogOptional = blogRepository.findById(id);
        if (blogOptional.isEmpty()) {
            logger.error("Blog with ID {} not found", id);
            throw new EntityNotFoundException("Blog with ID " + id + " not found");
        }
        return blogMapper.toDTO(blogOptional.get());
    }

    @Override
    @CacheEvict(value = "allBlogs", allEntries = true)
    public Blog createBlog(Blog blog, String username) {
        logger.info("Creating new blog for user: {}", username);
        Long authorId = userRepository.findUserIdByUsername(username);
        if (authorId == null) {
            logger.error("User not found with username: {}", username);
            throw new EntityNotFoundException("User not found with username: " + username);
        }

        blog.setAuthorId(authorId);
        blog.setAuthorUsername(username);
        blogRepository.save(blog);
        logger.info("Blog created successfully with ID: {}", blog.getBlogId());
        return blog;
    }

    @Override
    @CachePut(value = "blogs", key = "#blog.id")
    @CacheEvict(value = "allBlogs", allEntries = true)
    public BlogDto updateBlog(Blog blog) {
        logger.info("Updating blog with ID: {}", blog.getBlogId());
        blogRepository.update(blog);
        return blogMapper.toDTO(blog);
    }

    @Override
    @CacheEvict(value = {"blogs", "allBlogs"}, key = "#id", allEntries = true)
    public void deleteBlog(Long id) {
        logger.info("Deleting blog with ID: {}", id);
        blogRepository.deleteById(id);
    }

    @Override
    public String getBlogAuthorEmail(Long blogId) {
        logger.info("Fetching author email for blog ID: {}", blogId);
        Optional<Blog> blog = blogRepository.findById(blogId);
        
        if (blog.isEmpty()) {
            logger.warn("Blog with ID {} not found while fetching author email", blogId);
            throw new EntityNotFoundException("Blog with ID " + blogId + " not found");
        }

        Long authorId = blog.get().getAuthorId();
        return userService.getUserEmail(authorId);
    }
}
