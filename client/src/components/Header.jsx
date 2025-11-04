import React from 'react';

const Header = () => {
  return (
    <header className="fixed top-0 right-0 z-10 bg-white w-[calc(100%-256px)] ml-64 p-4 shadow">
      <h1 className="text-xl font-bold">Dashboard</h1>
      <div className="flex items-center space-x-4 mt-2">
        <button className="p-2 rounded-full hover:bg-gray-200">
          <span>🔔</span>
        </button>
        <button className="p-2 rounded-full hover:bg-gray-200">
          <span>⚙️</span>
        </button>
        <button className="p-2 rounded-full hover:bg-gray-200">
          <span>🔍</span>
        </button>
        <div className="flex items-center space-x-2">
          <img src="/default.png" alt="User" className="h-8 w-8 rounded-full" />
          <span>Profile</span>
          <span>▼</span>
        </div>
      </div>
    </header>
  );
};

export default Header;