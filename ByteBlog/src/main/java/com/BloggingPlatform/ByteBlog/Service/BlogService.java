package com.BloggingPlatform.ByteBlog.Service;

import java.util.List;

import com.BloggingPlatform.ByteBlog.Dto.BlogDto;
import com.BloggingPlatform.ByteBlog.Entity.Blog;

public interface BlogService {
	
	List<BlogDto> getAllBlogs();
	BlogDto getBlogById(Long id);
	Blog createBlog(Blog blog, String username);
	BlogDto updateBlog(Blog blog);
	void deleteBlog(Long id);
	String getBlogAuthorEmail(Long blogId);

}
