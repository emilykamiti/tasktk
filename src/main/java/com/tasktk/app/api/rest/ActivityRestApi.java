package com.tasktk.app.api.rest;

import com.tasktk.app.bean.beanI.ActivityBeanI;
import com.tasktk.app.entity.Activity;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/activity")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActivityRestApi extends BaseRestApi {

    @EJB
    private ActivityBeanI activityBean;

    @GET
    public Response getAllActivities() {
        try {
            List<Activity> activities = activityBean.list();
            return respond(Response.Status.OK, "Activities retrieved successfully", activities);
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving activities: " + e.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Response getActivityById(@PathParam("id") Long id) {
        try {
            Activity activity = activityBean.findById(id);
            return respond(Response.Status.OK, "Activity retrieved successfully", activity);
        } catch (RuntimeException e) {
            return respond(Response.Status.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving activity: " + e.getMessage());
        }
    }

    @POST
    public Response createActivity(Activity activity) {
        try {
            Activity createdActivity = activityBean.addOrUpdate(activity);
            return respond(Response.Status.CREATED, "Activity created successfully", createdActivity);
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error creating activity: " + e.getMessage());
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateActivity(@PathParam("id") Long id, Activity activityUpdate) {
        try {
            boolean updated = activityBean.update(id, activityUpdate);
            if (updated) {
                Activity updatedActivity = activityBean.findById(id);
                return respond(Response.Status.OK, "Activity updated successfully", updatedActivity);
            } else {
                return respond(Response.Status.NOT_FOUND, "Activity not found with id: " + id);
            }
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error updating activity: " + e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteActivity(@PathParam("id") Long id) {
        try {
            Activity activity = activityBean.findById(id);
            boolean deleted = activityBean.delete(activity);
            if (deleted) {
                return respond(Response.Status.OK, "Activity deleted successfully");
            } else {
                return respond(Response.Status.INTERNAL_SERVER_ERROR, "Failed to delete activity");
            }
        } catch (RuntimeException e) {
            return respond(Response.Status.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error deleting activity: " + e.getMessage());
        }
    }

    @GET
    @Path("/user/{userId}")
    public Response getActivitiesByUser(@PathParam("userId") Long userId) {
        try {
            List<Activity> activities = activityBean.findByUser(userId);
            return respond(Response.Status.OK, "User activities retrieved successfully", activities);
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving user activities: " + e.getMessage());
        }
    }

    @GET
    @Path("/task/{taskId}")
    public Response getActivitiesByTask(@PathParam("taskId") Long taskId) {
        try {
            List<Activity> activities = activityBean.findByTask(taskId);
            return respond(Response.Status.OK, "Task activities retrieved successfully", activities);
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving task activities: " + e.getMessage());
        }
    }

    @GET
    @Path("/type/{type}")
    public Response getActivitiesByType(@PathParam("type") String type) {
        try {
            Activity.Type activityType = Activity.Type.valueOf(type.toUpperCase());
            List<Activity> activities = activityBean.findByType(activityType);
            return respond(Response.Status.OK, "Activities retrieved successfully", activities);
        } catch (IllegalArgumentException e) {
            return respond(Response.Status.BAD_REQUEST, "Invalid activity type: " + type);
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving activities by type: " + e.getMessage());
        }
    }

    @GET
    @Path("/recent/{limit}")
    public Response getRecentActivities(@PathParam("limit") int limit) {
        try {
            if (limit <= 0 || limit > 100) {
                return respond(Response.Status.BAD_REQUEST, "Limit must be between 1 and 100");
            }
            List<Activity> activities = activityBean.getRecentActivities(limit);
            return respond(Response.Status.OK, "Recent activities retrieved successfully", activities);
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving recent activities: " + e.getMessage());
        }
    }
}