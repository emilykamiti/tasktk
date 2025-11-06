package com.tasktk.app.dao;

import jakarta.persistence.*;
import java.lang.reflect.Field;
import java.util.*;

public class GenericDao<T> implements GenericDaoI<T> {

    private EntityManager em;

    @Override
    public List<T> list(T entity) {
        return em.createQuery(
                "SELECT e FROM " + entity.getClass().getSimpleName() + " e",
                (Class<T>) entity.getClass()
        ).getResultList();
    }

    @Override
    public T addOrUpdate(T entity) {
        if (getEntityId(entity) == null) {
            em.persist(entity);
            return entity;
        } else {
            return em.merge(entity);
        }
    }

    @Override
    public boolean doesUserExistByEmail(String email) {
        try {
            Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return count > 0;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public T findByUserName(Class<T> entity, String userName) {
        try {
            String jpql = "SELECT e FROM " + entity.getSimpleName() + " e WHERE e.userName = :userName";
            TypedQuery<T> query = em.createQuery(jpql, entity);
            query.setParameter("userName", userName);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public T findById(Class<T> entity, Long id) {
        return em.find(entity, id);
    }

    @Override
    public void delete(Class<?> entityClass, Long id) {
        T entity = em.find((Class<T>) entityClass, id);
        if (entity != null) {
            em.remove(entity);
        } else {
            throw new EntityNotFoundException("Entity not found with id: " + id);
        }
    }

    @Override
    public boolean update(Long id, T entityUpdate) {
        if (id == null || entityUpdate == null) {
            throw new IllegalArgumentException("ID and entityUpdate cannot be null");
        }
        try {
            T existingEntity = findById((Class<T>) entityUpdate.getClass(), id);
            if (existingEntity == null) {
                System.out.println("Entity with ID " + id + " not found for update");
                return false;
            }

            Field[] fields = entityUpdate.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object newValue = field.get(entityUpdate);
                if (newValue != null) {
                    field.set(existingEntity, newValue);
                }
            }

            System.out.println("Updating entity with ID: " + id);
            em.merge(existingEntity);
            return true;
        } catch (Exception e) {
            System.out.println("Error updating entity with ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Failed to update entity: " + e.getMessage(), e);
        }
    }

    // ADD THIS METHOD:
    @Override
    public List<T> findByProperty(Class<T> clazz, String propertyName, Object value) {
        try {
            String jpql = "SELECT e FROM " + clazz.getSimpleName() + " e WHERE e." + propertyName + " = :value";
            TypedQuery<T> query = em.createQuery(jpql, clazz);
            query.setParameter("value", value);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error in findByProperty: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Helper method to get entity ID
    private Long getEntityId(T entity) {
        try {
            java.lang.reflect.Method getIdMethod = entity.getClass().getMethod("getId");
            return (Long) getIdMethod.invoke(entity);
        } catch (Exception e) {
            return null;
        }
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
}