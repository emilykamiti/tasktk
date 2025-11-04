import React, { useState, useEffect } from 'react';
import { useMessages } from '../hooks/useMessages';
import { useTeams } from '../hooks/useTeams';
import { useUsers } from '../hooks/useUsers';
import {
  PaperAirplaneIcon,
  TrashIcon,
  EyeIcon,
  EyeSlashIcon,
  UserGroupIcon,
  UserIcon,
  ClockIcon,
  MagnifyingGlassIcon,
  CheckIcon
} from '@heroicons/react/24/outline';

export default function Message() {
  const {
    messages,
    loading: messagesLoading,
    error: messagesError,
    createMessage,
    deleteMessage,
    markAsRead,
    markAllAsRead
  } = useMessages();

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

  const [selectedTeam, setSelectedTeam] = useState('');
  const [newMessage, setNewMessage] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [activeTab, setActiveTab] = useState('all'); // 'all', 'unread'

  const error = messagesError || teamsError || usersError;
  const loading = messagesLoading || teamsLoading || usersLoading;

  // Filter messages based on selected team and search term
  const filteredMessages = messages.filter(message => {
    const matchesTeam = !selectedTeam || message.team?.id?.toString() === selectedTeam;
    const matchesSearch = message.content?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         message.sender?.name?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesTab = activeTab === 'all' ||
                      (activeTab === 'unread' && !message.isRead);

    return matchesTeam && matchesSearch && matchesTab;
  });

  const handleSendMessage = async (e) => {
    e.preventDefault();
    if (!newMessage.trim() || !selectedTeam) return;

    try {
      await createMessage({
        content: newMessage.trim(),
        team: { id: parseInt(selectedTeam) },
        // In a real app, you'd get the current user from context/auth
        sender: { id: 1 } // Temporary - replace with actual user ID
      });
      setNewMessage('');
    } catch (err) {
      console.error('Failed to send message:', err);
    }
  };

  const handleMarkAsRead = async (messageId) => {
    try {
      await markAsRead(messageId);
    } catch (err) {
      console.error('Failed to mark message as read:', err);
    }
  };

  const handleMarkAllAsRead = async () => {
    if (!selectedTeam) return;
    try {
      // In a real app, you'd get the current user from context/auth
      await markAllAsRead(parseInt(selectedTeam), 1); // Temporary user ID
    } catch (err) {
      console.error('Failed to mark all as read:', err);
    }
  };

  const handleDeleteMessage = async (messageId) => {
    if (window.confirm('Are you sure you want to delete this message?')) {
      try {
        await deleteMessage(messageId);
      } catch (err) {
        console.error('Failed to delete message:', err);
      }
    }
  };

  const formatTime = (timestamp) => {
    if (!timestamp) return '';
    const date = new Date(timestamp);
    const now = new Date();
    const diffInHours = (now - date) / (1000 * 60 * 60);

    if (diffInHours < 24) {
      return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    } else {
      return date.toLocaleDateString();
    }
  };

  if (loading) {
    return (
      <div className="p-6 flex justify-center items-center h-64">
        <div className="text-lg text-gray-600">Loading messages...</div>
      </div>
    );
  }

  return (
    <div className="p-6 space-y-6 h-full flex flex-col">
      {/* Error Display */}
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
          {error}
        </div>
      )}

      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Messages</h1>
          <p className="text-gray-600">Communicate with your team members</p>
        </div>

        {selectedTeam && (
          <button
            onClick={handleMarkAllAsRead}
            className="bg-blue-600 text-white px-4 py-2 rounded-lg flex items-center space-x-2 hover:bg-blue-700 transition-colors"
          >
            <CheckIcon className="w-4 h-4" />
            <span>Mark All as Read</span>
          </button>
        )}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6 flex-1">
        {/* Sidebar */}
        <div className="lg:col-span-1 space-y-4">
          {/* Team Selection */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Select Team
            </label>
            <select
              value={selectedTeam}
              onChange={(e) => setSelectedTeam(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-600"
            >
              <option value="">All Teams</option>
              {teams.map(team => (
                <option key={team.id} value={team.id}>
                  {team.name}
                </option>
              ))}
            </select>
          </div>

          {/* Message Tabs */}
          <div className="flex space-x-1 bg-gray-100 p-1 rounded-lg">
            <button
              onClick={() => setActiveTab('all')}
              className={`flex-1 py-2 px-3 text-sm font-medium rounded-md transition-colors ${
                activeTab === 'all'
                  ? 'bg-white text-gray-900 shadow-sm'
                  : 'text-gray-600 hover:text-gray-900'
              }`}
            >
              All Messages
            </button>
            <button
              onClick={() => setActiveTab('unread')}
              className={`flex-1 py-2 px-3 text-sm font-medium rounded-md transition-colors ${
                activeTab === 'unread'
                  ? 'bg-white text-gray-900 shadow-sm'
                  : 'text-gray-600 hover:text-gray-900'
              }`}
            >
              Unread
            </button>
          </div>

          {/* Send Message Form */}
          {selectedTeam && (
            <div className="bg-gray-50 p-4 rounded-lg">
              <h3 className="text-sm font-medium text-gray-700 mb-3">Send Message</h3>
              <form onSubmit={handleSendMessage} className="space-y-3">
                <textarea
                  value={newMessage}
                  onChange={(e) => setNewMessage(e.target.value)}
                  placeholder="Type your message..."
                  rows="3"
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-600 resize-none"
                  maxLength="1000"
                />
                <div className="flex justify-between items-center">
                  <span className="text-xs text-gray-500">
                    {newMessage.length}/1000
                  </span>
                  <button
                    type="submit"
                    disabled={!newMessage.trim()}
                    className="bg-[#1D0E3D] text-white px-4 py-2 rounded-lg flex items-center space-x-2 hover:bg-purple-800 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    <PaperAirplaneIcon className="w-4 h-4" />
                    <span>Send</span>
                  </button>
                </div>
              </form>
            </div>
          )}
        </div>

        {/* Messages List */}
        <div className="lg:col-span-3 bg-white rounded-xl shadow-sm border border-gray-200">
          {/* Search Bar */}
          <div className="p-4 border-b border-gray-200">
            <div className="relative">
              <MagnifyingGlassIcon className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
              <input
                type="text"
                placeholder="Search messages..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-600 focus:border-transparent"
              />
            </div>
          </div>

          {/* Messages */}
          <div className="p-4 space-y-4 max-h-[calc(100vh-300px)] overflow-y-auto">
            {filteredMessages.length === 0 ? (
              <div className="text-center py-12 text-gray-500">
                {selectedTeam ? 'No messages found' : 'Select a team to view messages'}
              </div>
            ) : (
              filteredMessages.map((message) => (
                <div
                  key={message.id}
                  className={`p-4 rounded-lg border transition-colors ${
                    message.isRead
                      ? 'bg-white border-gray-200'
                      : 'bg-blue-50 border-blue-200'
                  }`}
                >
                  <div className="flex items-start justify-between mb-2">
                    <div className="flex items-center space-x-3">
                      <div className="w-8 h-8 bg-[#6B46C1] rounded-full flex items-center justify-center">
                        <span className="text-xs font-bold text-white">
                          {message.sender?.name?.charAt(0) || 'U'}
                        </span>
                      </div>
                      <div>
                        <div className="flex items-center space-x-2">
                          <span className="font-medium text-gray-900">
                            {message.sender?.name || 'Unknown User'}
                          </span>
                          {!message.isRead && (
                            <span className="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded-full">
                              New
                            </span>
                          )}
                        </div>
                        <div className="flex items-center space-x-2 text-sm text-gray-500">
                          <UserGroupIcon className="w-4 h-4" />
                          <span>{message.team?.name || 'Unknown Team'}</span>
                          <ClockIcon className="w-4 h-4 ml-2" />
                          <span>{formatTime(message.timeStamp)}</span>
                        </div>
                      </div>
                    </div>

                    <div className="flex space-x-2">
                      {!message.isRead && (
                        <button
                          onClick={() => handleMarkAsRead(message.id)}
                          className="p-1 text-gray-400 hover:text-green-600 hover:bg-gray-100 rounded"
                          title="Mark as read"
                        >
                          <EyeIcon className="w-4 h-4" />
                        </button>
                      )}
                      <button
                        onClick={() => handleDeleteMessage(message.id)}
                        className="p-1 text-gray-400 hover:text-red-600 hover:bg-gray-100 rounded"
                        title="Delete message"
                      >
                        <TrashIcon className="w-4 h-4" />
                      </button>
                    </div>
                  </div>

                  <p className="text-gray-700 whitespace-pre-wrap">
                    {message.content}
                  </p>
                </div>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
}