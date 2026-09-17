/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        bg: {
          primary: '#08070A',
          secondary: '#121118',
        },
        brand: {
          red: '#E50914',
          'red-glow': '#FF1E27',
          cyan: '#00D2FF',
        }
      },
      fontFamily: {
        netflix: ['"Bebas Neue"', 'sans-serif'],
        body: ['Montserrat', 'sans-serif'],
      },
      boxShadow: {
        'spidey-glow': '0 0 25px rgba(229, 9, 20, 0.6)',
        'cyan-glow': '0 0 15px rgba(0, 210, 255, 0.4)',
      },
      animation: {
        'upside-down-float': 'upsideDownFloat 6s ease-in-out infinite',
        'fade-in-up': 'fadeInUp 0.8s cubic-bezier(0.16, 1, 0.3, 1) forwards',
        'glow-pulse': 'glowPulse 3s ease-in-out infinite',
      },
      keyframes: {
        upsideDownFloat: {
          '0%, 100%': { transform: 'translateY(0px)' },
          '50%': { transform: 'translateY(-15px)' },
        },
        fadeInUp: {
          '0%': { opacity: '0', transform: 'translateY(30px)' },
          '100%': { opacity: '1', transform: 'translateY(0)' },
        },
        glowPulse: {
          '0%, 100%': { opacity: '0.4' },
          '50%': { opacity: '0.8' },
        }
      }
    },
  },
  plugins: [],
}