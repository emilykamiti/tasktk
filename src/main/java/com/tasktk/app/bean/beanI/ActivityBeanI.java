package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.Activity;

import java.sql.SQLException;

public interface ActivityBeanI extends GenericBeanI <Activity> {
    boolean updateActivity(Activity activity) throws SQLException;

}
