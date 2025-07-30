// SymbolChartViewer.jsx
import React, { useEffect, useState } from "react";
import SentimentChart from "./SentimentChart";
import Dropdown from "./Dropdown";

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

  const handleSymbolChange = (symbol) => {
    setSelectedSymbol(symbol);
  };

  return (
      // <div className="p-4 border rounded max-w-xl mx-auto mt-6 shadow">
      //   <h2 className="text-lg font-semibold mb-4">Remove Tracked Symbol</h2>
      //   <div className="p-4 border rounded max-w-xl mx-auto mt-8 shadow flex flex-col items-center space-y-4">
      <div className="p-4 border rounded max-w-xl mx-auto mt-6 shadow">
      <h2 className="text-lg font-semibold mb-4">Select Symbol to View Sentiment</h2>
        <div className="p-4 border rounded max-w-xl mx-auto mt-8 shadow flex flex-col items-center space-y-4">


        <Dropdown
            data-cy="viewer-dropdown"
            symbols={symbols}
            selectedSymbol={selectedSymbol}
            onSymbolChange={handleSymbolChange}
            className="w-full sm:max-w-xs"
            placeholder="-- Choose Symbol --"
        />
        </div>

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

              <div data-cy="sentiment-chart-wrapper"
                   style={{ display: 'flex', gap: '20px', flexWrap: 'wrap' }}>
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