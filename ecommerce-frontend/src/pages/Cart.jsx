import CartViewer from "../components/CartViewer";

const Cart = () => {
  return (
    <div className="container mx-auto px-4 py-6">
      <h1 className="text-2xl font-bold mb-4">🛒 Your Cart</h1>
      <CartViewer />
    </div>
  );
};
export default Cart;

// renders the cart viewer component
