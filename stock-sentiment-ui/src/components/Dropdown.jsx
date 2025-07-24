import React from 'react';
import PropTypes from "prop-types";

const Dropdown = ({
                      symbols = [],
                      selectedSymbol,
                      onSymbolChange,
                      className = "",
                      placeholder = "-- Choose Symbol --"
                  }) => {
    const validSymbols = Array.isArray(symbols) ? symbols : [];

    return (
        <select
            className={`w-full p-2 border rounded ${className}`}
            value={selectedSymbol}
            onChange={(e) => onSymbolChange(e.target.value)}
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
