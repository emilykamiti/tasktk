package com.tasktk.app.api.rest;

import com.tasktk.app.bean.beanI.GenericBeanI;
import com.tasktk.app.bean.beanI.TeamBeanI;
import com.tasktk.app.entity.Team;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/team")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamRestApi extends BaseEntityRestApi<Team> {

    @EJB
    private TeamBeanI teamBean;

    @Override
    protected GenericBeanI<Team> getBean() {
        return teamBean;
    }

    @Override
    protected Class<Team> getEntityClass() {
        return Team.class;
    }

    // Custom endpoints beyond basic CRUD
    @GET
    @Path("/{id}/members")
    public Response getTeamMembers(@PathParam("id") Long teamId) {
        try {
            List<Object> members = teamBean.getTeamMembers(teamId);
            return respond(Response.Status.OK, "Team members retrieved successfully", members);
        } catch (RuntimeException e) {
            return respond(Response.Status.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving team members: " + e.getMessage());
        }
    }

    @GET
    @Path("/name/{name}")
    public Response getTeamByName(@PathParam("name") String name) {
        try {
            Team team = teamBean.findByName(name);
            if (team != null) {
                return respond(Response.Status.OK, "Team retrieved successfully", team);
            } else {
                return respond(Response.Status.NOT_FOUND, "Team not found with name: " + name);
            }
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving team: " + e.getMessage());
        }
    }

    @POST
    @Path("/{teamId}/add-member/{userId}")
    public Response addTeamMember(@PathParam("teamId") Long teamId, @PathParam("userId") Long userId) {
        try {
            boolean added = teamBean.addMemberToTeam(teamId, userId);
            if (added) {
                return respond(Response.Status.OK, "Member added to team successfully");
            } else {
                return respond(Response.Status.BAD_REQUEST, "Failed to add member to team");
            }
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error adding team member: " + e.getMessage());
        }
    }

    @DELETE
    @Path("/{teamId}/remove-member/{userId}")
    public Response removeTeamMember(@PathParam("teamId") Long teamId, @PathParam("userId") Long userId) {
        try {
            boolean removed = teamBean.removeMemberFromTeam(teamId, userId);
            if (removed) {
                return respond(Response.Status.OK, "Member removed from team successfully");
            } else {
                return respond(Response.Status.BAD_REQUEST, "Failed to remove member from team");
            }
        } catch (RuntimeException e) {
            return respond(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return respond(Response.Status.INTERNAL_SERVER_ERROR, "Error removing team member: " + e.getMessage());
        }
    }
}