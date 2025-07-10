package com.tasktk.app.api.rest;

import com.tasktk.app.bean.TeamBean;
import com.tasktk.app.bean.beanI.TeamBeanI;
import com.tasktk.app.entity.Team;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.annotations.UpdateTimestamp;

@Path("/team")
public class TeamRestApi  extends BaseRestApi{

    @EJB
    private TeamBeanI teamBean;

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Team team){
        teamBean.addOrUpdate(team);
        return respond();
    }

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(){
        return respond(teamBean.list(new Team()));
    }

    @Path("/delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id){
        teamBean.delete(Team.class, id);
        return respond();
    }

}
