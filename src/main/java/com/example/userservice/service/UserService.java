package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import javax.swing.text.html.Option;
import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findUser(String userId){
        Optional<User> user = userRepository.findById(userId);
        return user;
    }

    public boolean saveUser(User user){
        User savedUser = userRepository.save(user);
        return savedUser.getUserId().isEmpty() ? false : true;

    }



}
