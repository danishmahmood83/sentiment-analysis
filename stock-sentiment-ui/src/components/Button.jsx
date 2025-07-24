import React from 'react';
import PropTypes from "prop-types";

const Button = ({
                    label,
                    size = "medium",
                    onClick,
                    backgroundColor = "ltgrey",
                    padding, // optional padding prop
                    ...props
                }) => {

    let scale = 1;
    if (size === "small") scale = 0.75;
    else if (size === "large") scale = 1.5;

    const computedPadding = padding !== undefined ? padding : `${scale * 0.5}rem ${scale * 1}rem`;

    const style = {
        backgroundColor,
        padding: padding || computedPadding,
        border: "none",
    };
    return (
        <button
            onClick={onClick} style={style} {...props}>
            {label}
        </button>
    );
}

Button.propTypes = {
    padding: PropTypes.string,
    backgroundColor: PropTypes.string,
    size: PropTypes.oneOf(['small', 'medium', 'large']),
    label: PropTypes.string.isRequired,
    onClick: PropTypes.func,
    style: PropTypes.object,
};


export default Button;