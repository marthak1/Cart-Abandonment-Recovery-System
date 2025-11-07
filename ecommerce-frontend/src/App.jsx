import { Routes, Route, Link } from "react-router-dom";
import { ShoppingBag, ShoppingCart, Home as HomeIcon, Package } from "lucide-react";
import Home from "./pages/Home";
import Cart from "./pages/Cart";
import RecoveryModal from "./components/CartRecoveryModal";

function App() {
    return (
        <div className="min-h-screen flex flex-col bg-gradient-to-br from-purple-50 via-blue-50 to-purple-50">
            {/* Header */}
            <header className="bg-gradient-to-r from-purple-600 to-blue-600 shadow-xl sticky top-0 z-50">
                <div className="container mx-auto px-4 py-4">
                    <div className="flex justify-between items-center">
                        {/* Logo Section */}
                        <Link to="/" className="flex items-center gap-3 group">
                            <div className="w-12 h-12 bg-white rounded-xl flex items-center justify-center shadow-lg group-hover:scale-110 transition-transform duration-200">
                                <ShoppingBag className="w-7 h-7 text-purple-600" />
                            </div>
                            <div className="flex flex-col">
                                <h1 className="text-2xl font-bold text-white">
                                    E-Commerce Store
                                </h1>
                                <p className="text-xs text-purple-100">
                                    Premium Shopping Experience
                                </p>
                            </div>
                        </Link>

                        {/* Navigation */}
                        <nav>
                            <ul className="flex space-x-2">
                                <li>
                                    <Link
                                        to="/"
                                        className="flex items-center gap-2 px-6 py-2.5 rounded-lg font-semibold text-white hover:bg-white/20 backdrop-blur-sm transition-all duration-200 border-2 border-transparent hover:border-white/30"
                                    >
                                        <HomeIcon className="w-5 h-5" />
                                        Home
                                    </Link>
                                </li>
                                <li>
                                    <Link
                                        to="/cart"
                                        className="flex items-center gap-2 px-6 py-2.5 rounded-lg font-semibold bg-white text-purple-600 hover:bg-purple-50 transition-all duration-200 shadow-lg hover:shadow-xl transform hover:scale-105"
                                    >
                                        <ShoppingCart className="w-5 h-5" />
                                        Cart
                                    </Link>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>

                {/* Decorative bottom border */}
                <div className="h-1 bg-gradient-to-r from-purple-400 via-blue-400 to-purple-400"></div>
            </header>

            {/* Main Content */}
            <main className="flex-1">
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/cart" element={<Cart />} />
                </Routes>
            </main>

            {/* Footer */}
            <footer className="bg-gradient-to-r from-purple-600 to-blue-600 text-white mt-12">
                <div className="container mx-auto px-4 py-8">
                    {/* Footer Content */}
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-6">
                        {/* Brand Section */}
                        <div>
                            <div className="flex items-center gap-2 mb-3">
                                <div className="w-10 h-10 bg-white rounded-lg flex items-center justify-center">
                                    <ShoppingBag className="w-6 h-6 text-purple-600" />
                                </div>
                                <span className="text-xl font-bold">E-Commerce Store</span>
                            </div>
                            <p className="text-purple-100 text-sm">
                                Your trusted destination for premium products and exceptional service.
                            </p>
                        </div>

                        {/* Quick Links */}
                        <div>
                            <h3 className="text-lg font-semibold mb-3">Quick Links</h3>
                            <ul className="space-y-2 text-purple-100">
                                <li>
                                    <Link to="/" className="hover:text-white transition-colors flex items-center gap-2">
                                        <HomeIcon className="w-4 h-4" />
                                        Home
                                    </Link>
                                </li>
                                <li>
                                    <Link to="/cart" className="hover:text-white transition-colors flex items-center gap-2">
                                        <ShoppingCart className="w-4 h-4" />
                                        Shopping Cart
                                    </Link>
                                </li>
                                <li>
                                    <a href="#" className="hover:text-white transition-colors flex items-center gap-2">
                                        <Package className="w-4 h-4" />
                                        Track Order
                                    </a>
                                </li>
                            </ul>
                        </div>

                        {/* Customer Service */}
                        <div>
                            <h3 className="text-lg font-semibold mb-3">Customer Service</h3>
                            <ul className="space-y-2 text-purple-100 text-sm">
                                <li className="flex items-center gap-2">
                                    <span className="w-2 h-2 bg-green-400 rounded-full"></span>
                                    24/7 Support Available
                                </li>
                                <li className="flex items-center gap-2">
                                    <span className="w-2 h-2 bg-green-400 rounded-full"></span>
                                    Free Shipping
                                </li>
                                <li className="flex items-center gap-2">
                                    <span className="w-2 h-2 bg-green-400 rounded-full"></span>
                                    30-Day Returns
                                </li>
                                <li className="flex items-center gap-2">
                                    <span className="w-2 h-2 bg-green-400 rounded-full"></span>
                                    Secure Payments
                                </li>
                            </ul>
                        </div>
                    </div>

                    {/* Bottom Bar */}
                    <div className="border-t border-purple-400/30 pt-6 flex flex-col md:flex-row justify-between items-center gap-4">
                        <p className="text-purple-100 text-sm">
                            &copy; 2025 E-Commerce Store. All rights reserved.
                        </p>

                        <div className="flex items-center gap-4 text-sm text-purple-100">
                            <a href="#" className="hover:text-white transition-colors">
                                Privacy Policy
                            </a>
                            <span className="text-purple-400">•</span>
                            <a href="#" className="hover:text-white transition-colors">
                                Terms of Service
                            </a>
                            <span className="text-purple-400">•</span>
                            <a href="#" className="hover:text-white transition-colors">
                                Contact Us
                            </a>
                        </div>
                    </div>
                </div>
            </footer>

            {/* Cart Recovery Modal */}
            <RecoveryModal />
        </div>
    );
}

export default App;


