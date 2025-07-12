package com.tasktk.app.bean.beanI;

import java.io.Serializable;
import java.util.List;

public interface GenericBeanI<T> extends Serializable {

    List<T> list(T entity);

    T addOrUpdate(T entity);

    void delete(Class<?> entityClass, Long id);

    T findById(Class<T> entity, Long id);

    boolean update(Long id, T entityUpdate);
}