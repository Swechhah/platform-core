package com.platform.vehicle.application.usecases;

import com.platform.common.domain.core.DomainEvent;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EventPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);
    private final ApplicationEventPublisher eventPublisher;

    public EventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * Publish a domain event to the application event system.
     * 
     * @param event The domain event to publish
     */
    public void publishEvent(DomainEvent event) {
        try {
            // Publish the event to Spring's application event system
            eventPublisher.publishEvent(new DomainEventWrapper(event));
            
            // Log the event for audit purposes
            logger.info("Domain event published: {} for aggregate: {} at {}", 
                       event.getEventType(), 
                       event.getAggregateId(), 
                       event.getTimestamp());
                       
        } catch (Exception e) {
            // Log error but don't throw to prevent business operation failure
            logger.error("Failed to publish domain event: {} for aggregate: {}", 
                        event.getEventType(), 
                        event.getAggregateId(), 
                        e);
        }
    }

    /**
     * Publish multiple events in batch.
     * 
     * @param events List of domain events to publish
     */
    public void publishEvents(java.util.List<DomainEvent> events) {
        events.forEach(this::publishEvent);
    }

    /**
     * Wrapper class to make DomainEvent compatible with Spring's ApplicationEvent.
     */
    public static class DomainEventWrapper {
        private final DomainEvent event;
        private final long timestamp = System.currentTimeMillis();

        public DomainEventWrapper(DomainEvent event) {
            this.event = event;
        }

        public DomainEvent getEvent() {
            return event;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}