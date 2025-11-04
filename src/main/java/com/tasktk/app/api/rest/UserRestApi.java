package com.tasktk.app.api.rest;

import com.tasktk.app.bean.beanI.UserBeanI;
import com.tasktk.app.entity.User;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRestApi extends BaseRestApi {

    @EJB
    private UserBeanI userBean;

    @POST
    @Path("/register")
    public Response register(User user) {
        try {
            User registeredUser = userBean.register(user);
            return respond(Response.Status.CREATED, "User registered successfully", registeredUser);
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Database error during registration");
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Registration failed: " + e.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        try {
            User user = userBean.findById(id);
            return respond(Response.Status.OK, "User retrieved successfully", user);
        } catch (RuntimeException e) {
            return respond(Response.Status.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving user: " + e.getMessage());
        }
    }

    @GET
    public Response getAllUsers() {
        try {
            List<User> users = userBean.list();
            return respond(Response.Status.OK, "Users retrieved successfully", users);
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving users: " + e.getMessage());
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, User userUpdate) {
        try {
            boolean updated = userBean.update(id, userUpdate);
            if (updated) {
                User updatedUser = userBean.findById(id);
                return respond(Response.Status.OK, "User updated successfully", updatedUser);
            } else {
                return respond(Response.Status.NOT_FOUND, "User not found with id: " + id);
            }
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error updating user: " + e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        try {
            User user = userBean.findById(id);
            boolean deleted = userBean.unregister(user);
            if (deleted) {
                return respond(Response.Status.OK, "User deleted successfully");
            } else {
                return respond(Response.Status.INTERNAL_SERVER_ERROR, "Failed to delete user");
            }
        } catch (RuntimeException e) {
            return respond(Response.Status.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error deleting user: " + e.getMessage());
        }
    }

    @POST
    @Path("/change-password")
    public Response changePassword(User user) {
        try {
            boolean passwordChanged = userBean.changePwd(user);
            if (passwordChanged) {
                return respond(Response.Status.OK, "Password changed successfully");
            } else {
                return respond(Response.Status.INTERNAL_SERVER_ERROR, "Failed to change password");
            }
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error changing password: " + e.getMessage());
        }
    }

    @GET
    @Path("/team/{teamId}")
    public Response getUsersByTeam(@PathParam("teamId") Long teamId) {
        try {
            List<User> users = userBean.getUsersByTeam(teamId);
            return respond(Response.Status.OK, "Users retrieved successfully", users);
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving users by team: " + e.getMessage());
        }
    }
}