import { useEffect, useState } from "react";
import api from "../api/client";
import { DataTable } from "../components/DataTable";
import { LoadingState } from "../components/LoadingState";
import { LowStockProduct } from "../types";

export function LowStockPage() {
  const [rows, setRows] = useState<LowStockProduct[] | null>(null);

  useEffect(() => {
    api.get<LowStockProduct[]>("/api/inventory/low-stock").then((response) => setRows(response.data));
  }, []);

  if (!rows) {
    return <LoadingState label="Loading low-stock alerts..." />;
  }

  return (
    <DataTable
      title="Low Stock Alerts"
      rows={rows}
      emptyMessage="No low-stock alerts."
      columns={[
        { key: "sku", header: "SKU", render: (row) => row.sku },
        { key: "name", header: "Product", render: (row) => row.name },
        { key: "stock", header: "Current Stock", render: (row) => row.stockQuantity },
        { key: "reorder", header: "Reorder Level", render: (row) => row.reorderLevel },
        { key: "supplier", header: "Supplier", render: (row) => row.supplierName }
      ]}
    />
  );
}
