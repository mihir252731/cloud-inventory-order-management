import { FormEvent, useEffect, useState } from "react";
import api from "../api/client";
import { DataTable } from "../components/DataTable";
import { FormCard } from "../components/FormCard";
import { LoadingState } from "../components/LoadingState";
import { useAuth } from "../hooks/useAuth";
import { Product, Supplier } from "../types";

export function ProductsPage() {
  const { auth } = useAuth();
  const [products, setProducts] = useState<Product[]>([]);
  const [suppliers, setSuppliers] = useState<Supplier[]>([]);
  const [loading, setLoading] = useState(true);
  const canManageProducts = auth?.roles.some((role) => role === "ADMIN" || role === "MANAGER");

  const loadData = async () => {
    const [productsResponse, suppliersResponse] = await Promise.all([
      api.get<Product[]>("/api/products"),
      api.get<Supplier[]>("/api/suppliers")
    ]);
    setProducts(productsResponse.data);
    setSuppliers(suppliersResponse.data);
    setLoading(false);
  };

  useEffect(() => {
    loadData();
  }, []);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    await api.post("/api/products", {
      sku: formData.get("sku"),
      name: formData.get("name"),
      description: formData.get("description"),
      category: formData.get("category"),
      unitPrice: Number(formData.get("unitPrice")),
      stockQuantity: Number(formData.get("stockQuantity")),
      reorderLevel: Number(formData.get("reorderLevel")),
      warehouseLocation: formData.get("warehouseLocation"),
      supplierId: Number(formData.get("supplierId"))
    });
    event.currentTarget.reset();
    loadData();
  }

  if (loading) {
    return <LoadingState label="Loading products..." />;
  }

  return (
    <div className="grid gap-6 xl:grid-cols-[360px_minmax(0,1fr)]">
      {canManageProducts ? (
        <FormCard title="Add Product">
          <form onSubmit={handleSubmit} className="space-y-4">
            <input className="input" name="sku" placeholder="SKU" required />
            <input className="input" name="name" placeholder="Product name" required />
            <textarea className="input min-h-28" name="description" placeholder="Description" required />
            <input className="input" name="category" placeholder="Category" required />
            <input className="input" name="unitPrice" type="number" step="0.01" placeholder="Unit price" required />
            <input className="input" name="stockQuantity" type="number" placeholder="Stock quantity" required />
            <input className="input" name="reorderLevel" type="number" placeholder="Reorder level" required />
            <input className="input" name="warehouseLocation" placeholder="Warehouse location" required />
            <select className="input" name="supplierId" required defaultValue="">
              <option value="" disabled>
                Select supplier
              </option>
              {suppliers.map((supplier) => (
                <option key={supplier.id} value={supplier.id}>
                  {supplier.name}
                </option>
              ))}
            </select>
            <button type="submit" className="button-primary w-full">
              Save Product
            </button>
          </form>
        </FormCard>
      ) : null}

      <DataTable
        title="Product Catalog"
        rows={products}
        emptyMessage="No products available."
        columns={[
          { key: "sku", header: "SKU", render: (row) => row.sku },
          { key: "name", header: "Name", render: (row) => row.name },
          { key: "supplier", header: "Supplier", render: (row) => row.supplier.name },
          { key: "stock", header: "Stock", render: (row) => row.stockQuantity },
          { key: "reorder", header: "Reorder Level", render: (row) => row.reorderLevel },
          { key: "price", header: "Unit Price", render: (row) => `$${row.unitPrice}` }
        ]}
      />
    </div>
  );
}
