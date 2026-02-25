package com.auth.infrastructure.persistence.entity;

import java.util.UUID;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String email;
    private String password;

    private String role;
}