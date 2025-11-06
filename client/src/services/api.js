// src/services/api.js
const BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/tasktk/rest';

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
  // --- Tasks --- (using /task endpoint)
  tasks: {
    getAll: () => fetch(`${BASE_URL}/task`).then(handleResponse),
    getById: (id) => fetch(`${BASE_URL}/task/${id}`).then(handleResponse),
    create: (data) => fetch(`${BASE_URL}/task`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    }).then(handleResponse),
    update: (id, data) => fetch(`${BASE_URL}/task/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    }).then(handleResponse),
    delete: (id) => fetch(`${BASE_URL}/task/${id}`, {
      method: 'DELETE',
    }).then(handleResponse),

    // Task-specific endpoints
    getByTeam: (teamId) => fetch(`${BASE_URL}/task/team/${teamId}`).then(handleResponse),
    getByStatus: (status) => fetch(`${BASE_URL}/task/status/${status}`).then(handleResponse),
    getByPriority: (priority) => fetch(`${BASE_URL}/task/priority/${priority}`).then(handleResponse),
    updateStatus: (id, status) => fetch(`${BASE_URL}/task/${id}/status?status=${status}`, {
      method: 'PATCH',
    }).then(handleResponse),
    toggleMeeting: (id, hasMeeting) => fetch(`${BASE_URL}/task/${id}/meeting?hasMeeting=${hasMeeting}`, {
      method: 'PATCH',
    }).then(handleResponse),
    updateProgress: (id, progress) => fetch(`${BASE_URL}/task/${id}/progress?progress=${progress}`, {
      method: 'PATCH',
    }).then(handleResponse),
  },

  // --- Users --- (using /user endpoint)
  users: {
    getAll: () => fetch(`${BASE_URL}/user`).then(handleResponse),
    getById: (id) => fetch(`${BASE_URL}/user/${id}`).then(handleResponse),
    register: (data) => fetch(`${BASE_URL}/user/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    }).then(handleResponse),
    update: (id, data) => fetch(`${BASE_URL}/user/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    }).then(handleResponse),
    delete: (id) => fetch(`${BASE_URL}/user/${id}`, {
      method: 'DELETE',
    }).then(handleResponse),
    changePassword: (data) => fetch(`${BASE_URL}/user/change-password`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    }).then(handleResponse),
    getByTeam: (teamId) => fetch(`${BASE_URL}/user/team/${teamId}`).then(handleResponse),
  },

  // --- Teams --- (using /team endpoint)
  teams: {
    getAll: () => fetch(`${BASE_URL}/team`).then(handleResponse),
    getById: (id) => fetch(`${BASE_URL}/team/${id}`).then(handleResponse),
    create: (data) => fetch(`${BASE_URL}/team`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    }).then(handleResponse),
    update: (id, data) => fetch(`${BASE_URL}/team/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    }).then(handleResponse),
    delete: (id) => fetch(`${BASE_URL}/team/${id}`, {
      method: 'DELETE',
    }).then(handleResponse),

    // Team-specific endpoints
    getMembers: (teamId) => fetch(`${BASE_URL}/team/${teamId}/members`).then(handleResponse),
    getByName: (name) => fetch(`${BASE_URL}/team/name/${name}`).then(handleResponse),
    addMember: (teamId, userId) => fetch(`${BASE_URL}/team/${teamId}/add-member/${userId}`, {
      method: 'POST',
    }).then(handleResponse),
    removeMember: (teamId, userId) => fetch(`${BASE_URL}/team/${teamId}/remove-member/${userId}`, {
      method: 'DELETE',
    }).then(handleResponse),
  },

  // --- Messages --- (using /message endpoint)
  messages: {
    getAll: () => fetch(`${BASE_URL}/message`).then(handleResponse),
    getById: (id) => fetch(`${BASE_URL}/message/${id}`).then(handleResponse),
    create: (data) => fetch(`${BASE_URL}/message`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    }).then(handleResponse),
    update: (id, data) => fetch(`${BASE_URL}/message/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    }).then(handleResponse),
    delete: (id) => fetch(`${BASE_URL}/message/${id}`, {
      method: 'DELETE',
    }).then(handleResponse),

    // Message-specific endpoints
    getByTeam: (teamId) => fetch(`${BASE_URL}/message/team/${teamId}`).then(handleResponse),
    getBySender: (senderId) => fetch(`${BASE_URL}/message/sender/${senderId}`).then(handleResponse),
    getUnread: (userId) => fetch(`${BASE_URL}/message/unread/${userId}`).then(handleResponse),
    markAsRead: (id) => fetch(`${BASE_URL}/message/${id}/read`, {
      method: 'PATCH',
    }).then(handleResponse),
    markAllAsRead: (teamId, userId) => fetch(`${BASE_URL}/message/team/${teamId}/read-all/${userId}`, {
      method: 'PATCH',
    }).then(handleResponse),
  },

  // --- Activities --- (using /activity endpoint)
  activities: {
    getAll: () => fetch(`${BASE_URL}/activity`).then(handleResponse),
    getById: (id) => fetch(`${BASE_URL}/activity/${id}`).then(handleResponse),
    create: (data) => fetch(`${BASE_URL}/activity`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    }).then(handleResponse),
    update: (id, data) => fetch(`${BASE_URL}/activity/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    }).then(handleResponse),
    delete: (id) => fetch(`${BASE_URL}/activity/${id}`, {
      method: 'DELETE',
    }).then(handleResponse),

    // Activity-specific endpoints
    getByUser: (userId) => fetch(`${BASE_URL}/activity/user/${userId}`).then(handleResponse),
    getByTask: (taskId) => fetch(`${BASE_URL}/activity/task/${taskId}`).then(handleResponse),
    getByType: (type) => fetch(`${BASE_URL}/activity/type/${type}`).then(handleResponse),
    getRecent: (limit) => fetch(`${BASE_URL}/activity/recent/${limit}`).then(handleResponse),
  },

  // --- Statistics ---
  statistics: {
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
      body: formData,
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

  // --- Auth ---
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