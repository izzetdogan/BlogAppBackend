package com.blog.dto;

import lombok.Data;

@Data
public class JwtAuthRequest {
    private  String username;
    private String password;
}
