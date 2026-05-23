import { useEffect, useState } from "react";
import api from "../api/client";
import { LoadingState } from "../components/LoadingState";
import { StatCard } from "../components/StatCard";
import { DashboardSummary } from "../types";

export function DashboardPage() {
  const [summary, setSummary] = useState<DashboardSummary | null>(null);

  useEffect(() => {
    api.get<DashboardSummary>("/api/dashboard/summary").then((response) => setSummary(response.data));
  }, []);

  if (!summary) {
    return <LoadingState label="Loading dashboard summary..." />;
  }

  return (
    <div className="space-y-6">
      <section className="panel p-6">
        <p className="text-sm uppercase tracking-[0.25em] text-teal-700">Overview</p>
        <h2 className="mt-2 text-3xl font-semibold text-slate-900">Operations dashboard summary</h2>
        <p className="mt-3 max-w-3xl text-sm text-slate-600">
          Use this view to track stock health, open orders, active users, and supplier coverage across
          the warehouse network.
        </p>
      </section>

      <section className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
        <StatCard title="Products" value={summary.totalProducts} accent="bg-teal-600" />
        <StatCard title="Suppliers" value={summary.totalSuppliers} accent="bg-sky-600" />
        <StatCard title="Stock Units" value={summary.totalStockUnits} accent="bg-amber-500" />
        <StatCard title="Low Stock Items" value={summary.lowStockItems} accent="bg-red-600" />
        <StatCard title="Open Customer Orders" value={summary.openCustomerOrders} accent="bg-slate-700" />
        <StatCard title="Pending Purchase Orders" value={summary.pendingPurchaseOrders} accent="bg-cyan-700" />
        <StatCard title="Active Users" value={summary.activeUsers} accent="bg-emerald-700" />
      </section>
    </div>
  );
}
