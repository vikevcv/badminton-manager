package com.badminton.management.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.border.EmptyBorder;

public class DashboardPanel extends JPanel {
    // Khai báo 4 nhãn hiển thị số
    private JLabel lblAvailableCourts, lblTotalCustomers, lblTodayBookings, lblMonthlyRevenue;
    private JButton btnReload;

    public DashboardPanel() {
    setLayout(new BorderLayout());
    setBorder(new EmptyBorder(20, 20, 20, 20));

    // ===== TOP PANEL (chứa nút reload) =====
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    topPanel.setOpaque(false);

    btnReload = new JButton("⟳ Làm Mới");
    btnReload.setFocusPainted(false);

    topPanel.add(btnReload);

    // ===== GRID PANEL (4 card) =====
    JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
    gridPanel.setBorder(new EmptyBorder(100,100,100,100));
    gridPanel.setOpaque(false);

    // Labels
    lblAvailableCourts = new JLabel("0", SwingConstants.CENTER);
    lblTotalCustomers = new JLabel("0", SwingConstants.CENTER);
    lblTodayBookings = new JLabel("0", SwingConstants.CENTER);
    lblMonthlyRevenue = new JLabel("0 VNĐ", SwingConstants.CENTER);

    gridPanel.add(createStatCard("Số sân đang trống", lblAvailableCourts, new Color(46, 204, 113)));
    gridPanel.add(createStatCard("Tổng Số Khách", lblTotalCustomers, new Color(52, 152, 219)));
    gridPanel.add(createStatCard("Doanh Thu Tháng", lblMonthlyRevenue, new Color(231, 76, 60)));
    gridPanel.add(createStatCard("Booking Hôm Nay", lblTodayBookings, new Color(241, 196, 15)));

    // ===== ADD vào layout =====
    add(topPanel, BorderLayout.NORTH);
    add(gridPanel, BorderLayout.CENTER);
}

    // Hàm tạo Card nhận trực tiếp cái JLabel đã khởi tạo
    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));

        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    public void updateStatistics(int available, long customers, int bookings, double revenue) {
        // Cập nhật giá trị
        lblAvailableCourts.setText(String.valueOf(available));
        lblTotalCustomers.setText(String.valueOf(customers));
        lblTodayBookings.setText(String.valueOf(bookings));
        lblMonthlyRevenue.setText(String.format("%,.0f VNĐ", revenue));
        
        // Quan trọng: Ép Swing vẽ lại giao diện ngay lập tức
        this.revalidate();
        this.repaint();
    }

    public void addReloadListener(ActionListener listener) {
        btnReload.addActionListener(listener);
    }
}