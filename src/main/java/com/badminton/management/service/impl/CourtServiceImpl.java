package com.badminton.management.service.impl;

import com.badminton.management.dao.CourtDAO;
import com.badminton.management.dao.impl.CourtDAOImpl;
import com.badminton.management.model.entity.Court;
import com.badminton.management.model.enumtype.CourtStatus;
import com.badminton.management.service.CourtService;
import java.util.List;

public class CourtServiceImpl implements CourtService{
    private CourtDAO courtDAO = new CourtDAOImpl();
    @Override
    public List<Court> getAllCourts(){
        return courtDAO.findAll();
    }
    @Override
    public List<Court> getAvailableCourts() {
        return courtDAO.findByStatus(CourtStatus.TRONG);
    }
    @Override
    public void updateCourtStatus(int courtId, CourtStatus status){
        Court court = courtDAO.findById(courtId);
        if (court != null) {
            court.setStatus(status);
            courtDAO.update(court);
        }
    }
    @Override
    public String addCourt(Court court) {
        try {
            courtDAO.save(court);
            return "SUCCESS";
        } catch (Exception e) {
            return "Lỗi thêm sân: " + e.getMessage();
        }
    }

    @Override
    public String updateCourt(Court court) {
        try {
            courtDAO.update(court);
            return "SUCCESS";
        } catch (Exception e) {
            return "Lỗi cập nhật sân: " + e.getMessage();
        }
    }

    @Override
    public String deleteCourt(int courtId) {
        try {
            courtDAO.delete(courtId);
            return "SUCCESS";
        } catch (Exception e) {
            // Lỗi kinh điển khi vướng khóa ngoại (Sân đã có lịch đặt)
            return "Không thể xóa sân này vì đã có lịch sử đặt sân! \nGợi ý: Hãy chuyển trạng thái sân thành BẢO TRÌ thay vì xóa.";
        }
    }
    @Override
    public int getAvailableCourtNow(){
        return courtDAO.getRealTimeAvailableCount();
    }
}
