package com.example.userservice.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SessionUserDto {

    private String userId;
    private String name;
    private String email;
    private List<String> roles;


}
