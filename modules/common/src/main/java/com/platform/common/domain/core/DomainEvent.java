package com.platform.common.domain.core;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class DomainEvent {
    
    private String eventId;
    private String aggregateId;
    private String eventType;
    private LocalDateTime timestamp;
    private String eventData;
    private String causedBy;
    private int version;

    // Constructors
    public DomainEvent() {
        this.eventId = generateEventId();
        this.timestamp = LocalDateTime.now();
        this.version = 1;
    }

    public DomainEvent(String aggregateId, String eventType) {
        this();
        this.aggregateId = aggregateId;
        this.eventType = eventType;
    }

    public DomainEvent(String aggregateId, String eventType, String eventData, String causedBy) {
        this(aggregateId, eventType);
        this.eventData = eventData;
        this.causedBy = causedBy;
    }

    // Helper Methods
    private String generateEventId() {
        return "evt-" + System.currentTimeMillis() + "-" + Thread.currentThread().getId();
    }

    // Getters and Setters
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public String getCausedBy() {
        return causedBy;
    }

    public void setCausedBy(String causedBy) {
        this.causedBy = causedBy;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainEvent that = (DomainEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "DomainEvent{" +
                "eventId='" + eventId + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", timestamp=" + timestamp +
                ", causedBy='" + causedBy + '\'' +
                ", version=" + version +
                '}';
    }
}