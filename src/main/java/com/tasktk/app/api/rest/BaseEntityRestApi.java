package com.tasktk.app.api.rest;

import com.tasktk.app.bean.beanI.GenericBeanI;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.logging.Logger;

public abstract class BaseEntityRestApi<T> extends BaseRestApi {
    private static final Logger LOGGER = Logger.getLogger(BaseEntityRestApi.class.getName());

    protected abstract GenericBeanI<T> getBean();

    protected abstract Class<T> getEntityClass();

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@Valid T entity) {
        try {
            getBean().addOrUpdate(entity);
            return respond();
        } catch (RuntimeException e) {
            LOGGER.severe("Error adding entity: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @Path("/update/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(
            @PathParam("id") Long id,
            @Valid T entityUpdate) {
        try {
            boolean success = getBean().update(id, entityUpdate);
            if (success) {
                T updatedEntity = getBean().findById(getEntityClass(), id);
                return Response.ok(updatedEntity).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (RuntimeException e) {
            LOGGER.severe("Error updating entity with ID " + id + ": " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @Path("/list")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response list() {
        try {
            return respond(getBean().list(getEntityClass().getDeclaredConstructor().newInstance()));
        } catch (Exception e) {
            LOGGER.severe("Error listing entities: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to list entities: " + e.getMessage())
                    .build();
        }
    }


    //Meant to delete and attribute and not an entire entity.
    @Path("/delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {
        try {
            getBean().delete(getEntityClass(), id);
            return respond();
        } catch (RuntimeException e) {
            LOGGER.severe("Error deleting entity with ID " + id + ": " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
}