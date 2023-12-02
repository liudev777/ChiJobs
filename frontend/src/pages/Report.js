import React from 'react';
import { Bar } from 'react-chartjs-2';
import { useNavigate, useLocation } from 'react-router-dom';

import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend,
  } from 'chart.js';
  
  ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
  );



  const Report = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { data, loading } = location.state || {};
  
    const chartData = {
      labels: data.map(item => item.title),
      datasets: [
        {
          label: 'Search Frequency',
          data: data.map(item => item.count),
          backgroundColor: 'rgba(0, 128, 128, 0.2)', // Teal background color
          borderColor: 'rgba(0, 128, 128, 1)', // Teal border color
          borderWidth: 1,
        },
      ],
    };
  
    const chartOptions = {
      scales: {
        x: {
          ticks: {
            color: 'black' // Black text for X-axis labels
          }
        },
        y: {
          ticks: {
            color: 'black' // Black text for Y-axis labels
          }
        }
      },
      plugins: {
        legend: {
          labels: {
            color: 'black' // Black text for legend
          }
        }
      }
    };
  
    return <Bar data={chartData} options={chartOptions} />;
  };

export default Report;
