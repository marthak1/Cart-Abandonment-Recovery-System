import { useState, useEffect, useCallback } from 'react';
import { checkRecoveryStatus, dismissRecovery, markCartRecovered } from '../services/cartService';

export const useCartRecovery = (sessionToken, cartItems) => {
    const [showRecoveryModal, setShowRecoveryModal] = useState(false);

    //  Poll backend every 10 seconds to check recovery flag
    useEffect(() => {
        if (!sessionToken || cartItems.length === 0) return;

        const checkRecovery = async () => {
            const status = await checkRecoveryStatus(sessionToken);

            if (status.showRecoveryModal && status.hasItems) {
                console.log("Backend flagged cart for recovery - showing modal");
                setShowRecoveryModal(true);
            }
        };

        // Check immediately
        checkRecovery();

        // Then check every 10 seconds
        const interval = setInterval(checkRecovery, 10000);

        return () => clearInterval(interval);
    }, [sessionToken, cartItems]);

    // Handle modal dismiss
    const handleDismiss = useCallback(async () => {
        setShowRecoveryModal(false);
        await dismissRecovery(sessionToken);
        console.log("Recovery modal dismissed");
    }, [sessionToken]);

    // Handle user proceeding to checkout
    const handleProceed = useCallback(async () => {
        setShowRecoveryModal(false);
        await markCartRecovered(sessionToken);
        console.log("Cart marked as recovered");
    }, [sessionToken]);

    return {
        showRecoveryModal,
        handleDismiss,
        handleProceed
    };
};