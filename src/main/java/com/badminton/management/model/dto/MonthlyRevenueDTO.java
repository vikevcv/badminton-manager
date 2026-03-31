package com.badminton.management.model.dto;

import java.math.BigDecimal;

public record MonthlyRevenueDTO(int year, int month, BigDecimal totalRevenue) {
}