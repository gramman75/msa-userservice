package com.example.userservice.vo;

import lombok.Data;

@Data
public class RequestUser{
    private String email;
    private String pwd;
    private String name;
    private String userId;
    private String encryptPwd;
}
