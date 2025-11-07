//This component shows a modal to recover the cart after user inactivity or return visit.
import { useEffect, useState } from 'react';
import { useCart } from '../hooks/useCart.js';
import {getSessionToken} from "../utils/session.js";
import { fetchCart } from '../services/cartService.js';


const RecoveryModal = () => {
    const [showModal, setShowModal] = useState(false);
    // const [lastActivity, setLastActivity] = useState(Date.now());
    // const [ setLastActivity] = useState(Date.now());
    const [savedCart, setSavedCart] = useState([]);
    const { cartItems, restoreCart } = useCart();

    const INACTIVITY_TIMEOUT =  10 * 1000; // 5 minutes in milliseconds
    // const INACTIVITY_TIMEOUT = 10 * 1000; // 10 seconds for testing

    // Track user activity
    // useEffect(() => {
    //     const activities = ['mousedown', 'mousemove', 'keypress', 'scroll', 'touchstart', 'click'];
    //
    //     const resetTimer = () => {
    //         setLastActivity(Date.now());
    //     };
    //
    //     // Add event listeners for user activity
    //     activities.forEach((activity) => {
    //         document.addEventListener(activity, resetTimer);
    //     });
    //
    //     return () => {
    //         activities.forEach((activity) => {
    //             document.removeEventListener(activity, resetTimer);
    //         });
    //     };
    // }, [setLastActivity]);
    useEffect(() => {
        const activities = ['mousedown', 'mousemove', 'keypress', 'scroll', 'touchstart', 'click'];

        const resetTimer = () => {
            localStorage.setItem('lastCartActivity', Date.now().toString());
        };

        activities.forEach((activity) => {
            document.addEventListener(activity, resetTimer);
        });

        return () => {
            activities.forEach((activity) => {
                document.removeEventListener(activity, resetTimer);
            });
        };
    }, []);


    // Check for inactivity and saved cart
    // useEffect(() => {
    //     const checkInactivity = setInterval(() => {
    //         const now = Date.now();
    //         const timeSinceLastActivity = now - lastActivity;
    //
    //         // Check if user has been inactive for 5 minutes
    //         if (timeSinceLastActivity >= INACTIVITY_TIMEOUT) {
    //             const storedCart = localStorage.getItem('cart');
    //
    //             if (storedCart) {
    //                 const parsedCart = JSON.parse(storedCart);
    //
    //                 // Only show modal if there's a saved cart and current cart is empty or different
    //                 if (parsedCart.length > 0 && cartItems.length === 0) {
    //                     setSavedCart(parsedCart);
    //                     setShowModal(true);
    //                 }
    //             }
    //         }
    //     }, 30000); // Check every 30 seconds
    //
    //     return () => clearInterval(checkInactivity);
    // }, [lastActivity, cartItems, INACTIVITY_TIMEOUT]);
    useEffect(() => {
        const token = getSessionToken();
        const storedCart = localStorage.getItem('cart');
        const lastVisit = localStorage.getItem('lastVisit');
        const lastActivity = parseInt(localStorage.getItem('lastCartActivity'), 10) || 0;
        const now = Date.now();

        const isAbandoned = lastActivity && now - lastActivity > INACTIVITY_TIMEOUT;
        const isReturning = lastVisit && now - parseInt(lastVisit, 10) > INACTIVITY_TIMEOUT;

        if ((isAbandoned || isReturning) && storedCart && cartItems.length === 0) {
            fetchCart(token)
                .then((res) => {
                    const items = Array.isArray(res.data) ? res.data : res.data.items;
                    if (Array.isArray(items) && items.length > 0) {
                        setSavedCart(items);
                        setShowModal(true);
                    }
                })
                .catch((err) => {
                    console.error("Cart recovery failed:", err);
                });
        }
    }, [INACTIVITY_TIMEOUT, cartItems.length]);

    // Also check on component mount for returning users
    // useEffect(() => {
    //     const storedCart = localStorage.getItem('cart');
    //     const lastVisit = localStorage.getItem('lastVisit');
    //
    //     if (storedCart && lastVisit) {
    //         const parsedCart = JSON.parse(storedCart);
    //         const timeSinceLastVisit = Date.now() - parseInt(lastVisit, 10);
    //
    //         // If user returns after 5+ minutes and has items in saved cart but empty current cart
    //         if (timeSinceLastVisit >= INACTIVITY_TIMEOUT && parsedCart.length > 0 && cartItems.length === 0) {
    //             setSavedCart(parsedCart);
    //             setShowModal(true);
    //         }
    //     }
    // }, [INACTIVITY_TIMEOUT, cartItems.length]);


    // Update last visit time on unmount
    useEffect(() => {
        return () => {
            localStorage.setItem('lastVisit', Date.now().toString());
        };
    }, []);
    //
    // const handleRestore = () => {
    //     restoreCart();
    //     setShowModal(false);
    //     setSavedCart([]);
    // };
    const handleRestore = async () => {
        await restoreCart(); // now backend-aware
        setShowModal(false);
        setSavedCart([]);
    };


    // const handleDismiss = () => {
    //     setShowModal(false);
    //     setSavedCart([]);
    //     // Optionally clear the saved cart
    //     localStorage.removeItem('cart');
    // };
    const handleDismiss = () => {
        setShowModal(false);
        setSavedCart([]);
        localStorage.removeItem('cart');
    };


    if (!showModal) return null;

    const totalItems = savedCart.reduce((sum, item) => sum + item.qty, 0);
    const totalPrice = savedCart.reduce((sum, item) => sum + item.price * item.qty, 0);

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
            <div className="bg-white rounded-lg shadow-xl max-w-md w-full p-6 animate-fadeIn">
                <div className="flex items-center justify-between mb-4">
                    <h2 className="text-2xl font-bold text-gray-800">Welcome Back! 🛍️</h2>
                    <button
                        onClick={handleDismiss}
                        className="text-gray-400 hover:text-gray-600 text-2xl"
                        aria-label="Close modal"
                    >

                    </button>
                </div>

                <div className="mb-6">
                    <p className="text-gray-600 mb-4">
                        You have items waiting in your cart from your last visit.
                    </p>

                    <div className="bg-blue-50 rounded-lg p-4 mb-4">
                        <div className="flex justify-between items-center mb-2">
                            <span className="text-gray-700 font-medium">Items in cart:</span>
                            <span className="text-blue-600 font-bold">{totalItems}</span>
                        </div>
                        <div className="flex justify-between items-center">
                            <span className="text-gray-700 font-medium">Total value:</span>
                            <span className="text-green-600 font-bold text-lg">
                £{totalPrice.toFixed(2)}
              </span>
                        </div>
                    </div>

                    <div className="bg-gray-50 rounded-lg p-3 max-h-48 overflow-y-auto">
                        <p className="text-sm font-medium text-gray-700 mb-2">Cart preview:</p>
                        <ul className="space-y-2">
                            {savedCart.slice(0, 5).map((item) => (
                                <li key={item.id} className="flex justify-between text-sm">
                  <span className="text-gray-600 truncate mr-2">
                    {item.name}
                  </span>
                                    <span className="text-gray-500 whitespace-nowrap">
                    × {item.qty}
                  </span>
                                </li>
                            ))}
                            {savedCart.length > 5 && (
                                <li className="text-sm text-gray-500 italic">
                                    +{savedCart.length - 5} more item(s)
                                </li>
                            )}
                        </ul>
                    </div>
                </div>

                <div className="flex gap-3">
                    <button
                        onClick={handleRestore}
                        className="flex-1 bg-blue-600 text-white py-3 px-4 rounded-lg hover:bg-blue-700 transition font-semibold"
                    >
                        Restore Cart
                    </button>
                    <button
                        onClick={handleDismiss}
                        className="flex-1 bg-gray-200 text-gray-700 py-3 px-4 rounded-lg hover:bg-gray-300 transition"
                    >
                        Start Fresh
                    </button>
                </div>

                <p className="text-xs text-gray-500 text-center mt-4">
                    Your cart will be saved for your next visit
                </p>
            </div>

            <style jsx>{`
                @keyframes fadeIn {
                    from {
                        opacity: 0;
                        transform: scale(0.95);
                    }
                    to {
                        opacity: 1;
                        transform: scale(1);
                    }
                }

            `}</style>
        </div>
    );
};

export default RecoveryModal;
