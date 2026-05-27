import { useEffect, useState } from "react";
import api from "../api/client";
import { DataTable } from "../components/DataTable";
import { LoadingState } from "../components/LoadingState";
import { FormCard } from "../components/FormCard";
import { IntegrationOverview, IntegrationSyncEvent, SalesforceHealth, SapHealth, CustomerAccount } from "../types";

export function IntegrationsPage() {
  const [overview, setOverview] = useState<IntegrationOverview | null>(null);
  const [sapHealth, setSapHealth] = useState<SapHealth | null>(null);
  const [salesforceHealth, setSalesforceHealth] = useState<SalesforceHealth | null>(null);
  const [customers, setCustomers] = useState<CustomerAccount[]>([]);
  const [syncing, setSyncing] = useState<string>("");

  const loadData = async () => {
    const [overviewResponse, sapHealthResponse, salesforceHealthResponse, customersResponse] = await Promise.all([
      api.get<IntegrationOverview>("/api/integrations/overview"),
      api.get<SapHealth>("/api/integrations/sap/health"),
      api.get<SalesforceHealth>("/api/integrations/salesforce/health"),
      api.get<CustomerAccount[]>("/api/customers")
    ]);

    setOverview(overviewResponse.data);
    setSapHealth(sapHealthResponse.data);
    setSalesforceHealth(salesforceHealthResponse.data);
    setCustomers(customersResponse.data);
  };

  useEffect(() => {
    loadData();
  }, []);

  async function runSapInventorySync() {
    setSyncing("sap-inventory");
    await api.post("/api/integrations/sap/inventory-sync");
    await loadData();
    setSyncing("");
  }

  async function runSapPurchaseOrderSync() {
    setSyncing("sap-po");
    await api.post("/api/integrations/sap/purchase-orders/sync");
    await loadData();
    setSyncing("");
  }

  async function fetchSalesforceHistory(email: string) {
    setSyncing(email);
    await api.get(`/api/integrations/salesforce/history/${encodeURIComponent(email)}`);
    await loadData();
    setSyncing("");
  }

  if (!overview || !sapHealth || !salesforceHealth) {
    return <LoadingState label="Loading enterprise integrations..." />;
  }

  return (
    <div className="space-y-6">
      <section className="panel p-6">
        <p className="text-sm uppercase tracking-[0.25em] text-teal-700">Integration Hub</p>
        <h2 className="mt-2 text-3xl font-semibold text-slate-900">{overview.projectName}</h2>
        <p className="mt-3 max-w-3xl text-sm text-slate-600">
          This hub demonstrates the platform acting as a central integration layer between SAP ERP,
          SAP S/4HANA, and Salesforce CRM while preserving the internal inventory and customer order workflows.
        </p>
      </section>

      <div className="grid gap-6 xl:grid-cols-2">
        <FormCard title="SAP ERP / SAP S/4HANA">
          <div className="space-y-3 text-sm text-slate-700">
            <p><strong>Status:</strong> {sapHealth.status}</p>
            <p><strong>ERP Base URL:</strong> {sapHealth.erpBaseUrl}</p>
            <p><strong>S/4HANA Base URL:</strong> {sapHealth.s4hanaBaseUrl}</p>
            <p><strong>Mock Mode:</strong> {sapHealth.mockMode ? "Enabled" : "Disabled"}</p>
          </div>
          <div className="grid gap-3 md:grid-cols-2">
            <button type="button" className="button-primary" onClick={runSapInventorySync} disabled={syncing !== ""}>
              {syncing === "sap-inventory" ? "Syncing..." : "Sync Inventory"}
            </button>
            <button type="button" className="button-secondary" onClick={runSapPurchaseOrderSync} disabled={syncing !== ""}>
              {syncing === "sap-po" ? "Syncing..." : "Sync Purchase Orders"}
            </button>
          </div>
        </FormCard>

        <FormCard title="Salesforce CRM">
          <div className="space-y-3 text-sm text-slate-700">
            <p><strong>Status:</strong> {salesforceHealth.status}</p>
            <p><strong>Base URL:</strong> {salesforceHealth.baseUrl}</p>
            <p><strong>Mock Mode:</strong> {salesforceHealth.mockMode ? "Enabled" : "Disabled"}</p>
            <p>
              Customer orders created inside the platform can be pushed into Salesforce CRM, and
              customer/order history can be fetched back into the integration layer.
            </p>
          </div>
        </FormCard>
      </div>

      <DataTable
        title="Salesforce Customer Accounts"
        rows={customers}
        emptyMessage="No customer accounts mapped yet."
        columns={[
          { key: "name", header: "Customer", render: (row) => row.customerName },
          { key: "email", header: "Email", render: (row) => row.customerEmail },
          { key: "account", header: "SF Account ID", render: (row) => row.salesforceAccountId },
          { key: "tier", header: "Tier", render: (row) => row.customerTier },
          { key: "history", header: "History Sync", render: (row) => (
            <button
              type="button"
              className="button-secondary py-2"
              onClick={() => fetchSalesforceHistory(row.customerEmail)}
              disabled={syncing !== ""}
            >
              {syncing === row.customerEmail ? "Fetching..." : "Fetch SOQL History"}
            </button>
          ) }
        ]}
      />

      <DataTable
        title="Recent SAP / Salesforce Sync Events"
        rows={overview.recentEvents}
        emptyMessage="No sync events available."
        columns={[
          { key: "system", header: "System", render: (row: IntegrationSyncEvent) => row.externalSystem },
          { key: "direction", header: "Direction", render: (row: IntegrationSyncEvent) => row.direction },
          { key: "operation", header: "Operation", render: (row: IntegrationSyncEvent) => row.operationName },
          { key: "reference", header: "Reference", render: (row: IntegrationSyncEvent) => row.referenceId },
          { key: "status", header: "Status", render: (row: IntegrationSyncEvent) => row.status }
        ]}
      />
    </div>
  );
}
