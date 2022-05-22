package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.model.UserEntity;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;


    public UserDto findUser(String userId){
        UserEntity user = userRepository.findByUserId(userId);
        UserDto userDto = mapper.map(user, UserDto.class);

        List<ResponseOrder> orders = new ArrayList<>();
        userDto.setOrders(orders);

        return userDto;

    }

    public List<UserDto> findUserAll() {
        List<UserEntity> users = userRepository.findAll();

        List<UserDto> userDtos = new ArrayList<>();

        users.forEach( user->{
            UserDto userDto = mapper.map(user, UserDto.class);
            List<ResponseOrder> orders = new ArrayList<>();

            userDto.setOrders(orders);
            userDtos.add(userDto);
        });

        return userDtos;
    }

    public boolean saveUser(UserEntity user){
        UserEntity savedUser = userRepository.save(user);
        return savedUser.getUserId().isEmpty() ? false : true;
    }

    public String SHA256Encrypt(String password){
        StringBuffer hexString = new StringBuffer();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));


            for(int i=0; i < hash.length; i++){
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');

                hexString.append(hex);
            }

        }catch(Exception e){
        }
        return hexString.toString().toUpperCase();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null){
            throw new UsernameNotFoundException(username);
        }

        return User.builder()
                .username(userEntity.getName())
                .password(userEntity.getPwd())
                .authorities(new ArrayList<>())
                .build();
    }
}
