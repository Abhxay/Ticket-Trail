import React, { createContext, useState, useEffect } from 'react';
import axiosClient from '../api/axiosClient';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [jwtToken, setJwtToken] = useState(() => localStorage.getItem('jwtToken'));
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (jwtToken) {
      localStorage.setItem('jwtToken', jwtToken);
      axiosClient.defaults.headers.common['Authorization'] = `Bearer ${jwtToken}`;
      try {
        const decoded = jwtDecode(jwtToken);
        console.log("Decoded JWT:", decoded);

        setUser({
          username: decoded.sub || decoded.username,
          roles: decoded.roles || [],  // ✅ roles now come directly from JWT claims
        });
      } catch (err) {
        console.error('Invalid JWT token', err);
        setUser(null);
      }
    } else {
      localStorage.removeItem('jwtToken');
      delete axiosClient.defaults.headers.common['Authorization'];
      setUser(null);
    }
  }, [jwtToken]);

  const login = async (username, password) => {
    try {
      const response = await axiosClient.post('/auth/login', { username, password });
      const token = response.data.token;
      setJwtToken(token);
      // ✅ Additional safeguard: also set roles from backend response if available
      setUser({
        username: response.data.username,
        roles: response.data.roles || [],
      });
      navigate('/dashboard');
    } catch (error) {
      throw error;
    }
  };

  const logout = () => {
    setJwtToken(null);
    navigate('/login');
  };

  return (
    <AuthContext.Provider value={{ user, jwtToken, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
