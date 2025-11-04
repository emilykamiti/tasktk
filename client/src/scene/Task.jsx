// src/scenes/Task.jsx
import React, { useState } from 'react';
import { useTasks } from '../hooks/useTasks';
import { useTeams } from '../hooks/useTeams';
import { useUsers } from '../hooks/useUsers';
import TaskForm from '../components/TaskForm';
import {
  MagnifyingGlassIcon,
  FunnelIcon,
  PlusIcon,
  ClockIcon,
  UserGroupIcon,
  ExclamationTriangleIcon,
  ClipboardDocumentListIcon,
  CheckCircleIcon,
  CalendarIcon,
  EllipsisVerticalIcon,
  TrashIcon,
  PencilIcon
} from '@heroicons/react/24/outline';
import {
  ExclamationTriangleIcon as ExclamationTriangleSolid,
  CheckCircleIcon as CheckCircleSolid
} from '@heroicons/react/24/solid';

export default function Task() {
  const {
    tasks,
    loading: tasksLoading,
    error: tasksError,
    createTask,
    updateTask,
    updateTaskStatus,
    toggleMeeting,
    updateProgress,
    deleteTask
  } = useTasks();

  const {
    teams,
    loading: teamsLoading,
    error: teamsError
  } = useTeams();

  const {
    users,
    loading: usersLoading,
    error: usersError
  } = useUsers();

  const [searchTerm, setSearchTerm] = useState('');
  const [selectedFilter, setSelectedFilter] = useState('all');
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingTask, setEditingTask] = useState(null);

  // Combine loading states and errors
  const error = tasksError || teamsError || usersError;
  const loading = tasksLoading || teamsLoading || usersLoading;

  // Event handlers
  const handleCreateTask = async (taskData) => {
    try {
      await createTask(taskData);
      setIsFormOpen(false);
    } catch (err) {
      console.error('Failed to create task:', err);
    }
  };

  const handleEditTask = async (taskData) => {
    try {
      await updateTask(editingTask.id, taskData);
      setEditingTask(null);
    } catch (err) {
      console.error('Failed to update task:', err);
    }
  };

  const handleStatusChange = async (taskId, newStatus) => {
    try {
      await updateTaskStatus(taskId, newStatus);
    } catch (err) {
      console.error('Failed to update status:', err);
    }
  };

  const handleMeetingToggle = async (taskId, hasMeeting) => {
    try {
      await toggleMeeting(taskId, hasMeeting);
    } catch (err) {
      console.error('Failed to toggle meeting:', err);
    }
  };

  const handleProgressUpdate = async (taskId, progress) => {
    try {
      await updateProgress(taskId, progress);
    } catch (err) {
      console.error('Failed to update progress:', err);
    }
  };

  const handleDeleteTask = async (taskId) => {
    if (window.confirm('Are you sure you want to delete this task?')) {
      try {
        await deleteTask(taskId);
      } catch (err) {
        console.error('Failed to delete task:', err);
      }
    }
  };

  const handleCompleteTask = async (taskId) => {
    try {
      await updateTaskStatus(taskId, 'COMPLETED');
    } catch (err) {
      console.error('Failed to complete task:', err);
    }
  };

  const openEditForm = (task) => {
    setEditingTask({
      ...task,
      team: { id: task.team?.id?.toString() || '' },
      assignedUsers: task.assignedUsers?.map(user => user.id.toString()) || []
    });
  };

  // UI helper functions (presentation only)
  const getPriorityColor = (priority) => {
    switch (priority) {
      case 'HIGH': return 'bg-red-100 text-red-800 border-red-200';
      case 'MEDIUM': return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      case 'LOW': return 'bg-green-100 text-green-800 border-green-200';
      default: return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  const getPriorityIcon = (priority) => {
    switch (priority) {
      case 'HIGH': return <ExclamationTriangleSolid className="w-4 h-4" />;
      case 'MEDIUM': return <ExclamationTriangleIcon className="w-4 h-4" />;
      case 'LOW': return <CheckCircleIcon className="w-4 h-4" />;
      default: return <ClockIcon className="w-4 h-4" />;
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'COMPLETED': return 'bg-green-500';
      case 'IN_PROGRESS': return 'bg-blue-500';
      case 'TODO': return 'bg-gray-400';
      default: return 'bg-gray-400';
    }
  };

  const getStatusText = (status) => {
    switch (status) {
      case 'COMPLETED': return 'Completed';
      case 'IN_PROGRESS': return 'In Progress';
      case 'TODO': return 'To Do';
      default: return 'To Do';
    }
  };

  // Simple frontend filtering (UI only)
  const filteredTasks = tasks.filter(task => {
    const matchesSearch = task.title?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         task.description?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         task.tags?.some(tag => tag.toLowerCase().includes(searchTerm.toLowerCase()));

    const matchesFilter = selectedFilter === 'all' ||
                         task.status === selectedFilter.toUpperCase() ||
                         task.priority === selectedFilter.toUpperCase();

    return matchesSearch && matchesFilter;
  });

  if (loading) {
    return (
      <div className="p-6 flex justify-center items-center h-64">
        <div className="text-lg text-gray-600">Loading tasks from backend...</div>
      </div>
    );
  }

  return (
    <div className="p-6 space-y-6">
      {/* Error Display */}
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
          {error}
          <button
            onClick={() => window.location.reload()}
            className="float-right font-bold"
          >
            ×
          </button>
        </div>
      )}

      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Tasks</h1>
          <p className="text-gray-600">Manage and track your team's tasks</p>
        </div>
        <button
          onClick={() => setIsFormOpen(true)}
          className="bg-[#1D0E3D] text-white px-4 py-2.5 rounded-lg flex items-center space-x-2 hover:bg-purple-800 transition-colors"
        >
          <PlusIcon className="w-5 h-5" />
          <span>New Task</span>
        </button>
      </div>

      {/* Search and Filter Bar */}
      <div className="flex items-center space-x-4">
        <div className="flex-1 relative max-w-md">
          <MagnifyingGlassIcon className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
          <input
            type="text"
            placeholder="Search tasks by title, description, or tags..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full pl-10 pr-4 py-2.5 text-sm border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-600 focus:border-transparent"
          />
        </div>

        <div className="relative">
          <select
            value={selectedFilter}
            onChange={(e) => setSelectedFilter(e.target.value)}
            className="appearance-none bg-white border border-gray-300 rounded-lg px-4 py-2.5 pr-8 focus:outline-none focus:ring-2 focus:ring-purple-600 focus:border-transparent"
          >
            <option value="all">All Tasks</option>
            <option value="todo">To Do</option>
            <option value="in_progress">In Progress</option>
            <option value="completed">Completed</option>
            <option value="high">High Priority</option>
            <option value="medium">Medium Priority</option>
            <option value="low">Low Priority</option>
          </select>
          <FunnelIcon className="absolute right-2 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-400 pointer-events-none" />
        </div>
      </div>

      {/* Tasks Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredTasks.map((task) => (
          <div key={task.id} className="bg-white rounded-xl shadow-sm border border-gray-200 p-5 hover:shadow-md transition-shadow">
            {/* Task Header */}
            <div className="flex items-start justify-between mb-3">
              <div className="flex items-center space-x-2">
                <div className={`w-3 h-3 rounded-full ${getStatusColor(task.status)}`}></div>
                <h3 className="font-semibold text-gray-900">{task.title}</h3>
              </div>
              <div className="flex space-x-1">
                <button
                  onClick={() => openEditForm(task)}
                  className="p-1 text-gray-400 hover:text-blue-600 hover:bg-gray-100 rounded"
                  title="Edit Task"
                >
                  <PencilIcon className="w-4 h-4" />
                </button>
                <button
                  onClick={() => handleCompleteTask(task.id)}
                  className="p-1 text-gray-400 hover:text-green-600 hover:bg-gray-100 rounded"
                  title="Complete Task"
                >
                  <CheckCircleIcon className="w-4 h-4" />
                </button>
                <button
                  onClick={() => handleDeleteTask(task.id)}
                  className="p-1 text-gray-400 hover:text-red-600 hover:bg-gray-100 rounded"
                  title="Delete Task"
                >
                  <TrashIcon className="w-4 h-4" />
                </button>
              </div>
            </div>

            {/* Status Badge */}
            <div className="flex items-center space-x-2 mb-3">
              <span className={`px-2 py-1 rounded-full text-xs font-medium ${
                task.status === 'COMPLETED' ? 'bg-green-100 text-green-800' :
                task.status === 'IN_PROGRESS' ? 'bg-blue-100 text-blue-800' :
                'bg-gray-100 text-gray-800'
              }`}>
                {getStatusText(task.status)}
              </span>
              {task.progressPercentage > 0 && (
                <span className="text-xs text-gray-500">{task.progressPercentage}% complete</span>
              )}
            </div>

            {/* Description */}
            <p className="text-gray-600 text-sm mb-4 leading-relaxed">{task.description}</p>

            {/* Progress Bar */}
            {task.progressPercentage > 0 && (
              <div className="w-full bg-gray-200 rounded-full h-2 mb-4">
                <div
                  className={`h-2 rounded-full ${
                    task.priority === 'HIGH' ? 'bg-red-500' :
                    task.priority === 'MEDIUM' ? 'bg-yellow-500' :
                    'bg-green-500'
                  }`}
                  style={{ width: `${task.progressPercentage}%` }}
                ></div>
              </div>
            )}

            {/* Tags */}
            <div className="flex flex-wrap gap-1 mb-4">
              {task.tags?.map((tag, index) => (
                <span key={index} className="px-2 py-1 bg-gray-100 text-gray-700 text-xs rounded-md">
                  {tag}
                </span>
              ))}
            </div>

            {/* Team Section */}
            <div className="mb-4 p-3 bg-gray-50 rounded-lg">
              <div className="flex items-center space-x-2 mb-2">
                <UserGroupIcon className="w-4 h-4 text-gray-600" />
                <span className="text-sm font-medium text-gray-700">Team Members</span>
              </div>
              <div className="flex flex-wrap gap-2">
                {task.assignedUsers?.map((user, index) => (
                  <div
                    key={index}
                    className="flex items-center space-x-2 bg-white px-3 py-2 rounded-lg border border-gray-200"
                    title={`${user.firstName} ${user.lastName} - ${user.role}`}
                  >
                    <div className="w-8 h-8 bg-[#6B46C1] rounded-full flex items-center justify-center">
                      <span className="text-xs font-bold text-white">
                        {user.firstName?.charAt(0)}{user.lastName?.charAt(0)}
                      </span>
                    </div>
                    <div className="flex flex-col">
                      <span className="text-sm font-medium text-gray-900">
                        {user.firstName} {user.lastName}
                      </span>
                      <span className="text-xs text-gray-500">{user.role}</span>
                    </div>
                  </div>
                ))}
                {(!task.assignedUsers || task.assignedUsers.length === 0) && (
                  <span className="text-sm text-gray-500">No team members assigned</span>
                )}
              </div>
            </div>

            {/* Priority and Due Date */}
            <div className="flex items-center justify-between mb-4">
              <div className={`flex items-center space-x-1 px-2 py-1 rounded-full border ${getPriorityColor(task.priority)}`}>
                {getPriorityIcon(task.priority)}
                <span className="text-xs font-medium capitalize">{task.priority?.toLowerCase()} Priority</span>
              </div>
              <div className="flex items-center space-x-1 text-gray-500">
                <ClockIcon className="w-4 h-4" />
                <span className="text-sm">
                  {task.dueDate ? new Date(task.dueDate).toLocaleDateString() : 'No due date'}
                </span>
              </div>
            </div>

            {/* Meeting Section */}
            <div className="flex items-center justify-between pt-4 border-t border-gray-100">
              <div className="flex items-center space-x-3">
                <div className="flex items-center space-x-2">
                  <CalendarIcon className="w-4 h-4 text-gray-400" />
                  <label className="relative inline-flex items-center cursor-pointer">
                    <input
                      type="checkbox"
                      checked={task.hasMeeting || false}
                      onChange={(e) => handleMeetingToggle(task.id, e.target.checked)}
                      className="sr-only peer"
                    />
                    <div className="w-9 h-5 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-4 after:w-4 after:transition-all peer-checked:bg-[#1D0E3D]"></div>
                  </label>
                  <span className="text-sm text-gray-600">
                    {task.hasMeeting ? 'Meeting scheduled' : 'No meeting'}
                  </span>
                </div>
              </div>

              {/* Progress Update Buttons */}
              <div className="flex space-x-1">
                {[25, 50, 75, 100].map(progress => (
                  <button
                    key={progress}
                    onClick={() => handleProgressUpdate(task.id, progress)}
                    className={`px-2 py-1 text-xs rounded ${
                      task.progressPercentage >= progress
                        ? 'bg-green-100 text-green-800'
                        : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
                    }`}
                    title={`Set progress to ${progress}%`}
                  >
                    {progress}%
                  </button>
                ))}
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Empty State */}
      {filteredTasks.length === 0 && !loading && (
        <div className="text-center py-12">
          <div className="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <ClipboardDocumentListIcon className="w-8 h-8 text-gray-400" />
          </div>
          <h3 className="text-lg font-medium text-gray-900 mb-2">
            {tasks.length === 0 ? 'No tasks created yet' : 'No tasks found'}
          </h3>
          <p className="text-gray-600 mb-4">
            {tasks.length === 0 ? 'Create your first task to get started' : 'Try adjusting your search or filter'}
          </p>
          <button
            onClick={() => setIsFormOpen(true)}
            className="bg-[#1D0E3D] text-white px-4 py-2 rounded-lg hover:bg-purple-800 transition-colors"
          >
            Create New Task
          </button>
        </div>
      )}

      {/* Task Forms */}
      <TaskForm
        isOpen={isFormOpen}
        onClose={() => setIsFormOpen(false)}
        onSubmit={handleCreateTask}
        teams={teams}
        users={users}
      />

      <TaskForm
        isOpen={!!editingTask}
        onClose={() => setEditingTask(null)}
        onSubmit={handleEditTask}
        teams={teams}
        users={users}
        initialData={editingTask}
        isEdit={true}
      />
    </div>
  );
}