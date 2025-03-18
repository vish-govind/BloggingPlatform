package com.BloggingPlatform.ByteBlog.Mapper;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.BloggingPlatform.ByteBlog.Dto.BlogDto;
import com.BloggingPlatform.ByteBlog.Entity.Blog;

@Component
public class BlogMapper {

    private static final Logger logger = LoggerFactory.getLogger(BlogMapper.class);

    public BlogDto toDTO(Blog blog) {
        if (blog == null) {
            logger.warn("Attempted to map a null Blog entity to DTO.");
            return null;
        }

        logger.info("Mapping Blog entity (id: {}) to DTO.", blog.getBlogId());
        return new BlogDto(blog);
    }

    public List<BlogDto> toDTOList(List<Blog> blogs) {
        if (blogs == null || blogs.isEmpty()) {
            logger.warn("Attempted to map an empty or null Blog list to DTO list.");
            return new ArrayList<>();
        }

        logger.info("Mapping {} Blog entities to DTO list.", blogs.size());
        List<BlogDto> blogDTOs = new ArrayList<>();
        for (Blog blog : blogs) {
            blogDTOs.add(new BlogDto(blog));
        }
        return blogDTOs;
    }
}
