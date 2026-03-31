package com.badminton.management.dao.impl;

import com.badminton.management.config.JpaUtil;
import com.badminton.management.dao.CourtDAO;
import com.badminton.management.model.entity.Court;
import com.badminton.management.model.enumtype.CourtStatus;

import java.util.List;
import jakarta.persistence.EntityManager;

public class CourtDAOImpl implements CourtDAO{
    @Override
    public void save(Court court){
        EntityManager em = JpaUtil.getEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(court);
            em.getTransaction().commit();
        } catch (Exception e) {
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Court court){
        EntityManager em = JpaUtil.getEntityManager();
        try{
            em.getTransaction().begin();
            em.merge(court);
            em.getTransaction().commit();
        }catch (Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
        }finally{
            em.close();
        }
    }
    @Override
    public void delete(int id){
        EntityManager em = JpaUtil.getEntityManager();
        try{
            em.getTransaction().begin();
            Court court = em.find(Court.class, id);
            if(court != null){
                em.remove(court);
            }
            em.getTransaction().commit();
        } catch (Exception e){
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    @Override
    public Court findById(int id){
        EntityManager em = JpaUtil.getEntityManager();
        try{
            Court court = em.find(Court.class, id);
            return court;
        } finally {
            em.close();
        }
    }
    @Override
    public List<Court> findAll(){
        EntityManager em = JpaUtil.getEntityManager();
        try{
            List<Court> list = em.createQuery("SELECT c FROM Court c", Court.class).getResultList();
            return list;
        } finally {
            em.close();
        }
    }
    @Override
    public List<Court> findByStatus(CourtStatus status){
        EntityManager em = JpaUtil.getEntityManager();
        try{
            return em.createQuery("SELECT c FROM Court c WHERE c.status = :status", Court.class)
                .setParameter("status", status)
                .getResultList();
        } finally {
            em.close();
        }
    }
}
