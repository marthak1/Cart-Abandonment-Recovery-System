import axios from 'axios'

const API_BASE = import.meta.env.VITE_API_BASE || 'https://api.escuelajs.co/api/v1/'
// 'http://localhost:8080/api'

export const api = axios.create({
  baseURL: API_BASE,
  headers: { 'Content-Type': 'application/json' }
})

// Products
export const fetchProducts = () => api.get('/products')
export const fetchProduct = (id) => api.get(`/products/${id}`)