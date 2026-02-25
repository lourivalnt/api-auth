package com.auth.core.domain.model;

import java.util.UUID;

import lombok.*;

@Getter
@Builder
public class User {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private Role role;
}
