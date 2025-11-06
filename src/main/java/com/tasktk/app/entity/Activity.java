package com.tasktk.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
@DynamicInsert
@DynamicUpdate
public class Activity extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @NotNull
    @Column(name = "time_stamp", nullable = false)
    private LocalDateTime timeStamp;

    @NotNull
    @Column(name = "details", nullable = false, length = 1000)
    private String details;

    // Constructors
    public Activity() {
        this.timeStamp = LocalDateTime.now();
    }

    public Activity(User user, Task task, Type type, String details) {
        this();
        this.user = user;
        this.task = task;
        this.type = type;
        this.details = details;
    }

    // Regular Getters and Setters
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public LocalDateTime getTimeStamp() { return timeStamp; }
    public void setTimeStamp(LocalDateTime timeStamp) { this.timeStamp = timeStamp; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public enum Type {
        CREATED, UPDATED, COMPLETED, DELETED
    }
}