package com.badminton.management.service;

import java.util.List;
import com.badminton.management.model.entity.Court;
import com.badminton.management.model.enumtype.CourtStatus;

public interface CourtService {
    List<Court> getAllCourts();
    List<Court> getAvailableCourts();
    void updateCourtStatus(int courtId, CourtStatus status);

    String addCourt(Court court);
    String updateCourt(Court court);
    String deleteCourt(int courtId);
    int getAvailableCourtNow();
}