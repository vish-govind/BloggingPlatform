package com.BloggingPlatform.ByteBlog.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    private String identifier;
    private String password;

}
