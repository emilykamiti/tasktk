import React from "react";
import { Link, useLocation } from "react-router-dom";
import {
  HomeIcon,
  ClipboardDocumentListIcon,
  CalendarIcon,
  ChartBarIcon,
  Cog6ToothIcon,
  ChatBubbleLeftIcon,
  UsersIcon
} from "@heroicons/react/24/outline";

export default function Sidebar() {
  const location = useLocation();

const navItems = [
    { icon: HomeIcon, label: "Home", path: "/layout/dashboard", dot: false },
    { icon: ClipboardDocumentListIcon, label: "Task", path: "/layout/task", dot: true },
    { icon: ClipboardDocumentListIcon, label: "Team", path: "/layout/team", dot: true },
    { icon: CalendarIcon, label: "Activity", path: "/layout/activity", dot: false },
    { icon: ChartBarIcon, label: "Statistics", path: "/layout/statistics", dot: false },
    { icon: ChatBubbleLeftIcon, label: "Messages", path: "/layout/message", dot: false },
    { icon: UsersIcon, label: "Users", path: "/layout/user", dot: false },
];

  return (
    <aside className="fixed inset-y-0 left-0 w-60 bg-[#1A0B2E] text-white flex flex-col">
      {/* Logo */}
      <div className="px-6 py-5">
        <h1 className="text-2xl font-bold tracking-tight">Tasktk.</h1>
      </div>

      {/* Navigation */}
      <nav className="flex-1 px-4 space-y-1">
        {navItems.map((item) => {
          const Icon = item.icon;
          const isActive = location.pathname === item.path;

          return (
            <Link
              key={item.label}
              to={item.path}
              className={`group flex items-center justify-between py-2.5 px-3 rounded-lg transition-all ${
                isActive
                  ? "bg-purple-600 text-white"
                  : "text-gray-400 hover:text-white "
              }`}
            >
              <div className="flex items-center space-x-3">
                <Icon className="w-5 h-5" />
                <span className="font-medium">{item.label}</span>
              </div>
              {item.dot && (
                <span className="w-2 h-2 bg-cyan-400 rounded-full"></span>
              )}
            </Link>
          );
        })}
      </nav>

      {/* Upgrade Card */}
      <div className="mx-4 mb-10 p-4 bg-gradient-to-r from-[#A0F1EA] to-[#5CE1E6] rounded-2xl text-[#1A0B2E] shadow-lg">
        <div className="flex items-center justify-between">
          <div>
            <p className="font-semibold text-sm">Upgrade to pro</p>
            <p className="text-xs mt-1 opacity-90">New feature</p>
          </div>
          <div className="w-10 h-10 bg-white/30 rounded-full"></div>
        </div>
      </div>

      {/* Settings */}
      <div className="px-4 py-3 border-t border-purple-400">
        <Link
          to="/settings"
          className="flex items-center space-x-3 text-purple-300 hover:text-white transition-colors"
        >
          <Cog6ToothIcon className="w-5 h-5" />
          <span className="font-medium">Settings</span>
        </Link>
      </div>
    </aside>
  );
}