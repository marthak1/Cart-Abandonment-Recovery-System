// Home.test.jsx
import { render, screen } from '@testing-library/react' //for rendering and querying-interacting with components
import Home from './Home'

describe('Home', () => {
  it('renders welcome message', () => {
    render(<Home />)
    expect(screen.getByText(/welcome to My Shop!/i)).toBeInTheDocument()
  })
})