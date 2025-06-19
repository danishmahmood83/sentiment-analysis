import React, { useEffect, useState } from 'react';
import axios from 'axios';
import SentimentChart from './SentimentChart';

const SymbolChartViewer = () => {
  const [symbols, setSymbols] = useState([]);
  const [selectedSymbol, setSelectedSymbol] = useState('');

  useEffect(() => {
    const fetchSymbols = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/tracked');
        setSymbols(response.data); // array of { id, symbol }
      } catch (error) {
        console.error('Error fetching tracked symbols:', error);
      }
    };

    fetchSymbols();
  }, []);

  return (
    <div className="p-4 max-w-md mx-auto">
      <h2 className="text-xl font-bold mb-2">Select Symbol to View Sentiment</h2>
      <select
        className="w-full p-2 border rounded mb-4"
        value={selectedSymbol}
        onChange={(e) => setSelectedSymbol(e.target.value)}
      >
        <option value="">-- Choose Symbol --</option>
        {symbols.map((item) => (
          <option key={item.id} value={item.symbol}>
            {item.symbol}
          </option>
        ))}
      </select>

      {selectedSymbol && <SentimentChart symbol={selectedSymbol} />}
    </div>
  );
};

export default SymbolChartViewer;
