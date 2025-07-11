package com.tasktk.app.api.rest;

import com.tasktk.app.bean.beanI.TeamBeanI;
import com.tasktk.app.entity.Team;
import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/team")
public class TeamRestApi extends BaseRestApi {
    @EJB
    private TeamBeanI teamBean;

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Team team) {
        teamBean.addOrUpdate(team);
        return respond();
    }

    @Path("/update/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTeam(
            @PathParam("id") Long id,
            @Valid Team teamUpdate) {
        boolean success = teamBean.updateTeam(id, teamUpdate);
        if (success) {
            Team updatedTeam = teamBean.findById(id);
            return Response.ok(updatedTeam).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("/list")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response list() {
        return respond(teamBean.list(new Team()));
    }

    @Path("/delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {
        teamBean.delete(Team.class, id);
        return respond();
    }
}