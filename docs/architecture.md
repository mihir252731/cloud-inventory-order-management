# System Architecture

```mermaid
flowchart LR
    A["React + TypeScript + Tailwind UI"] --> B["Spring Boot REST API"]
    B --> C["Spring Security + JWT + RBAC"]
    B --> D["PostgreSQL"]
    B --> E["Redis Cache"]
    F["Docker Compose"] --> A
    F --> B
    F --> D
    F --> E
```

## Key design notes

- The frontend is a role-aware SPA with route protection and authenticated API requests.
- The backend is organized by business modules: `auth`, `users`, `products`, `suppliers`, `inventory`, `orders`, and `dashboard`.
- PostgreSQL stores operational business records such as products, suppliers, users, stock movements, and orders.
- Redis caches product and low-stock queries to reduce repeated database reads.
- Docker Compose provides a one-command local environment for frontend, backend, PostgreSQL, and Redis.
