// This module sets up an Axios instance with interceptors for logging requests and responses.
import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_APP_API_URL  || 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
    timeout: 10000,
});

// Request interceptor
api.interceptors.request.use(
    (config) => {
        console.log(`[API Request] ${config.method?.toUpperCase()} ${config.url}`);
        return config;
    },
    (error) => {
        console.error('[API Request Error]', error);
        return Promise.reject(error);
    }
);

// Response interceptor
//success handler
api.interceptors.response.use(
    (response) => {
        console.log(`[API Response] ${response.config.url}`, response.status);
        return response;
    },
    //error handler
    (error) => {
        console.error('[API Response Error]', {
            url: error.config?.url,
            status: error.response?.status,
            data: error.response?.data,
            message: error.message,
        });
        return Promise.reject(error);
    }
);

export default api;