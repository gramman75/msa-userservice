package com.example.userservice.dto;

import lombok.Data;

@Data
public class RequestUser{
    private String email;
    private String pwd;
    private String name;
    private String userId;
    private String encryptPwd;
}
