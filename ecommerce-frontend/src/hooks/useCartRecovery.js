import { useState, useEffect, useCallback } from 'react';
// import { useContext } from 'react';
// import { CartContext } from '../context/CartContext.jsx';

export const useCartRecovery = (sessionToken, cartItems) => {
    const [showRecoveryModal, setShowRecoveryModal] = useState(false);
    const [lastActivity, setLastActivity] = useState(Date.now());
    const [isInactive, setIsInactive] = useState(false);

    const INACTIVITY_TIMEOUT = 5 * 60 * 1000; // 5 minutes in milliseconds

    // Reset activity timer
    const resetActivity = useCallback(() => {
        setLastActivity(Date.now());
        setIsInactive(false);
    }, []);

// Track user activity
    useEffect(() => {
        const events = ['mousedown', 'keydown', 'scroll', 'touchstart', 'mousemove'];

        const handleActivity = () => {
            resetActivity();
        };

        events.forEach(event => {
            window.addEventListener(event, handleActivity, { passive: true });
        });

        return () => {
            events.forEach(event => {
                window.removeEventListener(event, handleActivity);
            });
        };
    }, [resetActivity]);

// Check for inactivity
    useEffect(() => {
        const checkInactivity = setInterval(() => {
            const timeSinceLastActivity = Date.now() - lastActivity;

            if (timeSinceLastActivity >= INACTIVITY_TIMEOUT && cartItems.length > 0 && !isInactive) {
                setIsInactive(true);
                setShowRecoveryModal(true);
            }
        }, 1000); // Check every second

        return () => clearInterval(checkInactivity);
    }, [lastActivity, cartItems, isInactive]);

    const closeModal = () => {
        setShowRecoveryModal(false);
        resetActivity();
    };

    return {
        showRecoveryModal,
        closeModal,
        resetActivity,
        timeUntilInactive: Math.max(0, INACTIVITY_TIMEOUT - (Date.now() - lastActivity))
    };
};