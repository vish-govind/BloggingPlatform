-- Create USERS table if it doesn't exist
CREATE TABLE IF NOT EXISTS USERS (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    user_email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    provider VARCHAR(255),
    role VARCHAR(50) DEFAULT 'USER'
);

-- Create BLOGS table if it doesn't exist
CREATE TABLE IF NOT EXISTS BLOGS (
    blog_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,  -- TEXT instead of CLOB for MySQL
    author_id BIGINT NOT NULL,
    author_username VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES USERS(user_id) ON DELETE CASCADE
);

-- Create COMMENTS table if it doesn't exist
CREATE TABLE IF NOT EXISTS COMMENTS (
    comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    blog_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    author_name VARCHAR(255) NOT NULL,
    comment TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (blog_id) REFERENCES BLOGS(blog_id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES USERS(user_id) ON DELETE CASCADE
);


