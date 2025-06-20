package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.ActivityBeanI;
import com.tasktk.app.entity.Activity;
import com.tasktk.app.entity.Task;
import com.tasktk.app.utility.EncryptText;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ActivityBean extends GenericBean<Activity> implements ActivityBeanI{
    private static final Logger LOGGER = Logger.getLogger(ActivityBean.class.getName());
    @Inject
    private EncryptText encryptText;

    @PersistenceContext
    private EntityManager em;


    @Override
    public Activity createActivity(Activity activity) throws SQLException {
        LOGGER.info("create Activity: " + activity.getType());
        getDao().addOrUpdate(activity);

        return activity;
    }

    @Override
    public Activity findById(Long activityId) {
        return em.find(Activity.class, activityId);
    }
    public List<Activity> listAll() {
        LOGGER.info("Retrieving all activities");
        TypedQuery<Activity> query = em.createQuery("SELECT m FROM Activity a", Activity.class);
        return query.getResultList();
    }
    @Override
    public boolean updateActivity(Activity activity) throws SQLException {
        Activity existingActivity = em.find(Activity.class, activity.getId());
        if(existingActivity ==null){
            LOGGER.info("Activity with ID" + activity.getId() + "not found for update");
            return false;
        }
        if(activity.getType() !=null && activity.getType().equals(existingActivity.getType())){
            throw new RuntimeException(("Activity with name" + activity.getType()
                    + "already exists"));
        }
        existingActivity.setType(activity.getType());

        LOGGER.info("Updating Activity:" + existingActivity.getType());
        getDao().addOrUpdate(existingActivity);

        return true;
    }

    @Override
    public boolean deleteActivity(Activity activity) {
        if (activity == null || activity.getId()== null){
            throw  new IllegalArgumentException(("Activity and Activity ID are required for deletion"));
        }
        try{
            LOGGER.info("Deleting Activity with ID:" + activity.getId());
            getDao().delete(Activity.class, activity.getId());
            return true;
        }catch (EntityNotFoundException e){
            LOGGER.info("Activity with ID" + activity.getId() + "not found deletion");
            return false;
        }
    }

}
