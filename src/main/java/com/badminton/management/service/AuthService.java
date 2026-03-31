package com.badminton.management.service;

import com.badminton.management.model.entity.Account;

public interface AuthService {
    Account login(String username, String password);
    boolean changePassword(String username, String oldPass, String newPass);
    void logout();
    String register(String username, String password, String fullName, String phone, com.badminton.management.model.enumtype.CustomerType type);
}
