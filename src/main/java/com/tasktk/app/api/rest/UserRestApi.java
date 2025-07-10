package com.tasktk.app.api.rest;

import com.tasktk.app.bean.beanI.UserBeanI;
import com.tasktk.app.entity.User;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/user")
public class UserRestApi extends BaseRestApi {

    @EJB
    private UserBeanI userBean;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register (User user) {
        try {
            User registeredUser = userBean.register(user);
            return respond(Response.Status.CREATED, "User registered successfully", registeredUser);

        } catch (RuntimeException e) {
            return respond(Response.Status.CONFLICT, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Registration failed");
        }
    }
}


