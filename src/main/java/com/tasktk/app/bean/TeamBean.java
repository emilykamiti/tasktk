package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.TeamBeanI;
import com.tasktk.app.entity.Team;
import com.tasktk.app.entity.User;
import com.tasktk.app.entity.UserTeam;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Stateless
public class TeamBean extends GenericBean<Team> implements TeamBeanI {
    private static final Logger LOGGER = Logger.getLogger(TeamBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Override
    public Team addOrUpdate(Team team) {
        validateTeam(team);

        if (team.getId() == null && doesTeamExistByName(team.getName())) {
            throw new RuntimeException("Team with name " + team.getName() + " already exists");
        }

        LOGGER.info("Creating/Updating team: " + team.getName());
        return getDao().addOrUpdate(team);
    }

    @Override
    public Team findById(Long teamId) {
        if (teamId == null) {
            throw new IllegalArgumentException("Team ID cannot be null");
        }
        Team team = getDao().findById(Team.class, teamId);
        if (team == null) {
            throw new EntityNotFoundException("Team with ID " + teamId + " not found");
        }
        return team;
    }

    @Override
    public Team findById(Class<Team> entity, Long id) {
        return findById(id);
    }

    @Override
    public List<Team> list() {
        LOGGER.info("Retrieving all teams");
        return getDao().list(new Team());
    }

    @Override
    public boolean update(Long id, Team teamUpdate) {
        Team existingTeam = findById(id);

        if (teamUpdate.getName() != null && !teamUpdate.getName().equals(existingTeam.getName())) {
            validateTeamNameUniqueness(teamUpdate.getName(), id);
        }

        updateTeamFields(existingTeam, teamUpdate);
        getDao().addOrUpdate(existingTeam);
        return true;
    }

    @Override
    public boolean delete(Team team) {
        if (team == null || team.getId() == null) {
            throw new IllegalArgumentException("Team and team ID are required for deletion");
        }

        // Check if team has tasks before deletion
        if (hasAssociatedTasks(team.getId())) {
            throw new RuntimeException("Cannot delete team with associated tasks");
        }

        try {
            LOGGER.info("Deleting team with ID: " + team.getId());
            getDao().delete(Team.class, team.getId());
            return true;
        } catch (EntityNotFoundException e) {
            LOGGER.warning("Team with ID " + team.getId() + " not found for deletion");
            return false;
        }
    }

    // Implement the missing methods:
    @Override
    public List<User> getTeamMembers(Long teamId) {
        Team team = findById(teamId);
        return team.getUserTeams().stream()
                .map(UserTeam::getUser)
                .collect(Collectors.toList());
    }

    @Override
    public Team findByName(String name) {
        TypedQuery<Team> query = em.createQuery("SELECT t FROM Team t WHERE t.name = :name", Team.class)
                .setParameter("name", name);
        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public boolean addMemberToTeam(Long teamId, Long userId) {
        try {
            Team team = findById(teamId);
            User user = em.find(User.class, userId);

            if (user == null) {
                throw new EntityNotFoundException("User with ID " + userId + " not found");
            }

            // Check if user is already in team
            boolean alreadyMember = team.getUserTeams().stream()
                    .anyMatch(ut -> ut.getUser().getId().equals(userId));

            if (!alreadyMember) {
                UserTeam userTeam = new UserTeam(user, team);
                em.persist(userTeam);
            }

            return true;
        } catch (Exception e) {
            LOGGER.warning("Error adding member to team: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean removeMemberFromTeam(Long teamId, Long userId) {
        try {
            TypedQuery<UserTeam> query = em.createQuery(
                    "SELECT ut FROM UserTeam ut WHERE ut.team.id = :teamId AND ut.user.id = :userId",
                    UserTeam.class
            );
            query.setParameter("teamId", teamId);
            query.setParameter("userId", userId);

            UserTeam userTeam = query.getResultStream().findFirst().orElse(null);
            if (userTeam != null) {
                em.remove(userTeam);
                return true;
            }
            return false;
        } catch (Exception e) {
            LOGGER.warning("Error removing member from team: " + e.getMessage());
            return false;
        }
    }

    // Business logic methods
    private void validateTeam(Team team) {
        if (team.getName() == null || team.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Team name is required");
        }
        if (team.getDescription() == null || team.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Team description is required");
        }
    }

    private void validateTeamNameUniqueness(String name, Long excludeTeamId) {
        if (doesTeamExistByName(name)) {
            Team existingTeam = findTeamByName(name);
            if (!existingTeam.getId().equals(excludeTeamId)) {
                throw new RuntimeException("Team with name " + name + " already exists");
            }
        }
    }

    private void updateTeamFields(Team existingTeam, Team teamUpdate) {
        if (teamUpdate.getName() != null) existingTeam.setName(teamUpdate.getName());
        if (teamUpdate.getDescription() != null) existingTeam.setDescription(teamUpdate.getDescription());
    }

    private boolean doesTeamExistByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(t) FROM Team t WHERE t.name = :name", Long.class)
                .setParameter("name", name);
        return query.getSingleResult() > 0;
    }

    private Team findTeamByName(String name) {
        TypedQuery<Team> query = em.createQuery("SELECT t FROM Team t WHERE t.name = :name", Team.class)
                .setParameter("name", name);
        return query.getResultStream().findFirst().orElse(null);
    }

    private boolean hasAssociatedTasks(Long teamId) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(t) FROM Task t WHERE t.team.id = :teamId", Long.class)
                .setParameter("teamId", teamId);
        return query.getSingleResult() > 0;
    }
}