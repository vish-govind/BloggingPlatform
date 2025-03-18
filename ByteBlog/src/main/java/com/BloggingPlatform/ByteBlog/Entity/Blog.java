package com.BloggingPlatform.ByteBlog.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
	
	private Long blogId;
    private String title;
    private String content;
    private Long  authorId;
    private String authorUsername;
    private String createdAt;

}
