package com.cloudinventory.inventory.product;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cloudinventory.inventory.supplier.Supplier;
import com.cloudinventory.inventory.supplier.SupplierService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SupplierService supplierService;

    public ProductService(ProductRepository productRepository, SupplierService supplierService) {
        this.productRepository = productRepository;
        this.supplierService = supplierService;
    }

    @Cacheable("products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
    }

    @CacheEvict(value = {"products", "lowStockProducts"}, allEntries = true)
    public Product createProduct(ProductRequest request) {
        Product product = new Product();
        applyRequest(product, request);
        return productRepository.save(product);
    }

    @CacheEvict(value = {"products", "lowStockProducts"}, allEntries = true)
    public Product updateProduct(Long id, ProductRequest request) {
        Product product = getProductById(id);
        applyRequest(product, request);
        return productRepository.save(product);
    }

    @CacheEvict(value = {"products", "lowStockProducts"}, allEntries = true)
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Cacheable("lowStockProducts")
    public List<LowStockProductResponse> getLowStockProducts() {
        return productRepository.findLowStockProducts().stream()
                .map(LowStockProductResponse::from)
                .toList();
    }

    private void applyRequest(Product product, ProductRequest request) {
        Supplier supplier = supplierService.getSupplierById(request.supplierId());
        product.setSku(request.sku());
        product.setName(request.name());
        product.setDescription(request.description());
        product.setCategory(request.category());
        product.setUnitPrice(request.unitPrice());
        product.setStockQuantity(request.stockQuantity());
        product.setReorderLevel(request.reorderLevel());
        product.setWarehouseLocation(request.warehouseLocation());
        product.setSupplier(supplier);
    }
}
