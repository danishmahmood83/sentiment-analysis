import React from 'react';
import PropTypes from 'prop-types';

const Button = ({
                    label,
                    size = 'medium',
                    onClick,
                    backgroundColor = 'bg-gray-200', // Tailwind color
                    className = '',
                    ...props
                }) => {
    const sizeClasses = {
        small: 'text-sm py-1 px-3',
        medium: 'text-base py-2 px-4',
        large: 'text-lg py-3 px-6',
    };

    return (
        <button
            onClick={onClick}
            className={`
        ${sizeClasses[size]}
        ${backgroundColor}
        text-white font-semibold rounded 
        focus:outline-none hover:opacity-90 transition
        ${className}
      `}
            {...props}
        >
            {label}
        </button>
    );
};

Button.propTypes = {
    label: PropTypes.string.isRequired,
    size: PropTypes.oneOf(['small', 'medium', 'large']),
    onClick: PropTypes.func,
    backgroundColor: PropTypes.string, // expects Tailwind class (e.g. 'bg-blue-500')
    className: PropTypes.string,
};

export default Button;
