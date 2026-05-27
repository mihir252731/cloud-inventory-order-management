# API Endpoints

## Authentication

- `POST /api/auth/register`
- `POST /api/auth/login`

## Products

- `GET /api/products`
- `GET /api/products/{id}`
- `POST /api/products`
- `PUT /api/products/{id}`
- `DELETE /api/products/{id}`

## Suppliers

- `GET /api/suppliers`
- `GET /api/suppliers/{id}`
- `POST /api/suppliers`

## Inventory

- `POST /api/inventory/update`
- `GET /api/inventory/low-stock`
- `GET /api/inventory/transactions`

## Orders

- `POST /api/orders`
- `GET /api/orders`
- `PUT /api/orders/{id}/status`
- `POST /api/orders/purchase`
- `GET /api/orders/purchase`

## Dashboard

- `GET /api/dashboard/summary`

## Users

- `GET /api/users`

## Customers

- `GET /api/customers`

## Integrations

### Overview

- `GET /api/integrations/overview`

### SAP ERP / SAP S/4HANA

- `GET /api/integrations/sap/health`
- `POST /api/integrations/sap/inventory-sync`
- `POST /api/integrations/sap/purchase-orders/sync`
- `GET /api/integrations/sap/events`

### Salesforce CRM

- `GET /api/integrations/salesforce/health`
- `POST /api/integrations/salesforce/orders/{orderId}/push`
- `GET /api/integrations/salesforce/history/{customerEmail}`
- `GET /api/integrations/salesforce/events`
