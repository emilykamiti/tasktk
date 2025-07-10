package com.tasktk.app.bean;

import com.tasktk.app.bean.beanI.TaskBeanI;
import com.tasktk.app.entity.Activity;
import com.tasktk.app.entity.Task;
import com.tasktk.app.utility.EncryptText;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class TaskBean extends GenericBean<Task> implements TaskBeanI {
    private static final Logger LOGGER = Logger.getLogger(TaskBean.class.getName());


    @Override
    public Task addOrUpdate(Task task)  {
        LOGGER.info("create task: " + task.getTitle());
        return getDao().addOrUpdate(task);
    }

    public Task findById(Long taskId) {
        return getDao().findById(Task.class, taskId);
    }

    public List<Task>  list() {
        LOGGER.info("Retrieving all activities");
        return getDao().list(new Task());
    }

    //update task
    @Override
    public boolean updateTask(Task task) throws SQLException {
        Task existingTask = getDao().findById(Task.class, task.getId());
        if(existingTask ==null){
            LOGGER.info("Task with ID" + task.getId() + "not found for update");
            return false;
        }
        if(task.getTitle() !=null && task.getTitle().equals(existingTask.getTitle())){
            throw new RuntimeException(("Task with name" + task.getTitle()
                    + "already exists"));
        }
        existingTask.setTitle(task.getTitle());

        LOGGER.info("Updating task:" + existingTask.getTitle());
        getDao().addOrUpdate(existingTask);

        return true;
    }


    public boolean delete(Task task) {
        if (task == null || task.getId()== null){
            throw  new IllegalArgumentException(("Task and task ID are required for deletion"));
        }
        try{
            LOGGER.info("Deleting task with ID:" + task.getId());
            getDao().delete(Task.class, task.getId());
            return true;
        }catch (EntityNotFoundException e){
            LOGGER.info("Task with ID" + task.getId() + "not found deletion");
            return false;
        }
    }
}
