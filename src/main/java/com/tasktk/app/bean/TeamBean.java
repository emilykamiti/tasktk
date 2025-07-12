package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.TeamBeanI;
import com.tasktk.app.entity.Team;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class TeamBean extends GenericBean<Team> implements TeamBeanI {
    private static final Logger LOGGER = Logger.getLogger(TeamBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Override
    public Team addOrUpdate(Team team) {
        if (doesTeamExistByName(team.getName())) {
            throw new RuntimeException("Team with name " + team.getName() + " already exists");
        }
        LOGGER.info("create team: " + team.getName());
        return getDao().addOrUpdate(team);
    }

    @Override
    public boolean update(Long id, Team teamUpdate) {
        if (teamUpdate.getName() != null) {
            Team existingTeam = getDao().findById(Team.class, id);
            if (existingTeam != null && doesTeamExistByName(teamUpdate.getName()) && !id.equals(existingTeam.getId())) {
                throw new RuntimeException("Team with name " + teamUpdate.getName() + " already exists");
            }
        }
        return getDao().update(id, teamUpdate);
    }

    public Team findById(Long teamId) {
        if (teamId == null) {
            throw new IllegalArgumentException("Team ID cannot be null");
        }
            return getDao().findById(Team.class, teamId);
    }


    public List<Team> list() {
        LOGGER.info("Retrieving all teams");
        return getDao().list(new Team());
    }


    public boolean delete(Team team) {
        if (team == null || team.getId() == null) {
            throw new IllegalArgumentException("Team and team ID are required for deletion");
        }
        try {
            LOGGER.info("Deleting team with ID: " + team.getId());
            getDao().delete(Team.class, team.getId());
            return true;
        } catch (EntityNotFoundException e) {
            LOGGER.info("Team with ID " + team.getId() + " not found for deletion");
            return false;
        }
    }

    private boolean doesTeamExistByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(t) FROM Team t WHERE t.name = :name", Long.class)
                .setParameter("name", name);
        return query.getSingleResult() > 0;
    }
}