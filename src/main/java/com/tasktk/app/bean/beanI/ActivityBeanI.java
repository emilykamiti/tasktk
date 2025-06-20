package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.Activity;

import java.sql.SQLException;

public interface ActivityBeanI extends GenericBeanI<Activity>{
    Activity createActivity (Activity activity) throws SQLException;

    boolean updateActivity(Activity activity) throws SQLException;

    boolean deleteActivity(Activity activity);

    Activity findById(Long activityId);
}
