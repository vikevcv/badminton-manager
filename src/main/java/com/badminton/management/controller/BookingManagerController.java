package com.badminton.management.controller;

import com.badminton.management.dao.BookingDAO;
import com.badminton.management.dao.impl.BookingDAOImpl;
import com.badminton.management.model.entity.Booking;
import com.badminton.management.model.entity.Customer;
import com.badminton.management.service.BookingService;
import com.badminton.management.service.impl.BookingServiceImpl;
import com.badminton.management.view.BookingManagerPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class BookingManagerController {
    private BookingManagerPanel view;
    private BookingService bookingService;
    private BookingDAO bookingDAO; 
    
    private List<Booking> allBookingsCache; // Biến lưu tạm danh sách để tìm kiếm cho lẹ

    public BookingManagerController(BookingManagerPanel view) {
        this.view = view;
        this.bookingService = new BookingServiceImpl();
        this.bookingDAO = new BookingDAOImpl();

        // Lắng nghe các nút
        this.view.addUpdateListener(e -> handleUpdate());
        this.view.addDeleteListener(e -> handleDelete());
        this.view.addRefreshListener(e -> loadData());
        
        //  LẮNG NGHE NÚT TÌM KIẾM 
        this.view.addSearchListener(e -> handleSearch());

        loadData();
    }

    private void loadData() {
        allBookingsCache = bookingService.getAllBookings(); // Tải từ DB lên
        view.updateTable(allBookingsCache); // Vẽ lên bảng
    }

    private void handleSearch() {
        String keyword = view.getSearchKeyword().toLowerCase();

        // Nếu người dùng xóa trống ô tìm kiếm -> Hiện lại toàn bộ
        if (keyword.isEmpty()) {
            view.updateTable(allBookingsCache);
            return;
        }

        List<Booking> filteredList = new ArrayList<>();
        
        // Duyệt qua tất cả các đơn đặt sân đang có
        for (Booking b : allBookingsCache) {
            Customer c = b.getCustomer();
            
            // Lấy ra 3 thông tin: ID, Tên, Số điện thoại (Đổi hết thành chữ thường để so sánh)
            String id = String.valueOf(c.getCustomerId());
            String name = c.getCustomerName().toLowerCase();
            String phone = (c.getPhone() != null) ? c.getPhone().toLowerCase() : "";
            
            // Nếu 1 trong 3 thông tin này chứa từ khóa -> Cho vào danh sách kết quả
            if (id.contains(keyword) || name.contains(keyword) || phone.contains(keyword)) {
                filteredList.add(b);
            }
        }
        
        // Cập nhật lại cái Bảng với danh sách vừa lọc được
        view.updateTable(filteredList);
    }

    private void handleUpdate() {
        int id = view.getSelectedId();
        if (id == -1) {
            view.showMessage("Vui lòng chọn một đơn từ bảng!", "Chú ý", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Booking existing = bookingDAO.findById(id);
            if (existing != null) {
                existing.setBookingDate(view.getInputDate());
                existing.setStartTime(view.getInputStart());
                existing.setEndTime(view.getInputEnd());
                existing.setTotalPrice(view.getInputPrice());

                bookingDAO.update(existing); 
                view.showMessage("Cập nhật đơn thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
                view.clearForm();
                loadData();
            }
        } catch (Exception ex) {
            view.showMessage("Lỗi định dạng! Ngày (yyyy-MM-dd), Giờ (HH:mm), Tiền (Số).", "Lỗi Nhập Liệu", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int id = view.getSelectedId();
        if (id == -1) {
            view.showMessage("Vui lòng chọn đơn để xóa!", "Chú ý", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn HỦY/XÓA đơn đặt sân này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = bookingService.deleteBooking(id);
            if (success) {
                view.showMessage("Đã hủy đơn thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
                view.clearForm();
                loadData();
            } else {
                view.showMessage("Lỗi khi xóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}