import React, { useState } from 'react';
import axiosClient from '../../api/axiosClient';

const RequestForm = ({ onSuccess }) => {
  const [description, setDescription] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      await axiosClient.post('/requests', { description: description.trim() });
      setMessage('Request raised successfully');
      setDescription('');
      if (onSuccess) onSuccess();
    } catch {
      setMessage('Failed to raise request');
    }
  };

  return (
    <div className="bg-white p-4 rounded shadow mb-4">
      <h3 className="text-lg font-bold mb-2">Raise a Request</h3>
      <form onSubmit={handleSubmit}>
        <input
          className="w-full p-2 border rounded mb-2"
          placeholder="Description"
          value={description}
          onChange={e => setDescription(e.target.value)}
          required
        />
        <button
          type="submit"
          className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
        >
          Submit
        </button>
      </form>
      {message && <p className="mt-2">{message}</p>}
    </div>
  );
};

export default RequestForm;
