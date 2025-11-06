package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.ActivityBeanI;
import com.tasktk.app.entity.Activity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class ActivityBean extends GenericBean<Activity> implements ActivityBeanI {
    private static final Logger LOGGER = Logger.getLogger(ActivityBean.class.getName());

    @Override
    public Activity addOrUpdate(Activity activity) {
        validateActivity(activity);
        LOGGER.info("Creating/Updating activity: " + activity.getType());
        return getDao().addOrUpdate(activity);
    }

    @Override
    public Activity findById(Long activityId) {
        if (activityId == null) {
            throw new IllegalArgumentException("Activity ID cannot be null");
        }
        Activity activity = getDao().findById(Activity.class, activityId);
        if (activity == null) {
            throw new EntityNotFoundException("Activity with ID " + activityId + " not found");
        }
        return activity;
    }

    @Override
    public List<Activity> list() {
        LOGGER.info("Retrieving all activities");
        return getDao().list(new Activity());
    }

    @Override
    public boolean update(Long id, Activity activityUpdate) {
        Activity existingActivity = findById(id);
        updateActivityFields(existingActivity, activityUpdate);
        getDao().addOrUpdate(existingActivity);
        return true;
    }

    @Override
    public boolean delete(Activity activity) {
        if (activity == null || activity.getId() == null) {
            throw new IllegalArgumentException("Activity and Activity ID are required for deletion");
        }
        try {
            LOGGER.info("Deleting Activity with ID: " + activity.getId());
            getDao().delete(Activity.class, activity.getId());
            return true;
        } catch (EntityNotFoundException e) {
            LOGGER.warning("Activity with ID " + activity.getId() + " not found for deletion");
            return false;
        }
    }

    // Business logic methods
    private void validateActivity(Activity activity) {
        if (activity.getUser() == null) {
            throw new IllegalArgumentException("Activity must be associated with a user");
        }
        if (activity.getTask() == null) {
            throw new IllegalArgumentException("Activity must be associated with a task");
        }
        if (activity.getType() == null) {
            throw new IllegalArgumentException("Activity type is required");
        }
        if (activity.getDetails() == null || activity.getDetails().trim().isEmpty()) {
            throw new IllegalArgumentException("Activity details are required");
        }
    }

    private void updateActivityFields(Activity existingActivity, Activity activityUpdate) {
        if (activityUpdate.getUser() != null) existingActivity.setUser(activityUpdate.getUser());
        if (activityUpdate.getTask() != null) existingActivity.setTask(activityUpdate.getTask());
        if (activityUpdate.getType() != null) existingActivity.setType(activityUpdate.getType());
        if (activityUpdate.getDetails() != null) existingActivity.setDetails(activityUpdate.getDetails());
        if (activityUpdate.getTimeStamp() != null) existingActivity.setTimeStamp(activityUpdate.getTimeStamp());
    }

    // Additional business methods
    public List<Activity> findByUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return getDao().findByProperty(Activity.class, "user.id", userId);
    }

    public List<Activity> findByTask(Long taskId) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task ID cannot be null");
        }
        return getDao().findByProperty(Activity.class, "task.id", taskId);
    }

    public List<Activity> findByType(Activity.Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Activity type cannot be null");
        }
        return getDao().findByProperty(Activity.class, "type", type);
    }

    public List<Activity> getRecentActivities(int limit) {
        // This would require a custom query in your DAO
        // For now, return all activities and limit in memory (not optimal for large datasets)
        List<Activity> allActivities = list();
        return allActivities.stream()
                .sorted((a1, a2) -> a2.getTimeStamp().compareTo(a1.getTimeStamp()))
                .limit(limit)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Activity findById(Class<Activity> entity, Long id) {
        return findById(id); // Delegate to the other method
    }
}