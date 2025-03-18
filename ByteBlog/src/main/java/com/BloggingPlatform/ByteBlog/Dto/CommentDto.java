package com.BloggingPlatform.ByteBlog.Dto;

import com.BloggingPlatform.ByteBlog.Entity.Comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
	
	 private Long commentId;
	 private Long blogId;
	 private String comment;
	 private String authorUsername;
	 private String createdAt;
	 
	public CommentDto(Comment comment) {
		super();
		this.commentId = comment.getCommentId();
		this.blogId = comment.getBlogId();
		this.comment = comment.getComment();
		this.authorUsername = comment.getAuthorName();
		this.createdAt = comment.getCreatedAt();
	}

	    
	    
}
