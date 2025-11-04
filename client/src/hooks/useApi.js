// src/hooks/useApi.js
import { useState, useEffect } from 'react';
import api from '../services/api';

export const useApi = (entity, autoLoad = true) => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(autoLoad);
  const [error, setError] = useState(null);

  const entityApi = api[entity];

  const fetchData = async () => {
    try {
      setLoading(true);
      setError(null);
      const result = await entityApi.getAll();
      setData(result);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const create = async (itemData) => {
    try {
      setError(null);
      const newItem = await entityApi.create(itemData);
      setData(prev => [...prev, newItem]);
      return newItem;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  const update = async (id, itemData) => {
    try {
      setError(null);
      const updatedItem = await entityApi.update(id, itemData);
      setData(prev => prev.map(item => item.id === id ? updatedItem : item));
      return updatedItem;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  const remove = async (id) => {
    try {
      setError(null);
      await entityApi.delete(id);
      setData(prev => prev.filter(item => item.id !== id));
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  const getById = async (id) => {
    try {
      setError(null);
      return await entityApi.getById(id);
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  useEffect(() => {
    if (autoLoad) {
      fetchData();
    }
  }, [entity, autoLoad]);

  return {
    data,
    loading,
    error,
    fetchData,
    create,
    update,
    remove,
    getById,
    setData,
    setError,
  };
};