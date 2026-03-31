package com.badminton.management.dao;

import com.badminton.management.model.entity.Account;
import java.util.List;

public interface AccountDAO {
    void save(Account acc);
    Account findById(int id);
    Account findByName(String username);
    List<Account> findAll();
    void update(Account acc);
    void delete(int id);
} 