package com.badminton.management.controller;

import javax.swing.JOptionPane;

import com.badminton.management.model.entity.Account;
import com.badminton.management.model.entity.Customer;
import com.badminton.management.service.AuthService;
import com.badminton.management.service.CustomerService;
import com.badminton.management.service.impl.AuthServiceImpl;
import com.badminton.management.service.impl.CustomerServiceImpl;
import com.badminton.management.view.LoginFrame;
import com.badminton.management.view.MainFrame;
import com.badminton.management.view.RegisterFrame;
import com.badminton.management.model.enumtype.CustomerType;

public class AuthController {
    private LoginFrame loginView;
    private AuthService authService;
    private CustomerService customerService;

    public AuthController(LoginFrame view) {
        this.loginView = view;
        this.authService = new AuthServiceImpl(); // Khởi tạo Service kết nối Database
        this.customerService = new CustomerServiceImpl();

        // Gắn sự kiện click chuột từ View vào hàm xử lý của Controller
        this.loginView.addLoginListener(e -> handleLogin());

        // Gắn sự kiện cho nút Đăng ký
        this.loginView.addRegisterListener(e -> handleRegister());
    }

    // Xử lý logic Đăng nhập thực tế
    private void handleLogin() {
        // lấy tài khoản mật khẩu từ Field
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        // 1. Kiểm tra rỗng và ngoại lệ
        if (username.isEmpty() || password.isEmpty()) {
            loginView.showMessage("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!");
            return;
        }
        if (username.contains(" ")) {
            loginView.showMessage("Tên đăng nhập không được có khoảng trống!");
            return;
        }
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            loginView.showMessage("Tên đăng nhập không được chứa khoảng trắng hoặc ký tự đặc biệt!");
            return;
        }

        // 2. Gọi Service kiểm tra Database
        Account loggedInAccount = authService.login(username, password);

        if (loggedInAccount != null) {

            Customer customer = null;
            if (loggedInAccount.getRole() != null) {
                customer = customerService.getCustomerWithAccountID(loggedInAccount.getAccountId());
            }

            // Đăng nhập thành công -> Mở MainFrame truyền Account vào
            MainFrame mainFrame = new MainFrame(loggedInAccount, customer);
            // nạp controller mainFrame
            new MainController(mainFrame);
            // hiện mainframe
            mainFrame.setVisible(true);

            // Đóng cửa sổ Login
            loginView.dispose();
        } else {
            // Thất bại -> Báo lỗi cho View hiện lên
            loginView.showMessage("Sai tên đăng nhập hoặc mật khẩu!");
        }
    }

    // Hàm chuyển hướng sang màn hình Đăng ký
    private void handleRegister() {
        // 1. ẨN màn hình Login đi
        loginView.setVisible(false);

        // 2. Khởi tạo View Đăng ký
        RegisterFrame registerView = new RegisterFrame();

        registerView.addCancelListener(e -> {
            registerView.dispose(); // Đóng form đăng ký
            loginView.setVisible(true); // HIỆN LẠI form đăng nhập
        });

        registerView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                loginView.setVisible(true); // HIỆN LẠI form đăng nhập
            }
        });

        registerView.addSubmitListener(e -> {
            String username = registerView.getUsername();
            String password = registerView.getPassword();
            String fullName = registerView.getFullName();
            String phone = registerView.getPhone();
            CustomerType type = registerView.getCustomerType();

            //Kiểm tra rỗng và ngoại lệ
            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || phone.isEmpty()) {
                registerView.showMessage("Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (username.contains(" ")) {
                registerView.showMessage("Tên đăng nhập không được có khoảng trống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!username.matches("^[a-zA-Z0-9]+$")) {
                registerView.showMessage("Tên đăng nhập không được chứa khoảng trắng hoặc ký tự đặc biệt!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!phone.matches("^[0-9]+$")){
                registerView.showMessage("Số điện thoại phải là số!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // thự hiện đăng ký
            String result = authService.register(username, password, fullName, phone, type);

            if (result.equals("SUCCESS")) {
                registerView.showMessage("Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.", "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                registerView.dispose(); // Đóng form đăng ký
                loginView.setVisible(true); // HIỆN LẠI form đăng nhập

            } else {
                registerView.showMessage(result, "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Hiển thị form đăng ký
        registerView.setVisible(true);
    }
}