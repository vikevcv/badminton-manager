package com.badminton.management.service.impl;

import com.badminton.management.dao.AccountDAO;
import com.badminton.management.dao.CustomerDAO;
import com.badminton.management.dao.impl.AccountDAOImpl;
import com.badminton.management.dao.impl.CustomerDAOImpl;
import com.badminton.management.model.entity.Account;
import com.badminton.management.model.entity.Customer;
import com.badminton.management.service.AuthService;

public class AuthServiceImpl implements AuthService{
    private AccountDAO accountDAO = new AccountDAOImpl();
    private CustomerDAO customerDAO = new CustomerDAOImpl();
    @Override
    public Account login(String username, String password){
        Account acc = accountDAO.findByName(username);
        if (acc != null && acc.getPassword().equals(password)) {
            return acc; 
        }
        return null; 
    }
    @Override
    public boolean changePassword(String username, String oldPass, String newPass) {
        Account acc = login(username, oldPass); // Tận dụng hàm login để check pass cũ
        if (acc != null) {
            acc.setPassword(newPass);
            accountDAO.update(acc);
            return true;
        }
        return false;
    }
    @Override
    public void logout(){
        
    }
    @Override
    public String register(String username, String password, String fullName, String phone, com.badminton.management.model.enumtype.CustomerType type) {
        // 1. Kiểm tra xem Username đã bị ai dùng chưa
        if (accountDAO.findByName(username) != null) {
            return "Tên đăng nhập này đã tồn tại, vui lòng chọn tên khác!";
        }

        // 2. Kiểm tra Số điện thoại (Tùy chọn nhưng nên có)
        if (customerDAO.findByPhone(phone) != null) {
            return "Số điện thoại này đã được đăng ký!";
        }

        try {
            // 3. Tạo và lưu Account trước
            Account newAcc = new Account();
            newAcc.setUsername(username);
            newAcc.setPassword(password);
            newAcc.setRole(com.badminton.management.model.enumtype.Role.MEMBER); // Mặc định người đăng ký mới là MEMBER
            accountDAO.save(newAcc);

            // 4. Lấy lại Account vừa lưu từ DB để chắc chắn nó đã có ID
            Account savedAcc = accountDAO.findByName(username);

            // 5. Tạo và lưu Customer, móc nối với Account vừa tạo
            Customer newCus = new Customer();
            newCus.setCustomerName(fullName);
            newCus.setPhone(phone);
            newCus.setType(type);
            // Lưu ý: Trong Entity Customer của bạn, tên biến là accountId nhưng kiểu là Account
            newCus.setAccountId(savedAcc); 
            
            customerDAO.save(newCus);

            return "SUCCESS";
        } catch (Exception e) {
            return "Lỗi hệ thống trong quá trình đăng ký: " + e.getMessage();
        }
    }
}
