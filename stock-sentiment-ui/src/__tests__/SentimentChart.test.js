import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import SentimentChart from '../components/SentimentChart'; // Adjust path if needed

// Mock Pie chart from chart.js to avoid rendering real canvas
jest.mock('react-chartjs-2', () => ({
  Pie: ({ data, options }) => (
    <div data-testid="mock-pie-chart">
      <span>{JSON.stringify(data)}</span>
    </div>
  )
}));

describe('SentimentChart Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('does not fetch when symbol is not provided', async () => {
    global.fetch = jest.fn();
    render(<SentimentChart symbol="" analysisMethod="finbert" />);
    expect(global.fetch).not.toHaveBeenCalled();
    expect(screen.getByText(/no data available/i)).toBeInTheDocument();
  });

  test('renders "No data available" when no sentiment data is returned', async () => {
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve({}), // empty response
      })
    );

    render(<SentimentChart symbol="AAPL" analysisMethod="finbert" />);

    await waitFor(() => {
      expect(screen.getByText(/no data available/i)).toBeInTheDocument();
    });
  });

  test('renders Pie chart when valid sentiment data is returned', async () => {
    const mockResponse = {
      bullish: 10,
      bearish: 5,
      neutral: 2,
    };

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockResponse),
      })
    );

    render(<SentimentChart symbol="AAPL" analysisMethod="finbert" />);

    await waitFor(() => {
      expect(screen.getByTestId('mock-pie-chart')).toBeInTheDocument();
    });

    const chartText = screen.getByTestId('mock-pie-chart').textContent;
    expect(chartText).toContain('Bullish');
    expect(chartText).toContain('Bearish');
    expect(chartText).toContain('Neutral');
  });

  test('handles fetch error gracefully', async () => {
    global.fetch = jest.fn(() => Promise.reject('API failure'));

    render(<SentimentChart symbol="AAPL" analysisMethod="finbert" />);

    await waitFor(() => {
      expect(screen.getByText(/no data available/i)).toBeInTheDocument();
    });
  });

  test('renders analysis method in header', () => {
    render(<SentimentChart symbol="AAPL" analysisMethod="vader" />);
    expect(screen.getByText(/vader/i)).toBeInTheDocument();
  });

  test('defaults to "All Methods" if analysisMethod is not provided', () => {
    render(<SentimentChart symbol="AAPL" />);
    expect(screen.getByText(/all methods/i)).toBeInTheDocument();
  });
});
