package com.badminton.management.service;
import com.badminton.management.model.entity.Customer;
import com.badminton.management.model.enumtype.CustomerType;

import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();
    public Customer getCustomer(int id);
    String addCustomer(Customer customer);
    String updateCustomer(Customer customer);
    String deleteCustomer(int customerId);
    int getOrCreateWalkInCustomer(CustomerType type);
    long getCountCustomer();
    Customer getCustomerWithAccountID(int accountID);
}