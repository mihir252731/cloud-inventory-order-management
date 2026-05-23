import { Navigate, Route, Routes } from "react-router-dom";
import { ProtectedRoute } from "./components/ProtectedRoute";
import { AppShell } from "./layouts/AppShell";
import { DashboardPage } from "./pages/DashboardPage";
import { InventoryPage } from "./pages/InventoryPage";
import { LoginPage } from "./pages/LoginPage";
import { LowStockPage } from "./pages/LowStockPage";
import { OrdersPage } from "./pages/OrdersPage";
import { ProductsPage } from "./pages/ProductsPage";
import { SuppliersPage } from "./pages/SuppliersPage";
import { UserManagementPage } from "./pages/UserManagementPage";

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route element={<ProtectedRoute />}>
        <Route element={<AppShell />}>
          <Route path="/" element={<DashboardPage />} />
          <Route path="/products" element={<ProductsPage />} />
          <Route path="/suppliers" element={<SuppliersPage />} />
          <Route path="/inventory" element={<InventoryPage />} />
          <Route path="/orders" element={<OrdersPage />} />
          <Route path="/low-stock" element={<LowStockPage />} />
          <Route element={<ProtectedRoute roles={["ADMIN", "MANAGER"]} />}>
            <Route path="/users" element={<UserManagementPage />} />
          </Route>
        </Route>
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
