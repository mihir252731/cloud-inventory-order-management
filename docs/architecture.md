# System Architecture

```mermaid
flowchart LR
    A["React + TypeScript + Tailwind UI"] --> B["Spring Boot REST API"]
    B --> C["Spring Security + JWT + RBAC"]
    B --> D["PostgreSQL"]
    B --> E["Redis Cache"]
    B --> H["SAP ERP / SAP S/4HANA Integration Layer"]
    B --> I["Salesforce CRM Integration Layer"]
    F["Docker Compose"] --> A
    F --> B
    F --> D
    F --> E
    G["Kubernetes"] --> A
    G --> B
    G --> D
    G --> E
```

## Key design notes

- The frontend is a role-aware SPA with route protection and authenticated API requests.
- The backend is organized by business modules: `auth`, `users`, `customers`, `products`, `suppliers`, `inventory`, `orders`, `dashboard`, and enterprise integration packages for SAP and Salesforce.
- PostgreSQL stores operational business records such as products, suppliers, users, stock movements, and orders.
- Redis caches product and low-stock queries to reduce repeated database reads.
- SAP ERP and SAP S/4HANA flows simulate inbound OData synchronization for inventory and purchase order records.
- Salesforce CRM flows simulate outbound customer order synchronization plus inbound customer history retrieval patterns.
- Docker Compose provides a one-command local environment for frontend, backend, PostgreSQL, and Redis.
- Kubernetes manifests provide a cluster-ready deployment path for frontend, backend, PostgreSQL, and Redis services.
