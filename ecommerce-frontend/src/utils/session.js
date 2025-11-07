
/**
 * Generates a unique session token
 * @returns {string} UUID-style session token
 */
export const generateSessionToken = () => {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
        const r = (Math.random() * 16) | 0;
        const v = c === 'x' ? r : (r & 0x3) | 0x8;
        return v.toString(16);
    });
};

/**
 * Gets or creates a session token
 * @returns {string} Session token
 */
export const getSessionToken = () => {
    let token = localStorage.getItem('sessionToken');

    if (!token) {
        token = generateSessionToken();
        localStorage.setItem('sessionToken', token);
        console.log('[Session] New session token created:', token);
    } else {
        console.log('[Session] Existing session token:', token);
    }

    return token;
};

/**
 * Clears the current session token
 */
export const clearSessionToken = () => {
    localStorage.removeItem('sessionToken');
    console.log('[Session] Session token cleared');
};

/**
 * Validates session token format
 * @param {string} token - Token to validate
 * @returns {boolean} True if valid format
 */
export const isValidSessionToken = (token) => {
    if (!token || typeof token !== 'string') return false;

    // Check UUID format
    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    return uuidRegex.test(token);
};

