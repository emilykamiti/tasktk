package com.tasktk.app.dao;

import jakarta.persistence.EntityManager;

import java.io.Serializable;
import java.util.List;

public interface GenericDaoI<T> extends Serializable {

    List<T> list(T entity);

    T addOrUpdate(T entity);

    void delete(Class<?> entityClass, Long id);

    public boolean doesUserExistByEmail(String email);

    EntityManager getEm();

    T findById(Class<T> entity, Long id);

    void setEm(EntityManager em);

    boolean update(Long id, T entityUpdate);

    T findByUserName(Class<T> entity, String userName);
}
