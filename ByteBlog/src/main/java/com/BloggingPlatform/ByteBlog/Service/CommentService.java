package com.BloggingPlatform.ByteBlog.Service;

import java.util.List;

import com.BloggingPlatform.ByteBlog.Dto.CommentDto;
import com.BloggingPlatform.ByteBlog.Entity.Comment;

public interface CommentService {
	
	List<CommentDto> getCommentsByBlogId(Long blogId);
	CommentDto getCommentById(Long id);
	CommentDto addComment(Long blogId, Comment comment, String username);
	CommentDto updateComment(Comment comment);
	void deleteComment(Long id);
	String getCommentAuthorEmail(Long commentId);

}
