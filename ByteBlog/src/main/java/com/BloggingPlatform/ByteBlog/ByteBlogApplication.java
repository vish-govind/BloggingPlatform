package com.BloggingPlatform.ByteBlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ByteBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(ByteBlogApplication.class, args);
	}

}
