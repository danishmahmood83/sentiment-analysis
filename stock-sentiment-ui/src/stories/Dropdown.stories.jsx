import React, { useState } from 'react';
import Dropdown from '../components/Dropdown';

export default {
    title: 'Components/Dropdown',
    component: Dropdown,
    parameters: {
        docs: {
            description: {
                component: 'A dropdown for selecting stock symbols. Accepts an array of `{ id, symbol }` objects.',
            },
        },
    },
    argTypes: {
        symbols: {
            control: 'object',
            description: 'Array of symbol objects with `id` and `symbol` keys',
        },
        selectedSymbol: {
            control: false,
        },
        onSymbolChange: {
            control: false,
        },
        className: {
            control: 'text',
        },
        placeholder: {
            control: 'text',
        },
    },
};

const Template = (args) => {
    const [selectedSymbol, setSelectedSymbol] = useState('');
    return (
        <Dropdown
            {...args}
            selectedSymbol={selectedSymbol}
            onSymbolChange={setSelectedSymbol}
        />
    );
};

export const Default = Template.bind({});
Default.args = {
    symbols: [
        { id: 1, symbol: 'AAPL' },
        { id: 2, symbol: 'GOOGL' },
        { id: 3, symbol: 'MSFT' },
    ],
    className: '',
    placeholder: '-- Choose Symbol --',
};

Default.parameters = {
    docs: {
        controls: {
            include: ['symbols', 'className', 'placeholder'],
        },
    },
};
