// Home.test.jsx
import { render, screen } from '@testing-library/react' //for rendering and querying-interacting with components
import Home from '../pages/Home'

describe('Home', () => {
  it('renders welcome message', () => {
    render(<Home />)
    expect(screen.getByText(/welcome to My Shop!/i)).toBeInTheDocument()
  })
})

{/* <div className="mt-8 p-6 bg-white rounded-lg shadow"> */}
{/*           <h3 className="font-semibold mb-3 text-lg">Test Scenarios Covered:</h3> */}
{/*           <div className="space-y-2 text-sm text-gray-700"> */}
{/*             <p>✅ <strong>Unit Tests:</strong> Product rendering, CartItem calculations, empty cart state</p> */}
{/*             <p>✅ <strong>Integration Tests:</strong> Add to cart flow, quantity updates, item removal</p> */}
{/*             <p>✅ <strong>E2E Flow:</strong> Complete checkout process from product selection to order confirmation</p> */}
{/*             <p className="mt-4 text-xs text-gray-500">All components use data-testid attributes for reliable testing</p> */}
{/*           </div> */}
{/*         </div> */}
{/*       </div> */}
{/*     </div> */}
{/*   ); */}
{/* }; */}