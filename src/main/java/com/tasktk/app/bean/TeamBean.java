package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.TeamBeanI;
import com.tasktk.app.entity.Task;
import com.tasktk.app.entity.Team;
import com.tasktk.app.entity.User;
import com.tasktk.app.utility.EncryptText;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class TeamBean extends GenericBean<Team> implements TeamBeanI {
    private static final Logger LOGGER = Logger.getLogger(TeamBean.class.getName());

    @Inject
    private EncryptText encryptText;

    @PersistenceContext
    private EntityManager em;

    //create team
    @Override
    public Team createTeam(Team team) throws SQLException {
        if(doesTeamExistByName(team.getName()) ){
            throw new RuntimeException("Team with name" + team.getName() + "already exists");
        }
        LOGGER.info("create team: " + team.getName());
        getDao().addOrUpdate(team);

        return team;
    }

    //find team by id
    @Override
    public Team findById(Long teamId) {
        return em.find(Team.class, teamId);
    }

    //getAll
    public List<Team> listAll() {
        LOGGER.info("Retrieving all tasks");
        TypedQuery<Team> query = em.createQuery("SELECT m FROM Team t", Team.class);
        return query.getResultList();
    }

    //update team
    @Override
    public boolean updateTeam(Team team) throws SQLException {

        Team existingTeam = em.find(Team.class, team.getId());
        if(existingTeam ==null){
            LOGGER.info("Team with ID" + team.getId() + "not found for update");
            return false;
        }
        if(team.getName() !=null && team.getName().equals(existingTeam.getName())){
            throw new RuntimeException(("Team with name" + team.getName()
            + "already exists"));
        }
        existingTeam.setName(team.getName());
        existingTeam.setDescription(team.getDescription());

        LOGGER.info("Updating team:" + existingTeam.getName());
        getDao().addOrUpdate(existingTeam);

        return true;
    }


    //delete team
    @Override
    public boolean deleteTeam(Team team) {
        if (team == null || team.getId()== null){
            throw  new IllegalArgumentException(("Team and team ID are required for deletion"));
        }
        try{
            LOGGER.info("Deleting team with ID:" + team.getId());
            getDao().delete(Team.class, team.getId());
            return true;
        }catch (EntityNotFoundException e){
            LOGGER.info("Team with ID" + team.getId() + "not found deletion");
            return false;
        }
    }

    private boolean doesTeamExistByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(t) FROM) Team t WHERE t.name = :name", Long.class)
                .setParameter("name", name);
        return query.getSingleResult() >0;
    }
}
