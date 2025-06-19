import React from "react";
import SymbolSearch from "./components/SymbolSearch";
import TrackedSymbolDropdown from './components/TrackedSymbolDropdown';
import SymbolChartViewer from './components/SymbolChartViewer';

function App() {
  return (
    <div className="App">
     <h1 className="text-center text-2xl font-bold my-4">Stock Sentiment Dashboard</h1>
      <SymbolSearch />
      <TrackedSymbolDropdown />
      <SymbolChartViewer />
    </div>
  );
}

export default App;

