import React, { useState } from 'react';
import { Search } from 'lucide-react';

// Child component that logs when it re-renders
const ProductItem = ({ product, onAddToCart }) => {
  console.log(`🔄 ProductItem rendered: ${product.name}`);
  
  return (
    <div className="border rounded p-3 bg-white">
      <h3 className="font-semibold">{product.name}</h3>
      <p className="text-sm text-gray-600">${product.price}</p>
      <button
        onClick={() => onAddToCart(product)}
        className="mt-2 px-3 py-1 bg-blue-500 text-white rounded text-sm hover:bg-blue-600"
      >
        Add to Cart
      </button>
    </div>
  );
};

// ❌ PROBLEM #1: Expensive calculation runs on every render
const calculateExpensiveStats = (products) => {
  console.log('💰 Running EXPENSIVE calculation...');
  let total = 0;
  // Simulate expensive operation
  for (let i = 0; i < 1000000; i++) {
    total += i;
  }
  return products.reduce((sum, p) => sum + p.price, 0);
};

export default function ProductStore() {
  const [searchTerm, setSearchTerm] = useState('');
  const [cart, setCart] = useState([]);

  const products = [
    { id: 1, name: 'Laptop', price: 999, category: 'electronics' },
    { id: 2, name: 'Mouse', price: 29, category: 'electronics' },
    { id: 3, name: 'Desk', price: 299, category: 'furniture' },
    { id: 4, name: 'Chair', price: 199, category: 'furniture' },
    { id: 5, name: 'Monitor', price: 399, category: 'electronics' },
  ];

  // ❌ PROBLEM #1: This expensive calculation runs every time component re-renders
  // Even when typing in search box (which has nothing to do with this calculation)
  const totalInventoryValue = calculateExpensiveStats(products);

  // ❌ PROBLEM #2: Filtering happens on every render
  // Even when only the cart changes, we re-filter products unnecessarily
  console.log('🔍 Filtering products...');
  const filteredProducts = products.filter(product =>
    product.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // ❌ PROBLEM #3: New function created on every render
  // This causes ProductItem children to re-render unnecessarily
  const handleAddToCart = (product) => {
    setCart([...cart, product]);
  };

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-4xl mx-auto">
        <div className="bg-white rounded-lg shadow-lg p-6 mb-6">
          <h1 className="text-3xl font-bold mb-4">🐌 Performance Problems Demo</h1>
          
          <div className="bg-yellow-50 border-l-4 border-yellow-400 p-4 mb-4">
            <p className="text-sm text-yellow-800 font-semibold mb-2">
              Open your browser console (F12) to see the problems!
            </p>
            <ul className="text-xs text-yellow-700 space-y-1">
              <li>❌ Problem #1: Expensive calculation runs on every keystroke</li>
              <li>❌ Problem #2: Products filtered even when just adding to cart</li>
              <li>❌ Problem #3: All ProductItems re-render when anything changes</li>
            </ul>
          </div>

          {/* This input causes re-renders on every keystroke */}
          <div className="mb-4 relative">
            <Search className="absolute left-3 top-3 text-gray-400" size={20} />
            <input
              type="text"
              placeholder="Search products... (type and watch console)"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            />
          </div>

          <div className="flex justify-between items-center p-4 bg-blue-50 rounded mb-4">
            <div>
              <p className="text-sm text-gray-600">Total Inventory Value</p>
              <p className="text-2xl font-bold text-blue-600">${totalInventoryValue}</p>
            </div>
            <div>
              <p className="text-sm text-gray-600">Cart Items</p>
              <p className="text-2xl font-bold text-green-600">{cart.length}</p>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {filteredProducts.map(product => (
            <ProductItem
              key={product.id}
              product={product}
              onAddToCart={handleAddToCart}
            />
          ))}
        </div>

        {filteredProducts.length === 0 && (
          <div className="text-center py-8 text-gray-500">
            No products found
          </div>
        )}
      </div>
    </div>
  );
}