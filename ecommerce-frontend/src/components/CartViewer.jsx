import React from "react";
import { useCart } from "../context/CartContext";

const CartViewer = () => {
  const { cartItems, removeFromCart, getCartTotal, clearCart, updateQuantity } =
    useCart();

  return (
    <div className="max-w-md mx-auto bg-white shadow-md rounded-lg p-6 mt-6">
      
      {cartItems.length === 0 ? (
        <p className="text-gray-500">Your cart is empty.</p>
      ) : (
        <ul className="space-y-3">
          {cartItems.map((item) => (
            <li
              key={item.id}
              className="flex justify-between items-center border-b pb-2"
            >
              <span className="text-gray-800">
                {/* {item.title} (Qty: {item.qty})(${item.price}) */}
                <h4>{item.title}</h4>
                <p>
                  Qty:{" "}
                  <input
                    type="number"
                    min="1"
                    value={item.qty}
                    onChange={(e) =>
                      updateQuantity(item.id, parseInt(e.target.value, 10))
                    }
                  />
                </p>
                <p>Price: ${item.price}</p>
              </span>
              <button
                onClick={() => removeFromCart(item.id)}
                className="text-sm bg-red-100 text-red-600 px-3 py-1 rounded hover:bg-red-200"
              >
                Remove
              </button>
            </li>
          ))}
        </ul>
      )}

      <div className="mt-4 border-t pt-4">
        <p className="text-lg font-medium">
          Total:{" "}
          <span className="text-green-600">${getCartTotal().toFixed(2)}</span>
        </p>
        <button
          onClick={clearCart}
          className="mt-4 w-full py-2 px-4 rounded bg-red-600 text-white hover:bg-red-700"
        >
          Clear Cart
        </button>
        <button
          disabled={cartItems.length === 0}
          className={`mt-4 w-full py-2 px-4 rounded ${
            cartItems.length === 0
              ? "bg-gray-300 cursor-not-allowed"
              : "bg-blue-600 text-white hover:bg-blue-700"
          }`}
        >
          Proceed to Checkout
        </button>
      </div>
    </div>
  );
};

export default CartViewer;

