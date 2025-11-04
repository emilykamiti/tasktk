package com.tasktk.app.api.rest;

import com.tasktk.app.bean.beanI.MessageBeanI;
import com.tasktk.app.entity.Message;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageRestApi extends BaseRestApi {

    @EJB
    private MessageBeanI messageBean;

    @GET
    public Response getAllMessages() {
        try {
            List<Message> messages = messageBean.list();
            return respond(Response.Status.OK, "Messages retrieved successfully", messages);
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving messages: " + e.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Response getMessageById(@PathParam("id") Long id) {
        try {
            Message message = messageBean.findById(id);
            return respond(Response.Status.OK, "Message retrieved successfully", message);
        } catch (RuntimeException e) {
            return respond(Response.Status.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving message: " + e.getMessage());
        }
    }

    @POST
    public Response createMessage(Message message) {
        try {
            Message createdMessage = messageBean.addOrUpdate(message);
            return respond(Response.Status.CREATED, "Message created successfully", createdMessage);
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error creating message: " + e.getMessage());
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateMessage(@PathParam("id") Long id, Message messageUpdate) {
        try {
            boolean updated = messageBean.update(id, messageUpdate);
            if (updated) {
                Message updatedMessage = messageBean.findById(id);
                return respond(Response.Status.OK, "Message updated successfully", updatedMessage);
            } else {
                return respond(Response.Status.NOT_FOUND, "Message not found with id: " + id);
            }
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error updating message: " + e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMessage(@PathParam("id") Long id) {
        try {
            Message message = messageBean.findById(id);
            boolean deleted = messageBean.delete(message);
            if (deleted) {
                return respond(Response.Status.OK, "Message deleted successfully");
            } else {
                return respond(Response.Status.INTERNAL_SERVER_ERROR, "Failed to delete message");
            }
        } catch (RuntimeException e) {
            return respond(Response.Status.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error deleting message: " + e.getMessage());
        }
    }

    @GET
    @Path("/team/{teamId}")
    public Response getMessagesByTeam(@PathParam("teamId") Long teamId) {
        try {
            List<Message> messages = messageBean.findByTeam(teamId);
            return respond(Response.Status.OK, "Team messages retrieved successfully", messages);
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving team messages: " + e.getMessage());
        }
    }

    @GET
    @Path("/sender/{senderId}")
    public Response getMessagesBySender(@PathParam("senderId") Long senderId) {
        try {
            List<Message> messages = messageBean.findBySender(senderId);
            return respond(Response.Status.OK, "Sender messages retrieved successfully", messages);
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving sender messages: " + e.getMessage());
        }
    }

    @GET
    @Path("/unread/{userId}")
    public Response getUnreadMessages(@PathParam("userId") Long userId) {
        try {
            List<Message> messages = messageBean.findUnreadMessages(userId);
            return respond(Response.Status.OK, "Unread messages retrieved successfully", messages);
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving unread messages: " + e.getMessage());
        }
    }

    @PATCH
    @Path("/{id}/read")
    public Response markMessageAsRead(@PathParam("id") Long id) {
        try {
            boolean marked = messageBean.markAsRead(id);
            if (marked) {
                return respond(Response.Status.OK, "Message marked as read");
            } else {
                return respond(Response.Status.INTERNAL_SERVER_ERROR, "Failed to mark message as read");
            }
        } catch (RuntimeException e) {
            return respond(Response.Status.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error marking message as read: " + e.getMessage());
        }
    }

    @PATCH
    @Path("/team/{teamId}/read-all/{userId}")
    public Response markAllMessagesAsRead(@PathParam("teamId") Long teamId, @PathParam("userId") Long userId) {
        try {
            boolean marked = messageBean.markAllAsRead(teamId, userId);
            if (marked) {
                return respond(Response.Status.OK, "All messages marked as read");
            } else {
                return respond(Response.Status.INTERNAL_SERVER_ERROR, "Failed to mark messages as read");
            }
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error marking messages as read: " + e.getMessage());
        }
    }
}