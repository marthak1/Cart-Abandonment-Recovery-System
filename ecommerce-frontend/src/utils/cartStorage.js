/**
 * Saves cart to localStorage
 * @param {Array} cartItems - Cart items to save
 */
export const saveCartToStorage = (cartItems) => {
    try {
        localStorage.setItem('cart', JSON.stringify(cartItems));
        console.log('[Storage] Cart saved:', cartItems.length, 'items');
    } catch (err) {
        console.error('[Storage] Failed to save cart:', err);
    }
};

/**
 * Loads cart from localStorage
 * @returns {Array} Cart items or empty array
 */
export const loadCartFromStorage = () => {
    try {
        const stored = localStorage.getItem('cart');
        if (stored) {
            const parsed = JSON.parse(stored);
            console.log('[Storage] Cart loaded:', parsed.length, 'items');
            return parsed;
        }
    } catch (err) {
        console.error('[Storage] Failed to load cart:', err);
    }
    return [];
};

/**
 * Clears cart from localStorage
 */
export const clearCartFromStorage = () => {
    localStorage.removeItem('cart');
    console.log('[Storage] Cart cleared from storage');
};
