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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
//    public Response add(@Valid T entity) {
//        try {
//            LOGGER.info("Creating new entity...");
//            T createdEntity = getBean().addOrUpdate(entity);
//            LOGGER.info("Entity created with ID: " + getEntityId(createdEntity));
//
//            // Return the created entity with success response
//            return respond(Response.Status.CREATED, "Entity created successfully", createdEntity);
//
//        } catch (RuntimeException e) {
//            LOGGER.severe("Error adding entity: " + e.getMessage());
//            return respond(Response.Status.BAD_REQUEST, e.getMessage());
//        } catch (Exception e) {
//            LOGGER.severe("Unexpected error adding entity: " + e.getMessage());
//            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Failed to create entity: " + e.getMessage());
//        }
//    }

    public Response add(@Valid T entity) {
        try {
            System.out.println("=== BASEENTITYRESTAPI.ADD() STARTED ===");
            T createdEntity = getBean().addOrUpdate(entity);
            System.out.println("Entity created with ID: " + getEntityId(createdEntity));
            System.out.println("Entity object: " + createdEntity);

            // Use regular respond method but with debug
            System.out.println("Calling respond(Status, String, Object)...");
            Response response = respond(Response.Status.CREATED, "Entity created successfully", createdEntity);

            // Check what's in the response
            System.out.println("Response status: " + response.getStatus());
            Object responseEntity = response.getEntity();
            if (responseEntity instanceof RestResponseWrapper) {
                RestResponseWrapper wrapper = (RestResponseWrapper) responseEntity;
                System.out.println("Final wrapper data: " + wrapper.getData());
            }

            return response;

        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Failed to create entity: " + e.getMessage());
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(
            @PathParam("id") Long id,
            @Valid T entityUpdate) {
        try {
            LOGGER.info("Updating entity with ID: " + id);
            boolean success = getBean().update(id, entityUpdate);
            if (success) {
                T updatedEntity = getBean().findById(getEntityClass(), id);
                return respond(Response.Status.OK, "Entity updated successfully", updatedEntity);
            } else {
                return respond(Response.Status.NOT_FOUND, "Entity not found with id: " + id);
            }
        } catch (RuntimeException e) {
            LOGGER.severe("Error updating entity with ID " + id + ": " + e.getMessage());
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Unexpected error updating entity: " + e.getMessage());
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Failed to update entity: " + e.getMessage());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list() {
        try {
            LOGGER.info("Listing all entities");
            java.util.List<T> entities = getBean().list(getEntityClass().getDeclaredConstructor().newInstance());
            return respond(Response.Status.OK, "Entities retrieved successfully", entities);
        } catch (Exception e) {
            LOGGER.severe("Error listing entities: " + e.getMessage());
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Failed to list entities: " + e.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        try {
            LOGGER.info("Getting entity with ID: " + id);
            T entity = getBean().findById(getEntityClass(), id);
            if (entity != null) {
                return respond(Response.Status.OK, "Entity retrieved successfully", entity);
            } else {
                return respond(Response.Status.NOT_FOUND, "Entity not found with id: " + id);
            }
        } catch (Exception e) {
            LOGGER.severe("Error getting entity with ID " + id + ": " + e.getMessage());
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Failed to retrieve entity: " + e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {
        try {
            LOGGER.info("Deleting entity with ID: " + id);
            getBean().delete(getEntityClass(), id);
            return respond(Response.Status.OK, "Entity deleted successfully");
        } catch (RuntimeException e) {
            LOGGER.severe("Error deleting entity with ID " + id + ": " + e.getMessage());
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Unexpected error deleting entity: " + e.getMessage());
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Failed to delete entity: " + e.getMessage());
        }
    }

    // Helper method to get entity ID using reflection
    private Long getEntityId(T entity) {
        try {
            java.lang.reflect.Method getIdMethod = entity.getClass().getMethod("getId");
            return (Long) getIdMethod.invoke(entity);
        } catch (Exception e) {
            return null;
        }
    }
}