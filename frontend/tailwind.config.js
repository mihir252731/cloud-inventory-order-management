/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{ts,tsx}"],
  theme: {
    extend: {
      colors: {
        ink: "#0f172a",
        mist: "#eff6ff",
        signal: "#0f766e",
        ember: "#b45309",
        alert: "#b91c1c"
      },
      boxShadow: {
        panel: "0 18px 40px rgba(15, 23, 42, 0.10)"
      }
    }
  },
  plugins: []
};
