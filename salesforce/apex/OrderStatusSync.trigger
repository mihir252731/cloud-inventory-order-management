trigger OrderStatusSync on Order__c (after insert, after update) {
    if (Trigger.isAfter) {
        OrderStatusSyncHandler.syncPlatformStatus(Trigger.new, Trigger.oldMap);
    }
}
