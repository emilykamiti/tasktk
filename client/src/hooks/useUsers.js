import { useState, useEffect } from 'react';
import api from '../services/api';

export const useUsers = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Fetch all users
  const fetchUsers = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await api.users.getAll();
      setUsers(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  // Register user (create)
  const registerUser = async (userData) => {
    setError(null);
    try {
      // Use auth register if available, otherwise use users create
      const newUser = await api.auth.register
        ? await api.auth.register(userData)
        : await api.users.create(userData);

      setUsers(prev => [...prev, newUser]);
      return newUser;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  // Update user
  const updateUser = async (id, userData) => {
    setError(null);
    try {
      const updatedUser = await api.users.update(id, userData);
      setUsers(prev => prev.map(user =>
        user.id === id ? updatedUser : user
      ));
      return updatedUser;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  // Delete user
  const deleteUser = async (id) => {
    setError(null);
    try {
      await api.users.delete(id);
      setUsers(prev => prev.filter(user => user.id !== id));
      return true;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  // Get users by team
  const getUsersByTeam = async (teamId) => {
    setLoading(true);
    setError(null);
    try {
      const teamUsers = await api.users.getByTeam(teamId);
      return teamUsers;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  return {
    users,
    loading,
    error,
    registerUser,
    updateUser,
    deleteUser,
    getUsersByTeam,
    refetch: fetchUsers
  };
};