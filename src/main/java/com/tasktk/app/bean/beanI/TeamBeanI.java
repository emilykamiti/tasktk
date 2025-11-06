package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.Team;
import com.tasktk.app.entity.User;
import java.util.List;

public interface TeamBeanI extends GenericBeanI<Team> {

    Team addOrUpdate(Team team);
    Team findById(Long teamId);
    List<Team> list();
    boolean update(Long id, Team teamUpdate);
    boolean delete(Team team);

    // Additional business methods
    List<User> getTeamMembers(Long teamId);
    Team findByName(String name);
    boolean addMemberToTeam(Long teamId, Long userId);
    boolean removeMemberFromTeam(Long teamId, Long userId);

    // Method to match GenericBeanI signature
    Team findById(Class<Team> entity, Long id);
}