// // //This file provides the CartProvider component that uses CartContext to manage cart state and actions.
// // //context setup for cart state management and operations

import React, {useCallback, useEffect, useState} from "react"; //useMemo,
import { CartContext } from "./CartContext.jsx";
import { getSessionToken } from "../utils/session.js";
import { updateLastCartActivity } from "../utils/updateCartActivity.js";
import {
    addItemToCart,
    fetchCart,
    updateItemQuantity,
    removeItemFromCart,
    clearCartAPI
} from "../services/cartService.js";
import {isValidCart} from "../utils/cartValidator.js";

export const CartProvider = ({ children }) => {
    const [cartItems, setCartItems] = useState([]);
    const [cartTotal, setCartTotal] = useState(0);
    const [loading, setLoading] = useState(false);
    const [cartStatus, setCartStatus] = useState("EMPTY");
    const sessionToken = getSessionToken();

// Load cart from backend
    const hydrateCart = useCallback(async () => {
        if (!sessionToken) return;

        try {
            setLoading(true);
            const cart = await fetchCart(sessionToken);

            console.log("💧 Hydrated cart:", cart);

            setCartItems(cart.items || []);
            setCartTotal(cart.total || 0);
        } catch (error) {
            console.error('Failed to hydrate cart:', error);
        } finally {
            setLoading(false);
        }
    }, [sessionToken]);

    // Load cart on mount
    useEffect(() => {
        hydrateCart();
    }, [hydrateCart]);

    // Add item
    const addToCart = async (productId, quantity = 1) => {
        try {
            const cart = await addItemToCart(sessionToken, productId, quantity);

            if (!isValidCart(cart)) {
                console.warn("Invalid cart structure — triggering rollback.");
                await hydrateCart(sessionToken);
                return;
            }

            setCartItems(cart.items);
            setCartTotal(cart.total || 0);
            setCartStatus(cart.status || "ACTIVE");
            updateLastCartActivity();
        } catch (err) {
            console.error("Failed to add item:", err.message || err);
            await hydrateCart(sessionToken); // rollback
        }
    };



    // Update quantity
    const updateQuantity = async (productId, quantity) => {
        try {
            // Optimistic UI update
            setCartItems((prev) =>
                prev.map((item) =>
                    item.productId === productId ? { ...item, quantity } : item
                )
            );
            updateLastCartActivity();

            // Confirm with backend
            const cart = await updateItemQuantity(sessionToken, productId, quantity);

            // Reconcile with backend
            setCartItems(cart.items || []);
            setCartTotal(cart.total || 0);
            setCartStatus(cart.status || "ACTIVE");
        } catch (err) {
            console.error("Failed to update quantity:", err.message || err);
            await hydrateCart(); // rollback
        }
    };

// Remove item function
    const removeItem = async (productId) => {
        if (!sessionToken) {
            console.error("No session token available");
            return;
        }

        console.log("🗑️ Removing item with productId:", productId);

        try {
            // Optimistic UI update - Remove immediately
            setCartItems(prev => {
                const filtered = prev.filter(item => {
                    const itemProductId = item.product?.id || item.productId;
                    return itemProductId !== productId;
                });
                console.log("Optimistic removal - items left:", filtered.length);
                return filtered;
            });

            // Call backend API
            const updatedCart = await removeItemFromCart(sessionToken, productId);

            console.log("Backend response:", updatedCart);

            // Sync with backend response (reconciliation)
            if (updatedCart && Array.isArray(updatedCart.items)) {
                setCartItems(updatedCart.items);
                setCartTotal(updatedCart.total || 0);
                console.log("Cart synced - " + updatedCart.items.length + " items remaining");
            } else {
                console.warn("Invalid response, triggering full refresh");
                await hydrateCart();
            }

        } catch (error) {
            console.error("Failed to remove item:", error);
            // Rollback by refreshing from backend
            await hydrateCart();
        }
    };

    // Clear cart
    const clearCart = async () => {
        // 1. Optimistically clear cart
        setCartItems([]);
        setCartTotal(0);

        try {
            // 2. Confirm with backend
            const res = await clearCartAPI({ sessionToken, items: [] });

            // 3. Reconcile
            setCartItems(res.data.items);
            setCartTotal(res.data.total);
        } catch (err) {
            console.error("Failed to clear cart:", err);
            await hydrateCart(); // rollback
        }
    };


    //
    // useEffect(() => {
    //
    // }, []);



    return (
        <CartContext.Provider
            value={{
                cartItems,
                cartTotal,
                cartStatus,
                hydrateCart,
                addToCart,
                removeItem,
                updateQuantity,
                loading,
                // checkout,
               // updateEntireCart,
                clearCart,
              // deleteCart,
                // checkInactivity,
               // assignSession,
            }}
        >
            {children}
        </CartContext.Provider>
    );
};
