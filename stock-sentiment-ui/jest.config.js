/* eslint-disable no-undef */

module.exports = {
  transform: {
    '^.+\\.(js|jsx)$': 'babel-jest'
  },
  testEnvironment: 'jsdom',
  moduleNameMapper: {
    '\\.(css|less|scss)$': 'identity-obj-proxy'
  },
  transformIgnorePatterns: [
    '/node_modules/(?!axios)/'  // allow axios to be transformed
  ]
};
