import React, { useContext, useState } from 'react';
import { AuthContext } from '../context/AuthContext';
import { Link } from 'react-router-dom';
import RequestForm from '../components/requests/RequestForm';
import RequestList from '../components/requests/RequestList';

const Dashboard = () => {
  const { logout, user } = useContext(AuthContext);
  const [refresh, setRefresh] = useState(0);

  const isAdmin = user?.roles?.includes('ROLE_ADMIN');
  const isSuperAdmin = user?.roles?.includes('ROLE_SUPER_ADMIN');

  return (
    <div className="min-h-screen bg-gradient-to-tr from-blue-50 to-purple-100 py-10">
      <div className="max-w-5xl mx-auto bg-white shadow-lg rounded-lg p-6">
        {/* Header */}
        <div className="flex flex-wrap justify-between items-center mb-6">
          <h1 className="text-2xl font-bold">
            Welcome{user?.username ? `, ${user.username}` : ''}!
          </h1>

          <div className="flex flex-wrap gap-2">
            {isSuperAdmin && (
              <Link
                to="/admin/manage"
                className="bg-yellow-500 hover:bg-yellow-600 text-white px-4 py-2 rounded shadow-md transition"
              >
                Admin Management
              </Link>
            )}

            {(isAdmin || isSuperAdmin) && (
              <Link
                to="/admin/statistics"
                className="bg-indigo-500 hover:bg-indigo-600 text-white px-4 py-2 rounded shadow-md transition"
              >
                Admin Statistics
              </Link>
            )}

            <Link
              to="/account"
              className="bg-gray-600 hover:bg-gray-700 text-white px-4 py-2 rounded shadow-md transition"
            >
              Account Settings
            </Link>

            <button
              onClick={logout}
              className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded shadow-md transition"
            >
              Logout
            </button>
          </div>
        </div>

        {/* Request Form */}
        <RequestForm onSuccess={() => setRefresh(prev => prev + 1)} />

        {/* Request List */}
        <RequestList
          refresh={refresh}
          user={user}
          onDone={() => setRefresh(prev => prev + 1)}
        />
      </div>
    </div>
  );
};

export default Dashboard;
