package com.badminton.management.view;

import com.badminton.management.model.entity.Customer;
import com.badminton.management.model.enumtype.CustomerType;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class CustomerPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtName, txtPhone;
    private JComboBox<CustomerType> cbType;
    private JButton btnAddNew, btnSave, btnUpdate, btnDelete;
    private JButton btnRefresh;

    // Layout 4 vùng: North (Tiêu đề), West (Form nhập), Center (Bảng dữ liệu), South (Các nút chức năng).
    public CustomerPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(lblTitle, BorderLayout.WEST);

        btnRefresh = new JButton("Làm Mới Danh Sách");
        topPanel.add(btnRefresh, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);



        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 20));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Khách Hàng"));
        formPanel.setPreferredSize(new Dimension(300, 0));

        formPanel.add(new JLabel("Mã Khách:"));
        txtId = new JTextField(); txtId.setEditable(false);// Read-only ID: Khóa ô Mã Khách vì ID là khóa chính tự tăng (Auto Increment), người dùng không được sửa.
        formPanel.add(txtId);

        formPanel.add(new JLabel("Tên Khách:"));
        txtName = new JTextField(); formPanel.add(txtName);

        formPanel.add(new JLabel("Số Điện Thoại:"));
        txtPhone = new JTextField(); formPanel.add(txtPhone);

        formPanel.add(new JLabel("Loại Khách:"));
        cbType = new JComboBox<>(CustomerType.values());
        formPanel.add(cbType);

        add(formPanel, BorderLayout.WEST);

        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] cols = {"ID", "Tên Khách Hàng", "SĐT", "Loại", "Tài Khoản (Nếu có)"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnAddNew = new JButton("Thêm Khách Mới (Xóa Form)");
        btnSave = new JButton("Lưu Khách Hàng");
        btnUpdate = new JButton("Cập Nhật Thông Tin");
        btnDelete = new JButton("Xóa Khách");

        btnPanel.add(btnAddNew); btnPanel.add(btnSave); btnPanel.add(btnUpdate); btnPanel.add(btnDelete);
        add(btnPanel, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtId.setText(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                txtPhone.setText(tableModel.getValueAt(row, 2).toString());
                cbType.setSelectedItem(CustomerType.valueOf(tableModel.getValueAt(row, 3).toString()));
            }
        });

        btnAddNew.addActionListener(e -> clearForm());
    }

    // Data Refresh: Xóa bảng và nạp lại danh sách Khách hàng mới nhất từ Controller (Bao gồm kiểm tra xem khách có tài khoản hay là vãng lai).
    public void updateTable(List<Customer> customers) {
        tableModel.setRowCount(0);
        for (Customer c : customers) {
            String accInfo = (c.getAccountId() != null) ? c.getAccountId().getUsername() : "Khách vãng lai";
            tableModel.addRow(new Object[]{ c.getCustomerId(), c.getCustomerName(), c.getPhone(), c.getType(), accInfo });
        }
    }

    //// UI Reset: Làm sạch các ô nhập liệu về trạng thái trống để chuẩn bị thêm khách mới.
    public void clearForm() {
        txtId.setText(""); txtName.setText(""); txtPhone.setText("");
        cbType.setSelectedIndex(0); table.clearSelection();
    }

    // Object Packaging: Gom toàn bộ thông tin từ các ô nhập liệu vào một đối tượng Entity (Customer) để gửi đi lưu/cập nhật.
    public Customer getFormInput() {
        Customer c = new Customer();
        if (!txtId.getText().isEmpty()) c.setCustomerId(Integer.parseInt(txtId.getText()));
        c.setCustomerName(txtName.getText().trim());
        c.setPhone(txtPhone.getText().trim());
        c.setType((CustomerType) cbType.getSelectedItem());
        return c;
    }

    public int getSelectedId() { return txtId.getText().isEmpty() ? -1 : Integer.parseInt(txtId.getText()); }
    
    public void addSaveListener(ActionListener listener) { btnSave.addActionListener(listener); }
    public void addUpdateListener(ActionListener listener) { btnUpdate.addActionListener(listener); }
    public void addDeleteListener(ActionListener listener) { btnDelete.addActionListener(listener); }
    public void showMessage(String msg, String title, int type) { JOptionPane.showMessageDialog(this, msg, title, type); }
    public void addRefreshListener(ActionListener listener) { btnRefresh.addActionListener(listener); }
}