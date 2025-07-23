import React from 'react';
import Button from '../components/Button';

export default {
  title: 'Components/Button',
  component: Button,
  argTypes: {
    backgroundColor: { control: 'color' },
    size: {
      control: { type: 'select' },
      options: ['small', 'medium', 'large'],
    },
    padding: {
      control: 'text',
      description: 'Custom CSS padding, e.g. "1rem 2rem"',
    },
    onClick: { action: 'clicked' },
  },
};

const Template = (args) => <Button {...args} />;

export const Small = Template.bind({});
Small.args = {
  label: 'Small Button',
  size: 'small',
};

export const Medium = Template.bind({});
Medium.args = {
  label: 'Medium Button',
  size: 'medium',
};

export const Large = Template.bind({});
Large.args = {
  label: 'Large Button',
  size: 'large',
};

export const CustomPadding = Template.bind({});
CustomPadding.args = {
  label: 'Custom Padded Button',
  size: 'medium',
  padding: '1rem 2rem',
};
