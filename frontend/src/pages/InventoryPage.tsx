import { FormEvent, useEffect, useState } from "react";
import api from "../api/client";
import { DataTable } from "../components/DataTable";
import { FormCard } from "../components/FormCard";
import { LoadingState } from "../components/LoadingState";
import { InventoryTransaction, Product } from "../types";

export function InventoryPage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [transactions, setTransactions] = useState<InventoryTransaction[]>([]);
  const [loading, setLoading] = useState(true);

  const loadData = async () => {
    const [productsResponse, transactionsResponse] = await Promise.all([
      api.get<Product[]>("/api/products"),
      api.get<InventoryTransaction[]>("/api/inventory/transactions")
    ]);
    setProducts(productsResponse.data);
    setTransactions(transactionsResponse.data);
    setLoading(false);
  };

  useEffect(() => {
    loadData();
  }, []);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    await api.post("/api/inventory/update", {
      productId: Number(formData.get("productId")),
      transactionType: formData.get("transactionType"),
      quantity: Number(formData.get("quantity")),
      performedBy: formData.get("performedBy"),
      notes: formData.get("notes")
    });
    event.currentTarget.reset();
    loadData();
  }

  if (loading) {
    return <LoadingState label="Loading inventory..." />;
  }

  return (
    <div className="grid gap-6 xl:grid-cols-[360px_minmax(0,1fr)]">
      <FormCard title="Inventory Stock Update">
        <form onSubmit={handleSubmit} className="space-y-4">
          <select className="input" name="productId" defaultValue="" required>
            <option value="" disabled>
              Select product
            </option>
            {products.map((product) => (
              <option key={product.id} value={product.id}>
                {product.name} ({product.stockQuantity} in stock)
              </option>
            ))}
          </select>
          <select className="input" name="transactionType" defaultValue="STOCK_IN">
            <option value="STOCK_IN">Stock In</option>
            <option value="STOCK_OUT">Stock Out</option>
            <option value="PURCHASE_ORDER_RECEIPT">Purchase Order Receipt</option>
            <option value="ADJUSTMENT">Adjustment</option>
          </select>
          <input className="input" name="quantity" type="number" placeholder="Quantity" required />
          <input className="input" name="performedBy" placeholder="Performed by" required />
          <textarea className="input min-h-24" name="notes" placeholder="Notes" />
          <button type="submit" className="button-primary w-full">
            Submit Update
          </button>
        </form>
      </FormCard>

      <DataTable
        title="Recent Inventory Transactions"
        rows={transactions}
        emptyMessage="No inventory transactions yet."
        columns={[
          { key: "product", header: "Product", render: (row) => row.product.name },
          { key: "type", header: "Type", render: (row) => row.transactionType },
          { key: "change", header: "Quantity Change", render: (row) => row.quantityChange },
          { key: "stock", header: "Resulting Stock", render: (row) => row.resultingStock },
          { key: "by", header: "Performed By", render: (row) => row.performedBy }
        ]}
      />
    </div>
  );
}
