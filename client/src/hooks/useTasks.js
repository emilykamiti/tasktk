import { useState, useEffect } from 'react';
import api from '../services/api';

export const useTasks = () => {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Fetch all tasks
  const fetchTasks = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await api.tasks.getAll();
      setTasks(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  // Create task
  const createTask = async (taskData) => {
    setError(null);
    try {
      const newTask = await api.tasks.create(taskData);
      setTasks(prev => [...prev, newTask]);
      return newTask;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  // Update task
  const updateTask = async (id, taskData) => {
    setError(null);
    try {
      const updatedTask = await api.tasks.update(id, taskData);
      setTasks(prev => prev.map(task =>
        task.id === id ? updatedTask : task
      ));
      return updatedTask;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  // Delete task
  const deleteTask = async (id) => {
    setError(null);
    try {
      await api.tasks.delete(id);
      setTasks(prev => prev.filter(task => task.id !== id));
      return true;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  // Update task status
  const updateTaskStatus = async (id, status) => {
    setError(null);
    try {
      const updatedTask = await api.tasks.updateStatus(id, status);
      setTasks(prev => prev.map(task =>
        task.id === id ? updatedTask : task
      ));
      return updatedTask;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  // Toggle meeting status
  const toggleMeeting = async (id, hasMeeting) => {
    setError(null);
    try {
      const updatedTask = await api.tasks.toggleMeeting(id, hasMeeting);
      setTasks(prev => prev.map(task =>
        task.id === id ? updatedTask : task
      ));
      return updatedTask;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  // Update progress
  const updateProgress = async (id, progress) => {
    setError(null);
    try {
      const updatedTask = await api.tasks.updateProgress(id, progress);
      setTasks(prev => prev.map(task =>
        task.id === id ? updatedTask : task
      ));
      return updatedTask;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  // Get tasks by team
  const getTasksByTeam = async (teamId) => {
    setLoading(true);
    setError(null);
    try {
      const data = await api.tasks.getByTeam(teamId);
      return data;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  return {
    tasks,
    loading,
    error,
    createTask,
    updateTask,
    deleteTask,
    updateTaskStatus,
    toggleMeeting,
    updateProgress,
    getTasksByTeam,
    refetch: fetchTasks
  };
};