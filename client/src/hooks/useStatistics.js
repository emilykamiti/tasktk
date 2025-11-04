// src/hooks/useStatistics.js
import { useState, useEffect } from 'react';
import api from '../services/api';

export const useStatistics = () => {
  const [overview, setOverview] = useState(null);
  const [teamStats, setTeamStats] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchOverview = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await api.statistics.getOverview();
      setOverview(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const fetchTeamStats = async (teamId) => {
    try {
      setError(null);
      const data = await api.statistics.getTeamStats(teamId);
      setTeamStats(prev => ({ ...prev, [teamId]: data }));
      return data;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  useEffect(() => {
    fetchOverview();
  }, []);

  return {
    overview,
    teamStats,
    loading,
    error,
    fetchOverview,
    fetchTeamStats,
  };
};