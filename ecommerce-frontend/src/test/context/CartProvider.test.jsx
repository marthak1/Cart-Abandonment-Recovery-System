// CartProvider.test.jsx

import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, waitFor } from "@testing-library/react";
import { CartContext } from './CartContext.jsx';
import { CartProvider, CartContext } from "../context/CartProvider.jsx";
import * as cartItemService from "../services/cartItemService.jsx";
import * as sessionUtils from "../utils/session.js";

const mockCart = {
  success: true,
  items: [{ id: 1, name: "Majestic Mountain Tee", quantity: 2 }],
  total: 88,
};

describe("CartProvider", () => {
  beforeEach(() => {
    vi.spyOn(cartItemService, "fetchCart").mockResolvedValue(mockCart);
    vi.spyOn(sessionUtils, "getSessionToken").mockReturnValue("abc123");
    localStorage.clear();
  });

  it("initializes cart on mount using async/await", async () => {
    let contextValue;

   render(
  <CartProvider>
    <CartContext.Consumer>
      {(value) => {
        contextValue = value;
        return null;
      }}
    </CartContext.Consumer> {/* ✅ Properly closes the Consumer */}
  </CartProvider>
);


    await waitFor(() => {
      expect(contextValue.cartItems).toEqual(mockCart.items);
      expect(contextValue.cartTotal).toBe(mockCart.total);
    });

    expect(cartItemService.fetchCart).toHaveBeenCalledWith("abc123");
    expect(localStorage.getItem("cart")).toEqual(JSON.stringify(mockCart.items));
  });
});
