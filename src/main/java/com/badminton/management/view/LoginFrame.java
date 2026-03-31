package com.badminton.management.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;

    public LoginFrame() {//Cấu hình tiêu đề, kích thước và đặt form ở giữa màn hình.
        setTitle("Đăng nhập - Quản lý Sân Cầu Lông");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Đóng cửa sổ này là tắt toàn bộ chương trình (vì đây là màn hình chính lúc đầu).
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel chứa Form nhập liệu
        //Sắp xếp nhãn và ô nhập liệu theo dạng lưới (3 hàng, 2 cột) cho cân đối.
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Tên đăng nhập:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Mật khẩu:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        // Nút bấm
        btnLogin = new JButton("Đăng nhập");
        btnRegister = new JButton("Đăng ký khách mới");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Lấy dữ liệu người dùng nhập
    public String getUsername() {
        return txtUsername.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    // Mở cổng gắn sự kiện cho nút bấm
    public void addLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);
    }

    public void addRegisterListener(ActionListener listener) {
        btnRegister.addActionListener(listener);
    }

    // Hiển thị thông báo (Lỗi / Thành công)
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}