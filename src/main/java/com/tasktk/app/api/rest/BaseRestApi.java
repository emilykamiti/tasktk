package com.tasktk.app.api.rest;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public abstract class BaseRestApi {
    // Basic success response
    protected Response respond() {
        return Response.ok(RestResponseWrapper.success()).build();
    }

    // Success response with custom message
    protected Response respond(String message) {
        return Response.ok(RestResponseWrapper.success(message)).build();
    }

    // Success response with data
    protected Response respond(Object data) {
        return Response.ok(RestResponseWrapper.success(data)).build();
    }

    // Custom status and message
    protected Response respond(Status status, String message) {
        return Response.status(status)
                .entity(status.getFamily() == Status.Family.SUCCESSFUL
                        ? RestResponseWrapper.success(message)
                        : RestResponseWrapper.error(message))
                .build();
    }

    // Custom status, message and data
    protected Response respond(Status status, String message, Object data) {
        RestResponseWrapper wrapper = status.getFamily() == Status.Family.SUCCESSFUL
                ? RestResponseWrapper.success(message)
                : RestResponseWrapper.error(message);
        wrapper.setData(data);
        return Response.status(status).entity(wrapper).build();
    }

    // Error response with exception
    protected Response respond(Throwable ex) {
        return Response.serverError()
                .entity(RestResponseWrapper.error(ex.getMessage()))
                .build();
    }

    // Response with custom wrapper
    protected Response respond(RestResponseWrapper wrapper) {
        return Response.status(
                wrapper.isSuccess() ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR
        ).entity(wrapper).build();
    }
}