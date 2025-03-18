package com.BloggingPlatform.ByteBlog.Dto;

import com.BloggingPlatform.ByteBlog.Entity.Blog;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BlogDto {
	
	private Long blogId;
    private String title;
    private String content;
    private String authorUsername;
    private String createdAt;
	
	public BlogDto(Blog blog) {
		// TODO Auto-generated constructor stub
		this.blogId = blog.getBlogId();
		this.title = blog.getTitle();
		this.content = blog.getContent();
		this.authorUsername = blog.getAuthorUsername();
		this.createdAt = blog.getCreatedAt();
	}
	
	
    
}
