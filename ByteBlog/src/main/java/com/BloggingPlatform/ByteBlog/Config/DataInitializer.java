package com.BloggingPlatform.ByteBlog.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

	private final JdbcTemplate jdbcTemplate;

	public DataInitializer(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void run(String... args) throws Exception {

		Integer count = jdbcTemplate.queryForObject
				("SELECT COUNT(*) FROM USERS WHERE user_email = ?", Integer.class,"ByteBloguser@gmail.com");

		if (count == null || count == 0) {
			logger.info("Inserting default admin user...");

			jdbcTemplate.update
			("INSERT INTO USERS (user_name, user_email, password, provider, role) VALUES (?, ?, ?, ?, ?)",
					"ByteBlog", "ByteBloguser@gmail.com", "admin123", "LOCAL", "ADMIN");

			logger.info("Default admin user inserted successfully!");

		} else {
			logger.info("Admin user already exists, skipping insertion.");
		}

		// Insert sample Blog
		Integer authorId = jdbcTemplate.queryForObject("SELECT user_id FROM USERS WHERE user_email = ?", Integer.class,
				"ByteBloguser@gmail.com");

		if (authorId != null) {
			Integer blogCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM BLOGS WHERE author_id = ?",
					Integer.class, authorId);

			if (blogCount == null || blogCount == 0) {
				logger.info("Inserting sample blog...");

				jdbcTemplate.update(
						"INSERT INTO BLOGS (title, content, author_id, author_username) VALUES (?, ?, ?, ?)",
						"First Blog Post", "This is the content of the first blog post.", authorId, "ByteBlog");

				logger.info("Sample blog inserted successfully!");
			} else {
				logger.info("Sample blog already exists, skipping insertion.");
			}

			Integer blogId = jdbcTemplate.queryForObject("SELECT blog_id FROM BLOGS WHERE author_id = ? AND title = ?",
					Integer.class, authorId, "First Blog Post");

			if (blogId != null) {
				Integer commentCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM COMMENTS WHERE blog_id = ?",
						Integer.class, blogId);

				if (commentCount == null || commentCount == 0) {
					logger.info("Inserting sample comment...");

					jdbcTemplate.update(
							"INSERT INTO COMMENTS (blog_id, author_id, author_name, comment) VALUES (?, ?, ?, ?)",
							blogId, authorId, "ByteBlog", "Great blog post! Looking forward to more content.");

					logger.info("Sample comment inserted successfully!");
				} else {
					logger.info("Sample comment already exists, skipping insertion.");
				}
			}
		}
	}
}
