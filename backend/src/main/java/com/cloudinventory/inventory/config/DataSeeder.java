package com.cloudinventory.inventory.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cloudinventory.inventory.customer.CustomerAccount;
import com.cloudinventory.inventory.customer.CustomerAccountRepository;
import com.cloudinventory.inventory.integration.common.ExternalSystemType;
import com.cloudinventory.inventory.integration.common.IntegrationSyncEventService;
import com.cloudinventory.inventory.integration.common.SyncDirection;
import com.cloudinventory.inventory.integration.common.SyncStatus;
import com.cloudinventory.inventory.orders.CustomerOrder;
import com.cloudinventory.inventory.orders.CustomerOrderRepository;
import com.cloudinventory.inventory.orders.OrderItem;
import com.cloudinventory.inventory.orders.OrderStatus;
import com.cloudinventory.inventory.orders.PurchaseOrder;
import com.cloudinventory.inventory.orders.PurchaseOrderRepository;
import com.cloudinventory.inventory.orders.PurchaseOrderStatus;
import com.cloudinventory.inventory.product.Product;
import com.cloudinventory.inventory.product.ProductRepository;
import com.cloudinventory.inventory.supplier.Supplier;
import com.cloudinventory.inventory.supplier.SupplierRepository;
import com.cloudinventory.inventory.user.AppUser;
import com.cloudinventory.inventory.user.Role;
import com.cloudinventory.inventory.user.RoleName;
import com.cloudinventory.inventory.user.RoleRepository;
import com.cloudinventory.inventory.user.UserRepository;

@Component
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final CustomerAccountRepository customerAccountRepository;
    private final IntegrationSyncEventService integrationSyncEventService;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            RoleRepository roleRepository,
            UserRepository userRepository,
            SupplierRepository supplierRepository,
            ProductRepository productRepository,
            CustomerOrderRepository customerOrderRepository,
            PurchaseOrderRepository purchaseOrderRepository,
            CustomerAccountRepository customerAccountRepository,
            IntegrationSyncEventService integrationSyncEventService,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.customerAccountRepository = customerAccountRepository;
        this.integrationSyncEventService = integrationSyncEventService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedRoles();
        seedUsers();
        seedSuppliersAndProducts();
        seedOrders();
        seedCustomerAccounts();
        seedIntegrationEvents();
    }

    private void seedRoles() {
        for (RoleName roleName : RoleName.values()) {
            roleRepository.findByName(roleName).orElseGet(() -> roleRepository.save(new Role(roleName)));
        }
    }

    private void seedUsers() {
        createUserIfMissing("admin", "Admin User", "admin@cloudinventory.com", "Admin@123", RoleName.ADMIN);
        createUserIfMissing("manager", "Operations Manager", "manager@cloudinventory.com", "Manager@123", RoleName.MANAGER);
        createUserIfMissing("warehouse", "Warehouse Associate", "warehouse@cloudinventory.com", "Warehouse@123", RoleName.WAREHOUSE_USER);
    }

    private void createUserIfMissing(String username, String fullName, String email, String password, RoleName roleName) {
        if (userRepository.existsByUsername(username)) {
            return;
        }
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.getRoles().add(roleRepository.findByName(roleName).orElseThrow());
        userRepository.save(user);
    }

    private void seedSuppliersAndProducts() {
        if (supplierRepository.count() > 0 || productRepository.count() > 0) {
            return;
        }

        Supplier techSupply = createSupplier("TechSupply Inc.", "Hannah Cole", "sales@techsupply.com", "+1-312-555-0101", "Chicago", "USA", "5 business days");
        Supplier globalParts = createSupplier("Global Parts Co.", "Nate Rivera", "orders@globalparts.com", "+1-214-555-0148", "Dallas", "USA", "7 business days");
        Supplier warehouseDirect = createSupplier("Warehouse Direct", "Priya Shah", "support@warehousedirect.com", "+1-404-555-0162", "Atlanta", "USA", "3 business days");

        List<Product> products = List.of(
                createProduct("LAP-1001", "Business Laptop", "14-inch enterprise laptop for office teams.", "Electronics", "899.99", 42, 10, "A-01-01", techSupply),
                createProduct("MON-2001", "24-inch Monitor", "Full HD monitor for desks and control rooms.", "Electronics", "179.99", 18, 12, "A-02-03", techSupply),
                createProduct("KEY-3001", "Mechanical Keyboard", "Durable keyboard for warehouse and office use.", "Peripherals", "69.99", 120, 25, "B-03-06", globalParts),
                createProduct("MED-4100", "Sterile Gloves Pack", "Disposable medical-grade gloves.", "Medical Supplies", "24.50", 9, 20, "C-01-05", warehouseDirect),
                createProduct("SNS-5100", "IoT Temperature Sensor", "Industrial sensor for warehouse cold-chain tracking.", "Sensors", "129.00", 15, 8, "D-02-09", globalParts),
                createProduct("BAR-6100", "Barcode Scanner", "Wireless barcode scanner for picking operations.", "Warehouse Equipment", "149.99", 7, 10, "E-04-02", warehouseDirect)
        );

        productRepository.saveAll(products);
    }

    private Supplier createSupplier(String name, String contactName, String email, String phone, String city, String country, String leadTime) {
        Supplier supplier = new Supplier();
        supplier.setName(name);
        supplier.setContactName(contactName);
        supplier.setEmail(email);
        supplier.setPhone(phone);
        supplier.setCity(city);
        supplier.setCountry(country);
        supplier.setLeadTime(leadTime);
        return supplierRepository.save(supplier);
    }

    private Product createProduct(
            String sku,
            String name,
            String description,
            String category,
            String unitPrice,
            int stockQuantity,
            int reorderLevel,
            String warehouseLocation,
            Supplier supplier
    ) {
        Product product = new Product();
        product.setSku(sku);
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setUnitPrice(new BigDecimal(unitPrice));
        product.setStockQuantity(stockQuantity);
        product.setReorderLevel(reorderLevel);
        product.setWarehouseLocation(warehouseLocation);
        product.setSupplier(supplier);
        return product;
    }

    private void seedOrders() {
        if (customerOrderRepository.count() == 0 && productRepository.count() >= 2) {
            List<Product> products = productRepository.findAll();
            CustomerOrder order = new CustomerOrder();
            order.setOrderNumber("SO-DEMO-1001");
            order.setCustomerName("Midwest Health Systems");
            order.setCustomerEmail("procurement@midwesthealth.com");
            order.setStatus(OrderStatus.PROCESSING);

            OrderItem itemOne = createOrderItem(order, products.get(0), 2);
            OrderItem itemTwo = createOrderItem(order, products.get(3), 3);
            order.getItems().add(itemOne);
            order.getItems().add(itemTwo);
            order.setTotalAmount(itemOne.getLineTotal().add(itemTwo.getLineTotal()));
            customerOrderRepository.save(order);
        }

        if (purchaseOrderRepository.count() == 0 && supplierRepository.count() > 0) {
            Supplier supplier = supplierRepository.findAll().get(0);
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setPoNumber("PO-DEMO-2001");
            purchaseOrder.setSupplier(supplier);
            purchaseOrder.setRequestedBy("Operations Manager");
            purchaseOrder.setStatus(PurchaseOrderStatus.IN_TRANSIT);
            purchaseOrder.setTotalAmount(new BigDecimal("6500.00"));
            purchaseOrder.setNotes("Restocking electronics before monthly forecast spike.");
            purchaseOrderRepository.save(purchaseOrder);
        }
    }

    private void seedCustomerAccounts() {
        if (customerAccountRepository.count() > 0) {
            return;
        }

        customerAccountRepository.save(createCustomerAccount(
                "Midwest Health Systems",
                "procurement@midwesthealth.com",
                "SF-ACC-3001",
                "SF-CON-3001",
                "Enterprise",
                "North America"
        ));
        customerAccountRepository.save(createCustomerAccount(
                "Vertex Retail Group",
                "buying@vertexretail.com",
                "SF-ACC-3002",
                "SF-CON-3002",
                "Strategic",
                "North America"
        ));
    }

    private CustomerAccount createCustomerAccount(
            String customerName,
            String customerEmail,
            String salesforceAccountId,
            String salesforceContactId,
            String customerTier,
            String region
    ) {
        CustomerAccount account = new CustomerAccount();
        account.setCustomerName(customerName);
        account.setCustomerEmail(customerEmail);
        account.setSalesforceAccountId(salesforceAccountId);
        account.setSalesforceContactId(salesforceContactId);
        account.setCustomerTier(customerTier);
        account.setRegion(region);
        return account;
    }

    private void seedIntegrationEvents() {
        if (!integrationSyncEventService.getRecentEvents().isEmpty()) {
            return;
        }

        integrationSyncEventService.record(
                ExternalSystemType.SAP_ERP,
                SyncDirection.INBOUND,
                SyncStatus.MOCK_SUCCESS,
                "inventory-sync",
                "MAT-1001",
                "Seeded SAP ERP inventory synchronization event."
        );
        integrationSyncEventService.record(
                ExternalSystemType.SAP_S4HANA,
                SyncDirection.INBOUND,
                SyncStatus.MOCK_SUCCESS,
                "purchase-order-sync",
                "PO-S4-3002",
                "Seeded SAP S/4HANA purchase order synchronization event."
        );
        integrationSyncEventService.record(
                ExternalSystemType.SALESFORCE_CRM,
                SyncDirection.OUTBOUND,
                SyncStatus.MOCK_SUCCESS,
                "salesforce-order-push",
                "SO-DEMO-1001",
                "Seeded Salesforce CRM outbound order sync event."
        );
    }

    private OrderItem createOrderItem(CustomerOrder order, Product product, int quantity) {
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setUnitPrice(product.getUnitPrice());
        item.setLineTotal(product.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }
}
