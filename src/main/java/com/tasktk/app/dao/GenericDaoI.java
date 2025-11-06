package com.tasktk.app.dao;

import jakarta.persistence.EntityManager;
import java.util.List;

public interface GenericDaoI<T> {

    // EntityManager setter - ADD THIS METHOD
    void setEm(EntityManager em);

    // CRUD methods
    List<T> list(T entity);
    T addOrUpdate(T entity);
    T findById(Class<T> entity, Long id);
    void delete(Class<?> entityClass, Long id);
    boolean update(Long id, T entityUpdate);

    // Business-specific methods
    boolean doesUserExistByEmail(String email);
    T findByUserName(Class<T> entity, String userName);

    // Generic query method
    List<T> findByProperty(Class<T> clazz, String propertyName, Object value);
}