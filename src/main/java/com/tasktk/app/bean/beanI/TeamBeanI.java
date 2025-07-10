package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.Team;
import com.tasktk.app.entity.User;

import java.sql.SQLException;

public interface TeamBeanI extends GenericBeanI<Team>{

    boolean updateTeam(Team team) throws SQLException;
}
