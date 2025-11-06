package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.Activity;
import java.util.List;

public interface ActivityBeanI extends GenericBeanI<Activity> {
    Activity addOrUpdate(Activity activity);
    Activity findById(Long activityId);
    List<Activity> list();
    boolean update(Long id, Activity activityUpdate);
    boolean delete(Activity activity);
    List<Activity> findByUser(Long userId);
    List<Activity> findByTask(Long taskId);
    List<Activity> findByType(Activity.Type type);
    List<Activity> getRecentActivities(int limit);

    // Add this method to match GenericBeanI
    Activity findById(Class<Activity> entity, Long id);
}