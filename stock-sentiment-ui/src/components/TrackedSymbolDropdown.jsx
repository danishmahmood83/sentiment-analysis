import React, { useEffect, useState } from "react";
import axios from "axios";

const meta = {
  title: 'Tracked Symbol Dropdown',
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

const TrackedSymbolDropdown = ({ onSymbolRemoved, refreshKey }) => {
  const [symbols, setSymbols] = useState([]);
  const [selectedSymbol, setSelectedSymbol] = useState("");

  const fetchTrackedSymbols = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/tracked");
      setSymbols(response.data);
    } catch (error) {
      console.error("Error fetching tracked symbols:", error);
    }
  };

  useEffect(() => {
    fetchTrackedSymbols();
  }, [refreshKey]);

  const handleRemove = async () => {
    if (!selectedSymbol) return;

    try {
      await axios.delete(`http://localhost:8080/api/tracked/${selectedSymbol}`);
      setSelectedSymbol("");
      if (onSymbolRemoved) onSymbolRemoved();
    } catch (error) {
      console.error("Error removing symbol:", error);
    }
  };

  return (
    <div className="mt-4">
      <select
        className="w-full p-2 border rounded mb-4"
        value={selectedSymbol}
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
