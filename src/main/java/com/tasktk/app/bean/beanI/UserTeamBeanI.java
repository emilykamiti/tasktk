package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.UserTeam;

import java.sql.SQLException;

public interface UserTeamBeanI extends GenericBeanI<UserTeam>{

    boolean updateUserTeam(UserTeam userTeam) throws SQLException;

    boolean deleteUserTeam(UserTeam userTeam);

    UserTeam findById(Long userTeamId);
}
