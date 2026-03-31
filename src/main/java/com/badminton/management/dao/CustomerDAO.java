package com.badminton.management.dao;

import com.badminton.management.model.entity.Customer;
import java.util.List;

public interface CustomerDAO {
    void save(Customer cus);
    void update(Customer cus);
    void delete(int id);
    Customer findById(int id);
    Customer findByAccountId(int accountId);
    Customer findByPhone(String phone);
    List<Customer> findAll();
    List<Customer> findByName(String name);
} 