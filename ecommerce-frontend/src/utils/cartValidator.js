export const isValidCart = (cart) => {
    if (!cart) {
        console.error("Cart is null or undefined");
        return false;
    }

    if (!Array.isArray(cart?.items)) {
        console.error("Cart.items is not an array:", cart);
        return false;
    }

    if (typeof cart.total !== 'number') {
        console.warn("Cart.total is not a number:", cart.total);
        // Still return true as this might be optional
    }
    if(cart.items.length === 1)
        console.warn(`Cart is empty array`);

    return true;
};