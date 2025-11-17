package com.platform.vehicle.domain;

import com.platform.common.domain.core.BaseEntity;
import com.platform.common.domain.core.DomainEvent;
import com.platform.vehicle.domain.events.BookingEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Booking extends BaseEntity {
    
    public enum BookingStatus {
        PENDING("PENDING"), // Initial state when created
        APPROVED("APPROVED"), // Manager approved
        REJECTED("REJECTED"), // Manager rejected
        CONFIRMED("CONFIRMED"), // Admin confirmed (system confirmation)
        ACTIVE("ACTIVE"), // Trip is currently in progress
        COMPLETED("COMPLETED"), // Trip completed successfully
        CANCELLED("CANCELLED"), // Cancelled by user or admin
        NO_SHOW("NO_SHOW"); // User didn't show up

        private final String status;

        BookingStatus(String status) {
            this.status = status;
        }

        public String getValue() {
            return status;
        }

        public static BookingStatus fromString(String status) {
            return Arrays.stream(BookingStatus.values())
                    .filter(s -> s.status.equalsIgnoreCase(status))
                    .findFirst()
                    .orElse(PENDING);
        }

        public boolean canTransitionTo(BookingStatus newStatus) {
            switch (this) {
                case PENDING:
                    return newStatus == APPROVED || newStatus == REJECTED || newStatus == CANCELLED;
                case APPROVED:
                    return newStatus == CONFIRMED || newStatus == CANCELLED || newStatus == REJECTED;
                case CONFIRMED:
                    return newStatus == ACTIVE || newStatus == CANCELLED || newStatus == NO_SHOW;
                case ACTIVE:
                    return newStatus == COMPLETED || newStatus == CANCELLED;
                case REJECTED:
                case CANCELLED:
                case COMPLETED:
                case NO_SHOW:
                    return false; // Terminal states
                default:
                    return false;
            }
        }
    }

    public enum BookingType {
        BUSINESS_TRIP("BUSINESS_TRIP"),
        MEETING("MEETING"),
        DELIVERY("DELIVERY"),
        MAINTENANCE_TRIP("MAINTENANCE_TRIP"),
        TRAINING("TRAINING"),
        OTHER("OTHER");

        private final String type;

        BookingType(String type) {
            this.type = type;
        }

        public String getValue() {
            return type;
        }

        public static BookingType fromString(String type) {
            return Arrays.stream(BookingType.values())
                    .filter(t -> t.type.equalsIgnoreCase(type))
                    .findFirst()
                    .orElse(BUSINESS_TRIP);
        }
    }

    // Core booking fields
    private String bookingReference; // Unique booking reference
    private Long vehicleId; // References Vehicle entity
    private Long driverId; // References Driver entity (nullable for self-driving)
    private Long requesterId; // References User entity who made the request
    private Long approverId; // References User entity who approved/rejected
    private BookingType type;
    private BookingStatus status;
    
    // Time fields
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime actualStartTime; // When trip actually started
    private LocalDateTime actualEndTime; // When trip actually ended
    
    // Location fields
    private String pickupLocation;
    private String destination;
    private String returnLocation;
    
    // Purpose and details
    private String purpose;
    private String description;
    private int estimatedPassengers; // Number of expected passengers
    private String managerName; // For approval workflow
    private String costCenter;
    
    // Approval workflow
    private String approvalComment;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private String rejectionReason;
    
    // Notes and feedback
    private String internalNotes;
    private String feedback;
    private double actualMileage;
    private String additionalRequirements; // Special requirements
    
    // Tracking fields
    private List<DomainEvent> eventHistory;
    private int approvalLevel; // For multi-level approval
    private boolean isRecurring; // For recurring bookings
    private String recurringPattern; // If recurring
    private double estimatedCost;

    // Constructors
    public Booking() {
        super();
        this.status = BookingStatus.PENDING;
        this.approvalLevel = 1;
        this.estimatedPassengers = 1;
        this.eventHistory = new ArrayList<>();
        generateBookingReference();
    }

    public Booking(Long vehicleId, Long requesterId, String purpose, LocalDateTime startTime, LocalDateTime endTime) {
        this();
        this.vehicleId = vehicleId;
        this.requesterId = requesterId;
        this.purpose = purpose;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Business Methods
    private void generateBookingReference() {
        this.bookingReference = "BKG-" + System.currentTimeMillis() + "-" + (1000 + (int)(Math.random() * 9000));
    }

    public boolean isOverlapping(LocalDateTime start, LocalDateTime end) {
        return this.startTime.isBefore(end) && this.endTime.isAfter(start) &&
               (this.status == BookingStatus.APPROVED || 
                this.status == BookingStatus.CONFIRMED || 
                this.status == BookingStatus.ACTIVE);
    }

    public boolean isCurrent() {
        LocalDateTime now = LocalDateTime.now();
        return startTime.isBefore(now) && endTime.isAfter(now) && status == BookingStatus.ACTIVE;
    }

    public boolean isUpcoming() {
        return startTime.isAfter(LocalDateTime.now()) && 
               (status == BookingStatus.APPROVED || status == BookingStatus.CONFIRMED);
    }

    public boolean canBeCancelled() {
        return status == BookingStatus.PENDING || 
               status == BookingStatus.APPROVED || 
               status == BookingStatus.CONFIRMED;
    }

    public boolean needsApproval() {
        return status == BookingStatus.PENDING;
    }

    public boolean isExpired() {
        return startTime.isBefore(LocalDateTime.now()) && 
               (status == BookingStatus.PENDING || status == BookingStatus.APPROVED);
    }

    public void approve(String approverId, String comment) {
        if (!status.canTransitionTo(BookingStatus.APPROVED)) {
            throw new IllegalStateException("Cannot approve booking in status: " + status);
        }
        this.status = BookingStatus.APPROVED;
        this.approverId = Long.valueOf(approverId);
        this.approvalComment = comment;
        this.approvedAt = LocalDateTime.now();
        markUpdated(approverId);
        addEvent("BOOKING_APPROVED", "Booking approved by manager: " + comment, approverId);
    }

    public void reject(String approverId, String reason) {
        if (!status.canTransitionTo(BookingStatus.REJECTED)) {
            throw new IllegalStateException("Cannot reject booking in status: " + status);
        }
        this.status = BookingStatus.REJECTED;
        this.approverId = Long.valueOf(approverId);
        this.rejectionReason = reason;
        this.rejectedAt = LocalDateTime.now();
        markUpdated(approverId);
        addEvent("BOOKING_REJECTED", "Booking rejected: " + reason, approverId);
    }

    public void confirm() {
        if (!status.canTransitionTo(BookingStatus.CONFIRMED)) {
            throw new IllegalStateException("Cannot confirm booking in status: " + status);
        }
        this.status = BookingStatus.CONFIRMED;
        markUpdated("system");
        addEvent("BOOKING_CONFIRMED", "Booking confirmed by system", "system");
    }

    public void activate() {
        if (!status.canTransitionTo(BookingStatus.ACTIVE)) {
            throw new IllegalStateException("Cannot activate booking in status: " + status);
        }
        this.status = BookingStatus.ACTIVE;
        this.actualStartTime = LocalDateTime.now();
        markUpdated("system");
        addEvent("BOOKING_ACTIVATED", "Trip started", "system");
    }

    public void complete(String feedback, double actualMileage) {
        if (!status.canTransitionTo(BookingStatus.COMPLETED)) {
            throw new IllegalStateException("Cannot complete booking in status: " + status);
        }
        this.status = BookingStatus.COMPLETED;
        this.actualEndTime = LocalDateTime.now();
        this.feedback = feedback;
        this.actualMileage = actualMileage;
        markUpdated("system");
        addEvent("BOOKING_COMPLETED", "Trip completed", "system");
    }

    public void cancel(String reason) {
        if (!canBeCancelled()) {
            throw new IllegalStateException("Cannot cancel booking in status: " + status);
        }
        this.status = BookingStatus.CANCELLED;
        this.rejectionReason = reason;
        markUpdated("system");
        addEvent("BOOKING_CANCELLED", "Booking cancelled: " + reason, "system");
    }

    public void markNoShow() {
        if (!status.canTransitionTo(BookingStatus.NO_SHOW)) {
            throw new IllegalStateException("Cannot mark as no-show in status: " + status);
        }
        this.status = BookingStatus.NO_SHOW;
        markUpdated("system");
        addEvent("BOOKING_NO_SHOW", "User didn't show up", "system");
    }

    public void addEvent(String eventType, String eventData, String causedBy) {
        BookingEvent event = new BookingEvent(getBookingReference(), eventType, eventData, causedBy);
        eventHistory.add(event);
    }

    public long getDurationInHours() {
        return startTime.until(endTime, java.time.temporal.ChronoUnit.HOURS);
    }

    public long getActualDurationInHours() {
        if (actualStartTime != null && actualEndTime != null) {
            return actualStartTime.until(actualEndTime, java.time.temporal.ChronoUnit.HOURS);
        }
        return 0;
    }

    public String getDisplayStatus() {
        switch (status) {
            case PENDING: return "Pending Approval";
            case APPROVED: return "Approved";
            case REJECTED: return "Rejected";
            case CONFIRMED: return "Confirmed";
            case ACTIVE: return "In Progress";
            case COMPLETED: return "Completed";
            case CANCELLED: return "Cancelled";
            case NO_SHOW: return "No Show";
            default: return status.getValue();
        }
    }

    public String getTimeRange() {
        return startTime + " to " + endTime;
    }

    // Getters and Setters
    public String getBookingReference() {
        return bookingReference;
    }
    
    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public BookingType getType() {
        return type;
    }

    public void setType(BookingType type) {
        this.type = type;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(LocalDateTime actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public LocalDateTime getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(LocalDateTime actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getReturnLocation() {
        return returnLocation;
    }

    public void setReturnLocation(String returnLocation) {
        this.returnLocation = returnLocation;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEstimatedPassengers() {
        return estimatedPassengers;
    }

    public void setEstimatedPassengers(int estimatedPassengers) {
        this.estimatedPassengers = estimatedPassengers;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getApprovalComment() {
        return approvalComment;
    }

    public void setApprovalComment(String approvalComment) {
        this.approvalComment = approvalComment;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public LocalDateTime getRejectedAt() {
        return rejectedAt;
    }

    public void setRejectedAt(LocalDateTime rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getInternalNotes() {
        return internalNotes;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public double getActualMileage() {
        return actualMileage;
    }

    public void setActualMileage(double actualMileage) {
        this.actualMileage = actualMileage;
    }

    public String getAdditionalRequirements() {
        return additionalRequirements;
    }

    public void setAdditionalRequirements(String additionalRequirements) {
        this.additionalRequirements = additionalRequirements;
    }

    public List<DomainEvent> getEventHistory() {
        return eventHistory;
    }

    public void setEventHistory(List<DomainEvent> eventHistory) {
        this.eventHistory = eventHistory;
    }

    public int getApprovalLevel() {
        return approvalLevel;
    }

    public void setApprovalLevel(int approvalLevel) {
        this.approvalLevel = approvalLevel;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public String getRecurringPattern() {
        return recurringPattern;
    }

    public void setRecurringPattern(String recurringPattern) {
        this.recurringPattern = recurringPattern;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(bookingReference, booking.bookingReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingReference);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingReference='" + bookingReference + '\'' +
                ", vehicleId=" + vehicleId +
                ", driverId=" + driverId +
                ", requesterId=" + requesterId +
                ", type=" + type +
                ", status=" + status +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", purpose='" + purpose + '\'' +
                '}';
    }
}