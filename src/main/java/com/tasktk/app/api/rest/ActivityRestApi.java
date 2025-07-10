package com.tasktk.app.api.rest;

import com.tasktk.app.bean.beanI.ActivityBeanI;
import com.tasktk.app.bean.beanI.AuthBeanI;
import com.tasktk.app.entity.Activity;

import com.tasktk.app.entity.User;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path(("/activity"))
public class ActivityRestApi extends BaseRestApi {

    @EJB
    private ActivityBeanI activityBean;

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Activity activity){
        activityBean.addOrUpdate(activity);
        return respond();
    }

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(){
        return respond(activityBean.list(new Activity()));
    }

    @Path("/delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id){
        activityBean.delete(Activity.class, id);
        return respond();
    }


}
