import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  test: {
    environment: 'jsdom', // for React DOM testing
    globals: true,         // optional: Jest-like global APIs
    setupFiles: './src/setupTests.js', // optional: for global setup (see below)
  },
})
