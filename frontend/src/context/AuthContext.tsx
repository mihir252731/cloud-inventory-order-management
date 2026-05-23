import { createContext, useEffect, useMemo, useState, type ReactNode } from "react";
import api from "../api/client";
import { AuthResponse } from "../types";

interface LoginPayload {
  username: string;
  password: string;
}

interface RegisterPayload {
  fullName: string;
  username: string;
  email: string;
  password: string;
  role: "ADMIN" | "MANAGER" | "WAREHOUSE_USER";
}

interface AuthContextValue {
  auth: AuthResponse | null;
  login: (payload: LoginPayload) => Promise<void>;
  register: (payload: RegisterPayload) => Promise<void>;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextValue | undefined>(undefined);

const storageKey = "cloud_inventory_auth";

export function AuthProvider({ children }: { children: ReactNode }) {
  const [auth, setAuth] = useState<AuthResponse | null>(null);

  useEffect(() => {
    const stored = localStorage.getItem(storageKey);
    if (stored) {
      setAuth(JSON.parse(stored) as AuthResponse);
    }
  }, []);

  const persistAuth = (value: AuthResponse | null) => {
    setAuth(value);
    if (value) {
      localStorage.setItem(storageKey, JSON.stringify(value));
    } else {
      localStorage.removeItem(storageKey);
    }
  };

  const value = useMemo<AuthContextValue>(
    () => ({
      auth,
      login: async (payload) => {
        const response = await api.post<AuthResponse>("/api/auth/login", payload);
        persistAuth(response.data);
      },
      register: async (payload) => {
        const response = await api.post<AuthResponse>("/api/auth/register", payload);
        persistAuth(response.data);
      },
      logout: () => persistAuth(null)
    }),
    [auth]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
