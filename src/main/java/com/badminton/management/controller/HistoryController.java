package com.badminton.management.controller;

import com.badminton.management.model.entity.Booking;
import com.badminton.management.service.BookingService;
import com.badminton.management.service.impl.BookingServiceImpl;
import com.badminton.management.view.HistoryPanel;

import java.util.List;

public class HistoryController {
    private HistoryPanel view;
    private BookingService bookingService;
    private int customerId;

    public HistoryController(HistoryPanel view, int customerId) {
        this.view = view;
        this.customerId = customerId;
        this.bookingService = new BookingServiceImpl();

        // Gắn sự kiện nút Làm mới
        this.view.addRefreshListener(e -> loadHistory());

        // Tự động load dữ liệu lần đầu khi vừa mở màn hình
        loadHistory(); 
    }

    public void loadHistory() {
        // Lấy danh sách từ Database theo ID Khách hàng
        List<Booking> bookings = bookingService.getBookingsByCustomer(customerId);
        
        // Ra lệnh cho View vẽ lại bảng
        view.updateTable(bookings);
    }
}