//Product API Call
import api from "../api/api.js";

export const getAllProducts = async () => {
    try {
        const response = await api.get('/products');
        console.log("Service response:", response);
        return response;  // Return the full response object
    } catch (error) {
        console.error('Failed to fetch products:', error);
        throw error;
    }
};

export const getProductById = (id) => api.get(`/products/${id}`);

export const createProduct = (product) => api.post("/products", product);

export const updateProduct = (id, updatedProduct) =>
    api.put(`/products/${id}`, updatedProduct);

export const deleteProduct = (id) => api.delete(`/products/${id}`);
