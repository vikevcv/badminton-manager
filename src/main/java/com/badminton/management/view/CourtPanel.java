package com.badminton.management.view;

import com.badminton.management.model.entity.Court;
import com.badminton.management.model.enumtype.CourtStatus;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class CourtPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtCourtId, txtCourtName, txtPrice;
    private JComboBox<CourtStatus> cbStatus;
    
    // Đã khai báo nút Làm mới ở đây
    private JButton btnAddNew, btnSave, btnUpdate, btnDelete, btnRefresh;

    // UI Structure: Sử dụng BorderLayout để chia vùng; Tây (West) chứa Form nhập, Giữa (Center) chứa Bảng hiển thị.
    public CourtPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // 1. PHẦN TOP: TIÊU ĐỀ VÀ NÚT LÀM MỚI
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("QUẢN LÝ DANH SÁCH SÂN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(lblTitle, BorderLayout.WEST);

        btnRefresh = new JButton("Làm Mới Danh Sách");
        topPanel.add(btnRefresh, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH); // Gắn lên đỉnh

        // 2. PHẦN WEST: FORM NHẬP LIỆU
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 20));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Sân"));
        formPanel.setPreferredSize(new Dimension(300, 0));

        formPanel.add(new JLabel("Mã Sân (Tự tạo):"));
        txtCourtId = new JTextField();
        // Data Integrity: Khóa ô ID để tránh người dùng sửa nhầm Khóa chính (Primary Key) của sân.
        txtCourtId.setEditable(false); 
        formPanel.add(txtCourtId);

        formPanel.add(new JLabel("Tên Sân:"));
        txtCourtName = new JTextField();
        formPanel.add(txtCourtName);

        formPanel.add(new JLabel("Giá / Giờ (VNĐ):"));
        txtPrice = new JTextField();
        formPanel.add(txtPrice);

        formPanel.add(new JLabel("Trạng Thái:"));
        cbStatus = new JComboBox<>(CourtStatus.values());
        formPanel.add(cbStatus);
        add(formPanel, BorderLayout.WEST);

        // 3. PHẦN CENTER: BẢNG DỮ LIỆU
        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] cols = {"ID", "Tên Sân", "Giá/Giờ", "Trạng Thái"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // 4. PHẦN SOUTH: CÁC NÚT CHỨC NĂNG
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnAddNew = new JButton("Thêm Sân (Xóa Trống Form)");
        btnSave = new JButton("Lưu Sân Mới");
        btnUpdate = new JButton("Cập Nhật Sân Đang Chọn");
        btnDelete = new JButton("Xóa Sân");

        btnPanel.add(btnAddNew);
        btnPanel.add(btnSave);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        add(btnPanel, BorderLayout.SOUTH);

        //  CÁC SỰ KIỆN NỘI BỘ VIEW 
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtCourtId.setText(tableModel.getValueAt(row, 0).toString());
                txtCourtName.setText(tableModel.getValueAt(row, 1).toString());
                txtPrice.setText(tableModel.getValueAt(row, 2).toString());
                cbStatus.setSelectedItem(CourtStatus.valueOf(tableModel.getValueAt(row, 3).toString()));
            }
        });

        btnAddNew.addActionListener(e -> clearForm());
    }

    // Table Refresh: Xóa sạch dòng cũ và dùng vòng lặp nạp danh sách Sân mới nhất từ Database lên giao diện.
    public void updateTable(List<Court> courts) {
        tableModel.setRowCount(0);
        for (Court c : courts) {
            tableModel.addRow(new Object[]{ c.getCourtId(), c.getCourtName(), c.getPricePerHour(), c.getStatus() });
        }
    }

    // Form Reset: Đưa các ô nhập liệu về trạng thái mặc định (trống) để chuẩn bị thêm sân mới.
    public void clearForm() {
        txtCourtId.setText("");
        txtCourtName.setText("");
        txtPrice.setText("");
        cbStatus.setSelectedIndex(0);
        table.clearSelection();
    }

    // Data Packaging: Thu thập dữ liệu từ giao diện, ép kiểu (String sang BigDecimal/Int) và đóng gói vào đối tượng Court.
    public Court getFormInput() throws NumberFormatException {
        Court c = new Court();
        if (!txtCourtId.getText().isEmpty()) {
            c.setCourtId(Integer.parseInt(txtCourtId.getText()));
        }
        c.setCourtName(txtCourtName.getText().trim());
        c.setPricePerHour(new java.math.BigDecimal(txtPrice.getText().trim()));
        c.setStatus((CourtStatus) cbStatus.getSelectedItem());
        return c;
    }

    public int getSelectedCourtId() {
        if (!txtCourtId.getText().isEmpty()) return Integer.parseInt(txtCourtId.getText());
        return -1;
    }

    // Mở cổng API cho Controller (Đã có sẵn nút Refresh)
    public void addSaveListener(ActionListener listener) { btnSave.addActionListener(listener); }
    public void addUpdateListener(ActionListener listener) { btnUpdate.addActionListener(listener); }
    public void addDeleteListener(ActionListener listener) { btnDelete.addActionListener(listener); }
    public void addRefreshListener(ActionListener listener) { btnRefresh.addActionListener(listener); }
    
    public void showMessage(String msg, String title, int type) { JOptionPane.showMessageDialog(this, msg, title, type); }
}