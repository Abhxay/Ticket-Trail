import React, { useEffect, useState, useContext } from 'react';
import axiosClient from '../api/axiosClient';
import { AuthContext } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const AdminManagementPage = () => {
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);
  const [newUser, setNewUser] = useState({ username: '', password: '', roles: '' });

  const isSuperAdmin = user?.roles?.includes('ROLE_SUPER_ADMIN');

  const fetchUsers = async () => {
    const { data } = await axiosClient.get('/admin/users');
    setUsers(data);
  };

  const promoteToAdmin = async id => { await axiosClient.post(`/admin/manage/${id}/add-admin`); fetchUsers(); };
  const demoteFromAdmin = async id => { await axiosClient.post(`/admin/manage/${id}/remove-admin`); fetchUsers(); };
  const promoteToSuperAdmin = async id => { await axiosClient.post(`/admin/manage/${id}/add-superadmin`); fetchUsers(); };
  const demoteFromSuperAdmin = async id => { await axiosClient.post(`/admin/manage/${id}/remove-superadmin`); fetchUsers(); };

  const createUser = async e => {
    e.preventDefault();
    const rolesArray = newUser.roles.split(',').map(r => r.trim()).filter(r => r);
    await axiosClient.post('/admin/create-user', { ...newUser, roles: rolesArray });
    setNewUser({ username: '', password: '', roles: '' });
    fetchUsers();
  };

  useEffect(() => { fetchUsers(); }, []);

  if (!isSuperAdmin) return <div>Access Denied</div>;

  return (
    <div className="min-h-screen bg-gradient-to-tr from-purple-50 to-pink-100 py-10">
      <div className="p-6 bg-white shadow-lg rounded-lg max-w-4xl mx-auto">
        {/* Header with Back Button */}
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-2xl font-bold">Admin Management</h2>
          <button
            onClick={() => navigate('/dashboard')}
            className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
          >
            ← Back to Dashboard
          </button>
        </div>

        {/* Create User Form */}
        <form onSubmit={createUser} className="mb-6 grid grid-cols-1 sm:grid-cols-3 gap-4">
          <input
            type="text"
            placeholder="Username"
            value={newUser.username}
            onChange={e => setNewUser({ ...newUser, username: e.target.value })}
            className="border p-2 rounded w-full"
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={newUser.password}
            onChange={e => setNewUser({ ...newUser, password: e.target.value })}
            className="border p-2 rounded w-full"
            required
          />
          <input
            type="text"
            placeholder="Roles (comma separated)"
            value={newUser.roles}
            onChange={e => setNewUser({ ...newUser, roles: e.target.value })}
            className="border p-2 rounded w-full sm:col-span-2"
          />
          <button
            type="submit"
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            Create User
          </button>
        </form>

        {/* User List */}
        <ul className="divide-y">
          {users.map(u => (
            <li key={u.id} className="flex justify-between items-center py-2">
              <span className="font-medium">
                {u.username} 
                {u.roles?.map(role => (
                  <span key={role} className="ml-2 text-xs bg-gray-200 px-2 py-1 rounded-full">
                    {role}
                  </span>
                ))}
              </span>
              <div className="space-x-2">
                {!u.roles?.includes('ROLE_ADMIN') && (
                  <button onClick={() => promoteToAdmin(u.id)}
                    className="bg-green-600 text-white px-2 py-1 rounded hover:bg-green-700">
                    Make Admin
                  </button>
                )}
                {u.roles?.includes('ROLE_ADMIN') && u.username !== 'Superadmin' && (
                  <button onClick={() => demoteFromAdmin(u.id)}
                    className="bg-red-600 text-white px-2 py-1 rounded hover:bg-red-700">
                    Remove Admin
                  </button>
                )}
                {!u.roles?.includes('ROLE_SUPER_ADMIN') && (
                  <button onClick={() => promoteToSuperAdmin(u.id)}
                    className="bg-purple-600 text-white px-2 py-1 rounded hover:bg-purple-700">
                    Make Superadmin
                  </button>
                )}
                {u.roles?.includes('ROLE_SUPER_ADMIN') && u.username !== 'Superadmin' && (
                  <button onClick={() => demoteFromSuperAdmin(u.id)}
                    className="bg-purple-900 text-white px-2 py-1 rounded hover:bg-purple-800">
                    Remove Superadmin
                  </button>
                )}
              </div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default AdminManagementPage;
