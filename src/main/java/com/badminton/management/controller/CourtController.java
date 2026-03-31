package com.badminton.management.controller;

import com.badminton.management.model.entity.Court;
import com.badminton.management.service.CourtService;
import com.badminton.management.service.impl.CourtServiceImpl;
import com.badminton.management.view.CourtPanel;

import javax.swing.*;
import java.util.List;

public class CourtController {
    private CourtPanel view;
    private CourtService courtService;

    public CourtController(CourtPanel view) {
        this.view = view;
        this.courtService = new CourtServiceImpl();

        this.view.addSaveListener(e -> handleAddCourt());
        this.view.addUpdateListener(e -> handleUpdateCourt());
        this.view.addDeleteListener(e -> handleDeleteCourt());
        this.view.addRefreshListener(e -> loadData()); // Thêm dòng này
        loadData(); // Tải bảng sân khi vừa bật lên
    }

    private void loadData() {
        List<Court> courts = courtService.getAllCourts();
        view.updateTable(courts);
    }

    private void handleAddCourt() {
        try {
            Court court = view.getFormInput();
            if (court.getCourtName().isEmpty()) {
                view.showMessage("Tên sân không được để trống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String result = courtService.addCourt(court);
            if (result.equals("SUCCESS")) {
                view.showMessage("Thêm sân thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
                view.clearForm();
                loadData();
            } else {
                view.showMessage(result, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            view.showMessage("Giá tiền phải là một số hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateCourt() {
        try {
            Court court = view.getFormInput();
            if (court.getCourtId() == 0) {
                view.showMessage("Vui lòng chọn một sân từ bảng để cập nhật!", "Chú ý", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String result = courtService.updateCourt(court);
            if (result.equals("SUCCESS")) {
                view.showMessage("Cập nhật thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
                view.clearForm();
                loadData();
            } else {
                view.showMessage(result, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            view.showMessage("Giá tiền phải là một số hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteCourt() {
        int courtId = view.getSelectedCourtId();
        if (courtId == -1) {
            view.showMessage("Vui lòng chọn một sân từ bảng để xóa!", "Chú ý", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa sân này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String result = courtService.deleteCourt(courtId);
            if (result.equals("SUCCESS")) {
                view.showMessage("Xóa sân thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
                view.clearForm();
                loadData();
            } else {
                view.showMessage(result, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}