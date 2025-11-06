// services/api.js
const BASE_URL = 'http://localhost:8080/tasktk/rest';

// Helper function for API calls
const handleResponse = async (response) => {
  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }

  const result = await response.json();

  if (!result.success) {
    throw new Error(result.message || 'Request failed');
  }

  return result.data || result;
};

const api = {
  // Auth endpoints
  auth: {
    login: async (credentials) => {
      const response = await fetch(`${BASE_URL}/auth/login`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(credentials),
      });

      return handleResponse(response);
    },

    register: async (userData) => {
      const response = await fetch(`${BASE_URL}/auth/register`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
      });

      return handleResponse(response);
    },

    logout: async () => {
      const response = await fetch(`${BASE_URL}/auth/logout`, {
        method: 'POST',
        credentials: 'include',
      });

      return handleResponse(response);
    },

    check: async () => {
      const response = await fetch(`${BASE_URL}/auth/check`, {
        method: 'GET',
        credentials: 'include',
      });

      return handleResponse(response);
    },
  },

  // User endpoints
  users: {
    getAll: async () => {
      const response = await fetch(`${BASE_URL}/user`, {
        method: 'GET',
        credentials: 'include',
      });

      const result = await handleResponse(response);
      return Array.isArray(result) ? result : [];
    },

    getById: async (id) => {
      const response = await fetch(`${BASE_URL}/user/${id}`, {
        method: 'GET',
        credentials: 'include',
      });

      return handleResponse(response);
    },

    create: async (userData) => {
      const response = await fetch(`${BASE_URL}/user`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
      });

      return handleResponse(response);
    },

    update: async (id, userData) => {
      const response = await fetch(`${BASE_URL}/user/${id}`, {
        method: 'PUT',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
      });

      return handleResponse(response);
    },

    delete: async (id) => {
      const response = await fetch(`${BASE_URL}/user/${id}`, {
        method: 'DELETE',
        credentials: 'include',
      });

      return handleResponse(response);
    },

    getByTeam: async (teamId) => {
      const response = await fetch(`${BASE_URL}/user/team/${teamId}`, {
        method: 'GET',
        credentials: 'include',
      });

      const result = await handleResponse(response);
      return Array.isArray(result) ? result : [];
    },
  },

  // Team endpoints
  teams: {
    getAll: async () => {
      const response = await fetch(`${BASE_URL}/team`, {
        method: 'GET',
        credentials: 'include',
      });

      const result = await handleResponse(response);
      return Array.isArray(result) ? result : [];
    },

    getById: async (id) => {
      const response = await fetch(`${BASE_URL}/team/${id}`, {
        method: 'GET',
        credentials: 'include',
      });

      return handleResponse(response);
    },

    create: async (teamData) => {
      const response = await fetch(`${BASE_URL}/team`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(teamData),
      });

      return handleResponse(response);
    },

    update: async (id, teamData) => {
      const response = await fetch(`${BASE_URL}/team/${id}`, {
        method: 'PUT',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(teamData),
      });

      return handleResponse(response);
    },

    delete: async (id) => {
      const response = await fetch(`${BASE_URL}/team/${id}`, {
        method: 'DELETE',
        credentials: 'include',
      });

      return handleResponse(response);
    },

    addUser: async (teamId, userData) => {
      const response = await fetch(`${BASE_URL}/team/${teamId}/users`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
      });

      return handleResponse(response);
    },

    removeUser: async (teamId, userId) => {
      const response = await fetch(`${BASE_URL}/team/${teamId}/users/${userId}`, {
        method: 'DELETE',
        credentials: 'include',
      });

      return handleResponse(response);
    },
  },

  // Task endpoints
  tasks: {
    getAll: async () => {
      const response = await fetch(`${BASE_URL}/task`, {
        method: 'GET',
        credentials: 'include',
      });

      const result = await handleResponse(response);
      return Array.isArray(result) ? result : [];
    },

    getById: async (id) => {
      const response = await fetch(`${BASE_URL}/task/${id}`, {
        method: 'GET',
        credentials: 'include',
      });

      return handleResponse(response);
    },

    create: async (taskData) => {
      const response = await fetch(`${BASE_URL}/task`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskData),
      });

      return handleResponse(response);
    },

    update: async (id, taskData) => {
      const response = await fetch(`${BASE_URL}/task/${id}`, {
        method: 'PUT',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskData),
      });

      return handleResponse(response);
    },

    delete: async (id) => {
      const response = await fetch(`${BASE_URL}/task/${id}`, {
        method: 'DELETE',
        credentials: 'include',
      });

      return handleResponse(response);
    },

    getByTeam: async (teamId) => {
      const response = await fetch(`${BASE_URL}/task/team/${teamId}`, {
        method: 'GET',
        credentials: 'include',
      });

      const result = await handleResponse(response);
      return Array.isArray(result) ? result : [];
    },

    assignUsers: async (taskId, userIds) => {
      const response = await fetch(`${BASE_URL}/task/${taskId}/assign`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ userIds }),
      });

      return handleResponse(response);
    },
  },

  // Activity endpoints
  activities: {
    getAll: async () => {
      const response = await fetch(`${BASE_URL}/activity`, {
        method: 'GET',
        credentials: 'include',
      });

      const result = await handleResponse(response);
      return Array.isArray(result) ? result : [];
    },

    getByTask: async (taskId) => {
      const response = await fetch(`${BASE_URL}/activity/task/${taskId}`, {
        method: 'GET',
        credentials: 'include',
      });

      const result = await handleResponse(response);
      return Array.isArray(result) ? result : [];
    },

    getByUser: async (userId) => {
      const response = await fetch(`${BASE_URL}/activity/user/${userId}`, {
        method: 'GET',
        credentials: 'include',
      });

      const result = await handleResponse(response);
      return Array.isArray(result) ? result : [];
    },

    create: async (activityData) => {
      const response = await fetch(`${BASE_URL}/activity`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(activityData),
      });

      return handleResponse(response);
    },
  },

  // Message endpoints
  messages: {
    getByTeam: async (teamId) => {
      const response = await fetch(`${BASE_URL}/message/team/${teamId}`, {
        method: 'GET',
        credentials: 'include',
      });

      const result = await handleResponse(response);
      return Array.isArray(result) ? result : [];
    },

    create: async (messageData) => {
      const response = await fetch(`${BASE_URL}/message`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(messageData),
      });

      return handleResponse(response);
    },

    markAsRead: async (messageId) => {
      const response = await fetch(`${BASE_URL}/message/${messageId}/read`, {
        method: 'PUT',
        credentials: 'include',
      });

      return handleResponse(response);
    },
  },
};

export default api;