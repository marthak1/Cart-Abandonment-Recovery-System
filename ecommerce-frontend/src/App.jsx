import { Route, Routes, BrowserRouter, Link} from 'react-router-dom';
import Home from './pages/Home';
import Cart from './pages/Cart';    
import './App.css'
function App() {

  return (
    <div>
      <header className="bg-blue-600 text-white p-4">
        <div className="container mx-auto flex justify-between items-center">
        <h1 className="text-1xl font-bold">E-Commerce Store</h1>
        <nav>
          <ul className="flex space-x-4">
            <li>
              <Link to="/" className="hover:underline font-bold">Home</Link>
            </li>
            <li>
              <Link to="/cart" className="hover:underline font-bold">Cart</Link>
            </li>
          </ul>
        </nav>
        </div>
      </header>
  <main>
   
    <Routes>  
      <Route path="/" element={<Home />}/> 
       <Route path="/cart" element={<Cart />} />
    </Routes>
  
   </main>
   <footer className="bg-blue-600 text-white p-4 mt-4 text-center">
    &copy; 2025 E-Commerce Store
   </footer>
   </div>
  )
}
export default App

// https://www.w3schools.com/react/react_router.asp#:~:text=Routing%20means%20handling%20navigation%20between,URL%20parameters%20and%20query%20strings



