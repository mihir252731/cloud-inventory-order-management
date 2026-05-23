import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080",
  headers: {
    "Content-Type": "application/json"
  }
});

api.interceptors.request.use((config) => {
  const authStorage = localStorage.getItem("cloud_inventory_auth");
  if (authStorage) {
    const auth = JSON.parse(authStorage) as { token?: string };
    if (auth.token) {
      config.headers.Authorization = `Bearer ${auth.token}`;
    }
  }
  return config;
});

export default api;
