/* eslint-disable no-undef */
const { defineConfig } = require("cypress");

module.exports = defineConfig({
  e2e: {
    baseUrl: "http://localhost:3000", // adjust based on your app's frontend port
    setupNodeEvents(_on, _config) {
      // implement node event listeners here if needed
    },
  },
});