package com.badminton.management.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

import com.badminton.management.model.enumtype.CourtStatus;

@Entity
@Table(name = "Court")
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courtId;

    @Column(nullable = false)
    private String courtName;

    @Column(precision = 10, scale = 2)
    private BigDecimal pricePerHour;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourtStatus status;

    public Court() {
    }

    // getter setter
    public int getCourtId() {
        return courtId;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public BigDecimal getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(BigDecimal pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public CourtStatus getStatus() {
        return status;
    }

    public void setStatus(CourtStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Court{" +
                "courtId=" + courtId +
                ", courtName='" + courtName + '\'' +
                ", pricePerHour=" + pricePerHour +
                ", status=" + status +
                '}';
    }
}
