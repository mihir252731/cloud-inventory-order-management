package com.cloudinventory.inventory.supplier;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> getSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found: " + id));
    }

    public Supplier createSupplier(SupplierRequest request) {
        Supplier supplier = new Supplier();
        supplier.setName(request.name());
        supplier.setContactName(request.contactName());
        supplier.setEmail(request.email());
        supplier.setPhone(request.phone());
        supplier.setCity(request.city());
        supplier.setCountry(request.country());
        supplier.setLeadTime(request.leadTime());
        return supplierRepository.save(supplier);
    }
}
