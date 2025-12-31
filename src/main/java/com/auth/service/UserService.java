package com.auth.service;

import com.auth.entity.User;

public interface UserService {

    User getCurrentUser();

    User findByEmail(String email);
}
