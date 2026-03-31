package com.badminton.management.controller;

// === IMPORT CHUẨN XÁC TẠI ĐÂY ===

import com.badminton.management.model.entity.Booking;
import com.badminton.management.model.entity.Court;
import com.badminton.management.model.entity.Customer;
import com.badminton.management.model.enumtype.CustomerType;
import com.badminton.management.model.enumtype.Role; // <--- Import đúng Enum của hệ thống
import com.badminton.management.service.BookingService;
import com.badminton.management.service.CourtService;
import com.badminton.management.service.CustomerService;
import com.badminton.management.service.impl.BookingServiceImpl;
import com.badminton.management.service.impl.CourtServiceImpl;
import com.badminton.management.service.impl.CustomerServiceImpl;
import com.badminton.management.view.BookingPanel;

import javax.swing.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

public class BookingController {
    private BookingPanel view;
    private BookingService bookingService;
    private CourtService courtService;
    private CustomerService customerService;
    private int customerId;
    private Role userRole; // Nhận quyền Admin hay Member

    // Constructor
    public BookingController(BookingPanel view, int customerId, Role userRole) {
        this.view = view;
        this.customerId = customerId;
        this.userRole = userRole;
        this.bookingService = new BookingServiceImpl();
        this.courtService = new CourtServiceImpl();
        this.customerService = new CustomerServiceImpl();

        this.view.addLoadListener(e -> loadSchedule());
        this.view.addBookListener(e -> processBooking());
        this.view.addLoadSpinner(e -> loadSchedule());

        SwingUtilities.invokeLater(() -> loadSchedule());
    }

    public void loadSchedule() {
        LocalDate date = view.getSelectedDate();
        List<Court> courts = courtService.getAllCourts();
        List<Booking> bookings = bookingService.getBookingsByDate(date);
        
        view.updateSchedule(courts, bookings); 
    }

    public void processBooking() {
        List<Booking> selectedBookings = view.extractSelectedBookings();

        if (selectedBookings.isEmpty()) {
            view.showMessage("Vui lòng click vào các ô Trống để chọn giờ!", "Chú ý", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tạo biến ID tạm thời, mặc định là ID của người đang đăng nhập
        int finalCustomerId = this.customerId;

        if (this.userRole == Role.ADMIN) {
            Object[] options = {"Khách Sinh viên", "Khách Người lớn", "Hủy thao tác"};
            int choice = JOptionPane.showOptionDialog(null,
                    "Bạn đang đặt sân hộ cho khách vãng lai.\nVui lòng chọn loại khách hàng:",
                    "Xác nhận loại khách",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            // Nếu Admin bấm Hủy hoặc ấn nút X tắt cửa sổ -> Dừng việc đặt sân
            if (choice == 2 || choice == JOptionPane.CLOSED_OPTION) {
                return; 
            }

            // Lấy ra lựa chọn của Admin
            CustomerType selectedType = (choice == 0) ? CustomerType.SINHVIEN : CustomerType.NGUOILON;
            
            // Lấy ID của Khách vãng lai tương ứng gán vào biến
            finalCustomerId = customerService.getOrCreateWalkInCustomer(selectedType);
        }
        int successCount = 0;
        Customer c = customerService.getCustomer(finalCustomerId);
        
        BigDecimal price = BigDecimal.ZERO;

        for (Booking b : selectedBookings) {
            BigDecimal p = bookingService.getPriceBooking(
                b.getCourt().getCourtId(),
                b.getStartTime(),
                b.getEndTime(),
                c.getType()
            );
            price = price.add(p);
        }
        // định dạng tiền theo tiền vnd
        DecimalFormat df = new DecimalFormat("#,###");
        String formatPrice = df.format(price).replace(",", ".");
        //hiện bản giá
        Object[] options = {"Chấp Nhận", "Hủy"};
            int priceChoice = JOptionPane.showOptionDialog(null,
                    "Tổng Giá: " + formatPrice + " VND",
                    "Xác nhận giá sân",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
        if (priceChoice == 1 || priceChoice == JOptionPane.CLOSED_OPTION) {
                return; 
        }

        for (Booking b : selectedBookings) {
            // LƯU Ý: Dùng finalCustomerId ở đây để lưu đơn
            String result = bookingService.createBooking(
                finalCustomerId,
                b.getCourt().getCourtId(),
                b.getBookingDate(),
                b.getStartTime(),
                b.getEndTime()
            );
            
            if (result.equals("SUCCESS")) {
                successCount++;
            } else {
                view.showMessage("Lỗi đặt sân: " + result, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (successCount > 0) {
            view.showMessage("Đặt thành công " + successCount + " đơn đặt sân!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
            loadSchedule(); 
        }
    }

}