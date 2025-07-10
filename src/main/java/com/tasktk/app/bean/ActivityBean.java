package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.ActivityBeanI;
import com.tasktk.app.entity.Activity;
import jakarta.ejb.Stateless;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class ActivityBean extends GenericBean<Activity> implements ActivityBeanI {
    private static final Logger LOGGER = Logger.getLogger(ActivityBean.class.getName());

    @Override
    public Activity addOrUpdate(Activity activity){
        LOGGER.info("create Activity: " + activity.getType());
        return getDao().addOrUpdate(activity);
    }

    public Activity findById(Long activityId) {
        return getDao().findById(Activity.class, activityId);
    }

    public List<Activity>  list() {
        LOGGER.info("Retrieving all activities");
        return getDao().list(new Activity());
    }

    @Override
    public boolean updateActivity(Activity activity) throws SQLException {
        Activity existingActivity = getDao().findById(Activity.class, activity.getId());
        if (existingActivity == null) {
            LOGGER.info("Activity with ID " + activity.getId() + " not found for update");
            return false;
        }
        if (activity.getType() != null && activity.getType().equals(existingActivity.getType())) {
            throw new RuntimeException("Activity with name " + activity.getType() + " already exists");
        }
        existingActivity.setType(activity.getType());
        LOGGER.info("Updating Activity: " + existingActivity.getType());
        getDao().addOrUpdate(existingActivity);
        return true;
    }

    public boolean delete(Activity activity) {
        if (activity == null || activity.getId() == null) {
            throw new IllegalArgumentException("Activity and Activity ID are required for deletion");
        }
        try {
            LOGGER.info("Deleting Activity with ID: " + activity.getId());
            getDao().delete(Activity.class, activity.getId());
            return true;
        } catch (EntityNotFoundException e) {
            LOGGER.info("Activity with ID " + activity.getId() + " not found for deletion");
            return false;
        }
    }
}
