// src/hooks/index.js
import { useApi } from './useApi';

// Generic entities
export const useTasks = (autoLoad = true) => useApi('tasks', autoLoad);
export const useUsers = (autoLoad = true) => useApi('users', autoLoad);
export const useTeams = (autoLoad = true) => useApi('teams', autoLoad);
export const useMessages = (autoLoad = true) => useApi('messages', autoLoad);
export const useActivities = (autoLoad = true) => useApi('activities', autoLoad);
export const useComments = (autoLoad = true) => useApi('comments', autoLoad);

// Specialized hooks with custom methods
export { useTasks } from './useTasks'; // Your existing task hook with custom methods
export { useStatistics } from './useStatistics';
export { useDashboard } from './useDashboard';
export { useNotifications } from './useNotifications';