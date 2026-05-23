import { NavLink, Outlet } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import { Role } from "../types";

interface NavigationItem {
  label: string;
  path: string;
  roles: Role[];
}

const navigation: NavigationItem[] = [
  { label: "Dashboard", path: "/", roles: ["ADMIN", "MANAGER", "WAREHOUSE_USER"] },
  { label: "Products", path: "/products", roles: ["ADMIN", "MANAGER", "WAREHOUSE_USER"] },
  { label: "Suppliers", path: "/suppliers", roles: ["ADMIN", "MANAGER", "WAREHOUSE_USER"] },
  { label: "Inventory", path: "/inventory", roles: ["ADMIN", "MANAGER", "WAREHOUSE_USER"] },
  { label: "Orders", path: "/orders", roles: ["ADMIN", "MANAGER", "WAREHOUSE_USER"] },
  { label: "Low Stock Alerts", path: "/low-stock", roles: ["ADMIN", "MANAGER", "WAREHOUSE_USER"] },
  { label: "User Management", path: "/users", roles: ["ADMIN", "MANAGER"] }
];

export function AppShell() {
  const { auth, logout } = useAuth();
  const roles = auth?.roles ?? [];

  const visibleNavigation = navigation.filter((item) =>
    item.roles.some((role) => roles.includes(role))
  );

  return (
    <div className="min-h-screen">
      <header className="border-b border-white/40 bg-slate-950/95 text-white">
        <div className="mx-auto flex max-w-7xl items-center justify-between px-6 py-4">
          <div>
            <p className="text-xs uppercase tracking-[0.3em] text-teal-300">Enterprise Demo</p>
            <h1 className="text-2xl font-semibold">Cloud Inventory &amp; Order Management</h1>
          </div>
          <div className="text-right">
            <p className="text-sm font-medium">{auth?.fullName}</p>
            <p className="text-xs uppercase tracking-[0.2em] text-slate-300">{roles.join(", ")}</p>
          </div>
        </div>
      </header>

      <div className="mx-auto grid max-w-7xl gap-6 px-6 py-8 lg:grid-cols-[260px_minmax(0,1fr)]">
        <aside className="panel h-fit p-4">
          <nav className="space-y-2">
            {visibleNavigation.map((item) => (
              <NavLink
                key={item.path}
                to={item.path}
                className={({ isActive }) =>
                  `block rounded-xl px-4 py-3 text-sm font-medium transition ${
                    isActive ? "bg-teal-700 text-white" : "text-slate-700 hover:bg-slate-100"
                  }`
                }
              >
                {item.label}
              </NavLink>
            ))}
          </nav>
          <button type="button" onClick={logout} className="button-secondary mt-4 w-full">
            Sign Out
          </button>
        </aside>

        <main className="space-y-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
