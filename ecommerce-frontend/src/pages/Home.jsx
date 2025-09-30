import React, { useEffect, useState } from 'react';
import axios from 'axios';
import ProductList from '../components/ProductList';
const Home = () => {
    const [message, setMessage] = useState('');
    // backend/index.js

    useEffect(() => {axios.get(`${import.meta.env.VITE_APP_API_URL}/api/hello`) // used environment variable that was set in .env file
     .then(response => setMessage(response.data))
   .catch(err => setMessage('There was an error!'));
    }, []);
    return (
    <>
    <div className="container mx-auto px-4 py-6">
      <p>{message}</p>
      <h1 className="text-3xl font-bold text-center mb-6">Welcome to Our Store 🛍️</h1>
     </div>
      <ProductList />
    </>
  );
};
export default Home;
// used environment variables to configure the API endpoint