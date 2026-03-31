package com.badminton.management.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.badminton.management.model.entity.Booking;
import com.badminton.management.model.enumtype.CustomerType;

public interface BookingService {
    String createBooking(int customerId, int courtId, LocalDate date, LocalTime start, LocalTime end);
    BigDecimal getPriceBooking(int courtId, LocalTime start, LocalTime end,  CustomerType customerType);
    List<Booking> getTodaysSchedule();
    List<Booking> getBookingsByDate(LocalDate date);
    boolean cancelBooking(int bookingId);
    List<Booking> getBookingsByCustomer(int customerId);
    List<Booking> getAllBookings();
    boolean deleteBooking(int id);
    List<com.badminton.management.model.dto.BookingRevenueDTO> getRevenueReport();
    List<com.badminton.management.model.dto.MonthlyRevenueDTO> getMonthlyRevenueReport();
}