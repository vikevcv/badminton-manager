package com.badminton.management.view;

import com.badminton.management.model.entity.Booking;
import com.badminton.management.model.entity.Court;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingPanel extends JPanel {
    private JTable timelineTable;
    private DefaultTableModel tableModel;
    private List<String> timeSlots;
    private Map<Integer, Integer> rowToCourtIdMap;
    private JSpinner dateSpinner;
    
    private JButton btnLoad;
    private JButton btnBook;

    // State Constants: Các trạng thái của ô lưới, dùng để điều khiển màu sắc và chặn thao tác click.
    private final String AVAILABLE = "TRỐNG";
    private final String BOOKED = "ĐÃ ĐẶT";
    private final String SELECTING = "ĐANG CHỌN";
    private final String MAINTENANCE = "BẢO TRÌ";
    private final String PASSED = "ĐÃ QUA";

    public BookingPanel() {
        rowToCourtIdMap = new HashMap<>();
        setLayout(new BorderLayout()); //5 vùng đông tây nam bắc, trung tâm
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); //canh lề trên dưới trái phải cách 10

        //tạo topPanel có layout từ trái sang phải
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // thêm label cho layout toppanle
        topPanel.add(new JLabel("Chọn ngày xem lịch: "));


        dateSpinner = new JSpinner(new SpinnerDateModel());
        // định dạng lại spinner ngày, tháng, năm
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        // thêm spinner vào toppanle
        topPanel.add(dateSpinner);
        
        btnLoad = new JButton("Làm Mới"); 
        //thêm btn Làm mới vào toppanel
        topPanel.add(btnLoad);
        //thêm toppanel lên hướng NORTH của BookingPanel
        add(topPanel, BorderLayout.NORTH);

        setupTimelineTable();
        timelineTable.setShowGrid(true);
        timelineTable.setGridColor(new Color(200, 200, 200)); // màu xám nhẹ
        timelineTable.setIntercellSpacing(new Dimension(1, 1));
        
        JScrollPane scrollPane = new JScrollPane(timelineTable); // làm thanh cuộn cho timeline
        timelineTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // không cho cột bị ép nhỏ lại
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        legendPanel.add(createLegend(Color.WHITE, "Trống"));
        legendPanel.add(createLegend(new Color(255, 102, 102), "Đã đặt"));
        legendPanel.add(createLegend(new Color(102, 255, 102), "Đang chọn"));
        legendPanel.add(createLegend(new Color(220, 220, 220), "Đã qua"));
        legendPanel.add(createLegend(Color.DARK_GRAY, "Bảo trì"));
        bottomPanel.add(legendPanel, BorderLayout.WEST);

        btnBook = new JButton("Xác nhận Đặt Sân"); 
        btnBook.setFont(new Font("Arial", Font.BOLD, 14));
        btnBook.setBackground(new Color(0, 153, 255));
        btnBook.setForeground(Color.WHITE);
        bottomPanel.add(btnBook, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Timeline Generation: Tự động chia cột theo bước nhảy 30 phút (6:00 -> 22:00) bằng vòng lặp while.
    private void setupTimelineTable() {
        timeSlots = new ArrayList<>();
        timeSlots.add("Tên Sân"); 
        LocalTime time = LocalTime.of(6, 0);
        LocalTime endTime = LocalTime.of(22, 0);
        while (time.isBefore(endTime)) {
            LocalTime nextTime = time.plusMinutes(30);
            timeSlots.add(time.toString() + " - " + nextTime.toString());
            time = nextTime;
        }

        tableModel = new DefaultTableModel(timeSlots.toArray(), 0) {// tạo bản có tên cột là timeSlot.toArray(), 0 hàng mặc định
            @Override
            public boolean isCellEditable(int row, int column) { return false; }// không cho sửa các ô
        };
        timelineTable = new JTable(tableModel);
        timelineTable.setRowHeight(40);
        timelineTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        
        for (int i = 1; i < timelineTable.getColumnCount(); i++) {
            timelineTable.getColumnModel().getColumn(i).setPreferredWidth(95); 
        }

        // Cell Coloring Logic: "Bộ não" đồ họa - Tự động tô màu (Đỏ, Xanh, Xám) và đổi chữ hiển thị dựa trên giá trị của ô.
        timelineTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 0) {
                    c.setBackground(Color.LIGHT_GRAY);
                    setText(value != null ? value.toString() : "");
                    return c;
                }
                
                if (BOOKED.equals(value)) {
                    c.setBackground(new Color(255, 102, 102));
                    setText("Đã đặt");
                } else if (SELECTING.equals(value)) {
                    c.setBackground(new Color(102, 255, 102));
                    setText("Chọn");
                } else if (MAINTENANCE.equals(value)) {
                    c.setBackground(Color.DARK_GRAY);
                    c.setForeground(Color.WHITE); 
                    setText("Bảo trì");
                } else if (PASSED.equals(value)) {
                    c.setBackground(new Color(220, 220, 220)); // Xám nhạt
                    setText(""); // Để trống cho đỡ rối mắt
                } else {
                    c.setBackground(Color.WHITE);
                    setText("");
                }
                
                if (!MAINTENANCE.equals(value)) c.setForeground(Color.BLACK); 
                return c;
            }
        });

        timelineTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = timelineTable.rowAtPoint(e.getPoint());
                int col = timelineTable.columnAtPoint(e.getPoint());
                if (col == 0) return;
                
                String currentValue = (String) tableModel.getValueAt(row, col);
                
                // CHẶN CLICK VÀO CÁC Ô ĐÃ QUA 
                if (BOOKED.equals(currentValue) || MAINTENANCE.equals(currentValue) || PASSED.equals(currentValue)) {
                    return; 
                }
                
                if (AVAILABLE.equals(currentValue)) {
                    tableModel.setValueAt(SELECTING, row, col);
                } else if (SELECTING.equals(currentValue)) {
                    tableModel.setValueAt(AVAILABLE, row, col);
                }
            }
        });
        timelineTable.setCellSelectionEnabled(true); //cho phép chọn ô riêng lẻ
    }

    private JPanel createLegend(Color color, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel colorBox = new JLabel();
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(20, 20));
        colorBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        p.add(colorBox);
        p.add(new JLabel(text));
        return p;
    }

    public void addLoadListener(ActionListener listener) { btnLoad.addActionListener(listener); }
    public void addBookListener(ActionListener listener) { btnBook.addActionListener(listener); }
    public void addLoadSpinner(ChangeListener listener) { dateSpinner.addChangeListener(listener);}
    
    public LocalDate getSelectedDate() {
        Date selectedDate = (Date) dateSpinner.getValue();
        return selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    // Time-Sync Logic: Nạp dữ liệu sân, kiểm tra lịch bảo trì và đặc biệt là khóa các ô thời gian đã qua so với giờ hiện tại.
    public void updateSchedule(List<Court> courts, List<Booking> bookings) {
        tableModel.setRowCount(0);
        rowToCourtIdMap.clear();

        if (courts == null || courts.isEmpty()) return;

        // LẤY THỜI GIAN THỰC TẾ HIỆN TẠI ĐỂ SO SÁNH 
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        LocalDate selectedDate = getSelectedDate();

        for (int i = 0; i < courts.size(); i++) {
            Court court = courts.get(i);
            Object[] rowData = new Object[timeSlots.size()];
            rowData[0] = court.getCourtName();
            boolean isMaintenance = court.getStatus() != null && court.getStatus().name().equals("BAO_TRI");
            
            for (int j = 1; j < rowData.length; j++) {
                if (isMaintenance) {
                    rowData[j] = MAINTENANCE;
                } else {
                    // Tính toán giờ của cột hiện tại (Ví dụ: Cột 1 là 6h00)
                    int hour = 6 + (j - 1) / 2;
                    int minute = ((j - 1) % 2) * 30;
                    LocalTime slotStartTime = LocalTime.of(hour, minute);

                    // KHÓA THỜI GIAN QUÁ KHỨ
                    if (selectedDate.isBefore(currentDate) || 
                       (selectedDate.isEqual(currentDate) && slotStartTime.isBefore(currentTime))) {
                        rowData[j] = PASSED;
                    } else {
                        rowData[j] = AVAILABLE;
                    }
                }
            }
            tableModel.addRow(rowData);
            rowToCourtIdMap.put(i, court.getCourtId()); 
        }

        // Đổ dữ liệu các ô đã có người đặt lên đè lại
        if (bookings != null) {
            for (Booking b : bookings) {
                int rowIndex = -1;
                for (Map.Entry<Integer, Integer> entry : rowToCourtIdMap.entrySet()) {
                    if (entry.getValue() == b.getCourt().getCourtId()) {
                        rowIndex = entry.getKey();
                        break;
                    }
                }
                if (rowIndex != -1) {
                    int startCol = (b.getStartTime().getHour() - 6) * 2 + (b.getStartTime().getMinute() / 30) + 1;
                    int endCol = (b.getEndTime().getHour() - 6) * 2 + (b.getEndTime().getMinute() / 30) + 1;
                    for (int c = startCol; c < endCol; c++) {
                        // Đơn đặt sân trong quá khứ vẫn sẽ hiện màu đỏ để quản lý dễ xem
                        if (c < tableModel.getColumnCount()) tableModel.setValueAt(BOOKED, rowIndex, c);
                    }
                }
            }
        }
    }

    // Data Conversion: Thuật toán quét các ô màu xanh (SELECTING) để gộp lại thành các đối tượng Booking gửi về Controller lưu xuống DB.
    public List<Booking> extractSelectedBookings() {
        List<Booking> pendingBookings = new ArrayList<>();
        LocalDate date = getSelectedDate();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int courtId = rowToCourtIdMap.get(i);
            int startSlotCol = -1;
            int endSlotCol = -1;

            for (int j = 1; j <= tableModel.getColumnCount(); j++) {
                boolean isSelecting = (j < tableModel.getColumnCount()) && SELECTING.equals(tableModel.getValueAt(i, j));
                if (isSelecting) {
                    if (startSlotCol == -1) startSlotCol = j; 
                    endSlotCol = j; 
                } else {
                    if (startSlotCol != -1) {
                        LocalTime startTime = LocalTime.of(6 + (startSlotCol - 1) / 2, ((startSlotCol - 1) % 2) * 30);
                        LocalTime endTime = LocalTime.of(6 + (endSlotCol) / 2, ((endSlotCol) % 2) * 30);

                        Booking b = new Booking();
                        Court c = new Court(); 
                        c.setCourtId(courtId);
                        b.setCourt(c);
                        b.setBookingDate(date);
                        b.setStartTime(startTime);
                        b.setEndTime(endTime);
                        pendingBookings.add(b);

                        startSlotCol = -1; 
                    }
                }
            }
        }
        return pendingBookings;
    }
}