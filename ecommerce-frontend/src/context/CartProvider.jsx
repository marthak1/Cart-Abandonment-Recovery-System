// // CONTEXT & PROVIDER (Cart State Management)
// This file provides the CartProvider integration that uses CartContext to manage cart state and actions.
// // //context setup for cart state management and operations

import React, { useCallback, useEffect, useState } from "react";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { CartContext } from "./CartContext.jsx";
import { getSessionToken } from "../utils/session.js";
import { saveCartToStorage, loadCartFromStorage } from '../utils/cartStorage';
import { updateLastCartActivity } from "../utils/updateCartActivity.js";
import {
    addItemToCart,
    fetchCart,
    updateItemQuantity,
    removeItemFromCart,
    clearCartAPI,
    dismissRecovery,
    markCartRecovered
} from "../services/cartService.js";
import { isValidCart } from "../utils/cartValidator.js";

export const CartProvider = ({ children }) => {
    const [cartItems, setCartItems] = useState(() => loadCartFromStorage()); // new
    // const [cartItems, setCartItems] = useState([]); // remove
    const [cartTotal, setCartTotal] = useState(0);
    const [loading, setLoading] = useState(false);
    const [cartStatus, setCartStatus] = useState("EMPTY");
    const [recoveryFlag, setRecoveryFlag] = useState(false);
    const [showRecoveryModal, setShowRecoveryModal] = useState(false);

    const sessionToken = getSessionToken();

    // Load cart from backend
    const hydrateCart = useCallback(async () => {
        if (!sessionToken) return;

        try {
            setLoading(true);
            const cart = await fetchCart(sessionToken);

            console.log("Hydrated cart:", {
                items: cart.items?.length,
                total: cart.total,
                status: cart.status,
                recoveryFlag: cart.recoveryFlag
            });

            setCartItems(cart.items ?? []);
            setCartTotal(cart.total ?? 0);
            setCartStatus(cart.status ?? "ACTIVE");
            setRecoveryFlag(cart.recoveryFlag ?? false);

            //Show modal if recovery flag is true and cart has items
            if (cart.recoveryFlag && cart.items?.length > 0) {
                console.log("Recovery flag detected - showing modal");
                setShowRecoveryModal(true);
            }

            updateLastCartActivity();

            return cart;
        } catch (error) {
            console.error("Cart hydration error:", error);
            setRecoveryFlag(false);
            return { items: [], total: 0, status: "ERROR" };
        } finally {
            setLoading(false);
        }
    }, [sessionToken]);

    // Poll for recovery flag every 10 seconds
    useEffect(() => {
        if (!sessionToken || cartItems.length === 0) return;

        console.log("Starting recovery flag polling...");

        // Check immediately
        hydrateCart();

        // Then poll every 10 seconds
        const interval = setInterval(() => {
            console.log("Checking for recovery flag...");
            hydrateCart();
        }, 10000); // 10 seconds

        return () => {
            console.log("Stopping recovery flag polling");
            clearInterval(interval);
        };
    }, [sessionToken, cartItems.length, hydrateCart]);

    // --- Load cart on mount (localStorage first, then backend) ---
    useEffect(() => {
        const localCart = loadCartFromStorage(); // new
        if (localCart.length > 0) {
            console.log("[Storage] Hydrated cart from localStorage:", localCart.length, "items");
            setCartItems(localCart);
            setCartTotal(localCart.reduce((sum, item) => sum + item.price * item.quantity, 0));
            setCartStatus("ACTIVE");
        }
        hydrateCart();
    }, [hydrateCart]);

    // Handle modal dismiss
    const handleDismissRecovery = async () => {
        console.log("User dismissed recovery modal");
        setShowRecoveryModal(false);
        setRecoveryFlag(false);

        try {
            await dismissRecovery(sessionToken);
        } catch (error) {
            console.error("Failed to dismiss recovery:", error);
        }
    };

    // Handle proceed to checkout
    const handleProceedToCheckout = async () => {
        console.log("User proceeding to checkout from recovery modal");
        setShowRecoveryModal(false);
        setRecoveryFlag(false);

        try {
            await markCartRecovered(sessionToken);
        } catch (error) {
            console.error("Failed to mark cart as recovered:", error);
        }
    };

    // Add item
    const addToCart = async (productId, quantity = 1) => {
        try {
            const cart = await addItemToCart(sessionToken, productId, quantity);

            if (!isValidCart(cart)) {
                console.warn("Invalid cart structure — triggering rollback.");
                await hydrateCart();
                return;
            }

            setCartItems(cart.items);
            setCartTotal(cart.total || 0);
            setCartStatus(cart.status || "ACTIVE");
            const lastItem = cart.items[cart.items.length - 1];
            toast.success(`${lastItem.product.name} added to cart!`, {
                position: "top-right",
                autoClose: 3000,
            });


            updateLastCartActivity();
        } catch (err) {
            console.error("Failed to add item:", err.message || err);
            await hydrateCart();
        }
    };

    // Update quantity
    const updateQuantity = async (productId, quantity) => {
        try {
            setCartItems((prev) =>
                prev.map((item) =>
                    item.productId === productId ? { ...item, quantity } : item
                )
            );
            updateLastCartActivity();

            const cart = await updateItemQuantity(sessionToken, productId, quantity);

            setCartItems(cart.items || []);
            setCartTotal(cart.total || 0);
            setCartStatus(cart.status || "ACTIVE");
        } catch (err) {
            console.error("Failed to update quantity:", err.message || err);
            await hydrateCart();
        }
    };

    const removeItem = async (productId) => {
        if (!sessionToken) {
            console.error("No session token available");
            return;
        }

        try {
            // Optimistic update
            setCartItems(prev => prev.filter(item => item.productId !== productId));

            const updatedCart = await removeItemFromCart(sessionToken, productId);

            // Only update if backend returned valid data AND it's actually different
            if (updatedCart && Array.isArray(updatedCart.items)) {
                // Verify the item is actually gone from backend response
                const itemStillExists = updatedCart.items.some(item => item.productId === productId);

                if (itemStillExists) {
                    console.error("Backend failed to remove item");
                    await hydrateCart(); // Fallback to refresh
                } else {
                    setCartItems(updatedCart.items);
                    setCartTotal(updatedCart.total || 0);
                }
            } else {
                await hydrateCart();
            }
        } catch (error) {
            console.error("Failed to remove item:", error);
            await hydrateCart();
        }
    };
    const clearCart = async () => {
        if (!sessionToken) return;

        try {
            setCartItems([]);
            setCartTotal(0);
            await clearCartAPI(sessionToken);
        } catch (error) {
            console.error('Failed to clear cart:', error);
            await hydrateCart();
        }
    };

    const restoreCart = async () => {
        const token = getSessionToken();
        const freshCart = await fetchCart(token);
        setCartItems(freshCart.items ?? []);
        setCartTotal(freshCart.total ?? 0);
        setCartStatus(freshCart.status ?? "ACTIVE");
    };
    const cartCount = cartItems.reduce((sum, item) => sum + item.quantity, 0);

    useEffect(() => {
        saveCartToStorage(cartItems);
    }, [cartItems]);
    useEffect(() => {
        const saved = localStorage.getItem("cart");
        if (saved) {
            setCartItems(JSON.parse(saved));
        }
    }, []);
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
                restoreCart,
                loading,
                recoveryFlag,
                showRecoveryModal, // Expose modal state
                handleDismissRecovery, // Expose dismiss handler
                handleProceedToCheckout, // Expose proceed handler
                clearCart,
                cartCount,
            }}
        >
            {children}
        </CartContext.Provider>
    );
};

