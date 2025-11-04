package com.tasktk.app.bean.beanI;

import com.tasktk.app.entity.Task;
import java.util.List;

public interface TaskBeanI {
    Task addOrUpdate(Task task);
    Task findById(Long taskId);
    List<Task> list();
    boolean update(Long id, Task taskUpdate);
    boolean delete(Task task);
    Task updateTaskStatus(Long taskId, Task.Status status);
    Task toggleMeetingStatus(Long taskId, Boolean hasMeeting);
    Task updateProgressPercentage(Long taskId, Integer progressPercentage);
    List<Task> findByTeam(Long teamId);
    List<Task> findByStatus(Task.Status status);
    List<Task> findByPriority(Task.Priority priority);
}