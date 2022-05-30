package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.SessionUserDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.model.UserEntity;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final RestTemplate restTemplate;
    private final Environment env;
    private final OrderServiceClient orderServiceClient;



    public UserDto findUser(String userId){
        UserEntity user = userRepository.findByUserId(userId);
        UserDto userDto = mapper.map(user, UserDto.class);

        String orderUrl = String.format(env.getProperty("order_service.url"), userId);

//        ResponseEntity<List<ResponseOrder>> ordersResponse = restTemplate.exchange(orderUrl, HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<ResponseOrder>>() {
//        });

        List<ResponseOrder> orders = orderServiceClient.getOrders(userId);

//        List<ResponseOrder> orders = ordersResponse.getBody();
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
                .username(userEntity.getEmail())
                .password(userEntity.getPwd())
                .authorities(new ArrayList<>())
                .build();
    }

    public SessionUserDto findDetailUser(String userName) {
        UserEntity user = userRepository.findByEmail(userName);
        SessionUserDto sessionUserDto = mapper.map(user, SessionUserDto.class);
        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");
        roles.add("BUYER");
        sessionUserDto.setRoles(roles);

        return sessionUserDto;
    }
}
