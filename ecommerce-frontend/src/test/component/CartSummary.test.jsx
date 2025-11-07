// tests/components/CartSummary.test.jsx

import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import { CartProvider } from "@/context/CartProvider.jsx";
import * as cartItemService from "@/services/cartItemService.jsx";
import * as sessionUtils from "@/utils/session.js";

// Simple UI component that reads from CartContext
const CartSummary = () => {
  const { cartItems, cartTotal } = React.useContext(CartProvider._context);
  return (
    <div>
      <p data-testid="item-count">Items: {cartItems.length}</p>
      <p data-testid="cart-total">Total: £{cartTotal}</p>
    </div>
  );
};

const mockCart = {
  success: true,
  items: [{ id: 1, name: "Majestic Mountain Tee", quantity: 2 }],
  total: 88,
};

describe("CartSummary UI", () => {
  beforeEach(() => {
    vi.spyOn(cartItemService, "fetchCart").mockResolvedValue(mockCart);
    vi.spyOn(sessionUtils, "getSessionToken").mockReturnValue("abc123");
    localStorage.clear();
  });

  it("renders cart items and total after initialization", async () => {
    render(
      <CartProvider>
        <CartSummary />
      </CartProvider>
    );

    await waitFor(() => {
      expect(screen.getByTestId("item-count").textContent).toBe("Items: 1");
      expect(screen.getByTestId("cart-total").textContent).toBe("Total: £88");
    });
  });
});
