package com.tasktk.app.api.rest;

import com.tasktk.app.bean.beanI.TaskBeanI;
import com.tasktk.app.entity.Task;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/task")
public class TaskRestApi extends BaseRestApi{
    @EJB
    private TaskBeanI taskBean;

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Task task){
        taskBean.addOrUpdate(task);
        return respond();
    }

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(){
        return respond(taskBean.list(new Task()));
    }

    @Path("/delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id){
        taskBean.delete(Task.class, id);
        return respond();
    }
}
