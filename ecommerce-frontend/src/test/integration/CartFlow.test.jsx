import { describe, test, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';

import {CartProvider} from '../../context/CartProvider';
import ProductList  from '../../components/ProductList';
import CartViewer  from '../../components/CartViewer';

// Mock the exact module your components import
vi.mock('../../services/productService', () => ({
    getAllProducts: vi.fn(),
}));
vi.mock('../../services/cartService', () => ({
    fetchCart: vi.fn(),
    addItemToCart: vi.fn(),
    removeItemFromCart: vi.fn(),
}))
// Import from the same mocked module
import { getAllProducts } from '../../services/productService';
import { addItemToCart, fetchCart, removeItemFromCart} from '../../services/cartService';
addItemToCart.mockResolvedValue({
    items: [
        { productId: 1, name: 'Laptop', price: 999.99, quantity: 2 },
        { productId: 2, name: 'Mouse', price: 29.99, quantity: 1 }
    ],
    total: 2029.97,
    status: 'active',
    recoveryFlag: false
});

fetchCart.mockResolvedValue({
    items: [
        { productId: 1, name: 'Laptop', price: 999.99, quantity: 2 },
    ],
    total: 2029.97,
    status: 'active',
    recoveryFlag: false
});
removeItemFromCart.mockResolvedValue({

    items: [
        { productId: 2, name: 'Mouse', price: 29.99, quantity: 1 }
    ],
    total: 29.99
});


// Mock products data
const mockProducts = [
    {
        productId: 1,
        name: 'Laptop',
        price: 999.99,
        description: 'High-performance laptop',
        image: 'laptop.jpg'
    },
    {
        productId: 2,
        name: 'Mouse',
        price: 29.99,
        description: 'Wireless mouse',
        image: 'mouse.jpg'
    },
    {
        productId: 3,
        name: 'Keyboard',
        price: 79.99,
        description: 'Mechanical keyboard',
        image: 'keyboard.jpg'
    }
];
const localStorageMock = (() => {
    let store = {};
    return {
        getItem: vi.fn((key) => store[key] || null),
        setItem: vi.fn((key, value) => {
            store[key] = value.toString();
        }),
        removeItem: vi.fn((key) => {
            delete store[key];
        }),
        clear: vi.fn(() => {
            store = {};
        }),
    };
})();

// Replace window.localStorage with the mock
Object.defineProperty(window, 'localStorage', {
    value: localStorageMock,
});
const renderWithRouter = (component) => {
    return render(
        <MemoryRouter>
            <CartProvider>{component}</CartProvider>
        </MemoryRouter>
    );
};
describe('Cart Recovery Flow', () => {
    beforeEach(() => {
        window.localStorage.clear();
        window.confirm = vi.fn(() => true); // always "OK"
        vi.clearAllMocks();
        // Stub the API response
        getAllProducts.mockResolvedValue({ data: mockProducts });

    });

    test('renders products from mocked API', async () => {
        renderWithRouter(<ProductList />);
        expect(await screen.findByText('Laptop')).toBeInTheDocument();
        expect(await screen.findByText('Mouse')).toBeInTheDocument();
    });
    test('writes to localStorage', () => {
        window.localStorage.setItem('cart', JSON.stringify(
            [{ productId: 1, name:"Laptop" }]
        ));

        expect(window.localStorage.setItem).toHaveBeenCalledWith(
            'cart',
            expect.stringContaining('Laptop')
        );

        const savedCart = JSON.parse(window.localStorage.getItem('cart'));
        expect(savedCart).toHaveLength(1);
    });
    test('adds item and persists to localStorage', async () => {
        renderWithRouter(
            <>
                <ProductList />
                <CartViewer />
            </>
        );

        const addButton = await screen.findByTestId('add-to-cart-1');
        fireEvent.click(addButton);

        await waitFor(() => {
            const savedCart = JSON.parse(localStorage.getItem('cart'));
            expect(savedCart).toHaveLength(1);
            expect(savedCart[0].productId).toBe(1);
        });
        });

    test('removes item from cart', async () => {
        // Mock initial cart data via props or context
        renderWithRouter(<CartViewer initialCart={[
            { productId: 1, name: 'Laptop', price: 999.99, quantity: 1 },
            { productId: 2, name: 'Mouse', price: 29.99, quantity: 1 }
        ]} />);

        // Click remove button
        const removeButton = await screen.findByTestId('remove-1');
        fireEvent.click(removeButton);

        // Verify Laptop is gone from the UI
        await waitFor(() => {
            expect(screen.queryByText('Laptop')).not.toBeInTheDocument();
            expect(screen.getByText('Mouse')).toBeInTheDocument();
        });
    });

    test('recovers cart from localStorage on reload', async () => {
        // Seed localStorage with one item
        localStorage.setItem('cart', JSON.stringify([
            { productId: 1, name: 'Laptop', price: 999.99, quantity: 1 }
        ]));

        renderWithRouter(<CartViewer />);

        // Assert cart hydrates from localStorage
        await waitFor(() => {
            expect(screen.getByText('Laptop')).toBeInTheDocument();
            expect(screen.getByTestId('quantity-1')).toHaveTextContent('1');
        });
    });

});






