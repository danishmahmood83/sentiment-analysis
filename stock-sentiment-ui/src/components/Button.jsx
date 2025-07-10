import React from 'react';
import PropTypes from "prop-types";


const Button = ({
                    children,
                    onClick,
                    disabled = false,
                    type = 'button',
                    ...props }) => {
    const sizeClasses = {
        small: 'px-3 py-1 text-sm',
        medium: 'px-4 py-2',
        large: 'px-6 py-3 text-lg'
    };

    const baseClasses = primary
        ? 'bg-blue-600 text-white hover:bg-blue-700'
        : 'bg-gray-200 text-gray-900 hover:bg-gray-300';

    const classes = `${baseClasses} ${sizeClasses[size]} rounded disabled:opacity-50 disabled:cursor-not-allowed transition-colors duration-200`;

    return (
        <button
            type={type}
            onClick={onClick}
            disabled={disabled}
            style={backgroundCOlor ? { backgroundColor } : {}}
            {...props}
        >
            {label || children}
        </button>
    );
};

Button.propTypes = {
    /** Is this the principal call to action on the page? */
    primary: PropTypes.bool,
    /** What background color to use */
    backgroundColor: PropTypes.string,
    /** How large should the button be? */
    size: PropTypes.oneOf(['small', 'medium', 'large']),
    /** Button contents */
    label: PropTypes.string.isRequired,
    /** Optional click handler */
    onClick: PropTypes.func,
};


export default Button;