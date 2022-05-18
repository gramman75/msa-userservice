package com.example.userservice.controller;

import com.example.userservice.dto.RequestUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Log
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

    @PostMapping("/user")
    public void createUser(@RequestBody RequestUser requestUser) {

        log.info("=== createUser ===");
        log.info(requestUser.getEmail());
        log.info(requestUser.getPwd());
        log.info(requestUser.getName());

    }
}
