import React from 'react';
import PropTypes from "prop-types";

const Dropdown = ({
                      symbols = [],
                      selectedSymbol,
                      onSymbolChange,
                      className = "",
                      placeholder = "-- Choose Symbol --",
                      ...props
                  }) => {
    const validSymbols = Array.isArray(symbols) ? symbols : [];

    return (
        <select
            className={`block w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm bg-white text-gray-900 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${className}`}
            value={selectedSymbol}
            onChange={(e) => onSymbolChange(e.target.value)}
            {...props}
        >
            <option value="">{placeholder}</option>
            {validSymbols.map(item => (
                <option key={item.id} value={item.symbol}>
                    {item.symbol}
                </option>
            ))}
        </select>
    );
};

Dropdown.propTypes = {
    symbols: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.oneOfType([PropTypes.number, PropTypes.string]).isRequired,
            symbol: PropTypes.string.isRequired,
        })
    ),
    selectedSymbol: PropTypes.string,
    onSymbolChange: PropTypes.func,
    className: PropTypes.string,
    placeholder: PropTypes.string,
};

export default Dropdown;
