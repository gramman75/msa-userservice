package com.example.userservice.controller;

import com.example.userservice.dto.RequestUser;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user-service")
@RequiredArgsConstructor
@Log
public class UserController {

    public final Environment env;
    public final UserService userService;
    public final ModelMapper modelMapper;

    @GetMapping("/health-check")
    public String status(){
        return "It's Working" + env.getProperty("local.server.port");
    }

    @GetMapping("/welcome")
    public String welcome() {
        return env.getProperty("greeting.message");
    }

    @PostMapping("/user")
    public void createUser(@RequestBody RequestUser requestUser) {
        User user = modelMapper.map(requestUser, User.class);
        user.setPwd(userService.SHA256Encrypt(requestUser.getPwd()));
        user.setUserId(UUID.randomUUID().toString());
//        requestUser.setEncryptPwd(userService.SHA256Encrypt(requestUser.getPwd()));
//        user.setUserId(UUID.randomUUID().toString());
//        user.setName(requestUser.getName());
//        user.setEmail(requestUser.getEmail());
//        user.setPwd(requestUser.getEncryptPwd());

        userService.saveUser(user);

        log.info("=== createUser ===");
        log.info(requestUser.getEmail());
        log.info(requestUser.getPwd());
        log.info(requestUser.getName());

    }
}
