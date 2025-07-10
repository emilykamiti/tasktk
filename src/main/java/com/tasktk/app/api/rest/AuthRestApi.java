package com.tasktk.app.api.rest;

import com.tasktk.app.bean.beanI.AuthBeanI;
import com.tasktk.app.entity.User;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthRestApi extends BaseRestApi {

    @EJB
    private AuthBeanI authBean;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User loginRequest) {
        try {
            User authenticatedUser = authBean.authenticate(loginRequest);
            System.out.println(" the user is being logged in .........");
            RestResponseWrapper response = new RestResponseWrapper(true, "Login successful");
//           response.setData(User);
            return Response.ok().entity(response).build();

        } catch (Exception e) {
            RestResponseWrapper errorResponse = new RestResponseWrapper(false, e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorResponse).build();
        }
    }
}
