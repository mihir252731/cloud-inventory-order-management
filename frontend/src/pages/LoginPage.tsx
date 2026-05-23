import { FormEvent, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

type Mode = "login" | "register";

export function LoginPage() {
  const { login, register } = useAuth();
  const navigate = useNavigate();
  const [mode, setMode] = useState<Mode>("login");
  const [error, setError] = useState<string>("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setLoading(true);
    setError("");
    const formData = new FormData(event.currentTarget);

    try {
      if (mode === "login") {
        await login({
          username: String(formData.get("username") ?? ""),
          password: String(formData.get("password") ?? "")
        });
      } else {
        await register({
          fullName: String(formData.get("fullName") ?? ""),
          username: String(formData.get("username") ?? ""),
          email: String(formData.get("email") ?? ""),
          password: String(formData.get("password") ?? ""),
          role: String(formData.get("role") ?? "WAREHOUSE_USER") as
            | "ADMIN"
            | "MANAGER"
            | "WAREHOUSE_USER"
        });
      }
      navigate("/");
    } catch (err) {
      setError("Authentication request failed. Check the API and demo credentials.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center px-6 py-8">
      <div className="grid w-full max-w-6xl gap-8 lg:grid-cols-[1.1fr_0.9fr]">
        <section className="rounded-[2rem] bg-slate-950 p-10 text-white shadow-2xl">
          <p className="text-sm uppercase tracking-[0.35em] text-teal-300">Operations Control</p>
          <h1 className="mt-6 text-5xl font-semibold leading-tight">
            Inventory, suppliers, warehouse activity, and customer orders in one command center.
          </h1>
          <p className="mt-6 max-w-xl text-lg text-slate-300">
            This portfolio project simulates the workflows a growing company uses to manage products,
            purchase orders, fulfillment, stock movements, and role-based operations.
          </p>
          <div className="mt-8 grid gap-4 md:grid-cols-2">
            <div className="rounded-2xl border border-white/10 bg-white/5 p-4">
              <p className="text-sm uppercase tracking-[0.25em] text-slate-400">Demo Admin</p>
              <p className="mt-2 font-semibold">admin / Admin@123</p>
            </div>
            <div className="rounded-2xl border border-white/10 bg-white/5 p-4">
              <p className="text-sm uppercase tracking-[0.25em] text-slate-400">Demo Manager</p>
              <p className="mt-2 font-semibold">manager / Manager@123</p>
            </div>
          </div>
        </section>

        <section className="panel p-8">
          <div className="mb-6 flex items-center justify-between">
            <div>
              <p className="text-sm uppercase tracking-[0.25em] text-slate-500">Access</p>
              <h2 className="mt-2 text-3xl font-semibold text-slate-900">
                {mode === "login" ? "Sign in" : "Create account"}
              </h2>
            </div>
            <button
              type="button"
              onClick={() => setMode(mode === "login" ? "register" : "login")}
              className="button-secondary"
            >
              {mode === "login" ? "Register" : "Back to login"}
            </button>
          </div>

          <form onSubmit={handleSubmit} className="space-y-4">
            {mode === "register" && (
              <>
                <input className="input" name="fullName" placeholder="Full name" required />
                <input className="input" name="email" type="email" placeholder="Email" required />
                <select className="input" name="role" defaultValue="WAREHOUSE_USER">
                  <option value="WAREHOUSE_USER">Warehouse User</option>
                  <option value="MANAGER">Manager</option>
                  <option value="ADMIN">Admin</option>
                </select>
              </>
            )}
            <input className="input" name="username" placeholder="Username" required />
            <input className="input" name="password" type="password" placeholder="Password" required />
            {error ? <p className="text-sm text-red-600">{error}</p> : null}
            <button type="submit" className="button-primary w-full" disabled={loading}>
              {loading ? "Submitting..." : mode === "login" ? "Sign in" : "Create account"}
            </button>
          </form>
        </section>
      </div>
    </div>
  );
}
