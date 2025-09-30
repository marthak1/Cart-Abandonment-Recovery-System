// tailwind.config.js
export default {
  content: ['./index.html', './src/**/*.{js,jsx,ts,tsx}'],
  theme: { extend: {
    colors: {
 primary: '#1E40AF', // Deep blue for buttons and headers
secondary: '#F59E0B', // Amber for highlights and accents
accent: '#10B981', // Emerald for success states
 },
 spacing: {
'72': '18rem',
'84': '21rem',
'96': '24rem',
}
  } },
  plugins: [], 
  
};





