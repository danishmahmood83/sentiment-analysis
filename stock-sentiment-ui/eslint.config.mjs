import js from '@eslint/js';
import react from 'eslint-plugin-react';
import babelParser from '@babel/eslint-parser';

export default [
  js.configs.recommended,
  {
    files: ['**/*.stories.js', '**/*.stories.jsx'],
    languageOptions: {
      sourceType: 'module',
      ecmaVersion: 2022,
      globals: {
        jest: 'readonly',
        global: 'readonly',
        console: 'readonly',
        window: 'readonly',
        document: 'readonly',
      },
    },
    rules: {
      'no-undef': 'off', // optional: suppress all undefined warnings
    },
  },
  {
    files: ['**/__tests__/**/*.js', '**/*.test.js'],
    languageOptions: {
      sourceType: 'module',
      ecmaVersion: 2022,
      globals: {
        // Jest test globals
        describe: 'readonly',
        test: 'readonly',
        it: 'readonly',
        expect: 'readonly',
        beforeEach: 'readonly',
        afterEach: 'readonly',
        beforeAll: 'readonly',
        afterAll: 'readonly',
        jest: 'readonly',
        global: 'readonly',
        console: 'readonly',
      },
    },
  },
  {
    files: ['jest.config.js'],
    languageOptions: {
      sourceType: 'script',
      ecmaVersion: 2022,
    },
  },

  {
    files: ['**/*.config.js'],
    languageOptions: {
      sourceType: 'script',
      ecmaVersion: 2022,
    },
  },

  {
    files: ['.storybook/main.js'],
    languageOptions: {
      sourceType: 'module',
    },
  },

  {
    files: ['cypress/**/*.js', 'cypress/**/*.cy.js', 'cypress/**/*.spec.js'],
    languageOptions: {
      sourceType: 'script',
      ecmaVersion: 2022,
      globals: {
        cy: 'readonly',
        Cypress: 'readonly',
        describe: 'readonly',
        it: 'readonly',
        before: 'readonly',
        beforeEach: 'readonly',
        after: 'readonly',
        afterEach: 'readonly',
        context: 'readonly',
        expect: 'readonly',
      },
    },
    rules: {
      'no-undef': 'off',
    },
  },
  {
    files: ['**/*.js', '**/*.jsx'],
    languageOptions: {
      parser: babelParser,
      parserOptions: {
        ecmaVersion: 2022,
        sourceType: 'module',
        ecmaFeatures: { jsx: true },
        requireConfigFile: false,
      },
      globals: {
        fetch: 'readonly',
        console: 'readonly',
        setInterval: 'readonly',
        clearInterval: 'readonly',
        window: 'readonly',
        document: 'readonly',
      },
    },
    plugins: { react },
    rules: {
      'react/react-in-jsx-scope': 'off',
      'react/jsx-uses-react': 'error',
      'react/jsx-uses-vars': 'error',
      'no-unused-vars': ['warn', { argsIgnorePattern: '^_' }],
    },
  },
];
