import { useState, useEffect } from 'react';
import api from '../services/api';

export const useTeams = () => {
  const [teams, setTeams] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Fetch all teams
  const fetchTeams = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await api.teams.getAll();
      setTeams(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  // Create team
  const createTeam = async (teamData) => {
    setError(null);
    try {
      const newTeam = await api.teams.create(teamData);
      setTeams(prev => [...prev, newTeam]);
      return newTeam;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  // Update team
  const updateTeam = async (id, teamData) => {
    setError(null);
    try {
      const updatedTeam = await api.teams.update(id, teamData);
      setTeams(prev => prev.map(team =>
        team.id === id ? updatedTeam : team
      ));
      return updatedTeam;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  // Delete team
  const deleteTeam = async (id) => {
    setError(null);
    try {
      await api.teams.delete(id);
      setTeams(prev => prev.filter(team => team.id !== id));
      return true;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  // Get team members
  const getTeamMembers = async (teamId) => {
    setLoading(true);
    setError(null);
    try {
      const members = await api.teams.getMembers(teamId);
      return members;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTeams();
  }, []);

  return {
    teams,
    loading,
    error,
    createTeam,
    updateTeam,
    deleteTeam,
    getTeamMembers,
    refetch: fetchTeams
  };
};