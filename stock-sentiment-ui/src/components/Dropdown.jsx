// SymbolDropdown.jsx
import React from 'react';

const Dropdown = ({
                            symbols,
                            selectedSymbol,
                            onSymbolChange,
                            className = "",
                            placeholder = "-- Choose Symbol --"
                        }) => {
    return (
        <select
            className={`w-full p-2 border rounded ${className}`}
            value={selectedSymbol}
            onChange={(e) => onSymbolChange(e.target.value)}
        >
            <option value="">{placeholder}</option>
            {symbols.map(item => (
                <option key={item.id} value={item.symbol}>
                    {item.symbol}
                </option>
            ))}
        </select>
    );
};

export default Dropdown;