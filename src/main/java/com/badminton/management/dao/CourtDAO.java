package com.badminton.management.dao;

import com.badminton.management.model.entity.Court;
import com.badminton.management.model.enumtype.CourtStatus;

import java.util.List;
public interface CourtDAO {
    void save(Court court);
    void update(Court court);
    void delete(int id);
    Court findById(int id);
    List<Court> findAll();
    List<Court> findByStatus(CourtStatus status);
    int getRealTimeAvailableCount();
}
