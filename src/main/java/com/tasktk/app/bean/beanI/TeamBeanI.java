package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.Team;
import com.tasktk.app.entity.User;

import java.sql.SQLException;

public interface TeamBeanI extends GenericBeanI<Team>{
    Team createTeam (Team team) throws SQLException;

    boolean updateTeam(Team team) throws SQLException;

    boolean deleteTeam(Team team);

    Team findById(Long teamId);
}
