package com.tasktk.app.api.rest;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Produces(MediaType.APPLICATION_JSON)
public class RestResponseWrapper implements Serializable {
    private boolean success;
    private String message;
    private Object data;
    private Map<String, Object> meta;

    public RestResponseWrapper() {
        this(true, "OK");
    }

    public RestResponseWrapper(String message) {
        this(true, message);
    }

    public RestResponseWrapper(boolean success, String message) {
        this(success, message, null);
    }

    public RestResponseWrapper(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.meta = new HashMap<>();
    }


    public static RestResponseWrapper success() {
        return new RestResponseWrapper();
    }

    public static RestResponseWrapper success(String message) {
        return new RestResponseWrapper(message);
    }

    public static RestResponseWrapper success(Object data) {
        RestResponseWrapper wrapper = new RestResponseWrapper();
        wrapper.setData(data);
        return wrapper;
    }

    public static RestResponseWrapper error(String message) {
        return new RestResponseWrapper(false, message);
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public RestResponseWrapper addMeta(String key, Object value) {
        this.meta.put(key, value);
        return this;
    }
}