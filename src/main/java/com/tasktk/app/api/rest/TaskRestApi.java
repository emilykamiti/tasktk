package com.tasktk.app.api.rest;

import com.tasktk.app.bean.beanI.TaskBeanI;
import com.tasktk.app.entity.Task;

import com.tasktk.app.entity.Team;
import jakarta.ejb.EJB;
import jakarta.validation.Valid;
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
    public Response add(Task task) {
        taskBean.addOrUpdate(task);
        return respond();
    }

    @Path("/update/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(
            @PathParam("id") Long id,
            @Valid Task taskUpdate) {
        boolean success = taskBean.update(id, taskUpdate);
        if (success) {
            Task updatedTask= taskBean.findById(Task.class, id);
            return Response.ok(updatedTask).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("/list")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response list() {
        return respond(taskBean.list(new Task()));
    }

    @Path("/delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {
        taskBean.delete(Task.class, id);
        return respond();
    }
}

