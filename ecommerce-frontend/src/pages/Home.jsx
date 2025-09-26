import React, { useEffect, useState } from 'react';
import axios from 'axios';
const Home = () => {
    const [message, setMessage] = useState('');
    // backend/index.js

    useEffect(() => {axios.get(`${import.meta.env.VITE_APP_API_URL}/api/hello`) // used environment variable that was set in .env file
     .then(response => setMessage(response.data))
   .catch(err => setMessage('There was an error!'));
    }, []);
    return (
    <>
    <div>
        <p>{message}</p>
      <h1 className="text-3xl font-bold underline">Welcome to My Shop!</h1>
    </div>
    </>
  );
};
export default Home;
// used environment variables to configure the API endpoint