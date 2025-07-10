package com.tasktk.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Entity
@Table(name = "messages")
@DynamicInsert
@DynamicUpdate
public class Message extends BaseEntity{

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @NotNull
    @Column(name = "content", nullable = false, unique = true)
    private String content;

    @NotNull
    @Column(name = "timeStand", nullable = false, unique = true)
    private Date timeStamp;

    @NotNull
    @Column(name = "isRead", nullable = false, unique = true)
    private Boolean isRead;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public @NotNull Boolean getRead() {
        return isRead;
    }

    public void setRead(@NotNull Boolean read) {
        isRead = read;
    }
}
