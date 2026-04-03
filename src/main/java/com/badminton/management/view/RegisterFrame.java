package com.badminton.management.view;

import com.badminton.management.model.enumtype.CustomerType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtFullName;
    private JTextField txtPhone;
    private JComboBox<CustomerType> cbType;
    
    private JButton btnSubmit;
    private JButton btnCancel;

    //Thiết lập Layout (BorderLayout) và Form nhập liệu (GridLayout 5 hàng 2 cột).
    public RegisterFrame() {
        setTitle("Đăng ký tài khoản mới");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng form này, không tắt cả app
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Các ô nhập liệu
        formPanel.add(new JLabel("Họ và Tên:"));
        txtFullName = new JTextField();
        formPanel.add(txtFullName);

        formPanel.add(new JLabel("Số điện thoại:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);

        formPanel.add(new JLabel("Loại khách hàng:"));
        cbType = new JComboBox<>(CustomerType.values()); // Lấy tự động từ Enum 
        formPanel.add(cbType);

        formPanel.add(new JLabel("Tên đăng nhập:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Mật khẩu:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        // Nút bấm
        btnSubmit = new JButton("Hoàn tất Đăng ký");
        btnCancel = new JButton("Trở về");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSubmit);
        buttonPanel.add(btnCancel);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // API CHO CONTROLLER 
    public String getUsername() { return txtUsername.getText(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }
    public String getFullName() { return txtFullName.getText(); }
    public String getPhone() { return txtPhone.getText(); }
    public CustomerType getCustomerType() { return (CustomerType) cbType.getSelectedItem(); }

    public void addSubmitListener(ActionListener listener) { btnSubmit.addActionListener(listener); }
    public void addCancelListener(ActionListener listener) { btnCancel.addActionListener(listener); }

    public void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }
}