// SymbolChartViewer.jsx
import React, { useEffect, useState } from "react";
import SentimentChart from "./SentimentChart";
import Dropdown from "./Dropdown";

const meta = {
  title: 'Symbol Chart Viewer',
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
      <div className="p-4 max-w-4xl mx-auto">
        <h2 className="text-xl font-bold mb-2">Select Symbol to View Sentiment</h2>

        <Dropdown
            symbols={symbols}
            selectedSymbol={selectedSymbol}
            onSymbolChange={handleSymbolChange}
            className="mb-4"
            placeholder="-- Choose Symbol --"
        />

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