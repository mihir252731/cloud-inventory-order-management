# SAP and Salesforce Integration Notes

This project now positions the platform as a central integration layer between internal inventory operations, SAP ERP / SAP S/4HANA, and Salesforce CRM.

## SAP ERP and SAP S/4HANA

- SAP inventory sync endpoints simulate inbound OData-style inventory feeds.
- SAP purchase order sync endpoints simulate ERP purchase order ingestion into the local platform.
- Sync activity is captured in `integration_sync_events` for traceability.
- Internal product stock is updated from SAP-sourced quantity records, keeping the local platform aligned with ERP-style source-of-truth behavior.

## Salesforce CRM

- Customer orders can be pushed outbound to Salesforce CRM through dedicated integration endpoints.
- Customer account mappings are stored in `customer_accounts`.
- Customer and order history fetches simulate SOQL-driven CRM lookups.
- Example Apex trigger and handler assets are included in [salesforce/apex](../salesforce/apex).
- Example SOQL query assets are included in [salesforce/soql](../salesforce/soql).

## Why the integration layer matters

- SAP systems remain the source of truth for inventory and purchase order activity.
- Salesforce CRM remains the customer-facing system for account and order relationship data.
- The platform acts as an operational hub that normalizes internal workflows while syncing with both enterprise systems.
