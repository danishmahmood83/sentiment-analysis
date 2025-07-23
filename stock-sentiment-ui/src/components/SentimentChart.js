import React, { useEffect, useState } from 'react';
import { Pie } from 'react-chartjs-2';

import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend
} from 'chart.js';
import ChartDataLabels from 'chartjs-plugin-datalabels';

ChartJS.register(ArcElement, Tooltip, Legend, ChartDataLabels);

const meta = {
  title: 'Sentiment Chart',
  description: 'I am a description, and I can create multiple tags',
  tags: ['autodocs'],
  canonical: 'http://example.com/path/to/page',
  meta: {
    charset: 'utf-8',
    name: {
      keywords: 'react,meta,document,html,tags'
    }
  }
}

const SentimentChart = ({ symbol, analysisMethod }) => {
  const [chartData, setChartData] = useState(null);

  useEffect(() => {
    if (!symbol) return;

    const loadData = async () => {
      try {
        const url = `http://localhost:8080/api/sentiment/${symbol}/summary${analysisMethod ? `?analysisMethod=${analysisMethod}` : ''}`;
        const res = await fetch(url);
        if (!res.ok) throw new Error('Failed to fetch');
        const data = await res.json();

        const sentimentLabels = { bullish: 'Bullish', bearish: 'Bearish', neutral: 'Neutral' };
        const sentimentColors = { bullish: '#4caf50', bearish: '#f44336', neutral: '#ff9800' };
        const sentimentOrder = ['bullish', 'bearish', 'neutral'];

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
      } catch (error) {
        console.error(error);
        setChartData(null);
      }
    };

    loadData();
  }, [symbol, analysisMethod]);

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
    <div style={{ width: '300px', margin: 'auto' }}>
      <h4 style={{ textAlign: 'center' }}>{analysisMethod || "All Methods"}</h4>
      {chartData && chartData.labels.length > 0
        ? <Pie data={chartData} options={options} />
        : <p style={{ textAlign: 'center' }}>No data available.</p>}
    </div>
  );
};

export default SentimentChart;