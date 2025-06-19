import React, { useState } from 'react';
import axios from 'axios';

const TrackedSymbolDropdown = ({ onRemove }) => {
  const [symbols, setSymbols] = useState([]);
  const [selectedSymbol, setSelectedSymbol] = useState('');

  // Fetch tracked symbols from backend when dropdown is focused
  const fetchTrackedSymbols = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/tracked');
      setSymbols(response.data); // response.data is expected to be array of { id, symbol }
    } catch (error) {
      console.error('Error fetching tracked symbols:', error);
    }
  };

  // Remove the selected symbol
  const handleRemove = async () => {
    if (!selectedSymbol) return;
    try {
      await axios.delete(`http://localhost:8080/api/tracked/${selectedSymbol}`);
      setSelectedSymbol('');
      setSymbols((prev) => prev.filter((item) => item.symbol !== selectedSymbol));
      if (onRemove) onRemove(selectedSymbol);
    } catch (error) {
      console.error('Error removing symbol:', error);
    }
  };

  return (
    <div className="mt-4">
      <select
        className="w-full p-2 border rounded mb-4"
        value={selectedSymbol}
        onFocus={fetchTrackedSymbols}
        onChange={(e) => setSelectedSymbol(e.target.value)}
      >
        <option value="">-- Select Symbol to Remove --</option>
        {symbols.map((item) => (
          <option key={item.id} value={item.symbol}>
            {item.symbol}
          </option>
        ))}
      </select>

      {selectedSymbol && (
        <button
          className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
          onClick={handleRemove}
        >
          Remove
        </button>
      )}
    </div>
  );
};

export default TrackedSymbolDropdown;
