package com.tasktk.app.api.rest;

import com.tasktk.app.bean.beanI.ActivityBeanI;
import com.tasktk.app.bean.beanI.AuthBeanI;
import com.tasktk.app.bean.beanI.GenericBeanI;
import com.tasktk.app.entity.Activity;

import com.tasktk.app.entity.User;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path(("/activity"))
public class ActivityRestApi extends BaseEntityRestApi<Activity> {
    @EJB
    private ActivityBeanI activityBean;

    @Override
    protected GenericBeanI<Activity> getBean() {
        return activityBean;
    }

    @Override
    protected Class<Activity> getEntityClass() {
        return Activity.class;
    }
}
