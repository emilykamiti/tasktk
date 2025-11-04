// src/scenes/Team.jsx
import React, { useState, useEffect } from 'react';
import { useTeams } from '../hooks/useTeams';
import { useUsers } from '../hooks/useUsers';
import {
  PlusIcon,
  PencilIcon,
  TrashIcon,
  UserGroupIcon,
  MagnifyingGlassIcon
} from '@heroicons/react/24/outline';

export default function Team() {
  const { teams, loading, error, createTeam, updateTeam, deleteTeam } = useTeams();
  const { users } = useUsers();
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingTeam, setEditingTeam] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [formData, setFormData] = useState({
    name: '',
    description: ''
  });

  useEffect(() => {
    if (editingTeam) {
      setFormData({
        name: editingTeam.name || '',
        description: editingTeam.description || ''
      });
    } else {
      setFormData({ name: '', description: '' });
    }
  }, [editingTeam]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingTeam) {
        await updateTeam(editingTeam.id, formData);
        setEditingTeam(null);
      } else {
        await createTeam(formData);
      }
      setIsFormOpen(false);
      setFormData({ name: '', description: '' });
    } catch (err) {
      console.error('Failed to save team:', err);
    }
  };

  const handleDelete = async (teamId) => {
    if (window.confirm('Are you sure you want to delete this team?')) {
      try {
        await deleteTeam(teamId);
      } catch (err) {
        console.error('Failed to delete team:', err);
      }
    }
  };

  const filteredTeams = teams.filter(team =>
    team.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    team.description?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) {
    return (
      <div className="p-6 flex justify-center items-center h-64">
        <div className="text-lg text-gray-600">Loading teams...</div>
      </div>
    );
  }

  return (
    <div className="p-6 space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Teams</h1>
          <p className="text-gray-600">Manage your teams and members</p>
        </div>
        <button
          onClick={() => setIsFormOpen(true)}
          className="bg-[#1D0E3D] text-white px-4 py-2.5 rounded-lg flex items-center space-x-2 hover:bg-purple-800 transition-colors"
        >
          <PlusIcon className="w-5 h-5" />
          <span>New Team</span>
        </button>
      </div>

      {/* Search Bar */}
      <div className="relative max-w-md">
        <MagnifyingGlassIcon className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
        <input
          type="text"
          placeholder="Search teams..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="w-full pl-10 pr-4 py-2.5 text-sm border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-600 focus:border-transparent"
        />
      </div>

      {/* Error Display */}
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
          {error}
        </div>
      )}

      {/* Teams Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredTeams.map((team) => (
          <div key={team.id} className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 hover:shadow-md transition-shadow">
            <div className="flex items-start justify-between mb-4">
              <h3 className="text-lg font-semibold text-gray-900">{team.name}</h3>
              <div className="flex space-x-2">
                <button
                  onClick={() => setEditingTeam(team)}
                  className="p-1 text-gray-400 hover:text-blue-600 hover:bg-gray-100 rounded"
                >
                  <PencilIcon className="w-4 h-4" />
                </button>
                <button
                  onClick={() => handleDelete(team.id)}
                  className="p-1 text-gray-400 hover:text-red-600 hover:bg-gray-100 rounded"
                >
                  <TrashIcon className="w-4 h-4" />
                </button>
              </div>
            </div>

            <p className="text-gray-600 text-sm mb-4">{team.description}</p>

            <div className="flex items-center justify-between text-sm text-gray-500">
              <div className="flex items-center space-x-1">
                <UserGroupIcon className="w-4 h-4" />
                <span>{team.memberCount || 0} members</span>
              </div>
              <span>{team.taskCount || 0} tasks</span>
            </div>
          </div>
        ))}
      </div>

      {/* Empty State */}
      {filteredTeams.length === 0 && !loading && (
        <div className="text-center py-12">
          <div className="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <UserGroupIcon className="w-8 h-8 text-gray-400" />
          </div>
          <h3 className="text-lg font-medium text-gray-900 mb-2">
            {teams.length === 0 ? 'No teams created yet' : 'No teams found'}
          </h3>
          <p className="text-gray-600 mb-4">
            {teams.length === 0 ? 'Create your first team to get started' : 'Try adjusting your search'}
          </p>
          <button
            onClick={() => setIsFormOpen(true)}
            className="bg-[#1D0E3D] text-white px-4 py-2 rounded-lg hover:bg-purple-800 transition-colors"
          >
            Create New Team
          </button>
        </div>
      )}

      {/* Team Form Modal */}
      {isFormOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-xl p-6 w-full max-w-md">
            <h2 className="text-xl font-bold mb-4">
              {editingTeam ? 'Edit Team' : 'Create New Team'}
            </h2>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Team Name
                </label>
                <input
                  type="text"
                  required
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-600"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Description
                </label>
                <textarea
                  required
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  rows="3"
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-600"
                />
              </div>
              <div className="flex space-x-3 pt-4">
                <button
                  type="submit"
                  className="flex-1 bg-[#1D0E3D] text-white py-2.5 rounded-lg hover:bg-purple-800 transition-colors"
                >
                  {editingTeam ? 'Update Team' : 'Create Team'}
                </button>
                <button
                  type="button"
                  onClick={() => {
                    setIsFormOpen(false);
                    setEditingTeam(null);
                  }}
                  className="flex-1 bg-gray-300 text-gray-700 py-2.5 rounded-lg hover:bg-gray-400 transition-colors"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}