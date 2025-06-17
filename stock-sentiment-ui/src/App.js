import React from 'react';
import SentimentChart from './components/SentimentChart';

function App() {
  return (
    <div className="App">
      <h2>📈 Stock Sentiment Dashboard</h2>
      <SentimentChart symbol="AAPL" />
      <SentimentChart symbol="TSLA" />
      <SentimentChart symbol="NVDA" />
    </div>
  );
}
export default App;


