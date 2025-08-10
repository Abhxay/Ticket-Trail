import React, { useState } from "react";
import axiosClient from "../api/axiosClient";
import { useNavigate } from "react-router-dom";

const AccountSettings = () => {
  const [usernameForm, setUsernameForm] = useState({ newUsername: "" });
  const [passwordForm, setPasswordForm] = useState({
    currentPassword: "",
    newPassword: ""
  });
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleUsernameChange = async (e) => {
    e.preventDefault();
    try {
      await axiosClient.put("/account/change-username", usernameForm);
      setMessage("✅ Username changed successfully. Please log in again.");
      setError(null);
      localStorage.removeItem("jwtToken");
    } catch (err) {
      setError(err.response?.data || "Error changing username");
    }
  };

  const handlePasswordChange = async (e) => {
    e.preventDefault();
    try {
      await axiosClient.put("/account/change-password", passwordForm);
      setMessage("✅ Password changed successfully. Please log in again.");
      setError(null);
      localStorage.removeItem("jwtToken");
    } catch (err) {
      setError(err.response?.data || "Error changing password");
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-tr from-blue-50 to-purple-100 py-10">
      <div className="p-6 bg-white rounded shadow-lg max-w-md mx-auto">
        {/* Header with Back Button */}
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-bold">Account Settings</h2>
          <button
            onClick={() => navigate("/dashboard")}
            className="bg-gray-500 text-white px-3 py-1 rounded hover:bg-gray-600"
          >
            ← Back to Dashboard
          </button>
        </div>

        {/* Feedback messages */}
        {message && (
          <div className="bg-green-100 text-green-800 p-2 mb-2">{message}</div>
        )}
        {error && (
          <div className="bg-red-100 text-red-800 p-2 mb-2">{error}</div>
        )}

        {/* Change Username */}
        <form onSubmit={handleUsernameChange} className="mb-6">
          <h3 className="text-lg font-semibold mb-2">Change Username</h3>
          <input
            type="text"
            placeholder="New username"
            value={usernameForm.newUsername}
            onChange={(e) =>
              setUsernameForm({ ...usernameForm, newUsername: e.target.value })
            }
            className="border p-2 rounded w-full mb-2"
            required
          />
          <button
            type="submit"
            className="bg-blue-600 hover:bg-blue-700 px-3 py-1 text-white rounded"
          >
            Update Username
          </button>
        </form>

        {/* Change Password */}
        <form onSubmit={handlePasswordChange}>
          <h3 className="text-lg font-semibold mb-2">Change Password</h3>
          <input
            type="password"
            placeholder="Current password"
            value={passwordForm.currentPassword}
            onChange={(e) =>
              setPasswordForm({
                ...passwordForm,
                currentPassword: e.target.value
              })
            }
            className="border p-2 rounded w-full mb-2"
            required
          />
          <input
            type="password"
            placeholder="New password"
            value={passwordForm.newPassword}
            onChange={(e) =>
              setPasswordForm({ ...passwordForm, newPassword: e.target.value })
            }
            className="border p-2 rounded w-full mb-2"
            required
          />
          <button
            type="submit"
            className="bg-green-600 hover:bg-green-700 px-3 py-1 text-white rounded"
          >
            Update Password
          </button>
        </form>
      </div>
    </div>
  );
};

export default AccountSettings;
