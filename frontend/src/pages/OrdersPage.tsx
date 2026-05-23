import { FormEvent, useEffect, useState } from "react";
import api from "../api/client";
import { DataTable } from "../components/DataTable";
import { FormCard } from "../components/FormCard";
import { LoadingState } from "../components/LoadingState";
import { useAuth } from "../hooks/useAuth";
import { CustomerOrder, Product, PurchaseOrder, Supplier } from "../types";

export function OrdersPage() {
  const { auth } = useAuth();
  const [orders, setOrders] = useState<CustomerOrder[]>([]);
  const [purchaseOrders, setPurchaseOrders] = useState<PurchaseOrder[]>([]);
  const [products, setProducts] = useState<Product[]>([]);
  const [suppliers, setSuppliers] = useState<Supplier[]>([]);
  const [loading, setLoading] = useState(true);
  const canManageOrders = auth?.roles.some((role) => role === "ADMIN" || role === "MANAGER");

  const loadData = async () => {
    const [ordersResponse, purchaseOrdersResponse, productsResponse, suppliersResponse] = await Promise.all([
      api.get<CustomerOrder[]>("/api/orders"),
      api.get<PurchaseOrder[]>("/api/orders/purchase"),
      api.get<Product[]>("/api/products"),
      api.get<Supplier[]>("/api/suppliers")
    ]);
    setOrders(ordersResponse.data);
    setPurchaseOrders(purchaseOrdersResponse.data);
    setProducts(productsResponse.data);
    setSuppliers(suppliersResponse.data);
    setLoading(false);
  };

  useEffect(() => {
    loadData();
  }, []);

  async function handleCustomerOrder(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    await api.post("/api/orders", {
      customerName: formData.get("customerName"),
      customerEmail: formData.get("customerEmail"),
      items: [
        {
          productId: Number(formData.get("productId")),
          quantity: Number(formData.get("quantity"))
        }
      ]
    });
    event.currentTarget.reset();
    loadData();
  }

  async function handlePurchaseOrder(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    await api.post("/api/orders/purchase", {
      supplierId: Number(formData.get("supplierId")),
      requestedBy: formData.get("requestedBy"),
      totalAmount: Number(formData.get("totalAmount")),
      notes: formData.get("notes")
    });
    event.currentTarget.reset();
    loadData();
  }

  async function handleStatusUpdate(orderId: number, status: string) {
    await api.put(`/api/orders/${orderId}/status`, { status });
    loadData();
  }

  if (loading) {
    return <LoadingState label="Loading orders..." />;
  }

  return (
    <div className="space-y-6">
      <div className="grid gap-6 xl:grid-cols-2">
        <FormCard title="Create Customer Order">
          <form onSubmit={handleCustomerOrder} className="space-y-4">
            <input className="input" name="customerName" placeholder="Customer name" required />
            <input className="input" name="customerEmail" type="email" placeholder="Customer email" required />
            <select className="input" name="productId" defaultValue="" required>
              <option value="" disabled>
                Select product
              </option>
              {products.map((product) => (
                <option key={product.id} value={product.id}>
                  {product.name}
                </option>
              ))}
            </select>
            <input className="input" name="quantity" type="number" placeholder="Quantity" required />
            <button type="submit" className="button-primary w-full">
              Create Customer Order
            </button>
          </form>
        </FormCard>

        <FormCard title="Create Purchase Order">
          {canManageOrders ? (
            <form onSubmit={handlePurchaseOrder} className="space-y-4">
              <select className="input" name="supplierId" defaultValue="" required>
                <option value="" disabled>
                  Select supplier
                </option>
                {suppliers.map((supplier) => (
                  <option key={supplier.id} value={supplier.id}>
                    {supplier.name}
                  </option>
                ))}
              </select>
              <input className="input" name="requestedBy" placeholder="Requested by" required />
              <input className="input" name="totalAmount" type="number" step="0.01" placeholder="Total amount" required />
              <textarea className="input min-h-24" name="notes" placeholder="Notes" />
              <button type="submit" className="button-primary w-full">
                Create Purchase Order
              </button>
            </form>
          ) : (
            <p className="text-sm text-slate-500">Purchase order creation is available to admins and managers.</p>
          )}
        </FormCard>
      </div>

      <DataTable
        title="Customer Orders"
        rows={orders}
        emptyMessage="No customer orders yet."
        columns={[
          { key: "number", header: "Order Number", render: (row) => row.orderNumber },
          { key: "customer", header: "Customer", render: (row) => row.customerName },
          {
            key: "status",
            header: "Status",
            render: (row) => (
              <select
                className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
                value={row.status}
                disabled={!canManageOrders}
                onChange={(event) => handleStatusUpdate(row.id, event.target.value)}
              >
                {["PENDING", "PROCESSING", "PICKED", "SHIPPED", "DELIVERED", "CANCELLED"].map((status) => (
                  <option key={status} value={status}>
                    {status}
                  </option>
                ))}
              </select>
            )
          },
          { key: "items", header: "Items", render: (row) => row.items.length },
          { key: "total", header: "Total", render: (row) => `$${row.totalAmount}` }
        ]}
      />

      <DataTable
        title="Purchase Orders"
        rows={purchaseOrders}
        emptyMessage="No purchase orders yet."
        columns={[
          { key: "number", header: "PO Number", render: (row) => row.poNumber },
          { key: "supplier", header: "Supplier", render: (row) => row.supplier.name },
          { key: "requested", header: "Requested By", render: (row) => row.requestedBy },
          { key: "status", header: "Status", render: (row) => row.status },
          { key: "total", header: "Total", render: (row) => `$${row.totalAmount}` }
        ]}
      />
    </div>
  );
}
