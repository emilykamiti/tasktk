package com.tasktk.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "user_teams")
@DynamicInsert
@DynamicUpdate
public class UserTeam extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role_in_team", nullable = false)
    private RoleInTeam roleInTeam;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public RoleInTeam getRoleInTeam() {
        return roleInTeam;
    }

    public void setRoleInTeam(RoleInTeam roleInTeam) {
        this.roleInTeam = roleInTeam;
    }

    public enum RoleInTeam {
        ADMIN,
        MANAGER,
        MEMBER
    }
}