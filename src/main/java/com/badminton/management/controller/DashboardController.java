package com.badminton.management.controller;

import com.badminton.management.service.BookingService;
import com.badminton.management.service.CourtService;
import com.badminton.management.service.CustomerService;
import com.badminton.management.service.impl.BookingServiceImpl;
import com.badminton.management.service.impl.CourtServiceImpl;
import com.badminton.management.service.impl.CustomerServiceImpl;
import com.badminton.management.view.DashboardPanel;
import javax.swing.Timer;

public class DashboardController {
    private DashboardPanel view;
    private CourtService courtService;
    private CustomerService customerService;
    private BookingService bookingService;

    public DashboardController(DashboardPanel view) {
        this.view = view;
        this.courtService = new CourtServiceImpl();
        this.customerService = new CustomerServiceImpl();
        this.bookingService = new BookingServiceImpl();

        this.view.addReloadListener(e -> refreshData());
        refreshData();
        // Thiết lập tự động cập nhật mỗi 1 p (Real-time) 
        Timer timer = new Timer(60000, e -> refreshData());
        timer.start();
    }

    public void refreshData() {
        // Gọi database
        int availableCourts = courtService.getAvailableCourtNow();
        long totalCustomers = customerService.getCountCustomer();
        int todayBookings = bookingService.getCountBooingToday();
        double monthlyRevenue = bookingService.getCurrentMonthRevenueReport().totalRevenue().doubleValue();
        view.updateStatistics(availableCourts, totalCustomers, todayBookings, monthlyRevenue);

    }
}
