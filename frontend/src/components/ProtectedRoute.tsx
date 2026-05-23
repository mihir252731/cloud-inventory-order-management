import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import { Role } from "../types";

export function ProtectedRoute({ roles }: { roles?: Role[] }) {
  const { auth } = useAuth();

  if (!auth) {
    return <Navigate to="/login" replace />;
  }

  if (roles && !roles.some((role) => auth.roles.includes(role))) {
    return <Navigate to="/" replace />;
  }

  return <Outlet />;
}
