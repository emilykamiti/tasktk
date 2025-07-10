package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.Task;

import java.sql.SQLException;

public interface TaskBeanI extends GenericBeanI<Task>{

    boolean updateTask(Task task) throws SQLException;
}
