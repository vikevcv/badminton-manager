package com.badminton.management.controller;

import com.badminton.management.dao.impl.CustomerDAOImpl;
import com.badminton.management.model.entity.Customer;
import com.badminton.management.service.CustomerService;
import com.badminton.management.service.impl.CustomerServiceImpl;
import com.badminton.management.view.CustomerPanel;

import javax.swing.*;
import java.util.List;

public class CustomerController {
    private CustomerPanel view;
    private CustomerService customerService;

    public CustomerController(CustomerPanel view) {
        this.view = view;
        this.customerService = new CustomerServiceImpl();

        this.view.addSaveListener(e -> handleAdd());
        this.view.addUpdateListener(e -> handleUpdate());
        this.view.addDeleteListener(e -> handleDelete());
        this.view.addRefreshListener(e -> loadData());
        
        loadData();
    }

    private void loadData() {
        List<Customer> customers = customerService.getAllCustomers();
        view.updateTable(customers);
    }

    private void handleAdd() {
        Customer c = view.getFormInput();
        if (c.getCustomerName().isEmpty() || c.getPhone().isEmpty()) {
            view.showMessage("Tên và SĐT không được để trống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String result = customerService.addCustomer(c);
        if (result.equals("SUCCESS")) {
            view.showMessage("Thêm khách hàng thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
            view.clearForm(); loadData();
        } else view.showMessage(result, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void handleUpdate() {
        Customer formCus = view.getFormInput();
        if (formCus.getCustomerId() == 0) {
            view.showMessage("Vui lòng chọn khách hàng cần cập nhật!", "Chú ý", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // MẸO RẤT QUAN TRỌNG: Gọi DB lấy Customer cũ ra để giữ nguyên accountId của họ, chỉ đè Tên và SĐT lên
        Customer existing = new CustomerDAOImpl().findById(formCus.getCustomerId());
        if (existing != null) {
            existing.setCustomerName(formCus.getCustomerName());
            existing.setPhone(formCus.getPhone());
            existing.setType(formCus.getType());
            
            String result = customerService.updateCustomer(existing);
            if (result.equals("SUCCESS")) {
                view.showMessage("Cập nhật thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
                view.clearForm(); loadData();
            } else view.showMessage(result, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int cusId = view.getSelectedId();
        if (cusId == -1) {
            view.showMessage("Vui lòng chọn khách hàng để xóa!", "Chú ý", JOptionPane.WARNING_MESSAGE); return;
        }
        int confirm = JOptionPane.showConfirmDialog(null, "Xóa khách hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String result = customerService.deleteCustomer(cusId);
            if (result.equals("SUCCESS")) {
                view.showMessage("Xóa thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
                view.clearForm(); loadData();
            } else view.showMessage(result, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}