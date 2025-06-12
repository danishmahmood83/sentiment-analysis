import React, { useEffect, useState } from 'react';
import { Pie } from 'react-chartjs-2';
import { fetchSentimentData } from '../api';

import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend
} from 'chart.js';
ChartJS.register(ArcElement, Tooltip, Legend);

const SentimentChart = ({ symbol }) => {
  const [chartData, setChartData] = useState(null);

  useEffect(() => {
    const loadData = async () => {
      const data = await fetchSentimentData(symbol);
      const labels = Object.keys(data);
      const values = Object.values(data);

      setChartData({
        labels,
        datasets: [{
          label: 'Sentiment Count',
          data: values,
          backgroundColor: ['#4caf50', '#f44336', '#ff9800'], // green, red, orange
          borderWidth: 1
        }]
      });
    };
    loadData();
  }, [symbol]);

  return (
    <div style={{ width: '400px', margin: 'auto' }}>
      <h3>{symbol} Sentiment</h3>
      {chartData && <Pie data={chartData} />}
    </div>
  );
};

export default SentimentChart;
