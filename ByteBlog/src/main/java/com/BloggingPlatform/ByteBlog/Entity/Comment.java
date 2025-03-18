package com.BloggingPlatform.ByteBlog.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

	private Long commentId;
	private Long blogId;
	private Long authorId;
	private String authorName;
	private String comment;
	private String createdAt;

}
