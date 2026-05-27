export type Role = "ADMIN" | "MANAGER" | "WAREHOUSE_USER";

export interface AuthResponse {
  token: string;
  username: string;
  fullName: string;
  roles: Role[];
}

export interface User {
  id: number;
  username: string;
  email: string;
  fullName: string;
  active: boolean;
  roles: Role[];
}

export interface Supplier {
  id: number;
  name: string;
  contactName: string;
  email: string;
  phone: string;
  city: string;
  country: string;
  leadTime: string;
}

export interface Product {
  id: number;
  sku: string;
  name: string;
  description: string;
  category: string;
  unitPrice: number;
  stockQuantity: number;
  reorderLevel: number;
  warehouseLocation: string;
  supplier: Supplier;
}

export interface InventoryTransaction {
  id: number;
  product: Product;
  transactionType: string;
  quantityChange: number;
  resultingStock: number;
  referenceType: string;
  performedBy: string;
  notes: string;
}

export interface LowStockProduct {
  id: number;
  sku: string;
  name: string;
  stockQuantity: number;
  reorderLevel: number;
  supplierName: string;
}

export interface OrderItem {
  id: number;
  product: Product;
  quantity: number;
  unitPrice: number;
  lineTotal: number;
}

export interface CustomerOrder {
  id: number;
  orderNumber: string;
  customerName: string;
  customerEmail: string;
  status: string;
  totalAmount: number;
  items: OrderItem[];
}

export interface PurchaseOrder {
  id: number;
  poNumber: string;
  supplier: Supplier;
  requestedBy: string;
  status: string;
  totalAmount: number;
  notes: string;
}

export interface DashboardSummary {
  totalProducts: number;
  totalSuppliers: number;
  totalStockUnits: number;
  lowStockItems: number;
  openCustomerOrders: number;
  pendingPurchaseOrders: number;
  activeUsers: number;
}

export interface CustomerAccount {
  id: number;
  customerName: string;
  customerEmail: string;
  salesforceAccountId: string;
  salesforceContactId: string;
  customerTier: string;
  region: string;
}

export interface IntegrationSyncEvent {
  id: number;
  externalSystem: string;
  direction: string;
  status: string;
  operationName: string;
  referenceId: string;
  detailMessage: string;
}

export interface IntegrationOverview {
  projectName: string;
  sapMockMode: boolean;
  salesforceMockMode: boolean;
  recentEvents: IntegrationSyncEvent[];
}

export interface SapHealth {
  integrationName: string;
  erpBaseUrl: string;
  s4hanaBaseUrl: string;
  mockMode: boolean;
  status: string;
}

export interface SalesforceHealth {
  integrationName: string;
  baseUrl: string;
  mockMode: boolean;
  status: string;
}
