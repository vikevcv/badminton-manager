package com.badminton.management.view;

import com.badminton.management.controller.BookingController;
import com.badminton.management.controller.HistoryController;
import com.badminton.management.dao.CustomerDAO;
import com.badminton.management.dao.impl.CustomerDAOImpl;
import com.badminton.management.model.entity.Account;
import com.badminton.management.model.entity.Customer;
import com.badminton.management.model.enumtype.Role;
import com.badminton.management.controller.AuthController;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private Account loggedInAccount;
    private JPanel contentPanel; 
    private CardLayout cardLayout;
    
    // Khai báo HistoryController ra ngoài để Menu có thể gọi được
    private HistoryController historyController; 

    public MainFrame(Account account) {
        this.loggedInAccount = account;
        
        setTitle("Hệ thống quản lý - Xin chào " + account.getUsername());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        //Khởi tạo View + Controller, sau đó dán nhãn (Tag) để nạp vào CardLayout.
        setupContentPanels(); 
        
        JPanel sidebar = createSidebar();

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void setupContentPanels() {
        BookingPanel bookingPanel = new BookingPanel();
        
        int customerId = -1; 
        CustomerDAO customerDAO = new CustomerDAOImpl();
        
        if (loggedInAccount != null) {
            Customer customer = customerDAO.findByAccountId(loggedInAccount.getAccountId());
            if (customer != null) customerId = customer.getCustomerId();
        }

        if (customerId == -1) {
            List<Customer> allCus = customerDAO.findAll();
            if (!allCus.isEmpty()) {
                customerId = allCus.get(0).getCustomerId(); // Lấy ông khách đầu tiên làm khách mặc định
            } else {
                customerId = 1; // Hết cách, gán đại bằng 1 
            }
        }
        
        new BookingController(bookingPanel, customerId, loggedInAccount.getRole());
        contentPanel.add(bookingPanel, "BOOKING_VIEW");
        
        HistoryPanel historyPanel = new HistoryPanel();
        historyController = new HistoryController(historyPanel, customerId); // Gán vào biến toàn cục
        contentPanel.add(historyPanel, "HISTORY_VIEW");

        // Chỉ nạp màn hình quản lý nếu là Admin
        if (loggedInAccount.getRole() == Role.ADMIN) {
            CourtPanel courtPanel = new CourtPanel();
            new com.badminton.management.controller.CourtController(courtPanel);
            contentPanel.add(courtPanel, "COURT_MGR"); 

            CustomerPanel customerPanel = new CustomerPanel();
            new com.badminton.management.controller.CustomerController(customerPanel);
            contentPanel.add(customerPanel, "CUSTOMER_MGR");
            
            BookingManagerPanel bookingMgrPanel = new BookingManagerPanel();
            new com.badminton.management.controller.BookingManagerController(bookingMgrPanel);
            contentPanel.add(bookingMgrPanel, "BOOKING_MGR");

            ReportPanel reportPanel = new ReportPanel();
            new com.badminton.management.controller.ReportController(reportPanel);
            contentPanel.add(reportPanel, "REPORT_VIEW");
        }

        cardLayout.show(contentPanel, "BOOKING_VIEW");
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBackground(Color.DARK_GRAY);
        
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 2, 10, 2));

        sidebar.add(createMenuButton("Đặt Sân Ngay", "BOOKING_VIEW"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10))); 
        
        //Hiển thị nút "Lịch Sử" riêng cho Khách 
        if (loggedInAccount.getRole() == Role.MEMBER) {
            JButton btnHistory = createMenuButton("Lịch Sử Của Tôi", "HISTORY_VIEW");
            btnHistory.addActionListener(e -> {
                if (historyController != null) {
                    historyController.loadHistory(); 
                }
            });
            sidebar.add(btnHistory);
        }

        if (loggedInAccount.getRole() == Role.ADMIN) {
            sidebar.add(Box.createRigidArea(new Dimension(0, 20))); 
            JLabel lblAdmin = new JLabel("ADMIN MENU");
            lblAdmin.setForeground(Color.YELLOW);
            lblAdmin.setAlignmentX(Component.CENTER_ALIGNMENT); // Ép tiêu đề này nằm giữa
            sidebar.add(lblAdmin);
            
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(createMenuButton("Quản lý Sân", "COURT_MGR"));
            
            sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
            sidebar.add(createMenuButton("Quản lý Khách", "CUSTOMER_MGR"));
            
            sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
            // Đã rút gọn chữ để nhìn cân đối hơn
            sidebar.add(createMenuButton("Quản lý Booking", "BOOKING_MGR")); 
            
            sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
            sidebar.add(createMenuButton("Báo Cáo Doanh Thu", "REPORT_VIEW"));
        }

        sidebar.add(Box.createVerticalGlue()); 

        // KHU VỰC HIỂN THỊ THÔNG TIN VÀ ĐĂNG XUẤT 
        String fullName = "Quản trị viên";
        String userType = "Admin hệ thống";

        if (loggedInAccount.getRole() == Role.MEMBER) {
            CustomerDAO customerDAO = new CustomerDAOImpl();
            Customer customer = customerDAO.findByAccountId(loggedInAccount.getAccountId());
            if (customer != null) {
                fullName = customer.getCustomerName();
                userType = customer.getType().name().equals("SINHVIEN") ? "Sinh viên" : "Người lớn";
            }
        }

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(Color.DARK_GRAY);
        
        JLabel lblName = new JLabel(fullName);
        lblName.setForeground(Color.WHITE); 
        lblName.setFont(new Font("Arial", Font.BOLD, 15));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT); 

        JLabel lblType = new JLabel("Loại thẻ: " + userType);
        lblType.setForeground(new Color(180, 180, 180)); 
        lblType.setFont(new Font("Arial", Font.ITALIC, 12));
        lblType.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnLogout = new JButton("Đăng xuất");//
        btnLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 
        btnLogout.setBackground(new Color(220, 53, 69)); 
        btnLogout.setForeground(Color.WHITE); 
        btnLogout.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogout.setFocusPainted(false); 
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnLogout.addActionListener(e -> {
            this.dispose(); //Giải phóng (dispose) Frame hiện tại và quay về màn hình đăng nhập (khởi tạo lại AuthController).
            LoginFrame loginFrame = new LoginFrame();
            new AuthController(loginFrame);
            loginFrame.setVisible(true); 
        });

        bottomPanel.add(lblName);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        bottomPanel.add(lblType);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        bottomPanel.add(btnLogout);

        sidebar.add(bottomPanel);

        return sidebar;
    }

    // Hàm tiện ích để tạo nút menu đổi màn hình
    private JButton createMenuButton(String text, String cardName) {
        JButton btn = new JButton(text);
        
        // Ép nút kéo giãn ngang toàn bộ 100%
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 
        
        // Căn nút nằm ở chính giữa
        btn.setAlignmentX(Component.CENTER_ALIGNMENT); 
        
        // MẸO: Ép lề (margin) bên trong nút nhỏ lại để nhường diện tích hiển thị chữ
        btn.setMargin(new Insets(2, 0, 2, 0)); 
        
        btn.setFocusPainted(false); // Tắt viền nét đứt khi bấm
        
        btn.addActionListener(e -> cardLayout.show(contentPanel, cardName)); 
        return btn;
    }
}