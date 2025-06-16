import React, { useEffect, useState } from 'react';
import { Pie } from 'react-chartjs-2';
import { fetchSentimentData } from '../api';

import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend
} from 'chart.js';
import ChartDataLabels from 'chartjs-plugin-datalabels';

ChartJS.register(ArcElement, Tooltip, Legend, ChartDataLabels);

const SentimentChart = ({ symbol }) => {
  const [chartData, setChartData] = useState(null);

  useEffect(() => {
    const loadData = async () => {
      const data = await fetchSentimentData(symbol);

      const sentimentLabels = {
        bullish: 'Bullish',
        bearish: 'Bearish',
        neutral: 'Neutral'
      };

      const sentimentColors = {
        bullish: '#4caf50', // green
        bearish: '#f44336', // red
        neutral: '#ff9800'  // orange
      };

      const sentimentOrder = ['bullish', 'bearish', 'neutral'];

      // Filter out zero values
      const filtered = sentimentOrder
        .filter(key => data[key] && data[key] > 0)
        .map(key => ({
          label: sentimentLabels[key],
          value: data[key],
          color: sentimentColors[key]
        }));

      setChartData({
        labels: filtered.map(item => item.label),
        datasets: [{
          label: 'Sentiment Count',
          data: filtered.map(item => item.value),
          backgroundColor: filtered.map(item => item.color),
          borderWidth: 1
        }]
      });
    };

    loadData();
  }, [symbol]);

  const options = {
    plugins: {
      legend: {
        position: 'bottom'
      },
      datalabels: {
        color: '#fff',
        formatter: (value, context) => {
          const total = context.chart.data.datasets[0].data.reduce((acc, val) => acc + val, 0);
          const percentage = ((value / total) * 100).toFixed(1);
          return `${percentage}%`;
        },
        font: {
          weight: 'bold'
        }
      }
    }
  };

  return (
    <div style={{ width: '400px', margin: 'auto' }}>
      <h3>{symbol} Sentiment</h3>
      {chartData && chartData.labels.length > 0
        ? <Pie data={chartData} options={options} />
        : <p>No sentiment data available.</p>}
    </div>
  );
};

export default SentimentChart;
