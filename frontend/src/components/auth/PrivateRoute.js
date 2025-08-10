import React, { useContext } from 'react';
import { AuthContext } from '../../context/AuthContext';
import { Navigate } from 'react-router-dom';

const PrivateRoute = ({ children, requiredRole }) => {
  const { jwtToken, user } = useContext(AuthContext);

  if (!jwtToken) {
    return <Navigate to="/login" replace />;
  }

  if (requiredRole) {
    // Support multiple roles in comma-separated string or array
    const allowedRoles = Array.isArray(requiredRole)
      ? requiredRole
      : requiredRole.split(',').map(r => r.trim());

    const hasPermission = user?.roles?.some(role => allowedRoles.includes(role));

    if (!hasPermission) {
      return <Navigate to="/dashboard" replace />;
    }
  }

  return children;
};

export default PrivateRoute;
