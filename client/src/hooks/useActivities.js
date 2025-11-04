import { useState, useEffect } from 'react';
import api from '../services/api';

export const useActivities = () => {
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchActivities = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await api.activities.getAll();
      // Sort by timestamp descending (newest first)
      const sortedActivities = data.sort((a, b) =>
        new Date(b.timeStamp) - new Date(a.timeStamp)
      );
      setActivities(sortedActivities);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const deleteActivity = async (id) => {
    setError(null);
    try {
      await api.activities.delete(id);
      setActivities(prev => prev.filter(activity => activity.id !== id));
      return true;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  useEffect(() => {
    fetchActivities();
  }, []);

  return {
    activities,
    loading,
    error,
    deleteActivity,
    refetch: fetchActivities
  };
};