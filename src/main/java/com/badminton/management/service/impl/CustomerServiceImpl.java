package com.badminton.management.service.impl;

import com.badminton.management.dao.CustomerDAO;
import com.badminton.management.dao.impl.CustomerDAOImpl;
import com.badminton.management.model.entity.Customer;
import com.badminton.management.model.enumtype.CustomerType;
import com.badminton.management.service.CustomerService;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDAO customerDAO = new CustomerDAOImpl();

    @Override public List<Customer> getAllCustomers() { return customerDAO.findAll(); }


    @Override public Customer getCustomer(int id) { return customerDAO.findById(id); }

    @Override public String addCustomer(Customer customer) {
        try { customerDAO.save(customer); return "SUCCESS"; } 
        catch (Exception e) { return "Lỗi hệ thống: " + e.getMessage(); }
    }

    @Override public String updateCustomer(Customer customer) {
        try { customerDAO.update(customer); return "SUCCESS"; } 
        catch (Exception e) { return "Lỗi hệ thống: " + e.getMessage(); }
    }

    @Override public String deleteCustomer(int customerId) {
        try { customerDAO.delete(customerId); return "SUCCESS"; } 
        catch (Exception e) { return "Không thể xóa khách hàng này vì họ đã có lịch sử đặt sân!"; }
    }
    @Override
    public int getOrCreateWalkInCustomer(CustomerType type) {
        List<Customer> allCus = customerDAO.findAll();
        
        // 1. Quét tìm xem có sẵn chưa
        for (Customer c : allCus) {
            if (c.getAccountId() == null && c.getType() == type && c.getCustomerName().startsWith("Khách vãng lai")) {
                return c.getCustomerId();
            }
        }
        
        // 2. Chưa có thì tạo mới
        Customer newWalkIn = new Customer();
        String typeName = (type == CustomerType.SINHVIEN) ? "Sinh viên" : "Người lớn";
        newWalkIn.setCustomerName("Khách vãng lai (" + typeName + ")");
        newWalkIn.setPhone("0000000000");
        newWalkIn.setType(type);
        
        try {
            customerDAO.save(newWalkIn); 
            allCus = customerDAO.findAll(); 
            return allCus.get(allCus.size() - 1).getCustomerId(); 
        } catch (Exception e) {
            return 1; // Fallback an toàn
        }
    }
    @Override
    public long getCountCustomer(){
        return customerDAO.getCountCustomer();
    }
    @Override
    public Customer getCustomerWithAccountID(int accountID){
        return customerDAO.findByAccountId(accountID);
    }
}