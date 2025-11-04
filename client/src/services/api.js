// src/services/api.js
const BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/tasktk/app/api/rest';

const handleResponse = async (response) => {
  if (!response.ok) {
    const error = await response.text();
    throw new Error(error);
  }
  return response.json();
};

// Generic CRUD operations for any entity
const createEntityApi = (endpoint) => ({
  getAll: () => fetch(`${BASE_URL}/${endpoint}`).then(handleResponse),
  getById: (id) => fetch(`${BASE_URL}/${endpoint}/${id}`).then(handleResponse),
  create: (data) => fetch(`${BASE_URL}/${endpoint}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  }).then(handleResponse),
  update: (id, data) => fetch(`${BASE_URL}/${endpoint}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  }).then(handleResponse),
  delete: (id) => fetch(`${BASE_URL}/${endpoint}/${id}`, {
    method: 'DELETE',
  }).then(handleResponse),
});

const api = {
  // --- Tasks ---
  tasks: {
    ...createEntityApi('tasks'),

    // Task-specific endpoints from your TaskRestApi
    getByTeam: (teamId) => fetch(`${BASE_URL}/tasks/team/${teamId}`).then(handleResponse),
    getByStatus: (status) => fetch(`${BASE_URL}/tasks/status/${status}`).then(handleResponse),
    getByPriority: (priority) => fetch(`${BASE_URL}/tasks/priority/${priority}`).then(handleResponse),
    updateStatus: (id, status) => fetch(`${BASE_URL}/tasks/${id}/status?status=${status}`, {
      method: 'PATCH',
    }).then(handleResponse),
    toggleMeeting: (id, hasMeeting) => fetch(`${BASE_URL}/tasks/${id}/meeting?hasMeeting=${hasMeeting}`, {
      method: 'PATCH',
    }).then(handleResponse),
    updateProgress: (id, progress) => fetch(`${BASE_URL}/tasks/${id}/progress?progress=${progress}`, {
      method: 'PATCH',
    }).then(handleResponse),
  },

  // --- Users ---
  users: {
    ...createEntityApi('users'),
    // Add user-specific endpoints as needed
    getByTeam: (teamId) => fetch(`${BASE_URL}/users/team/${teamId}`).then(handleResponse),
  },

  // --- Teams ---
  teams: {
    ...createEntityApi('teams'),
    // Add team-specific endpoints as needed
    getMembers: (teamId) => fetch(`${BASE_URL}/teams/${teamId}/members`).then(handleResponse),
  },

  // --- Messages ---
  messages: {
    ...createEntityApi('messages'),
    // Add message-specific endpoints as needed
    getByUser: (userId) => fetch(`${BASE_URL}/messages/user/${userId}`).then(handleResponse),
    getByChannel: (channelId) => fetch(`${BASE_URL}/messages/channel/${channelId}`).then(handleResponse),
    markAsRead: (id) => fetch(`${BASE_URL}/messages/${id}/read`, {
      method: 'PATCH',
    }).then(handleResponse),
  },

  // --- Activities ---
  activities: {
    ...createEntityApi('activities'),
    // Add activity-specific endpoints as needed
    getByUser: (userId) => fetch(`${BASE_URL}/activities/user/${userId}`).then(handleResponse),
    getRecent: () => fetch(`${BASE_URL}/activities/recent`).then(handleResponse),
  },

  // --- Statistics ---
  statistics: {
    // Statistics endpoints (usually GET only)
    getOverview: () => fetch(`${BASE_URL}/statistics/overview`).then(handleResponse),
    getTeamStats: (teamId) => fetch(`${BASE_URL}/statistics/team/${teamId}`).then(handleResponse),
    getUserStats: (userId) => fetch(`${BASE_URL}/statistics/user/${userId}`).then(handleResponse),
    getTaskStats: () => fetch(`${BASE_URL}/statistics/tasks`).then(handleResponse),
  },

  // --- Dashboard ---
  dashboard: {
    getData: () => fetch(`${BASE_URL}/dashboard`).then(handleResponse),
    getTeamDashboard: (teamId) => fetch(`${BASE_URL}/dashboard/team/${teamId}`).then(handleResponse),
    getUserDashboard: (userId) => fetch(`${BASE_URL}/dashboard/user/${userId}`).then(handleResponse),
  },

  // --- Settings ---
  settings: {
    getUserSettings: () => fetch(`${BASE_URL}/settings/user`).then(handleResponse),
    updateUserSettings: (settings) => fetch(`${BASE_URL}/settings/user`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(settings),
    }).then(handleResponse),
    getAppSettings: () => fetch(`${BASE_URL}/settings/app`).then(handleResponse),
  },

  // --- Notifications ---
  notifications: {
    getAll: () => fetch(`${BASE_URL}/notifications`).then(handleResponse),
    markAsRead: (id) => fetch(`${BASE_URL}/notifications/${id}/read`, {
      method: 'PATCH',
    }).then(handleResponse),
    markAllAsRead: () => fetch(`${BASE_URL}/notifications/read-all`, {
      method: 'PATCH',
    }).then(handleResponse),
    getUnreadCount: () => fetch(`${BASE_URL}/notifications/unread/count`).then(handleResponse),
  },

  // --- Files/Attachments ---
  files: {
    upload: (formData) => fetch(`${BASE_URL}/files/upload`, {
      method: 'POST',
      body: formData, // FormData with file
    }).then(handleResponse),
    download: (fileId) => fetch(`${BASE_URL}/files/${fileId}/download`).then(handleResponse),
    delete: (fileId) => fetch(`${BASE_URL}/files/${fileId}`, {
      method: 'DELETE',
    }).then(handleResponse),
    getByTask: (taskId) => fetch(`${BASE_URL}/files/task/${taskId}`).then(handleResponse),
  },

  // --- Comments ---
  comments: {
    ...createEntityApi('comments'),
    getByTask: (taskId) => fetch(`${BASE_URL}/comments/task/${taskId}`).then(handleResponse),
    getByUser: (userId) => fetch(`${BASE_URL}/comments/user/${userId}`).then(handleResponse),
  },

  // --- Auth (if you add authentication later) ---
  auth: {
    login: (credentials) => fetch(`${BASE_URL}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(credentials),
    }).then(handleResponse),
    register: (userData) => fetch(`${BASE_URL}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(userData),
    }).then(handleResponse),
    logout: () => fetch(`${BASE_URL}/auth/logout`, {
      method: 'POST',
    }).then(handleResponse),
    getCurrentUser: () => fetch(`${BASE_URL}/auth/me`).then(handleResponse),
    refreshToken: () => fetch(`${BASE_URL}/auth/refresh`, {
      method: 'POST',
    }).then(handleResponse),
  }
};

export default api;