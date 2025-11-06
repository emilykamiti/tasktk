package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.TaskBeanI;
import com.tasktk.app.entity.Task;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.logging.Logger;


@Stateless
public class TaskBean extends GenericBean<Task> implements TaskBeanI {
    private static final Logger LOGGER = Logger.getLogger(TaskBean.class.getName());

    @Override
    public Task addOrUpdate(Task task) {
        LOGGER.info("Creating/Updating task: " + task.getTitle());

        // Validate required fields
        validateTask(task);

        // Set timestamps
        if (task.getId() == null) {
            task.setCreatedAt(java.time.LocalDateTime.now());
        }
        task.setUpdatedAt(java.time.LocalDateTime.now());

        return getDao().addOrUpdate(task);
    }

    @Override
    public Task findById(Long taskId) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task ID cannot be null");
        }
        Task task = getDao().findById(Task.class, taskId);
        if (task == null) {
            throw new EntityNotFoundException("Task with ID " + taskId + " not found");
        }
        return task;
    }

    @Override
    public List<Task> list() {
        LOGGER.info("Retrieving all tasks");
        return getDao().list(new Task());
    }

    @Override
    public boolean update(Long id, Task taskUpdate) {
        if (id == null) {
            throw new IllegalArgumentException("Task ID cannot be null for update");
        }

        Task existingTask = findById(id);

        // Validate title uniqueness if changed
        if (taskUpdate.getTitle() != null && !taskUpdate.getTitle().equals(existingTask.getTitle())) {
            validateTitleUniqueness(taskUpdate.getTitle(), id);
        }

        // Update fields if provided
        updateTaskFields(existingTask, taskUpdate);
        existingTask.setUpdatedAt(java.time.LocalDateTime.now());

        getDao().addOrUpdate(existingTask);
        return true;
    }
    @Override
    public Task findById(Class<Task> entity, Long id) {
        return findById(id); // Delegate to the other method
    }
    @Override
    public boolean delete(Task task) {
        if (task == null || task.getId() == null) {
            throw new IllegalArgumentException("Task and task ID are required for deletion");
        }

        try {
            LOGGER.info("Deleting task with ID: " + task.getId());
            getDao().delete(Task.class, task.getId());
            return true;
        } catch (EntityNotFoundException e) {
            LOGGER.warning("Task with ID " + task.getId() + " not found for deletion");
            return false;
        }
    }

    @Override
    public Task updateTaskStatus(Long taskId, Task.Status status) {
        Task task = findById(taskId);
        task.setStatus(status);

        // Update progress based on status
        if (status == Task.Status.COMPLETED) {
            task.setProgressPercentage(100);
        } else if (status == Task.Status.IN_PROGRESS && task.getProgressPercentage() == 0) {
            task.setProgressPercentage(25); // Start with 25% when moving to in-progress
        }

        task.setUpdatedAt(java.time.LocalDateTime.now());
        return getDao().addOrUpdate(task);
    }

    @Override
    public Task toggleMeetingStatus(Long taskId, Boolean hasMeeting) {
        Task task = findById(taskId);
        task.setHasMeeting(hasMeeting);
        task.setUpdatedAt(java.time.LocalDateTime.now());
        return getDao().addOrUpdate(task);
    }

    @Override
    public Task updateProgressPercentage(Long taskId, Integer progressPercentage) {
        if (progressPercentage < 0 || progressPercentage > 100) {
            throw new IllegalArgumentException("Progress percentage must be between 0 and 100");
        }

        Task task = findById(taskId);
        task.setProgressPercentage(progressPercentage);

        // Auto-update status based on progress
        if (progressPercentage == 100) {
            task.setStatus(Task.Status.COMPLETED);
        } else if (progressPercentage > 0 && task.getStatus() == Task.Status.TODO) {
            task.setStatus(Task.Status.IN_PROGRESS);
        }

        task.setUpdatedAt(java.time.LocalDateTime.now());
        return getDao().addOrUpdate(task);
    }

    @Override
    public List<Task> findByTeam(Long teamId) {
        if (teamId == null) {
            throw new IllegalArgumentException("Team ID cannot be null");
        }
        return getDao().findByProperty(Task.class, "team.id", teamId);
    }

    @Override
    public List<Task> findByStatus(Task.Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        return getDao().findByProperty(Task.class, "status", status);
    }

    @Override
    public List<Task> findByPriority(Task.Priority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }
        return getDao().findByProperty(Task.class, "priority", priority);
    }

    // Private helper methods for business logic
    private void validateTask(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title is required");
        }
        if (task.getDescription() == null || task.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Task description is required");
        }
        if (task.getTeam() == null) {
            throw new IllegalArgumentException("Task must be assigned to a team");
        }
        if (task.getDueDate() == null) {
            throw new IllegalArgumentException("Due date is required");
        }

        // Validate due date is not in the past
        if (task.getDueDate().before(new java.util.Date())) {
            throw new IllegalArgumentException("Due date cannot be in the past");
        }
    }

    private void validateTitleUniqueness(String title, Long excludeTaskId) {
        List<Task> tasksWithSameName = getDao().findByProperty(Task.class, "title", title);
        if (!tasksWithSameName.isEmpty() && !tasksWithSameName.get(0).getId().equals(excludeTaskId)) {
            throw new RuntimeException("Task with title '" + title + "' already exists");
        }
    }

    private void updateTaskFields(Task existingTask, Task taskUpdate) {
        if (taskUpdate.getTitle() != null) existingTask.setTitle(taskUpdate.getTitle());
        if (taskUpdate.getDescription() != null) existingTask.setDescription(taskUpdate.getDescription());
        if (taskUpdate.getStatus() != null) existingTask.setStatus(taskUpdate.getStatus());
        if (taskUpdate.getPriority() != null) existingTask.setPriority(taskUpdate.getPriority());
        if (taskUpdate.getDueDate() != null) existingTask.setDueDate(taskUpdate.getDueDate());
        if (taskUpdate.getAssignee() != null) existingTask.setAssignee(taskUpdate.getAssignee());
        if (taskUpdate.getTeam() != null) existingTask.setTeam(taskUpdate.getTeam());
        if (taskUpdate.getHasMeeting() != null) existingTask.setHasMeeting(taskUpdate.getHasMeeting());
        if (taskUpdate.getProgressPercentage() != null) existingTask.setProgressPercentage(taskUpdate.getProgressPercentage());
        if (taskUpdate.getTags() != null) existingTask.setTags(taskUpdate.getTags());
    }
}