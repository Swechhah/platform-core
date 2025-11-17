package com.platform.vehicle.domain.events;

import com.platform.common.domain.core.DomainEvent;

public class BookingEvent extends DomainEvent {
    
    public BookingEvent(String aggregateId, String eventType, String eventData, String causedBy) {
        super(aggregateId, eventType, eventData, causedBy);
    }
    
    public BookingEvent(String aggregateId, String eventType, String eventData) {
        super(aggregateId, eventType, eventData, "system");
    }
    
    public BookingEvent(String aggregateId, String eventType) {
        super(aggregateId, eventType, null, "system");
    }
}