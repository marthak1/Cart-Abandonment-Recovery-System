// This component displays the contents of the shopping cart and allows users to modify item quantities or remove items.
import { useCart } from "../hooks/useCart.js";
import { ShoppingCart, Trash2, Plus, Minus, CreditCard, Package } from "lucide-react";
const CartViewer = () => {
    const { cartItems, removeItem, clearCart, updateQuantity,cartTotal} = useCart();
    console.log("Validate cartItem Structure:",  cartItems);
    const handleQuantityChange = ( item, newQuantity) => {
        if (newQuantity < 1) {
            // Remove item if quantity is 0
            removeItem(item);
            return;
        }

        // Get the product ID correctly
        console.log("Cart item:", item);
        const productId = item;


        console.log("Updating quantity:", { productId, newQuantity });
        updateQuantity(productId, newQuantity);
    };

    const handleClearCart = async () => {
        try {
            await clearCart(); // Calls backend and resets cart state
            console.log("Cart cleared successfully");
        } catch (error) {
            console.error("Failed to clear cart:", error.message || error);
        }
    };

    const handleRemoveItem = async (item) => {
        // Get product ID safely
        const productId = item;

        if (!productId) {
            console.error("Cannot remove item - no product ID:", item);
            return;
        }

        console.log("Removing item:", {
            itemId: item,
            productId: productId
        });

        // Confirm before removing
        const confirmed = window.confirm(
            `Remove ${item.product?.name || item.name} from cart?`
        );

        if (confirmed) {
            await removeItem(productId);
        }
    };


    return (
        <div className="max-w-2xl mx-auto bg-white shadow-xl rounded-2xl overflow-hidden mt-6">
            {/* Header */}
            <div className="bg-gradient-to-r from-purple-600 to-blue-600 p-6">
                <div className="flex items-center gap-3 text-white">
                    <ShoppingCart className="w-8 h-8" />
                    <div>
                        <h2 className="text-2xl font-bold">Shopping Cart</h2>
                        <p className="text-purple-100 text-sm">
                            {cartItems.length} {cartItems.length === 1 ? "item" : "items"} in your cart
                        </p>
                    </div>
                </div>
            </div>

            {/* Cart Content */}
            <div className="p-6">
                {cartItems.length === 0 ? (
                    <div className="text-center py-16">
                        <Package className="w-20 h-20 mx-auto mb-4 text-gray-300" />
                        <p className="text-gray-500 text-lg font-medium mb-2">Your cart is empty</p>
                        <p className="text-gray-400 text-sm">Add some items to get started!</p>
                    </div>
                ) : (
                    <>
                        <ul className="space-y-4 mb-6">

                            {cartItems.map((item) => (

                                <li
                                    key={item.productId || item.id}
                                    className="flex gap-4 p-4 border border-gray-200 rounded-xl hover:shadow-md transition-all duration-200 bg-gray-50"
                                >






                                    {/* Product Image */}
                                    <img
                                        src={item.product?.imageUrl || item.imageUrl}
                                        alt={item.product?.name || item.name}
                                        className="w-28 h-28 object-cover rounded-lg shadow-sm"
                                    />

                                    {/* Product Details */}
                                    <div className="flex-1 flex flex-col justify-between">
                                        <div>
                                            <h4 className="text-lg font-semibold text-gray-900 mb-1">
                                                {item.product?.name || item.name}
                                            </h4>
                                            <p className="text-sm text-gray-600">
                                                Unit Price: <span className="font-medium">£{item.price.toFixed(2)}</span>
                                            </p>
                                        </div>

                                        {/* Quantity Controls */}
                                        <div className="flex items-center gap-4 mt-3">
                                            <div className="flex items-center gap-2">
                                                <span className="text-sm font-medium text-gray-700">Qty:</span>
                                                <div className="flex items-center border border-gray-300 rounded-lg overflow-hidden bg-white shadow-sm">
                                                    <button
                                                        onClick={() => handleQuantityChange(item.productId, item.quantity - 1)}
                                                        className="p-2 hover:bg-gray-100 transition-colors"
                                                        disabled={item.quantity <= 1}
                                                    >
                                                        <Minus className="w-4 h-4 text-gray-600" />
                                                    </button>
                                                    <input
                                                        className="w-16 px-3 py-2 text-center text-gray-900 font-medium focus:outline-none focus:ring-2 focus:ring-purple-500 border-x border-gray-300"
                                                        type="number"
                                                        min="1"
                                                        value={item.quantity}
                                                        onChange={(e) =>
                                                            handleQuantityChange(item.productId, parseInt(e.target.value, 10) || 1)
                                                        }
                                                    />
                                                    <button
                                                        onClick={() => handleQuantityChange(item.productId, item.quantity + 1)}
                                                        className="p-2 hover:bg-gray-100 transition-colors"
                                                    >
                                                        <Plus className="w-4 h-4 text-gray-600" />
                                                    </button>
                                                </div>
                                            </div>

                                            <div className="flex-1" />

                                            {/* Item Total */}
                                            <p className="text-lg font-bold text-purple-600">
                                                £{(item.price * item.quantity).toFixed(2)}
                                            </p>
                                        </div>
                                    </div>

                                    {/* Remove Button */}
                                    <button
                                        onClick={() => handleRemoveItem(item.productId)}
                                        className="self-start p-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors group"
                                        title="Remove item"
                                    >
                                        <Trash2 className="w-5 h-5 group-hover:scale-110 transition-transform" />
                                    </button>
                                </li>
                            ))}
                        </ul>

                        {/* Cart Summary */}
                        <div className="border-t-2 border-gray-200 pt-6">
                            {/* Subtotal breakdown */}
                            <div className="space-y-2 mb-4">
                                <div className="flex justify-between text-gray-700">
                                    <span>Subtotal ({cartItems.length} items)</span>
                                    <span className="font-medium">£{cartTotal.toFixed(2)}</span>
                                </div>
                                <div className="flex justify-between text-gray-700">
                                    <span>Shipping</span>
                                    <span className="font-medium text-green-600">FREE</span>
                                </div>
                            </div>

                            {/* Total */}
                            <div className="flex justify-between items-center py-4 bg-gradient-to-r from-purple-50 to-blue-50 rounded-lg px-4 mb-6">
                                <span className="text-xl font-bold text-gray-900">Total:</span>
                                <span className="text-2xl font-bold text-purple-600">
                  £{cartTotal?.toFixed(2)}
                </span>
                            </div>

                            {/* Action Buttons */}
                            <div className="flex flex-col sm:flex-row gap-3">
                                <button
                                    onClick={handleClearCart}
                                    className="flex-1 py-3 px-6 rounded-lg border-2 border-red-300 text-red-600 font-semibold hover:bg-red-50 hover:border-red-400 transition-all duration-200 flex items-center justify-center gap-2"
                                >
                                    <Trash2 className="w-5 h-5" />
                                    Clear Cart
                                </button>

                                <button
                                    disabled={cartItems.length === 0}
                                    className={`flex-1 py-3 px-6 rounded-lg font-semibold transition-all duration-200 flex items-center justify-center gap-2 shadow-lg ${
                                        cartItems.length === 0
                                            ? "bg-gray-300 text-gray-500 cursor-not-allowed"
                                            : "bg-gradient-to-r from-purple-600 to-blue-600 text-white hover:from-purple-700 hover:to-blue-700 transform hover:scale-105"
                                    }`}
                                >
                                    <CreditCard className="w-5 h-5" />
                                    Proceed to Checkout
                                </button>
                            </div>

                            {/* Security Badge */}
                            <div className="mt-4 text-center">
                                <p className="text-xs text-gray-500 flex items-center justify-center gap-1">
                                    <span>🔒</span>
                                    Secure checkout • Free returns within 30 days
                                </p>
                            </div>
                        </div>
                    </>
                )}
            </div>
        </div>
    );
};

export default CartViewer;

