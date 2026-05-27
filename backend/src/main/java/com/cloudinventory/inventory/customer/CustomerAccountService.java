package com.cloudinventory.inventory.customer;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CustomerAccountService {

    private final CustomerAccountRepository customerAccountRepository;

    public CustomerAccountService(CustomerAccountRepository customerAccountRepository) {
        this.customerAccountRepository = customerAccountRepository;
    }

    public List<CustomerAccount> getCustomerAccounts() {
        return customerAccountRepository.findAll();
    }

    public CustomerAccount getOrCreate(String customerName, String customerEmail) {
        return customerAccountRepository.findByCustomerEmail(customerEmail)
                .orElseGet(() -> {
                    CustomerAccount account = new CustomerAccount();
                    account.setCustomerName(customerName);
                    account.setCustomerEmail(customerEmail);
                    account.setSalesforceAccountId("SF-ACC-" + Math.abs(customerEmail.hashCode()));
                    account.setSalesforceContactId("SF-CON-" + Math.abs(customerName.hashCode()));
                    account.setCustomerTier("Enterprise");
                    account.setRegion("North America");
                    return customerAccountRepository.save(account);
                });
    }
}
