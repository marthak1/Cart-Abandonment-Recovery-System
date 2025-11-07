/**
 * Updates the last cart activity timestamp
 */
export const updateLastCartActivity = () => {
    const timestamp = Date.now();
    localStorage.setItem('lastCartActivity', timestamp.toString());
    console.log('[Activity] Last cart activity updated:', new Date(timestamp).toISOString());
};

/**
 * Gets the last cart activity timestamp
 * @returns {number|null} Timestamp or null if not set
 */
export const getLastCartActivity = () => {
    const timestamp = localStorage.getItem('lastCartActivity');
    return timestamp ? parseInt(timestamp, 10) : null;
};

/**
 * Checks if cart is inactive for specified duration
 * @param {number} durationMs - Duration in milliseconds
 * @returns {boolean} True if inactive
 */
export const isCartInactive = (durationMs = 5 * 60 * 1000) => {
    const lastActivity = getLastCartActivity();

    if (!lastActivity) return false;

    const now = Date.now();
    const inactive = (now - lastActivity) >= durationMs;

    console.log('[Activity] Cart inactive check:', {
        lastActivity: new Date(lastActivity).toISOString(),
        now: new Date(now).toISOString(),
        inactive,
        durationMs
    });

    return inactive;
};

/**
 * Clears the last cart activity timestamp
 */
export const clearCartActivity = () => {
    localStorage.removeItem('lastCartActivity');
    console.log('[Activity] Cart activity cleared');
};
