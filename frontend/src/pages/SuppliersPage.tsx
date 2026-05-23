import { FormEvent, useEffect, useState } from "react";
import api from "../api/client";
import { DataTable } from "../components/DataTable";
import { FormCard } from "../components/FormCard";
import { LoadingState } from "../components/LoadingState";
import { useAuth } from "../hooks/useAuth";
import { Supplier } from "../types";

export function SuppliersPage() {
  const { auth } = useAuth();
  const [suppliers, setSuppliers] = useState<Supplier[]>([]);
  const [loading, setLoading] = useState(true);
  const canManageSuppliers = auth?.roles.some((role) => role === "ADMIN" || role === "MANAGER");

  const loadSuppliers = async () => {
    const response = await api.get<Supplier[]>("/api/suppliers");
    setSuppliers(response.data);
    setLoading(false);
  };

  useEffect(() => {
    loadSuppliers();
  }, []);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    await api.post("/api/suppliers", {
      name: formData.get("name"),
      contactName: formData.get("contactName"),
      email: formData.get("email"),
      phone: formData.get("phone"),
      city: formData.get("city"),
      country: formData.get("country"),
      leadTime: formData.get("leadTime")
    });
    event.currentTarget.reset();
    loadSuppliers();
  }

  if (loading) {
    return <LoadingState label="Loading suppliers..." />;
  }

  return (
    <div className="grid gap-6 xl:grid-cols-[360px_minmax(0,1fr)]">
      {canManageSuppliers ? (
        <FormCard title="Add Supplier">
          <form onSubmit={handleSubmit} className="space-y-4">
            <input className="input" name="name" placeholder="Supplier name" required />
            <input className="input" name="contactName" placeholder="Contact name" required />
            <input className="input" name="email" type="email" placeholder="Email" required />
            <input className="input" name="phone" placeholder="Phone" required />
            <input className="input" name="city" placeholder="City" required />
            <input className="input" name="country" placeholder="Country" required />
            <input className="input" name="leadTime" placeholder="Lead time" />
            <button type="submit" className="button-primary w-full">
              Save Supplier
            </button>
          </form>
        </FormCard>
      ) : null}

      <DataTable
        title="Suppliers"
        rows={suppliers}
        emptyMessage="No suppliers available."
        columns={[
          { key: "name", header: "Name", render: (row) => row.name },
          { key: "contact", header: "Contact", render: (row) => row.contactName },
          { key: "email", header: "Email", render: (row) => row.email },
          { key: "phone", header: "Phone", render: (row) => row.phone },
          { key: "region", header: "Region", render: (row) => `${row.city}, ${row.country}` },
          { key: "lead", header: "Lead Time", render: (row) => row.leadTime }
        ]}
      />
    </div>
  );
}
