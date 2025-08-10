  import React, { useEffect, useState, useCallback } from 'react';
  import axiosClient from '../../api/axiosClient';

  const RequestList = ({ refresh, user, onDone }) => {
    const [requests, setRequests] = useState([]);
    const [status, setStatus] = useState('');
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [loading, setLoading] = useState(false);

    // ✅ Allow ADMIN and SUPER_ADMIN to mark done
    const canMarkDone =
      user?.roles?.includes('ROLE_ADMIN') || user?.roles?.includes('ROLE_SUPER_ADMIN');

    // ✅ Memoize fetchRequests so it can be safely added to useEffect dependencies
    const fetchRequests = useCallback(async () => {
      try {
        setLoading(true);
        const params = { page, size: 10 };
        if (status) params.status = status;
        const { data } = await axiosClient.get('/requests', { params });
        setRequests(data.content || data);
        setTotalPages(data.totalPages || 1);
      } finally {
        setLoading(false);
      }
    }, [page, status]); // dependencies used inside fetchRequests

    // Fetch whenever dependencies change
    useEffect(() => {
      fetchRequests();
    }, [fetchRequests, refresh]); // now includes fetchRequests safely

    const markDone = async id => {
      await axiosClient.put(`/requests/${id}/done`);
      if (onDone) onDone();
      fetchRequests(); // re-fetch list after marking as done
    };

    return (
      <div className="bg-white p-4 rounded shadow">
        <h3 className="text-lg font-bold mb-2">All Requests</h3>

        {/* Filter */}
        <select
          value={status}
          onChange={e => setStatus(e.target.value)}
          className="border p-2 rounded mb-2"
        >
          <option value="">All</option>
          <option value="raised">Raised</option>
          <option value="done">Done</option>
        </select>

        {/* List */}
        {loading ? (
          <p>Loading...</p>
        ) : (
          <ul className="divide-y">
            {requests.map(req => (
              <li key={req.id} className="py-2 flex justify-between items-center">
                <span>
                  {req.description} — <b>{req.status}</b>
                </span>
                {canMarkDone && req.status !== 'done' && (
                  <button
                    className="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700"
                    onClick={() => markDone(req.id)}
                  >
                    Mark as Done
                  </button>
                )}
              </li>
            ))}
          </ul>
        )}

        {/* Pagination */}
        <div className="flex justify-between mt-3">
          <button
            disabled={page <= 0}
            onClick={() => setPage(p => p - 1)}
            className="px-3 py-1 border rounded disabled:opacity-50"
          >
            Prev
          </button>
          <span>
            Page {page + 1} of {totalPages}
          </span>
          <button
            disabled={page >= totalPages - 1}
            onClick={() => setPage(p => p + 1)}
            className="px-3 py-1 border rounded disabled:opacity-50"
          >
            Next
          </button>
        </div>
      </div>
    );
  };

  export default RequestList;
