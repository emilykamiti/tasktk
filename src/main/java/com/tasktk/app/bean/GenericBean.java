package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.GenericBeanI;
import com.tasktk.app.dao.GenericDao;
import com.tasktk.app.dao.GenericDaoI;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.List;

@Stateless
public abstract class GenericBean<T> implements GenericBeanI<T>, Serializable {

    @PersistenceContext
    private EntityManager em;

    @Inject
    GenericDaoI<T> genericDao;

    @Override
    public List<T> list(T entity) {
        genericDao.setEm(em);
        return genericDao.list(entity);
    }

    @Override
    public T addOrUpdate(T entity) {
        genericDao.setEm(em);
        return genericDao.addOrUpdate(entity);
    }

    @Override
    public void delete(Class<?> entityClass, Long id) {
        genericDao.setEm(em);
        genericDao.delete((Class<T>) entityClass, id);
    }

    @Override
    public T findById(Class<T> entity, Long id) {
        genericDao.setEm(em);
        return genericDao.findById(entity, id);
    }

    public GenericDao<T> getDao() {
        genericDao.setEm(em);
        return (GenericDao<T>) genericDao;
    }


}