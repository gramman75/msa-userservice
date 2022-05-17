package com.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    public final Environment env;

    @GetMapping("/health-check")
    public String status(){
        return "It's Working";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return env.getProperty("greeting.message");

    }
}