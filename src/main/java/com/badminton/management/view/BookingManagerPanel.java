package com.badminton.management.view;

import com.badminton.management.model.entity.Booking;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class BookingManagerPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    
    private JTextField txtBookingId, txtCustomer, txtCourt, txtDate, txtStart, txtEnd, txtPrice;
    private JButton btnUpdate, btnDelete, btnClear, btnRefresh;
     
    //  KHAI BÁO THÊM BIẾN TÌM KIẾM 
    private JTextField txtSearch;
    private JButton btnSearch;

    // Layout 4 vùng: North (Tìm kiếm), West (Chi tiết đơn), Center (Danh sách), South (Nút hành động).
    public BookingManagerPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. PHẦN TOP: TIÊU ĐỀ + TÌM KIẾM + LÀM MỚI
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("QUẢN LÝ TOÀN BỘ ĐƠN ĐẶT SÂN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(lblTitle, BorderLayout.WEST);

        // Khối chứa các nút bên phải
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.add(new JLabel("Tìm khách (Tên, SĐT, ID):"));
        
        txtSearch = new JTextField(15); // Ô nhập từ khóa
        // Search Bar: Cung cấp ô nhập liệu để tìm kiếm khách hàng theo Tên, SĐT hoặc mã ID.
        actionPanel.add(txtSearch);
        
        btnSearch = new JButton("Tìm"); // Nút tìm
        actionPanel.add(btnSearch);
        
        btnRefresh = new JButton("Làm Mới"); // Nút làm mới
        actionPanel.add(btnRefresh);

        topPanel.add(actionPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        // 2. PHẦN TRÁI: FORM CHI TIẾT
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết Đơn Đặt Sân"));
        formPanel.setPreferredSize(new Dimension(320, 0));

        formPanel.add(new JLabel("Mã Đơn:"));
        txtBookingId = new JTextField(); txtBookingId.setEditable(false);// Read-only fields: Khóa mã đơn, tên khách và tên sân để đảm bảo tính nhất quán (Chỉ cho phép sửa giờ và tiền).
        formPanel.add(txtBookingId);

        formPanel.add(new JLabel("Khách Hàng:"));
        txtCustomer = new JTextField(); txtCustomer.setEditable(false);
        formPanel.add(txtCustomer);

        formPanel.add(new JLabel("Tên Sân:"));
        txtCourt = new JTextField(); txtCourt.setEditable(false);
        formPanel.add(txtCourt);

        formPanel.add(new JLabel("Ngày Đặt (yyyy-MM-dd):"));
        txtDate = new JTextField(); formPanel.add(txtDate);

        formPanel.add(new JLabel("Giờ Bắt Đầu (HH:mm):"));
        txtStart = new JTextField(); formPanel.add(txtStart);

        formPanel.add(new JLabel("Giờ Kết Thúc (HH:mm):"));
        txtEnd = new JTextField(); formPanel.add(txtEnd);

        formPanel.add(new JLabel("Tổng Tiền (VNĐ):"));
        txtPrice = new JTextField(); formPanel.add(txtPrice);

        add(formPanel, BorderLayout.WEST);
        // 3. PHẦN GIỮA: BẢNG DỮ LIỆU
        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] cols = {"Mã Đơn", "Khách Hàng", "Sân", "Ngày", "Bắt Đầu", "Kết Thúc", "SĐT", "Tổng Tiền"};
        tableModel = new DefaultTableModel(cols, 0) {

            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);
        // 4. PHẦN DƯỚI: NÚT CHỨC NĂNG
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnUpdate = new JButton("Lưu Cập Nhật Đơn");
        btnDelete = new JButton("Hủy/Xóa Đơn Này");
        btnClear = new JButton("Xóa Trống Form");

        btnPanel.add(btnUpdate); btnPanel.add(btnDelete); btnPanel.add(btnClear);
        add(btnPanel, BorderLayout.SOUTH);

        // Sự kiện click bảng
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtBookingId.setText(tableModel.getValueAt(row, 0).toString());
                txtCustomer.setText(tableModel.getValueAt(row, 1).toString());
                txtCourt.setText(tableModel.getValueAt(row, 2).toString());
                txtDate.setText(tableModel.getValueAt(row, 3).toString());
                txtStart.setText(tableModel.getValueAt(row, 4).toString());
                txtEnd.setText(tableModel.getValueAt(row, 5).toString());
                txtPrice.setText(tableModel.getValueAt(row, 6).toString());
            }
        });

        btnClear.addActionListener(e -> clearForm());
    }

    // Refresh Data: Nạp lại danh sách tất cả các đơn đặt sân từ Database lên bảng hiển thị.
    public void updateTable(List<Booking> bookings) {
        tableModel.setRowCount(0);
        for (Booking b : bookings) {
            tableModel.addRow(new Object[]{
                b.getBookingId(), b.getCustomer().getCustomerName(), b.getCourt().getCourtName(),
                b.getBookingDate(), b.getStartTime(), b.getEndTime(), b.getCustomer().getPhone(), b.getTotalPrice()
            });
        }
    }

    public void clearForm() {
        txtBookingId.setText(""); txtCustomer.setText(""); txtCourt.setText("");
        txtDate.setText(""); txtStart.setText(""); txtEnd.setText(""); txtPrice.setText("");
        table.clearSelection();
    }

    // Data Parsing: Chuyển đổi dữ liệu từ dạng chữ (String) sang kiểu dữ liệu chuẩn (LocalDate, LocalTime) để xử lý logic.
    public int getSelectedId() {
        return txtBookingId.getText().isEmpty() ? -1 : Integer.parseInt(txtBookingId.getText());
    }
    public LocalDate getInputDate() { return LocalDate.parse(txtDate.getText().trim()); }
    public LocalTime getInputStart() { return LocalTime.parse(txtStart.getText().trim()); }
    public LocalTime getInputEnd() { return LocalTime.parse(txtEnd.getText().trim()); }
    public java.math.BigDecimal getInputPrice() { return new java.math.BigDecimal(txtPrice.getText().trim()); }
    
    //  LẤY TỪ KHÓA TÌM KIẾM 
    public String getSearchKeyword() { return txtSearch.getText().trim(); }

    public void addUpdateListener(ActionListener listener) { btnUpdate.addActionListener(listener); }
    public void addDeleteListener(ActionListener listener) { btnDelete.addActionListener(listener); }
    public void addRefreshListener(ActionListener listener) { btnRefresh.addActionListener(listener); }
    public void addSearchListener(ActionListener listener) { btnSearch.addActionListener(listener); } // API cho Controller
    
    public void showMessage(String msg, String title, int type) { JOptionPane.showMessageDialog(this, msg, title, type); }
}