package com.badminton.management.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "courtId", nullable = false)
    private Court court;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;

    private LocalTime startTime;
    private LocalTime endTime;

    private LocalDate bookingDate;

    public Booking() {}

    //  Getter Setter 
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Court getCourt() { return court; }
    public void setCourt(Court court) { this.court = court; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", totalPrice=" + totalPrice +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", bookingDate=" + bookingDate +
                '}';
    }
}