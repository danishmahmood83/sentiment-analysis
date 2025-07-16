import React, { useEffect, useState } from "react";
import axios from "axios";
import Button from "./Button";
import Dropdown from "./Dropdown";

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

  const handleSymbolChange = (symbol) => {
    setSelectedSymbol(symbol);
  };

  return (
      <div className="flex gap-2 items-center">
        <Dropdown
            symbols={symbols}
            selectedSymbol={selectedSymbol}
            onSymbolChange={handleSymbolChange}
            placeholder="-- Select Symbol to Remove --"
            className="flex-1"
        />

        {selectedSymbol && (
            <Button onClick={handleRemove} primary>
              Remove
            </Button>
        )}
      </div>
  );
};

export default TrackedSymbolDropdown;