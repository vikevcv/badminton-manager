package com.badminton.management.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.time.Duration;

import com.badminton.management.dao.BookingDAO;
import com.badminton.management.dao.CourtDAO;
import com.badminton.management.dao.CustomerDAO;
import com.badminton.management.dao.impl.BookingDAOImpl;
import com.badminton.management.dao.impl.CourtDAOImpl;
import com.badminton.management.dao.impl.CustomerDAOImpl;
import com.badminton.management.model.entity.Booking;
import com.badminton.management.model.entity.Court;
import com.badminton.management.model.entity.Customer;
import com.badminton.management.model.enumtype.CustomerType;
import com.badminton.management.service.BookingService;

public class BookingServiceImpl implements BookingService{
    private BookingDAO bookingDAO = new BookingDAOImpl();
    private CourtDAO courtDAO = new CourtDAOImpl();
    private CustomerDAO customerDAO = new CustomerDAOImpl();

    @Override
    public String createBooking(int customerId, int courtId, LocalDate date, LocalTime start, LocalTime end) {
        // 1. Kiểm tra trùng lịch 
        List<Booking> overlaps = bookingDAO.findOverlappingBookings(courtId, date, start, end);
        if (!overlaps.isEmpty()) {
            return "Sân đã có người đặt trong khung giờ này!";
        }

        // 2. Lấy thông tin Customer và Court từ ID
        Customer customer = customerDAO.findById(customerId);
        Court court = courtDAO.findById(courtId);

        if (customer == null || court == null) {
            return "Thông tin khách hàng hoặc sân không tồn tại!";
        }

        // 3. Tạo đối tượng Booking mới
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setCourt(court);
        booking.setBookingDate(date);
        booking.setStartTime(start);
        booking.setEndTime(end);

        // 4. Tính toán tổng tiền (Sử dụng BigDecimal để chính xác)
        long minutes = Duration.between(start, end).toMinutes();
        double hours = minutes / 60.0;
        BigDecimal totalPrice = court.getPricePerHour().multiply(BigDecimal.valueOf(hours));
        //5. kiểm tra sinh viên hay người lớn
        if(customer.getType() == CustomerType.SINHVIEN) totalPrice = totalPrice.multiply(BigDecimal.valueOf(0.7));
        booking.setTotalPrice(totalPrice);

        // 5. Lưu vào Database
        try {
            bookingDAO.save(booking);
            return "SUCCESS";
        } catch (Exception e) {
            return "Lỗi hệ thống: " + e.getMessage();
        }
    }
    @Override
    public BigDecimal getPriceBooking(int courtId, LocalTime start, LocalTime end, CustomerType customerType){
        Court court = courtDAO.findById(courtId);
        // 4. Tính toán tổng tiền (Sử dụng BigDecimal để chính xác)
        long minutes = Duration.between(start, end).toMinutes();
        double hours = minutes / 60.0;
        BigDecimal totalPrice = court.getPricePerHour().multiply(BigDecimal.valueOf(hours));
        if(customerType == CustomerType.SINHVIEN) totalPrice = totalPrice.multiply(BigDecimal.valueOf(0.7));
        return totalPrice;
    }
    @Override
    public List<Booking> getTodaysSchedule() {
        // Gọi hàm findByDate với ngày hiện tại
        return bookingDAO.findByDate(LocalDate.now());
    }
    @Override
    public boolean cancelBooking(int bookingId) {
        try {
            Booking booking = bookingDAO.findById(bookingId);
            if (booking != null) {
                bookingDAO.delete(bookingId);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public List<Booking> getBookingsByDate(LocalDate date) {
        return bookingDAO.findByDate(date);
    }
    @Override
    public List<Booking> getBookingsByCustomer(int customerId) {
        return bookingDAO.findByCustomer(customerId); 
    }
    @Override
    public List<Booking> getAllBookings() {
        return bookingDAO.findAll();
    }

    @Override
    public boolean deleteBooking(int id) {
        try {
            Booking booking = bookingDAO.findById(id);
            if (booking != null) {
                bookingDAO.delete(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public List<com.badminton.management.model.dto.BookingRevenueDTO> getRevenueReport() {
        return bookingDAO.getRevenueReport();
    }
    @Override
    public List<com.badminton.management.model.dto.MonthlyRevenueDTO> getMonthlyRevenueReport() {
        return bookingDAO.getMonthlyRevenueReport();
    }
}
