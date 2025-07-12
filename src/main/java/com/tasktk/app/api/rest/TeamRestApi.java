package com.tasktk.app.api.rest;

import com.tasktk.app.bean.beanI.GenericBeanI;
import com.tasktk.app.bean.beanI.TeamBeanI;
import com.tasktk.app.entity.Team;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;


@Path("/team")
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
}