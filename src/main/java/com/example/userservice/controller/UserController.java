package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.model.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.ResponseUser;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Log
public class UserController {

    private final Environment env;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/health-check")
    @Timed(value = "users.status", longTask = true)
    public String status(){
        return String.format("It's Working port : %s, token secret: %s, token expiration_time: %s "
                ,env.getProperty("local.server.port")
                ,env.getProperty("token.secret")
                ,env.getProperty("token.expiration_time")
        );
    }

    @GetMapping("/welcome")
    @Timed(value = "users.welcome", longTask = true)
    public String welcome() {
        return env.getProperty("greeting.message");
    }

    @PostMapping("/user")
    public void createUser(@RequestBody RequestUser requestUser) {
        UserEntity user = modelMapper.map(requestUser, UserEntity.class);
        user.setPwd(passwordEncoder.encode(requestUser.getPwd()));
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

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers(){
        List<UserDto> userAll = userService.findUserAll();
        List<ResponseUser> result = new ArrayList<>();

        userAll.forEach(user->{
            result.add(modelMapper.map(user, ResponseUser.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId){
        UserDto user = userService.findUser(userId);
        ResponseUser result = modelMapper.map(user, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}

