package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.UserTeam;
import java.sql.SQLException;
import java.util.List;

public interface UserTeamBeanI extends GenericBeanI<UserTeam> {

    UserTeam addOrUpdate(UserTeam userTeam);
    UserTeam findById(Long userTeamId);
    List<UserTeam> list();
    boolean update(Long id, UserTeam userTeamUpdate);
    boolean delete(UserTeam userTeam);

    // Additional business methods
    boolean updateUserTeam(UserTeam userTeam) throws SQLException;
    boolean deleteUserTeam(UserTeam userTeam);

    // Method to match GenericBeanI signature
    UserTeam findById(Class<UserTeam> entity, Long id);
}