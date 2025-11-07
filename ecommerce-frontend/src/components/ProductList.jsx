// This component displays a list of products fetched from the backend API, allowing users to add items to their cart.
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useCart } from "../hooks/useCart.js";
import { getAllProducts } from "../services/productService";


const ProductList = () => {
    const [products, setProducts] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [addingProductId, setAddingProductId] = useState(null);
    const { addToCart } = useCart();
    const navigate = useNavigate();

    useEffect(() => {
    }, [products]);

    useEffect(() => {
        const loadProducts = async () => {
            try {
                setLoading(true);
                setError(null);

                const response = await getAllProducts();
                console.log("Fetched products:", response.data);

                // Handle both direct array and wrapped response
                const productsData = Array.isArray(response.data)
                    ? response.data
                    : response.data.data  || [];

                if (productsData.length === 0) {
                    setError("No products available");
                } else {
                    setProducts(productsData);
                }
            } catch (err) {
                console.error("Product fetch failed:", err.response?.data || err.message);
                setError(err.response?.data?.message || "Failed to load products. Please try again later.");
            } finally {
                setLoading(false);
            }
        };

        loadProducts();
    }, []);

    const handleAddToCart = async (product) => {
        if (!product || typeof product !== 'object') {
            console.error('[AddToCart] Invalid product object:', product);
            setError("Invalid product");
            return;
        }

        const { id, price } = product;

        if (!id || isNaN(price)) {
            console.error('[AddToCart] Missing or invalid product fields:', { id, price });
            setError("Product data is incomplete");
            return;
        }

        setAddingProductId(id);
        console.log('[AddToCart] Sending cart item:', product);

        try {
            await addToCart(product);
            setTimeout(() => navigate("/cart"), 300);
        } catch (err) {
            console.error("Failed to add product to cart:", err?.response?.data || err.message);
            setError("Failed to add product to cart");
        } finally {
            setTimeout(() => setAddingProductId(null), 500);
        }
    };

    if (loading) {
        return (
            <div className="container mx-auto px-4 py-12">
                <div className="flex flex-col items-center justify-center">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mb-4"></div>
                    <p className="text-gray-600 text-lg">Loading products...</p>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="container mx-auto px-4 py-12">
                <div className="bg-red-50 border border-red-200 rounded-lg p-6 max-w-md mx-auto">
                    <div className="flex items-center mb-2">
                        <svg className="w-6 h-6 text-red-600 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                        </svg>
                        <h3 className="text-red-800 font-semibold">Error Loading Products</h3>
                    </div>
                    <p className="text-red-600">{error}</p>
                    <button
                        onClick={() => window.location.reload()}
                        className="mt-4 bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 transition"
                    >
                        Retry
                    </button>
                </div>
            </div>
        );
    }

    if (products.length === 0) {
        return (
            <div className="container mx-auto px-4 py-12">
                <div className="text-center">
                    <svg className="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
                    </svg>
                    <h3 className="text-xl font-semibold text-gray-700 mb-2">No Products Available</h3>
                    <p className="text-gray-500">Check back later for new products</p>
                </div>
            </div>
        );
    }

    return (
        <div className="container mx-auto px-4 py-6">
            <h2 className="text-3xl font-bold mb-8 text-center text-gray-800">
                Product Catalog
            </h2>

            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                {products.map((product) => (
                    <div
                        key={product.id}
                        className="bg-white shadow-md rounded-lg overflow-hidden hover:shadow-xl transition-shadow duration-300 flex flex-col"
                    >
                        {/* Product Image */}
                        <div className="relative h-48 bg-gray-100">
                            <img
                                src={product.imageUrl|| "https://via.placeholder.com/200"}
                                alt={product.name}
                                className="w-full h-full object-cover"
                                onError={(e) => {
                                    e.target.src = "https://via.placeholder.com/200?text=No+Image";
                                }}
                            />
                            {product.category?.name && (
                                <span className="absolute top-2 left-2 bg-blue-600 text-white text-xs px-2 py-1 rounded">
                                    {product.name}
                                </span>
                            )}
                        </div>

                        {/* Product Details */}
                        <div className="p-4 flex flex-col flex-grow">
                            <h3 className="text-lg font-semibold text-gray-800 mb-2 line-clamp-2 min-h-[3.5rem]">
                                {product.name}
                            </h3>

                            {product.description && (
                                <p className="text-sm text-gray-600 mb-3 line-clamp-2">
                                    {product.description}
                                </p>
                            )}

                            <div className="mt-auto">
                                <div className="flex items-center justify-between mb-3">
                                    <span className="text-2xl font-bold text-green-600">
                                        £{Number(product.price).toFixed(2)}
                                    </span>
                                </div>


                                <button
                                    onClick={() => handleAddToCart(product)}
                                    disabled={addingProductId === product.id}
                                    className={`w-full py-2 px-4 rounded-lg font-semibold transition-all duration-200 ${
                                        addingProductId === product.id
                                            ? "bg-green-500 text-white cursor-default"
                                            : "bg-blue-600 text-white hover:bg-blue-700 active:scale-95"
                                    }`}
                                >
                                    {addingProductId === product.id ? (
                                        <span className="flex items-center justify-center">
                                            <svg className="animate-spin h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24">
                                                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                                                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                            </svg>
                                            Adding...
                                        </span>
                                    ) : (
                                        <span className="flex items-center justify-center">
                                            <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                                            </svg>
                                            Add to Cart

                                        </span>

                                    )}
                                </button>

                            </div>
                        </div>
                    </div>
                ))}
            </div>

            {/* Product Count */}
            <div className="text-center mt-8 text-gray-600">
                Showing {products.length} product{products.length !== 1 ? 's' : ''}
            </div>
        </div>
    );
};

export default ProductList;
