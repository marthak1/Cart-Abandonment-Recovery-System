// import { defineConfig } from 'vite'
// import react from '@vitejs/plugin-react'
//
// // https://vite.dev/config/
// export default defineConfig({
//   plugins: [react()],
//   test: {
//     environment: 'jsdom', // for React DOM testing
//     globals: true,         // optional: Jest-like global APIs
//     setupFiles: './src/setupTests.js', // optional: for global setup (see below)
//   },
//     server: {
//         proxy: {
//             '/cartItems': 'http://localhost:8080',
//         },
//     },
// })

import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
    plugins: [react()],
    test: {
        globals: true,
        environment: 'jsdom',
        setupFiles: './src/test/setup.js',
        css: true,
        coverage: {
            provider: 'v8',
            reporter: ['text', 'json', 'html'],
            exclude: [
                'node_modules/',
                'src/test/',
                '**/*.spec.js',
                '**/*.test.js'
            ],
            thresholds: {
                branches: 80,
                functions: 80,
                lines: 80,
                statements: 80
            }
        }


    },
    server: {
        proxy: {
            '/cartItems': 'http://localhost:8080',
        },
    },

})
