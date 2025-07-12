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
    public boolean update(Long id, Activity activityUpdate) {
        if (activityUpdate.getType() != null) {
            Activity existingActivity = getDao().findById(Activity.class, id);
            if (existingActivity!= null  && !id.equals(existingActivity.getId())) {
                throw new RuntimeException("Team with name " + activityUpdate.getType() + " already exists");
            }
        }
        return getDao().update(id, activityUpdate);
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
