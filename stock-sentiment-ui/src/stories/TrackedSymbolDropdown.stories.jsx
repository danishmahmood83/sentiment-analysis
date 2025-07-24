import React, { useState } from 'react';
import TrackedSymbolDropdown from '../components/TrackedSymbolDropdown';
import Dropdown from '../components/Dropdown';

export default {
    title: 'Components/TrackedSymbolDropdown',
    component: TrackedSymbolDropdown,
    parameters: {
        docs: {
            source: {
                type: 'code', // disables auto-render preview at the top
            },
        },
    },
};

const MockTrackedSymbolDropdown = ({ initialSymbol = 'TSLA', symbols = [] }) => {
    const [selectedSymbol, setSelectedSymbol] = useState(initialSymbol);

    const handleRemove = () => {
        alert(`Removing symbol: ${selectedSymbol}`);
        setSelectedSymbol('');
    };

    return (
        <div style={{ display: 'flex', alignItems: 'center' }}>
            <Dropdown
                symbols={symbols}
                selectedSymbol={selectedSymbol}
                onSymbolChange={setSelectedSymbol}
                placeholder="-- Select Symbol to Remove --"
            />
            {selectedSymbol && (
                <button
                    onClick={handleRemove}
                    style={{
                        backgroundColor: 'skyblue',
                        padding: '0.5rem 1rem',
                        marginLeft: '8px',
                        border: 'none',
                    }}
                >
                    Remove
                </button>
            )}
        </div>
    );
};

export const SelectedTSLA = () => (
    <MockTrackedSymbolDropdown
        initialSymbol="TSLA"
        symbols={[
            { id: 1, symbol: 'AAPL' },
            { id: 2, symbol: 'GOOGL' },
            { id: 3, symbol: 'TSLA' },
        ]}
    />
);

export const UnselectedState = () => {
    const [selected, setSelected] = useState(''); // No symbol selected

    return (
        <TrackedSymbolDropdown
            symbols={[
                { id: 1, symbol: 'AAPL' },
                { id: 2, symbol: 'GOOGL' },
                { id: 3, symbol: 'TSLA' },
            ]}
            selectedSymbol={selected}
            onSymbolChange={setSelected}
            onSymbolRemoved={() => alert('Nothing to remove')}
        />
    );
};

