package com.badminton.management.view;

import com.badminton.management.model.entity.Booking;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class HistoryPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRefresh;

    //Thiết lập bố cục chính (BorderLayout) với khoảng cách lề (EmptyBorder) cho thoáng.
    public HistoryPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Top Panel: Tiêu đề và Nút Làm mới
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("LỊCH SỬ ĐẶT SÂN CỦA BẠN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(lblTitle, BorderLayout.WEST);

        btnRefresh = new JButton("Làm mới danh sách");
        topPanel.add(btnRefresh, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 2. Center Panel: Bảng hiển thị
        String[] columns = {"Mã Đơn", "Tên Sân", "Ngày Đặt", "Giờ Bắt Đầu", "Giờ Kết Thúc", "Tổng Tiền"};
        //Định nghĩa 6 cột và khóa tính năng sửa ô (isCellEditable = false) để bảo vệ dữ liệu lịch sử.
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên bảng lịch sử
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        //Gắn bảng vào khung cuộn để hiển thị tốt khi danh sách đơn đặt sân tăng lên.
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    //  API CHO CONTROLLER 
    
    // Mở cổng sự kiện cho nút Làm mới
    public void addRefreshListener(ActionListener listener) {
        btnRefresh.addActionListener(listener);
    }

    // Nhận dữ liệu từ Controller và đổ vào bảng
    public void updateTable(List<Booking> bookings) {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        if (bookings != null) {
            for (Booking b : bookings) {
                Object[] row = {
                    "BK" + b.getBookingId(), // Thêm chữ BK cho mã đơn nhìn chuyên nghiệp
                    b.getCourt().getCourtName(),
                    b.getBookingDate().toString(),
                    b.getStartTime().toString(),
                    b.getEndTime().toString(),
                    b.getTotalPrice() + " VNĐ"
                };
                tableModel.addRow(row);
            }
        }
    }
}