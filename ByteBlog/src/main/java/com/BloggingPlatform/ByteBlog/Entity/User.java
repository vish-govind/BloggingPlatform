package com.BloggingPlatform.ByteBlog.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "role")
public class User {

	private Long userId;
	private String userName;
	private String userEmail;
	private String password;
	private String provider;
	private Role role = Role.USER;
}
