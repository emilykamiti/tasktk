package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.UserBeanI;
import com.tasktk.app.bean.beanI.UserTeamBeanI;
import com.tasktk.app.entity.UserTeam;

import java.sql.SQLException;

public class UserTeamBean extends GenericBean<UserTeam> implements UserTeamBeanI {

    @Override
    public boolean updateUserTeam(UserTeam userTeam) throws SQLException {
        return false;
    }

    @Override
    public boolean deleteUserTeam(UserTeam userTeam) {
        return false;
    }

    @Override
    public UserTeam findById(Long userTeamId) {
        return null;
    }
}
