import React, { useState } from "react";
import SymbolSearch from "./components/SymbolSearch";
import TrackedSymbolDropdown from "./components/TrackedSymbolDropdown";
import SymbolChartViewer from "./components/SymbolChartViewer";
import NotificationPanel from "./components/NotificationPanel";
function App() {
  const [refreshKey, setRefreshKey] = useState(0);

  const handleUpdate = () => {
    setRefreshKey(prev => prev + 1);
  };

  return (
    <div className="App">
      <h1 className="text-center text-2xl font-bold my-4">Stock Sentiment Dashboard</h1>
      <SymbolSearch onSymbolAdded={handleUpdate} />
      <TrackedSymbolDropdown onSymbolRemoved={handleUpdate} refreshKey={refreshKey} />
      <SymbolChartViewer refreshKey={refreshKey} />
      <NotificationPanel />
    </div>
  );
}

export default App;
