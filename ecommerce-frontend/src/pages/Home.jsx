import ProductList from "../components/ProductList";
import { ShoppingBag, Sparkles, TrendingUp, Shield } from "lucide-react";

const Home = () => {
    return (
        <div className="min-h-screen bg-gradient-to-br from-purple-50 via-blue-50 to-purple-50">
            {/* Hero Section */}
            <div className="bg-gradient-to-r from-purple-600 to-blue-600 shadow-xl">
                <div className="container mx-auto px-4 py-12">
                    <div className="max-w-4xl mx-auto text-center">
                        <div className="inline-flex items-center justify-center w-20 h-20 bg-white rounded-full mb-6 shadow-lg">
                            <ShoppingBag className="w-10 h-10 text-purple-600" />
                        </div>

                        <h1 className="text-5xl font-bold text-white mb-4">
                            Welcome to Our Store
                        </h1>

                        <p className="text-xl text-purple-100 mb-8">
                            Discover amazing products with unbeatable prices and quality
                        </p>

                        {/* Feature Badges */}
                        <div className="flex flex-wrap justify-center gap-4 mb-6">
                            <div className="flex items-center gap-2 bg-white/20 backdrop-blur-sm px-4 py-2 rounded-full text-white">
                                <Sparkles className="w-5 h-5" />
                                <span className="text-sm font-medium">Premium Quality</span>
                            </div>
                            <div className="flex items-center gap-2 bg-white/20 backdrop-blur-sm px-4 py-2 rounded-full text-white">
                                <TrendingUp className="w-5 h-5" />
                                <span className="text-sm font-medium">Best Prices</span>
                            </div>
                            <div className="flex items-center gap-2 bg-white/20 backdrop-blur-sm px-4 py-2 rounded-full text-white">
                                <Shield className="w-5 h-5" />
                                <span className="text-sm font-medium">Secure Shopping</span>
                            </div>
                        </div>

                        {/* Stats */}
                        <div className="grid grid-cols-3 gap-6 max-w-2xl mx-auto mt-8">
                            <div className="bg-white/10 backdrop-blur-sm rounded-lg p-4">
                                <p className="text-3xl font-bold text-white">500+</p>
                                <p className="text-sm text-purple-100">Products</p>
                            </div>
                            <div className="bg-white/10 backdrop-blur-sm rounded-lg p-4">
                                <p className="text-3xl font-bold text-white">10K+</p>
                                <p className="text-sm text-purple-100">Happy Customers</p>
                            </div>
                            <div className="bg-white/10 backdrop-blur-sm rounded-lg p-4">
                                <p className="text-3xl font-bold text-white">FREE</p>
                                <p className="text-sm text-purple-100">Shipping</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Products Section */}
            <div className="container mx-auto px-4 py-12">
                <div className="max-w-7xl mx-auto">
                    {/* Section Header */}
                    <div className="bg-white rounded-2xl shadow-lg p-6 mb-8">
                        <div className="flex items-center justify-between mb-2">
                            <h2 className="text-3xl font-bold text-gray-900">
                                Featured Products
                            </h2>
                            <div className="flex items-center gap-2 text-sm text-gray-600">
                                <div className="w-2 h-2 bg-green-500 rounded-full animate-pulse"></div>
                                <span>Updated daily</span>
                            </div>
                        </div>
                        <p className="text-gray-600">
                            Explore our curated collection of premium products
                        </p>
                    </div>

                    {/* Product List Component */}
                    <ProductList />

                    {/* Benefits Section */}
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-12">
                        <div className="bg-white rounded-xl shadow-md p-6 hover:shadow-xl transition-all duration-200">
                            <div className="w-12 h-12 bg-gradient-to-r from-purple-600 to-blue-600 rounded-lg flex items-center justify-center mb-4">
                                <Shield className="w-6 h-6 text-white" />
                            </div>
                            <h3 className="text-lg font-semibold text-gray-900 mb-2">
                                Secure Payment
                            </h3>
                            <p className="text-gray-600 text-sm">
                                100% secure transactions with encrypted payment processing
                            </p>
                        </div>

                        <div className="bg-white rounded-xl shadow-md p-6 hover:shadow-xl transition-all duration-200">
                            <div className="w-12 h-12 bg-gradient-to-r from-purple-600 to-blue-600 rounded-lg flex items-center justify-center mb-4">
                                <ShoppingBag className="w-6 h-6 text-white" />
                            </div>
                            <h3 className="text-lg font-semibold text-gray-900 mb-2">
                                Free Shipping
                            </h3>
                            <p className="text-gray-600 text-sm">
                                Free delivery on all orders with no minimum purchase required
                            </p>
                        </div>

                        <div className="bg-white rounded-xl shadow-md p-6 hover:shadow-xl transition-all duration-200">
                            <div className="w-12 h-12 bg-gradient-to-r from-purple-600 to-blue-600 rounded-lg flex items-center justify-center mb-4">
                                <Sparkles className="w-6 h-6 text-white" />
                            </div>
                            <h3 className="text-lg font-semibold text-gray-900 mb-2">
                                Quality Guarantee
                            </h3>
                            <p className="text-gray-600 text-sm">
                                30-day money-back guarantee on all products, no questions asked
                            </p>
                        </div>
                    </div>

                    {/* Call to Action */}
                    <div className="mt-12 bg-gradient-to-r from-purple-600 to-blue-600 rounded-2xl shadow-xl p-8 text-center">
                        <h3 className="text-2xl font-bold text-white mb-3">
                            Start Shopping Today!
                        </h3>
                        <p className="text-purple-100 mb-6">
                            Join thousands of satisfied customers enjoying premium products
                        </p>
                        <button className="bg-white text-purple-600 font-semibold px-8 py-3 rounded-lg hover:bg-gray-100 transition-all transform hover:scale-105 shadow-lg">
                            Browse All Products
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Home;

