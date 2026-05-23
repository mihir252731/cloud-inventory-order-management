# Database Schema Explanation

## Core tables

- `users`: Application users with login credentials, profile info, and active status.
- `roles`: Role catalog for `ADMIN`, `MANAGER`, and `WAREHOUSE_USER`.
- `user_roles`: Join table linking users to one or more roles.
- `suppliers`: Vendor master data including contact and region details.
- `products`: Product catalog with SKU, pricing, stock quantity, reorder level, warehouse location, and supplier relationship.
- `inventory_transactions`: Auditable stock movement ledger for inbound, outbound, allocation, receipt, and adjustment events.
- `orders`: Customer sales orders with lifecycle status and total amount.
- `order_items`: Line items inside each customer order.
- `purchase_orders`: Supplier-facing replenishment orders for restocking inventory.

## Relationship summary

- One supplier can provide many products.
- One user can have one or more roles.
- One customer order can have many order items.
- One product can appear in many order items and inventory transactions.
- One supplier can have many purchase orders.

## Why this schema works well

- It separates master data (`products`, `suppliers`, `users`) from transactional data (`orders`, `inventory_transactions`, `purchase_orders`).
- It supports auditability for warehouse operations through explicit inventory movement records.
- It keeps role-based access control extensible through normalized role tables.
- It reflects realistic enterprise inventory workflows instead of a toy single-table CRUD design.
