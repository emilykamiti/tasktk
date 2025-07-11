package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.Team;
import java.sql.SQLException;

public interface TeamBeanI extends GenericBeanI<Team> {
    boolean updateTeam(Long id, Team teamUpdate) ;
    Team findById(Long id); // Add this
}