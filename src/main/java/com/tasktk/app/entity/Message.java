package com.tasktk.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@DynamicInsert
@DynamicUpdate
public class Message extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @NotNull
    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @NotNull
    @Column(name = "time_stamp", nullable = false)
    private LocalDateTime timeStamp;

    @NotNull
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    // Constructors
    public Message() {
        this.timeStamp = LocalDateTime.now();
        this.isRead = false;
    }

    public Message(Team team, User sender, String content) {
        this();
        this.team = team;
        this.sender = sender;
        this.content = content;
    }

    // Getters and Setters
    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }

    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getTimeStamp() { return timeStamp; }
    public void setTimeStamp(LocalDateTime timeStamp) { this.timeStamp = timeStamp; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
}