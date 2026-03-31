package com.badminton.management.dao;

import com.badminton.management.model.entity.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingDAO {
    void save(Booking booking);
    void update(Booking booking);
    void delete(int id);
    Booking findById(int id);
    List<Booking> findAll();
    List<Booking> findByDate(LocalDate date);
    List<Booking> findByCustomer(int customerId);
    List<Booking> findByCourt(int courtId, LocalDate date);
    List<Booking> findOverlappingBookings(int courtId, LocalDate date, LocalTime start, LocalTime end);
    List<com.badminton.management.model.dto.BookingRevenueDTO> getRevenueReport();
    List<com.badminton.management.model.dto.MonthlyRevenueDTO> getMonthlyRevenueReport();
}
