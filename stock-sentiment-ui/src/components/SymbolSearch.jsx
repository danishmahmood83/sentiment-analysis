import React, { useState } from "react";
import axios from "axios";

function SymbolSearch() {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState(""); // To show success/error msgs

  const handleSearch = async () => {
    if (!query.trim()) return;
    setLoading(true);
    setMessage("");
    try {
      const response = await axios.get(`http://localhost:8080/api/search?query=${query}`);
      setResults(response.data);
    } catch (error) {
      console.error("Error fetching search results:", error);
      setResults([]);
      setMessage("Failed to fetch results.");
    }
    setLoading(false);
  };

  const handleAddToScheduler = async (item) => {
    setMessage("");
    try {
      // Prepare payload for tracked symbol
      const trackedSymbol = {
        symbol: item.symbol,
        companyName: item.name,
        exchange: item.exchangeShortName,
      };

      await axios.post("http://localhost:8080/api/tracked", trackedSymbol);
      setMessage(`Added ${item.symbol} to scheduler.`);
    } catch (error) {
      console.error("Failed to add symbol:", error);
      setMessage(`Failed to add ${item.symbol}.`);
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
          onKeyDown={(e) => { if (e.key === "Enter") handleSearch(); }}
        />
        <button
          onClick={handleSearch}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          Search
        </button>
      </div>

      {loading ? (
        <p>Loading...</p>
      ) : results.length > 0 ? (
        <ul className="divide-y">
          {results.map((item, index) => (
            <li
              key={index}
              className="py-2 flex justify-between items-center"
            >
              <div>
                <strong>{item.symbol}</strong> - {item.name} ({item.exchangeShortName})
              </div>
              <button
                onClick={() => handleAddToScheduler(item)}
                className="bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700"
              >
                Add to Scheduler
              </button>
            </li>
          ))}
        </ul>
      ) : (
        <p className="text-gray-500">No results to display.</p>
      )}

      {message && <p className="mt-4 text-center">{message}</p>}
    </div>
  );
}

export default SymbolSearch;
