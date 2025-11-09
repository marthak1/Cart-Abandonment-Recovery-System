import React from 'react';
import { X, ShoppingCart, TrendingDown, Clock, AlertCircle } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../hooks/useCart';

export const RecoveryModal = () => {
    const navigate = useNavigate();
    const {
        showRecoveryModal,
        handleDismissRecovery,
        handleProceedToCheckout,
        cartItems,
        cartTotal
    } = useCart();

    if (!showRecoveryModal || cartItems.length === 0) return null;

    const handleCheckout = async () => {
        await handleProceedToCheckout();
        navigate('/cart');
    };

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center overflow-y-auto z-50 animate-in fade-in duration-300">
            <div className="bg-white rounded-lg shadow-xl max-w-md w-full p-4 overflow-y-auto animate-fadeIn max-h-screen  duration-300">
                {/* Header */}
                <div className="bg-gradient-to-r from-purple-600 to-blue-600 p-10 relative">
                    <button
                        onClick={handleDismissRecovery}
                        className="absolute top-2 right-2 text-white hover:bg-white hover:bg-opacity-20 rounded-full p-1 transition-colors"
                    >
                        <X className="w-5 h-5" />
                    </button>
                    <div className="flex items-center gap-3 text-white">
                        <div className="w-12 h-12 bg-white/20 rounded-full flex items-center justify-center">
                            <ShoppingCart className="w-7 h-7" />
                        </div>
                        <div>
                            <h2 className="text-2xl font-bold">Still Shopping?</h2>
                            <p className="text-purple-100 text-sm flex items-center gap-1">
                                <Clock className="w-4 h-4" />
                                Your cart has been waiting for you
                            </p>
                        </div>
                    </div>
                </div>

                {/* Content */}
                <div className="p-6">
                    {/* Urgency Banner */}
                    <div className="bg-yellow-50 border-2 border-yellow-200 rounded-lg p-4 mb-6 flex items-start gap-3">
                        <AlertCircle className="w-5 h-5 text-yellow-600 flex-shrink-0 mt-0.5" />
                        <div>
                            <p className="text-sm font-semibold text-yellow-900">
                                ⏰ Limited Time Offer!
                            </p>
                            <p className="text-xs text-yellow-700 mt-1">
                                Items in your cart are selling fast. Complete your purchase before they're gone!
                            </p>
                        </div>
                    </div>

                    {/* Cart Preview */}
                    <div className="space-y-3 mb-6 max-h-80 overflow-y-auto">
                        {cartItems.map((item) => {
                            const product = item.product || {};
                            const price = product.price || item.price || 0;
                            const imageUrl = product.imageUrl || item.imageUrl || 'https://via.placeholder.com/56';
                            const name = product.name || item.name || 'Product';

                            return (
                                <div key={item.id} className="flex gap-3 items-center p-2 hover:bg-gray-50 rounded-lg">
                                    <img
                                        src={imageUrl}
                                        alt={name}
                                        className="w-16 h-16 object-cover rounded-lg shadow-sm"
                                    />
                                    <div className="flex-1 min-w-0">
                                        <p className="font-semibold text-gray-900 text-sm truncate">
                                            {name}
                                        </p>
                                        <p className="text-xs text-gray-600">
                                            Qty: {item.quantity} • £{price.toFixed(2)} each
                                        </p>
                                    </div>
                                    <p className="font-bold text-purple-600">
                                        £{(price * item.quantity).toFixed(2)}
                                    </p>
                                </div>
                            );
                        })}
                    </div>

                    {/* Total Section */}
                    <div className="bg-gradient-to-r from-purple-50 to-blue-50 rounded-lg p-4 mb-6 border-2 border-purple-200">
                        <div className="flex justify-between items-center mb-2">
                            <span className="text-lg font-semibold text-gray-900">Cart Total:</span>
                            <span className="text-3xl font-bold text-purple-600">
                                £{cartTotal.toFixed(2)}
                            </span>
                        </div>
                        <p className="text-xs text-gray-600 flex items-center gap-1">
                            <TrendingDown className="w-3 h-3" />
                            Free shipping on orders over £50
                        </p>
                    </div>

                    {/* Action Buttons */}
                    <div className="space-y-3">
                        <button
                            onClick={handleCheckout}
                            className="w-full bg-gradient-to-r from-purple-600 to-blue-600 text-white font-bold py-4 rounded-lg hover:from-purple-700 hover:to-blue-700 transition-all transform hover:scale-105 shadow-lg hover:shadow-xl"
                        >
                            🛒 Complete My Purchase
                        </button>
                        <button
                            onClick={handleDismissRecovery}
                            className="w-full border-2 border-gray-300 text-gray-700 font-semibold py-3 rounded-lg hover:bg-gray-50 transition-colors"
                        >
                            Continue Shopping
                        </button>
                    </div>

                    {/* Trust Badges */}
                    <div className="mt-4 flex items-center justify-center gap-4 text-xs text-gray-500">
                        <span className="flex items-center gap-1">
                            🔒 Secure checkout
                        </span>
                        <span>•</span>
                        <span className="flex items-center gap-1">
                            📦 Free returns
                        </span>
                        <span>•</span>
                        <span className="flex items-center gap-1">
                            ⚡ Fast delivery
                        </span>
                    </div>
                </div>
            </div>
        </div>
    );
};