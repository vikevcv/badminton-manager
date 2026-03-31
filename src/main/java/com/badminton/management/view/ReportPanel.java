package com.badminton.management.view;

import com.badminton.management.model.dto.BookingRevenueDTO;
import com.badminton.management.model.dto.MonthlyRevenueDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ReportPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRefresh;
    private JComboBox<String> cbFilter; // Hộp chọn bộ lọc

    //Dựng khung giao diện, chia 2 phần North (Menu) và Center (Bảng)
    public ReportPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Top Panel (Chứa Tiêu đề và Bộ lọc)
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("BÁO CÁO DOANH THU SÂN CẦU LÔNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(lblTitle, BorderLayout.WEST);

        // Panel chứa bộ lọc và nút Làm mới
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.add(new JLabel("Kiểu xem: "));
        
        String[] filters = {"Chi tiết theo Ngày & Sân", "Tổng kết theo Tháng/Năm"};
        cbFilter = new JComboBox<>(filters);
        filterPanel.add(cbFilter);
        
        btnRefresh = new JButton("Làm mới");
        filterPanel.add(btnRefresh);
        
        topPanel.add(filterPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 2. Bảng dữ liệu (Cột sẽ được định nghĩa lại khi đổi bộ lọc)
        tableModel = new DefaultTableModel(0, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    //  CÁC HÀM CẬP NHẬT GIAO DIỆN 

    // Chế độ 1: Đổi tên cột, xóa data cũ và nạp báo cáo Ngày & Sân
    public void updateDailyTable(List<BookingRevenueDTO> data) {
        String[] cols = {"Ngày Giao Dịch", "Tên Sân", "Tổng Doanh Thu (VNĐ)"};
        tableModel.setColumnIdentifiers(cols); // Đổi tên cột
        tableModel.setRowCount(0);             // Xóa data cũ
        for (BookingRevenueDTO dto : data) {
            tableModel.addRow(new Object[]{
                dto.bookingDate(), 
                dto.courtName(), 
                String.format("%,d", dto.totalRevenue().longValue())
            });
        }
    }

    // Chế độ 2: Đổi tên cột, xóa data cũ và nạp báo cáo Tháng / Năm
    public void updateMonthlyTable(List<MonthlyRevenueDTO> data) {
        String[] cols = {"Năm", "Tháng", "Tổng Doanh Thu Của Toàn Bộ Sân (VNĐ)"};
        tableModel.setColumnIdentifiers(cols); // Đổi tên cột
        tableModel.setRowCount(0);             // Xóa data cũ
        for (MonthlyRevenueDTO dto : data) {
            tableModel.addRow(new Object[]{
                dto.year(), 
                "Tháng " + dto.month(), 
                String.format("%,d", dto.totalRevenue().longValue())
            });
        }
    }

    // API CHO CONTROLLER 
    public int getFilterIndex() {
        return cbFilter.getSelectedIndex();
    }

    public void addRefreshListener(ActionListener listener) {
        btnRefresh.addActionListener(listener);
    }

    public void addFilterChangeListener(ActionListener listener) {
        cbFilter.addActionListener(listener); // Bắt sự kiện khi Admin đổi menu xổ xuống
    }
}