import React, { useEffect, useState } from "react";
import { fetchProducts } from "../services/api";
import { useCart } from "../context/CartContext";
import { useNavigate } from "react-router-dom";

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const { addToCart } = useCart();
  const navigate = useNavigate();

  useEffect(() => {
    fetchProducts()
      .then((res) => setProducts(res.data))
      .catch(() => {});
  }, []);

  return (
    <div className="container mx-auto px-4 py-6">
      <h2 className="text-2xl font-bold mb-6 text-center">Product Catalog</h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
        {products.map((product) => (
          <div
            key={product.id}
            className="bg-white shadow-md rounded-lg p-4 flex flex-col items-center"
          >
            <img
              src={product.category.image}
              alt={product.category.name}
              className="w-32 h-32 object-cover mb-4 rounded"
            />
            <h3 className="text-secondary text-lg font-semibold">
              {product.title}
            </h3>
            <span className="text-accent font-bold mb-4">${product.price.toFixed(2)}</span>
            <button
              onClick={() => {
                addToCart(product); // ✅ update cart
                navigate("/cart"); // ✅ redirect to cart page
              }}
              className="bg-primary text-white px-4 py-2 rounded hover:bg-blue-700 transition"
            >
              Add to Cart
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ProductList;


