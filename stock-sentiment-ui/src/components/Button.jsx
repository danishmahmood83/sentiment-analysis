import React from 'react';

const Button = ({
                    children,
                    label,
                    primary = false,
                    size = 'medium',
                    onClick,
                    disabled = false,
                    type = 'button',
                    backgroundColor,
                    ...props
                }) => {
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
            className={classes}
            style={backgroundColor ? { backgroundColor } : {}}
            {...props}
        >
            {label || children}
        </button>
    );
};

export default Button;