package com.badminton.management.dao.impl;

import com.badminton.management.config.JpaUtil;
import com.badminton.management.dao.CustomerDAO;
import com.badminton.management.model.entity.Customer;
import java.util.List;
import jakarta.persistence.EntityManager;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public void save(Customer cus) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cus);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Customer cus) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(cus);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Customer cus = em.find(Customer.class, id);
            if (cus != null)
                em.remove(cus);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public Customer findById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Customer cus = em.find(Customer.class, id);
            return cus;
        } finally {
            em.close();
        }
    }

    @Override
    public Customer findByAccountId(int accountId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // --- SỬA DÒNG NÀY: c.account.accountId thành c.accountId.accountId ---
            Customer cus = em.createQuery("SELECT c FROM Customer c WHERE c.accountId.accountId = :accountId", Customer.class)
                    .setParameter("accountId", accountId)
                    .getSingleResult();
            return cus;
        } catch (Exception e) {
            return null; // Bắt lỗi nên nếu query sai nó sẽ trả về null luôn
        } finally {
            em.close();
        }
    }

    @Override
    public Customer findByPhone(String phone) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Customer cus = em.createQuery("SELECT c FROM Customer c WHERE c.phone = :phone", Customer.class)
                    .setParameter("phone", phone)
                    .getSingleResult();
            return cus;
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Customer> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Customer> list = em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Customer> findByName(String name) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Customer c WHERE c.customerName LIKE :name", Customer.class)
                    .setParameter("name", "%" + name + "%") 
                    .getResultList();
        } finally {
            em.close();
        }
    }
    @Override
    public long getCountCustomer(){
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(c.customerId) FROM Customer c", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }
}
