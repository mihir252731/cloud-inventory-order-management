public with sharing class OrderStatusSyncHandler {
    @future(callout=true)
    public static void syncPlatformStatus(List<Order__c> newOrders, Map<Id, Order__c> oldMap) {
        for (Order__c orderRecord : newOrders) {
            Order__c previous = oldMap != null ? oldMap.get(orderRecord.Id) : null;
            if (previous == null || previous.Status__c != orderRecord.Status__c) {
                System.debug('Push order status change to integration platform for ' + orderRecord.External_Order_Number__c);
            }
        }
    }
}
