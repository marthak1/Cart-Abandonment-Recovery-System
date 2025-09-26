import { Route, Routes, BrowserRouter} from 'react-router-dom';
import Home from './pages/Home';

import './App.css'

function App() {
  
// https://www.w3schools.com/react/react_router.asp#:~:text=Routing%20means%20handling%20navigation%20between,URL%20parameters%20and%20query%20strings
  return (
   <BrowserRouter>
    <Routes>  
      <Route path="/" element={<Home />}/> 
    </Routes>
   </BrowserRouter>
  )
}

export default App
