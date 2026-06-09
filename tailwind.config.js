/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/main/resources/templates/**/*.html",
    "./src/main/resources/static/js/**/*.js"
  ],
  theme: {
    extend: {
      colors: {
        'landing-accent': 'var(--landing-accent)',
        'landing-bg': 'var(--landing-bg)',
        'landing-surface': 'var(--landing-surface)',
        'landing-border': 'var(--landing-border)',
        'landing-text': 'var(--landing-text)',
      }
    },
  },
  plugins: [],
}
