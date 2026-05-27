# Kubernetes Deployment

This project includes Kubernetes manifests in [k8s/base](../k8s/base) so it can be deployed beyond local Docker Compose. The manifests are provided as deployment assets and were not validated against a live cluster in this repository workflow.

## Included resources

- namespace
- config map
- example secret manifest
- PostgreSQL deployment, service, and PVC
- Redis deployment and service
- backend deployment and service
- frontend deployment and NodePort service
- Kustomize manifest

## Image expectation

The manifests currently reference:

- `mihir252731/enterprise-inventory-platform-backend:latest`
- `mihir252731/enterprise-inventory-platform-frontend:latest`

Update those image names if you publish under a different Docker Hub or registry namespace.

## Deploy with kubectl

1. Create a real secret from the example:
   - copy `k8s/base/secret.example.yaml`
   - save it as `k8s/base/secret.yaml`
   - replace placeholder credentials
2. Apply the manifests:

```bash
kubectl apply -f k8s/base/secret.yaml
kubectl apply -k k8s/base
```

3. Check rollout status:

```bash
kubectl get pods -n enterprise-inventory
kubectl get svc -n enterprise-inventory
```

## Why this matters

Kubernetes support makes the project more aligned with cloud-style deployment workflows and gives you a stronger story around consistent, environment-driven deployments beyond a local-only setup.
