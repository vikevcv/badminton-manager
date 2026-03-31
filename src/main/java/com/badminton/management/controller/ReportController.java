package com.badminton.management.controller;

import com.badminton.management.model.dto.BookingRevenueDTO;
import com.badminton.management.model.dto.MonthlyRevenueDTO;
import com.badminton.management.service.BookingService;
import com.badminton.management.service.impl.BookingServiceImpl;
import com.badminton.management.view.ReportPanel;
import java.util.List;

public class ReportController {
    private ReportPanel view;
    private BookingService bookingService;

    public ReportController(ReportPanel view) {
        this.view = view;
        this.bookingService = new BookingServiceImpl();

        // Lắng nghe sự kiện Bấm nút Làm mới HOẶC Đổi bộ lọc
        this.view.addRefreshListener(e -> loadData());
        this.view.addFilterChangeListener(e -> loadData());

        loadData(); // Load ngay lần đầu
    }

    private void loadData() {
        int filterIndex = view.getFilterIndex();

        if (filterIndex == 0) {
            // Admin chọn "Chi tiết theo Ngày & Sân"
            List<BookingRevenueDTO> data = bookingService.getRevenueReport();
            view.updateDailyTable(data);
        } else {
            // Admin chọn "Tổng kết theo Tháng/Năm"
            List<MonthlyRevenueDTO> data = bookingService.getMonthlyRevenueReport();
            view.updateMonthlyTable(data);
        }
    }
}