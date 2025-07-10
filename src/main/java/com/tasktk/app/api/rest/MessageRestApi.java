package com.tasktk.app.api.rest;

import com.tasktk.app.bean.MessageBean;
import com.tasktk.app.bean.beanI.MessageBeanI;
import com.tasktk.app.entity.Message;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/message")
public class MessageRestApi extends BaseRestApi{

    @EJB
    private MessageBeanI messageBean;

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Message message){
        messageBean.addOrUpdate(message);
        return respond();
    }

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(){
        return respond(messageBean.list(new Message()));
    }

    @Path("/delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id){
        messageBean.delete(Message.class, id);
        return respond();
    }
}
