import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import PrivateRoute from './components/auth/PrivateRoute';
import LoginPage from './pages/LoginPage';
import Dashboard from './pages/Dashboard';
import AccountSettings from './account/AccountSettings'; 
import Register from './components/auth/Register';
import AdminStatisticsPage from './components/admin/AdminStatisticsPage';
import AdminManagementPage from './components/AdminManagementPage';

function App() {
  return (
    <Routes>
      {/* Default redirect */}
      <Route path="/" element={<Navigate to="/login" replace />} />

      {/* Public Routes */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<Register />} />

      {/* Protected Routes */}
      <Route
        path="/dashboard"
        element={
          <PrivateRoute>
            <Dashboard />
          </PrivateRoute>
        }
      />

      <Route
        path="/admin/manage"
        element={
          <PrivateRoute requiredRole="ROLE_SUPER_ADMIN">
            <AdminManagementPage />
          </PrivateRoute>
        }
      />

      <Route
        path="/account"
        element={
          <PrivateRoute>
            <AccountSettings />
          </PrivateRoute>
        }
      />

      {/* ✅ Allow both ADMIN and SUPER_ADMIN for statistics */}
      <Route 
        path="/admin/statistics"
        element={
          <PrivateRoute requiredRole="ROLE_ADMIN,ROLE_SUPER_ADMIN">
            <AdminStatisticsPage />
          </PrivateRoute>
        }
      /> 
    </Routes>
  );
}

export default App;
