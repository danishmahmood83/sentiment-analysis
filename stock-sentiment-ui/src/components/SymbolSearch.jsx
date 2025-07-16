import React, { useState } from "react";
import axios from "axios";
import Button from "./Button";

const meta = {
  title: 'Symbol Search',
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

function SymbolSearch({ onSymbolAdded }) {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState([]);
  const [selectedSymbol, setSelectedSymbol] = useState(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  const handleSearch = async () => {
    if (!query.trim()) return;
    setLoading(true);
    setMessage("");
    setSelectedSymbol(null);
    try {
      const response = await axios.get(
        `http://localhost:8080/api/search?query=${query}`
      );
      setResults(response.data);
    } catch (error) {
      console.error("Error fetching search results:", error);
      setResults([]);
      setMessage("Failed to fetch results.");
    }
    setLoading(false);
  };

  const handleAddToScheduler = async () => {
    if (!selectedSymbol) return;

    const trackedSymbol = {
      symbol: selectedSymbol.symbol,
      companyName: selectedSymbol.name,
      exchange: selectedSymbol.exchangeShortName
    };

    try {
      await axios.post("http://localhost:8080/api/tracked", trackedSymbol);
      setMessage(`✅ Added ${selectedSymbol.symbol} to scheduler.`);
      if (onSymbolAdded) onSymbolAdded();
    } catch (error) {
      console.error("Failed to add symbol:", error);
      setMessage(`❌ Failed to add ${selectedSymbol.symbol}.`);
    }
  };

  const handleSelectionChange = (e) => {
    const selectedIndex = e.target.value;
    if (selectedIndex === "") {
      setSelectedSymbol(null);
    } else {
      setSelectedSymbol(results[selectedIndex]);
    }
  };

  return (
    <div className="p-4 border rounded max-w-xl mx-auto mt-8 shadow">
      <h2 className="text-xl font-semibold mb-4">Search Stock Symbol</h2>
      <div className="flex gap-2 mb-4">
        <input
          type="text"
          placeholder="Enter company name or symbol..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          className="flex-1 p-2 border rounded"
          onKeyDown={(e) => {
            if (e.key === "Enter") handleSearch();
          }}
        />
        <Button
          onClick={handleSearch}
        >
          Search
        </Button>
      </div>

      {loading && <p>Loading...</p>}

      {!loading && results.length > 0 && (
        <div className="mb-4">
          <select
            className="w-full p-2 border rounded"
            defaultValue=""
            onChange={handleSelectionChange}
          >
            <option value="">-- Select a symbol --</option>
            {results.map((item, index) => (
              <option key={item.symbol} value={index}>
                {item.symbol} - {item.name} ({item.exchangeShortName})
              </option>
            ))}
          </select>
        </div>
      )}

      {selectedSymbol && (
        <Button
          onClick={handleAddToScheduler}
          className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 mt-2"
        >
          ➕ Add to Scheduler
        </Button>
      )}

      {message && (
        <p className="mt-4 text-center font-medium text-blue-700">{message}</p>
      )}
    </div>
  );
}

export default SymbolSearch;
