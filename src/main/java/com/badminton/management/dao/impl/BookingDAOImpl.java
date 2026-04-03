package com.badminton.management.dao.impl;

import com.badminton.management.config.JpaUtil;
import com.badminton.management.dao.BookingDAO;
import com.badminton.management.model.dto.MonthlyRevenueDTO;
import com.badminton.management.model.entity.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.swing.text.html.parser.Entity;

import jakarta.persistence.EntityManager;

public class BookingDAOImpl implements BookingDAO {
    @Override
    public void save(Booking booking) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(booking);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Booking booking) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(booking);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Booking booking = em.find(Booking.class, id);
            if (booking != null) {
                em.remove(booking);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public Booking findById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Booking booking = em.find(Booking.class, id);
            return booking;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Booking> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Booking> list = em.createQuery("SELECT b FROM Booking b", Booking.class).getResultList();
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Booking> findByDate(LocalDate date) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT b FROM Booking b " +
                            "JOIN FETCH b.customer " +
                            "JOIN FETCH b.court " +
                            "WHERE b.bookingDate = :date " +
                            "ORDER BY b.startTime ASC ",
                    Booking.class)
                    .setParameter("date", date)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Booking> findByCustomer(int customerId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT b FROM Booking b " +
                    "JOIN FETCH b.court " + // Lấy luôn thông tin sân để hiện lên bảng
                    "WHERE b.customer.customerId = :cusId " +
                    "ORDER BY b.bookingDate DESC"; // Hiện đơn mới nhất lên đầu

            return em.createQuery(jpql, Booking.class)
                    .setParameter("cusId", customerId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Booking> findByCourt(int courtId, LocalDate date) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT b FROM Booking b " +
                    "JOIN FETCH b.customer " + // Để biết ai đang chiếm sân
                    "WHERE b.court.courtId = :courtId " +
                    "AND b.bookingDate = :date " +
                    "ORDER BY b.startTime ASC";

            return em.createQuery(jpql, Booking.class)
                    .setParameter("courtId", courtId)
                    .setParameter("date", date)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Booking> findOverlappingBookings(int courtId, LocalDate date, LocalTime start, LocalTime end) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT b FROM Booking b " +
                    "WHERE b.court.courtId = :courtId " +
                    "AND b.bookingDate = :date " +
                    "AND b.startTime < :newEnd " + // Giờ bắt đầu cũ < Giờ kết thúc mới
                    "AND b.endTime > :newStart"; // Giờ kết thúc cũ > Giờ bắt đầu mới

            return em.createQuery(jpql, Booking.class)
                    .setParameter("courtId", courtId)
                    .setParameter("date", date)
                    .setParameter("newStart", start)
                    .setParameter("newEnd", end)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<com.badminton.management.model.dto.BookingRevenueDTO> getRevenueReport() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Nhờ DB tính tổng tiền (SUM) và gom nhóm theo Ngày và Tên sân
            String jpql = "SELECT new com.badminton.management.model.dto.BookingRevenueDTO(" +
                    "b.court.courtName, b.bookingDate, SUM(b.totalPrice)) " +
                    "FROM Booking b " +
                    "GROUP BY b.court.courtName, b.bookingDate " +
                    "ORDER BY b.bookingDate DESC, b.court.courtName ASC";

            return em.createQuery(jpql, com.badminton.management.model.dto.BookingRevenueDTO.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<com.badminton.management.model.dto.MonthlyRevenueDTO> getMonthlyRevenueReport() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Dùng YEAR() và MONTH() của JPQL để gom nhóm
            String jpql = "SELECT new com.badminton.management.model.dto.MonthlyRevenueDTO(" +
                    "YEAR(b.bookingDate), MONTH(b.bookingDate), SUM(b.totalPrice)) " +
                    "FROM Booking b " +
                    "GROUP BY YEAR(b.bookingDate), MONTH(b.bookingDate) " +
                    "ORDER BY YEAR(b.bookingDate) DESC, MONTH(b.bookingDate) DESC";

            return em.createQuery(jpql, com.badminton.management.model.dto.MonthlyRevenueDTO.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public int getCountBookingToday() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            LocalDate today = LocalDate.now();
            long result = em.createQuery("SELECT COUNT(b.bookingId) " +
                    "FROM Booking b " +
                    "WHERE b.bookingDate = :Today", Long.class)
                    .setParameter("Today", today)
                    .getSingleResult();
            return (int) (result);
        } finally {
            em.close();
        }
    }

    @Override
    public com.badminton.management.model.dto.MonthlyRevenueDTO getCurrentMonthRevenueReport() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            LocalDate now = LocalDate.now();
            LocalDate firstDay = now.withDayOfMonth(1);
            LocalDate lastDay = now.withDayOfMonth(now.lengthOfMonth());

            String jpql = "SELECT new com.badminton.management.model.dto.MonthlyRevenueDTO(" +
                    ":year, :month, SUM(b.totalPrice)) " +
                    "FROM Booking b " +
                    "WHERE b.bookingDate BETWEEN :firstDay AND :lastDay";

            MonthlyRevenueDTO result = em.createQuery(jpql, MonthlyRevenueDTO.class)
                    .setParameter("year", now.getYear())
                    .setParameter("month", now.getMonthValue())
                    .setParameter("firstDay", firstDay)
                    .setParameter("lastDay", lastDay)
                    .getSingleResult();
            return result;
        } finally {
            em.close();
        }
    }
}
