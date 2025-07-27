import React, { useEffect, useState } from "react";
import axios from "axios";
import Button from "./Button";
import Dropdown from "./Dropdown";

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

  const handleSymbolChange = (symbol) => {
    setSelectedSymbol(symbol);
  };

  return (
      <div className="p-4 border rounded max-w-xl mx-auto mt-6 shadow">
        <h2 className="text-lg font-semibold mb-4">Remove Tracked Symbol</h2>
        <div className="p-4 border rounded max-w-xl mx-auto mt-8 shadow flex flex-col items-center space-y-4">
        <Dropdown
            symbols={symbols}
            selectedSymbol={selectedSymbol}
            onSymbolChange={handleSymbolChange}
            placeholder="-- Select Symbol to Remove --"
            className="w-full sm:max-w-xs"
        />

        {selectedSymbol && (
            <Button
                onClick={handleRemove}
                label="Remove"
                backgroundColor="skyblue"
                size="small"
                style={{ marginLeft: '8px' }}
            />
        )}
        </div>
      </div>
  );
};

export default TrackedSymbolDropdown;