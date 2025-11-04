import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useAppContext } from './context/AppContext';
import Layout from './scene/Layout.jsx';
import Activity from './scene/Activity.jsx';
import Team from './scene/Team.jsx';
import Message from './scene/Message.jsx';
import Task from './scene/Task.jsx';
import Statistics from './scene/Statistics.jsx';
import User from './scene/User.jsx';
import Dashboard from './components/DashBoard.jsx'
import { AuthProvider } from './utils/AuthContext';

function App() {
  const { mode } = useAppContext();

  return (
    <div className={`min-h-screen ${mode === 'dark' ? 'dark bg-gray-900' : 'bg-gray-50'}`}>
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            <Route path="/layout" element={<Layout />}>
              <Route index element={<Dashboard />} />
              <Route path="team" element={<Team />} />
              <Route path="statistics" element={<Statistics />} />
              <Route path="task" element={<Task />} />
              <Route path="message" element={<Message />} />
              <Route path="activity" element={<Activity />} />
              <Route path="user" element={<User />} />
              <Route path="dashboard" element={<Dashboard />}/>
            </Route>


            <Route path="/" element={<Navigate to="/layout/dashboard" replace />} />

          </Routes>
        </AuthProvider>
      </BrowserRouter>
    </div>
  );
}

export default App;