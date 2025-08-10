import React, { useEffect, useState } from "react";
import axiosClient from "../../api/axiosClient";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  LineElement,
  PointElement,
  Title,
  Tooltip,
  Legend
} from "chart.js";
import { Line } from "react-chartjs-2";
import { useNavigate } from "react-router-dom";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

const AdminStatisticsPage = () => {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      setLoading(true);
      const res = await axiosClient.get("/admin/statistics");
      setStats(res.data);
    } catch (err) {
      console.error("Error fetching statistics", err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <p>Loading statistics...</p>;
  if (!stats) return <p>No data available</p>;

  const trendLabels = stats.trend.map(t => t.date);
  const trendCounts = stats.trend.map(t => t.count);

  const trendData = {
    labels: trendLabels,
    datasets: [
      {
        label: "Requests Created",
        data: trendCounts,
        fill: false,
        borderColor: "#3b82f6",
        backgroundColor: "#3b82f6",
        tension: 0.2
      }
    ]
  };

  const trendOptions = {
    responsive: true,
    plugins: {
      legend: { position: "top" },
      title: { display: true, text: "Requests Created (Last 7 Days)" }
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-tr from-blue-50 to-purple-100 py-10">
      <div className="max-w-3xl mx-auto p-6 bg-white rounded-lg shadow">
        {/* Header with Back Button */}
        <div className="flex justify-between items-center mb-4">
          <h1 className="text-2xl font-bold">Admin Statistics</h1>
          <button
            onClick={() => navigate("/dashboard")}
            className="bg-gray-500 text-white px-3 py-1 rounded hover:bg-gray-600"
          >
            ← Back to Dashboard
          </button>
        </div>

        {/* Overview Cards */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-6">
          <div className="p-4 bg-blue-100 rounded text-center shadow">
            <p className="text-sm text-gray-600">Total Requests</p>
            <p className="text-2xl font-bold">{stats.totalRequests}</p>
          </div>
          <div className="p-4 bg-green-100 rounded text-center shadow">
            <p className="text-sm text-gray-600">Done Requests</p>
            <p className="text-2xl font-bold">{stats.doneRequests}</p>
          </div>
          <div className="p-4 bg-yellow-100 rounded text-center shadow">
            <p className="text-sm text-gray-600">Pending Requests</p>
            <p className="text-2xl font-bold">{stats.raisedRequests}</p>
          </div>
          <div className="p-4 bg-indigo-100 rounded text-center shadow">
            <p className="text-sm text-gray-600">Completion Rate</p>
            <p className="text-2xl font-bold">{stats.completionRate.toFixed(1)}%</p>
          </div>
        </div>

        {/* Trend Chart */}
        <Line data={trendData} options={trendOptions} />
      </div>
    </div>
  );
};

export default AdminStatisticsPage;
