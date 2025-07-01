import React, { useEffect, useState } from "react";
import SentimentChart from "./SentimentChart";

const AVAILABLE_METHODS = ["finbert", "gpt", "stanford"]; // adjust to your actual methods

const SymbolChartViewer = ({ refreshKey }) => {
  const [symbols, setSymbols] = useState([]);
  const [selectedSymbol, setSelectedSymbol] = useState("");
  const [selectedMethods, setSelectedMethods] = useState([]);

  useEffect(() => {
    const fetchSymbols = async () => {
      try {
        const res = await fetch("http://localhost:8080/api/tracked");
        if (!res.ok) throw new Error("Failed to fetch symbols");
        const data = await res.json();
        setSymbols(data);
      } catch (error) {
        console.error("Error fetching tracked symbols:", error);
      }
    };
    fetchSymbols();
  }, [refreshKey]);

  const toggleMethod = (method) => {
    setSelectedMethods(prev =>
      prev.includes(method)
        ? prev.filter(m => m !== method)
        : [...prev, method]
    );
  };

  return (
    <div className="p-4 max-w-4xl mx-auto">
      <h2 className="text-xl font-bold mb-2">Select Symbol to View Sentiment</h2>
      <select
        className="w-full p-2 border rounded mb-4"
        value={selectedSymbol}
        onChange={(e) => setSelectedSymbol(e.target.value)}
      >
        <option value="">-- Choose Symbol --</option>
        {symbols.map(item => (
          <option key={item.id} value={item.symbol}>
            {item.symbol}
          </option>
        ))}
      </select>

      {selectedSymbol && (
        <>
          <h3 className="mb-2">Select Analysis Methods to Compare</h3>
          <div className="flex gap-4 mb-4">
            {AVAILABLE_METHODS.map(method => (
              <label key={method} style={{ cursor: "pointer" }}>
                <input
                  type="checkbox"
                  checked={selectedMethods.includes(method)}
                  onChange={() => toggleMethod(method)}
                />{" "}
                {method}
              </label>
            ))}
          </div>

          <div style={{ display: 'flex', gap: '20px', flexWrap: 'wrap' }}>
            {selectedMethods.length === 0 ? (
              <p>Select at least one analysis method to view charts.</p>
            ) : (
              selectedMethods.map(method => (
                <SentimentChart
                  key={method}
                  symbol={selectedSymbol}
                  analysisMethod={method}
                />
              ))
            )}
          </div>
        </>
      )}
    </div>
  );
};

export default SymbolChartViewer;
