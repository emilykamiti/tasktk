import { useState, useEffect } from 'react';
import api from '../services/api';

export const useMessages = () => {
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchMessages = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await api.messages.getAll();
      setMessages(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const createMessage = async (messageData) => {
    setError(null);
    try {
      const newMessage = await api.messages.create(messageData);
      setMessages(prev => [newMessage, ...prev]);
      return newMessage;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  const deleteMessage = async (id) => {
    setError(null);
    try {
      await api.messages.delete(id);
      setMessages(prev => prev.filter(message => message.id !== id));
      return true;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  const markAsRead = async (id) => {
    setError(null);
    try {
      await api.messages.markAsRead(id);
      setMessages(prev => prev.map(message =>
        message.id === id ? { ...message, isRead: true } : message
      ));
      return true;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  const markAllAsRead = async (teamId, userId) => {
    setError(null);
    try {
      await api.messages.markAllAsRead(teamId, userId);
      // Refetch messages to get updated read status
      await fetchMessages();
      return true;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  useEffect(() => {
    fetchMessages();
  }, []);

  return {
    messages,
    loading,
    error,
    createMessage,
    deleteMessage,
    markAsRead,
    markAllAsRead,
    refetch: fetchMessages
  };
};