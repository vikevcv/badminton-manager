package com.badminton.management.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;


public record BookingRevenueDTO(String courtName, LocalDate bookingDate, BigDecimal totalRevenue) {
}