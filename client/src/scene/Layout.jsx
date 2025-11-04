import React from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from '../components/SideBar';

export default function Layout() {
  return (
    <div className="flex h-screen">
      <Sidebar />
      <div className="flex-1 flex flex-col ml-64">
        <main className="flex-1 overflow-auto">
          <Outlet /> {/* This will show either Dashboard or other scenes */}
        </main>
      </div>
    </div>
  );
}