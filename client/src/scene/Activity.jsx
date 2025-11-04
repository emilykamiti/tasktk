import React, { useState, useEffect } from 'react';
import { useActivities } from '../hooks/useActivities';
import { useTasks } from '../hooks/useTasks';
import { useUsers } from '../hooks/useUsers';
import {
  MagnifyingGlassIcon,
  FunnelIcon,
  ClockIcon,
  UserIcon,
  DocumentTextIcon,
  CheckCircleIcon,
  PencilIcon,
  TrashIcon,
  PlusIcon
} from '@heroicons/react/24/outline';

export default function Activity() {
  const {
    activities,
    loading: activitiesLoading,
    error: activitiesError,
    deleteActivity
  } = useActivities();

  const {
    tasks,
    loading: tasksLoading,
    error: tasksError
  } = useTasks();

  const {
    users,
    loading: usersLoading,
    error: usersError
  } = useUsers();

  const [searchTerm, setSearchTerm] = useState('');
  const [selectedType, setSelectedType] = useState('all');
  const [selectedUser, setSelectedUser] = useState('all');
  const [selectedTask, setSelectedTask] = useState('all');

  const error = activitiesError || tasksError || usersError;
  const loading = activitiesLoading || tasksLoading || usersLoading;

  // Filter activities
  const filteredActivities = activities.filter(activity => {
    const matchesSearch = activity.details?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         activity.user?.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         activity.task?.title?.toLowerCase().includes(searchTerm.toLowerCase());

    const matchesType = selectedType === 'all' || activity.type === selectedType.toUpperCase();
    const matchesUser = selectedUser === 'all' || activity.user?.id?.toString() === selectedUser;
    const matchesTask = selectedTask === 'all' || activity.task?.id?.toString() === selectedTask;

    return matchesSearch && matchesType && matchesUser && matchesTask;
  });

  const getActivityIcon = (type) => {
    switch (type) {
      case 'CREATED':
        return <PlusIcon className="w-4 h-4 text-green-600" />;
      case 'UPDATED':
        return <PencilIcon className="w-4 h-4 text-blue-600" />;
      case 'COMPLETED':
        return <CheckCircleIcon className="w-4 h-4 text-purple-600" />;
      case 'DELETED':
        return <TrashIcon className="w-4 h-4 text-red-600" />;
      default:
        return <DocumentTextIcon className="w-4 h-4 text-gray-600" />;
    }
  };

  const getActivityColor = (type) => {
    switch (type) {
      case 'CREATED':
        return 'bg-green-100 text-green-800';
      case 'UPDATED':
        return 'bg-blue-100 text-blue-800';
      case 'COMPLETED':
        return 'bg-purple-100 text-purple-800';
      case 'DELETED':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const formatTime = (timestamp) => {
    if (!timestamp) return '';
    const date = new Date(timestamp);
    const now = new Date();
    const diffInMinutes = (now - date) / (1000 * 60);
    const diffInHours = diffInMinutes / 60;
    const diffInDays = diffInHours / 24;

    if (diffInMinutes < 1) {
      return 'Just now';
    } else if (diffInMinutes < 60) {
      return `${Math.floor(diffInMinutes)} minutes ago`;
    } else if (diffInHours < 24) {
      return `${Math.floor(diffInHours)} hours ago`;
    } else if (diffInDays < 7) {
      return `${Math.floor(diffInDays)} days ago`;
    } else {
      return date.toLocaleDateString();
    }
  };

  const handleDeleteActivity = async (activityId) => {
    if (window.confirm('Are you sure you want to delete this activity?')) {
      try {
        await deleteActivity(activityId);
      } catch (err) {
        console.error('Failed to delete activity:', err);
      }
    }
  };

  if (loading) {
    return (
      <div className="p-6 flex justify-center items-center h-64">
        <div className="text-lg text-gray-600">Loading activities...</div>
      </div>
    );
  }

  return (
    <div className="p-6 space-y-6">
      {/* Error Display */}
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
          {error}
        </div>
      )}

      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Activity Log</h1>
          <p className="text-gray-600">Track all team activities and changes</p>
        </div>
      </div>

      {/* Filters */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4">
        {/* Search */}
        <div className="lg:col-span-2 relative">
          <MagnifyingGlassIcon className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
          <input
            type="text"
            placeholder="Search activities..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-600 focus:border-transparent"
          />
        </div>

        {/* Activity Type Filter */}
        <div className="relative">
          <select
            value={selectedType}
            onChange={(e) => setSelectedType(e.target.value)}
            className="w-full appearance-none bg-white border border-gray-300 rounded-lg px-4 py-2 pr-8 focus:outline-none focus:ring-2 focus:ring-purple-600 focus:border-transparent"
          >
            <option value="all">All Types</option>
            <option value="created">Created</option>
            <option value="updated">Updated</option>
            <option value="completed">Completed</option>
            <option value="deleted">Deleted</option>
          </select>
          <FunnelIcon className="absolute right-2 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-400 pointer-events-none" />
        </div>

        {/* User Filter */}
        <div className="relative">
          <select
            value={selectedUser}
            onChange={(e) => setSelectedUser(e.target.value)}
            className="w-full appearance-none bg-white border border-gray-300 rounded-lg px-4 py-2 pr-8 focus:outline-none focus:ring-2 focus:ring-purple-600 focus:border-transparent"
          >
            <option value="all">All Users</option>
            {users.map(user => (
              <option key={user.id} value={user.id}>
                {user.name}
              </option>
            ))}
          </select>
          <UserIcon className="absolute right-2 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-400 pointer-events-none" />
        </div>

        {/* Task Filter */}
        <div className="relative">
          <select
            value={selectedTask}
            onChange={(e) => setSelectedTask(e.target.value)}
            className="w-full appearance-none bg-white border border-gray-300 rounded-lg px-4 py-2 pr-8 focus:outline-none focus:ring-2 focus:ring-purple-600 focus:border-transparent"
          >
            <option value="all">All Tasks</option>
            {tasks.map(task => (
              <option key={task.id} value={task.id}>
                {task.title}
              </option>
            ))}
          </select>
          <DocumentTextIcon className="absolute right-2 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-400 pointer-events-none" />
        </div>
      </div>

      {/* Activities List */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200">
        <div className="p-6 space-y-4">
          {filteredActivities.length === 0 ? (
            <div className="text-center py-12 text-gray-500">
              No activities found matching your filters
            </div>
          ) : (
            filteredActivities.map((activity) => (
              <div
                key={activity.id}
                className="flex items-start space-x-4 p-4 rounded-lg border border-gray-100 hover:bg-gray-50 transition-colors"
              >
                {/* Activity Icon */}
                <div className="flex-shrink-0">
                  <div className="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center">
                    {getActivityIcon(activity.type)}
                  </div>
                </div>

                {/* Activity Content */}
                <div className="flex-1 min-w-0">
                  <div className="flex items-center space-x-2 mb-1">
                    <span className={`px-2 py-1 rounded-full text-xs font-medium ${getActivityColor(activity.type)}`}>
                      {activity.type?.toLowerCase()}
                    </span>
                    <span className="text-sm text-gray-500">
                      {formatTime(activity.timeStamp)}
                    </span>
                  </div>

                  <p className="text-gray-900 mb-2">
                    <span className="font-medium">{activity.user?.name || 'Unknown User'}</span>
                    {' '}{activity.details}
                  </p>

                  {activity.task && (
                    <div className="flex items-center space-x-2 text-sm text-gray-600">
                      <DocumentTextIcon className="w-4 h-4" />
                      <span className="font-medium">Task:</span>
                      <span>{activity.task.title}</span>
                    </div>
                  )}
                </div>

                {/* Actions */}
                <div className="flex-shrink-0">
                  <button
                    onClick={() => handleDeleteActivity(activity.id)}
                    className="p-1 text-gray-400 hover:text-red-600 hover:bg-gray-100 rounded transition-colors"
                    title="Delete activity"
                  >
                    <TrashIcon className="w-4 h-4" />
                  </button>
                </div>
              </div>
            ))
          )}
        </div>
      </div>

      {/* Stats Summary */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div className="bg-white p-4 rounded-lg shadow-sm border border-gray-200">
          <div className="flex items-center space-x-3">
            <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
              <PlusIcon className="w-6 h-6 text-green-600" />
            </div>
            <div>
              <p className="text-2xl font-bold text-gray-900">
                {activities.filter(a => a.type === 'CREATED').length}
              </p>
              <p className="text-sm text-gray-600">Created</p>
            </div>
          </div>
        </div>

        <div className="bg-white p-4 rounded-lg shadow-sm border border-gray-200">
          <div className="flex items-center space-x-3">
            <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
              <PencilIcon className="w-6 h-6 text-blue-600" />
            </div>
            <div>
              <p className="text-2xl font-bold text-gray-900">
                {activities.filter(a => a.type === 'UPDATED').length}
              </p>
              <p className="text-sm text-gray-600">Updated</p>
            </div>
          </div>
        </div>

        <div className="bg-white p-4 rounded-lg shadow-sm border border-gray-200">
          <div className="flex items-center space-x-3">
            <div className="w-12 h-12 bg-purple-100 rounded-full flex items-center justify-center">
              <CheckCircleIcon className="w-6 h-6 text-purple-600" />
            </div>
            <div>
              <p className="text-2xl font-bold text-gray-900">
                {activities.filter(a => a.type === 'COMPLETED').length}
              </p>
              <p className="text-sm text-gray-600">Completed</p>
            </div>
          </div>
        </div>

        <div className="bg-white p-4 rounded-lg shadow-sm border border-gray-200">
          <div className="flex items-center space-x-3">
            <div className="w-12 h-12 bg-red-100 rounded-full flex items-center justify-center">
              <TrashIcon className="w-6 h-6 text-red-600" />
            </div>
            <div>
              <p className="text-2xl font-bold text-gray-900">
                {activities.filter(a => a.type === 'DELETED').length}
              </p>
              <p className="text-sm text-gray-600">Deleted</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}