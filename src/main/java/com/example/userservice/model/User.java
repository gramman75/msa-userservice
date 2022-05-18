package com.example.userservice.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String userId;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name = "pwd", nullable = false)
    private String pwd;

}
