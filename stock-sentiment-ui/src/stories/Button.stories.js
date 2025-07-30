import React from 'react';
import Button from '../components/Button';

export default {
  title: 'Components/Button',
  component: Button,
  argTypes: {
    backgroundColor: {
      control: { type: 'select' },
      options: [
        'bg-blue-500',
        'bg-green-600',
        'bg-red-500',
        'bg-gray-700',
        'bg-yellow-500',
      ],
    },
    size: {
      control: { type: 'select' },
      options: ['small', 'medium', 'large'],
    },
    onClick: { action: 'clicked' },
  },
};

const Template = (args) => <Button {...args} />;

export const Small = Template.bind({});
Small.args = {
  label: 'Small Button',
  size: 'small',
  backgroundColor: 'bg-blue-500',
};

export const Medium = Template.bind({});
Medium.args = {
  label: 'Medium Button',
  size: 'medium',
  backgroundColor: 'bg-green-600',
};

export const Large = Template.bind({});
Large.args = {
  label: 'Large Button',
  size: 'large',
  backgroundColor: 'bg-red-500',
};
