// CartProvider.test.jsx

import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, waitFor } from "@testing-library/react";
import * as cartService from "../../services/cartService.js";
import { CartContext } from '../../context/CartContext.jsx';
import {
    CartProvider
} from "../../context/CartProvider.jsx";
// import * as cartService from "../services/cartService.jsx";
import * as sessionUtils from "../../utils/session.js";

const mockCart = {
  success: true,
  items: [{ id: 1, name: "Majestic Mountain Tee", quantity: 2 }],
  total: 88,
};

describe("CartProvider", () => {
  beforeEach(() => {
    vi.spyOn(cartService, "fetchCart").mockResolvedValue(mockCart);
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

    expect(cartService.fetchCart).toHaveBeenCalledWith("abc123");
    expect(localStorage.getItem("cart")).toEqual(JSON.stringify(mockCart.items));
  });
});
