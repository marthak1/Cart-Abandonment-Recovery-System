import api from "../api/api.js";
// Cart Service API calls: Create, Fetch, Update, Clear, Delete, Checkout, etc.
export const fetchCart = async (sessionToken) => {
    try {
        const response = await api.get(`/cart/${sessionToken}`, {
            headers: {
                "X-Session-Token": String(sessionToken),
            },
        });

        return {
            success: true,
            data: response.data,
        };
    } catch (error) {
        console.error("Fetch cart failed:", error?.response?.data || error.message);
        return {
            success: false,
            error: error?.response?.data?.message || "Unknown error",
        };
    }
};

export const addItemToCart = async (sessionToken, productId, quantity = 1) => {
    console.log(typeof productId, productId);
    const payload = {
        productId: productId.id || productId,
        quantity,
    };

    try {
        const response = await api.post(`/cart/addItem`, payload, {
            headers: {
                "X-Session-Token": String(sessionToken),
            },
        });

        // Backend should return: { items: [...], total: 0 }
        return response.data;

    } catch (error) {
        console.error("Add to cart failed:", error?.response?.data || error.message);
        throw error; // Let caller handle the error
    }
};
/**
 * Update item quantity — expects sessionToken and productId as path params, quantity in body
 * PUT /cart/{sessionToken}/items/{productId}
 */
export const updateItemQuantity = async (sessionToken, productId, quantity) => {
    try {
        console.log(typeof productId, productId);

        const response = await api.put(
            `/cart/${sessionToken}/item/${productId}`,
            { quantity },
            {
                headers: {
                    "X-Session-Token": sessionToken,
                },
            }

        );

        console.log("Update quantity response:", response.data );
        return response.data; // CartDTO with { items: [...], total: 0 }

    } catch (error) {
        console.error("Update quantity failed:", error?.response?.data|| error.message);
        throw error;
    }
};

export const removeItemFromCart = async (sessionToken, productId) => {
    try {
        console.log("🗑️ Removing item:", { sessionToken, productId });

        const response = await api.delete(
            `/cart/${sessionToken}/item/${productId}`,
            {
                headers: {
                    "X-Session-Token": sessionToken
                },
            }
        );

        console.log("Remove item response:", response.data);

        // Backend should return updated CartDTO
        return response.data; // { items: [...], total: 0 }

    } catch (error) {
        console.error("Remove item failed:", error?.response?.data || error.message);
        throw error;
    }
};
/**
 * Calculate cart total — expects sessionToken as path param
 * GET /cart/{sessionToken}/total
 */
export const calculateCartTotal = async (sessionToken) => {
    try {
        const response = await api.get(`/cart/${sessionToken}/total`);
        console.log("Cart total response:", response);
        // Optional: validate response structure
        if (response?.data !== undefined) {


            return response.data;
        } else {
            throw new Error("Unexpected response format: 'total' not found.");
        }
    } catch (error) {
        console.error("Error calculating cart total:", error.message || error);

        // Optional: rethrow or return a fallback value
        throw new Error("Failed to retrieve cart total. Please try again later.");
    }
};
/**
 * Check if cart is inactive — expects sessionToken as path param and thresholdSeconds as query param
 * GET /cart/{sessionToken}/inactive?thresholdSeconds=300
 */
export const isCartInactive = async (sessionToken, thresholdSeconds) => {
    return api.get(`/cart/${sessionToken}/inactive`, {
        params: { thresholdSeconds },
    });
};

export const clearCartAPI = async (sessionToken) => {
    await api.post("/cart/clearCart", sessionToken);
    return { items: [], total: 0 }; // Return empty cart
};

// Delete Cart
export const deleteCartAPI = async (sessionToken) => {
    try {
        await api.delete(`/cart/${sessionToken}/delete`);
        return true; // Cart deleted
    } catch (error) {
        console.error("Failed to delete cart:", error.message || error);
        throw new Error("Could not delete cart.");
    }
};

/**
 * Checkout cart — expects sessionToken as path param
 * POST /cart/{sessionToken}/checkout
 */
// export const checkoutCart = async (sessionToken) => {
//   return api.post(`/cart/${sessionToken}/checkout`);
// };



