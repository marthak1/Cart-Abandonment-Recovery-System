import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.jsx";
import { CartProvider } from './context/CartProvider.jsx';
import { BrowserRouter } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import ErrorBoundary from "./errorBoundary/ErrorBoundary.jsx";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <BrowserRouter>
        <ErrorBoundary>
    <CartProvider>
      <App />
        <ToastContainer />
    </CartProvider>
    </ErrorBoundary>
     </BrowserRouter>
  </StrictMode>
);


// main.jsx
// │
// ├── wrapped in <CartProvider>
// │
// ├── CartViewer.jsx
// │     └── uses useCart()
// │         └── gets cartItems, addItem(), removeItem(), updateQuantity()
// │
// ├── CartRecoveryModal.jsx
// │     └── uses useCart()
// │         └── gets checkInactivity(), clear(), deleteCurrentCart()
// │
// ├── Other Components
// │     └── can use useCart() to access shared cart state
// │
// └── CartProvider.jsx
//       └── uses session.js to get sessionToken
//       └── hydrates cart from backend via cartService.js
//       └── exposes cart state + mutation functions via CartContext
// Key Relationships
// CartProvider.jsx wraps the app and injects cart state into the React tree

// useCart() is the custom hook that reads from CartContext

// Components like CartViewer and CartRecoveryModal use useCart() to:

// Read cartItems, cartTotal, cartStatus

// Call mutation functions like addItem(), clear(), checkout(), etc.

// Insight
// The cart architecture uses CartProvider.jsx to centralize session-scoped 
// cart state and expose modular mutation functions. Components access this 
// shared state via useCart(), enabling reproducible flows for cart recovery, 
// mutation, and checkout across the frontend.
