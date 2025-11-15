//HOOK (To interact with cart)
//This lets any integration consume cart state via useCart(), it exposes context hook
import { useContext } from 'react';
import { CartContext } from '../context/CartContext.jsx';

/**
 * Custom hook to access cart context
 * @returns {Object} Cart context value
 */
export const useCart = () => {
    const context = useContext(CartContext);


    if (!context) {
        throw new Error('useCart must be used within a CartProvider');
    }

    return context;
};
