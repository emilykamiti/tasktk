package com.tasktk.app.api.rest;

import com.tasktk.app.bean.beanI.TaskBeanI;
import com.tasktk.app.entity.Task;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/task")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskRestApi extends BaseRestApi {

    @EJB
    private TaskBeanI taskBean;

    @GET
    public Response getAllTasks() {
        try {
            List<Task> tasks = taskBean.list();
            return respond(Response.Status.OK, "Tasks retrieved successfully", tasks);
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving tasks: " + e.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Response getTaskById(@PathParam("id") Long id) {
        try {
            Task task = taskBean.findById(id);
            return respond(Response.Status.OK, "Task retrieved successfully", task);
        } catch (RuntimeException e) {
            return respond(Response.Status.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving task: " + e.getMessage());
        }
    }

    @POST
    public Response createTask(Task task) {
        try {
            Task createdTask = taskBean.addOrUpdate(task);
            return respond(Response.Status.CREATED, "Task created successfully", createdTask);
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error creating task: " + e.getMessage());
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateTask(@PathParam("id") Long id, Task taskUpdate) {
        try {
            boolean updated = taskBean.update(id, taskUpdate);
            if (updated) {
                Task updatedTask = taskBean.findById(id);
                return respond(Response.Status.OK, "Task updated successfully", updatedTask);
            } else {
                return respond(Response.Status.NOT_FOUND, "Task not found with id: " + id);
            }
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error updating task: " + e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTask(@PathParam("id") Long id) {
        try {
            Task task = taskBean.findById(id);
            boolean deleted = taskBean.delete(task);
            if (deleted) {
                return respond(Response.Status.OK, "Task deleted successfully");
            } else {
                return respond(Response.Status.INTERNAL_SERVER_ERROR, "Failed to delete task");
            }
        } catch (RuntimeException e) {
            return respond(Response.Status.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error deleting task: " + e.getMessage());
        }
    }

    @PATCH
    @Path("/{id}/status")
    public Response updateTaskStatus(@PathParam("id") Long id, @QueryParam("status") String status) {
        try {
            Task.Status taskStatus = Task.Status.valueOf(status.toUpperCase());
            Task updatedTask = taskBean.updateTaskStatus(id, taskStatus);
            return respond(Response.Status.OK, "Task status updated successfully", updatedTask);
        } catch (IllegalArgumentException e) {
            return respond(Response.Status.BAD_REQUEST, "Invalid status: " + status);
        } catch (RuntimeException e) {
            return respond(Response.Status.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error updating task status: " + e.getMessage());
        }
    }

    @PATCH
    @Path("/{id}/meeting")
    public Response toggleMeeting(@PathParam("id") Long id, @QueryParam("hasMeeting") Boolean hasMeeting) {
        try {
            Task updatedTask = taskBean.toggleMeetingStatus(id, hasMeeting);
            return respond(Response.Status.OK, "Meeting status updated successfully", updatedTask);
        } catch (RuntimeException e) {
            return respond(Response.Status.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error toggling meeting status: " + e.getMessage());
        }
    }

    @PATCH
    @Path("/{id}/progress")
    public Response updateProgress(@PathParam("id") Long id, @QueryParam("progress") Integer progress) {
        try {
            Task updatedTask = taskBean.updateProgressPercentage(id, progress);
            return respond(Response.Status.OK, "Progress updated successfully", updatedTask);
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error updating progress: " + e.getMessage());
        }
    }

    @GET
    @Path("/team/{teamId}")
    public Response getTasksByTeam(@PathParam("teamId") Long teamId) {
        try {
            List<Task> tasks = taskBean.findByTeam(teamId);
            return respond(Response.Status.OK, "Tasks retrieved successfully", tasks);
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving tasks by team: " + e.getMessage());
        }
    }

    @GET
    @Path("/status/{status}")
    public Response getTasksByStatus(@PathParam("status") String status) {
        try {
            Task.Status taskStatus = Task.Status.valueOf(status.toUpperCase());
            List<Task> tasks = taskBean.findByStatus(taskStatus);
            return respond(Response.Status.OK, "Tasks retrieved successfully", tasks);
        } catch (IllegalArgumentException e) {
            return respond(Response.Status.BAD_REQUEST, "Invalid status: " + status);
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving tasks by status: " + e.getMessage());
        }
    }

    @GET
    @Path("/priority/{priority}")
    public Response getTasksByPriority(@PathParam("priority") String priority) {
        try {
            Task.Priority taskPriority = Task.Priority.valueOf(priority.toUpperCase());
            List<Task> tasks = taskBean.findByPriority(taskPriority);
            return respond(Response.Status.OK, "Tasks retrieved successfully", tasks);
        } catch (IllegalArgumentException e) {
            return respond(Response.Status.BAD_REQUEST, "Invalid priority: " + priority);
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving tasks by priority: " + e.getMessage());
        }
    }
}