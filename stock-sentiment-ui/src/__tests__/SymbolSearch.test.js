import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import SymbolSearch from '../components/SymbolSearch';
import axios from 'axios';

jest.mock('axios');

test('renders input and performs search', async () => {
  const mockData = [
    { symbol: 'AAPL', name: 'Apple Inc.', exchangeShortName: 'NASDAQ' }
  ];
  axios.get.mockResolvedValue({ data: mockData });

  render(<SymbolSearch onSymbolAdded={jest.fn()} />);

  const input = screen.getByPlaceholderText(/enter company name/i);
  fireEvent.change(input, { target: { value: 'Apple' } });
  fireEvent.keyDown(input, { key: 'Enter', code: 'Enter' });

  await waitFor(() =>
    expect(screen.getByText(/AAPL - Apple Inc./)).toBeInTheDocument()
  );
});
