package com.badminton.management.dao.impl;

import com.badminton.management.config.JpaUtil;
import com.badminton.management.dao.AccountDAO;
import com.badminton.management.model.entity.Account;
import java.util.List;
import jakarta.persistence.EntityManager;

public class AccountDAOImpl implements AccountDAO {
    @Override
    public void save(Account acc){
        EntityManager em = JpaUtil.getEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(acc);
            em.getTransaction().commit();
        } catch (Exception e) {
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public Account findById(int id){
        EntityManager em = JpaUtil.getEntityManager();
        try{
            Account acc = em.find(Account.class, id);
            return acc;
        } finally {
            em.close();
        }
    }

    @Override
    public Account findByName(String username){
        EntityManager em = JpaUtil.getEntityManager();
        try{
            Account acc = em.createQuery("SELECT a FROM Account a WHERE a.username = :user", Account.class)
                .setParameter("user", username)
                .getSingleResult();
            return acc;
        } catch (Exception e){
            return null;
        } finally {
            em.close();
        }
        
    }
    @Override
    public List<Account> findAll(){
        EntityManager em = JpaUtil.getEntityManager();
        try{
            List<Account> list = em.createQuery("SELECT a FROM Account a", Account.class).getResultList();
            return list;
        } finally {
            em.close();
        }
    }
    @Override
    public void update(Account acc){
        EntityManager em = JpaUtil.getEntityManager();
        try{
            em.getTransaction().begin();
            em.merge(acc);
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
            Account acc = em.find(Account.class, id);
            if(acc != null){
                em.remove(acc);
            }
            em.getTransaction().commit();
        } catch (Exception e){
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
