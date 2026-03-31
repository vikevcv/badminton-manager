package com.badminton.management;

import com.badminton.management.controller.AuthController;
import com.badminton.management.view.LoginFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            // 1. Khởi tạo Giao diện Đăng nhập
            LoginFrame loginView = new LoginFrame();
            
            // 2. Khởi tạo Controller và giao View cho nó quản lý
            new AuthController(loginView); 
            
            // 3. Hiển thị View lên màn hình
            loginView.setVisible(true);
        });
    }
}