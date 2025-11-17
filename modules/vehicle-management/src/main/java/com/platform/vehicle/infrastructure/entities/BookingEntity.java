package com.platform.vehicle.infrastructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity for Booking in the vehicle management system.
 * Maps the domain Booking entity to database persistence.
 */
@Entity
@Table(name = "bookings")
@EntityListeners(AuditingEntityListener.class)
public class BookingEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "booking_reference", nullable = false, unique = true)
    @NotBlank
    @Size(max = 50)
    private String bookingReference;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull
    private BookingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private BookingType type;

    // Foreign key relationships
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "driver_id")
    private Long driverId; // Nullable for self-driving

    @Column(name = "requester_id", nullable = false)
    private Long requesterId;

    @Column(name = "approver_id")
    private Long approverId;

    // Time fields
    @Column(name = "start_time", nullable = false)
    @NotNull
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    @NotNull
    private LocalDateTime endTime;

    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime;

    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;

    // Location fields
    @Column(name = "pickup_location")
    @Size(max = 200)
    private String pickupLocation;

    @Column(name = "destination")
    @Size(max = 200)
    private String destination;

    @Column(name = "return_location")
    @Size(max = 200)
    private String returnLocation;

    // Purpose and details
    @Column(name = "purpose", nullable = false)
    @NotBlank
    @Size(max = 200)
    private String purpose;

    @Column(name = "description")
    private String description;

    @Column(name = "estimated_passengers", nullable = false)
    private int estimatedPassengers = 1;

    @Column(name = "manager_name")
    @Size(max = 100)
    private String managerName;

    @Column(name = "cost_center")
    @Size(max = 50)
    private String costCenter;

    // Approval workflow
    @Column(name = "approval_comment")
    private String approvalComment;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    // Notes and feedback
    @Column(name = "internal_notes")
    private String internalNotes;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "actual_mileage", nullable = false)
    private double actualMileage = 0.0;

    @Column(name = "additional_requirements")
    private String additionalRequirements;

    // Tracking fields
    @Column(name = "approval_level", nullable = false)
    private int approvalLevel = 1;

    @Column(name = "is_recurring", nullable = false)
    private boolean isRecurring = false;

    @Column(name = "recurring_pattern")
    private String recurringPattern;

    @Column(name = "estimated_cost", nullable = false)
    private double estimatedCost = 0.0;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", insertable = false, updatable = false)
    private VehicleEntity vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", insertable = false, updatable = false)
    private DriverEntity driver;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookingEventEntity> events = new ArrayList<>();

    // Constructors
    public BookingEntity() {
        this.status = BookingStatus.PENDING;
        this.approvalLevel = 1;
        this.estimatedPassengers = 1;
        generateBookingReference();
    }

    public BookingEntity(Long vehicleId, Long requesterId, String purpose, LocalDateTime startTime, LocalDateTime endTime) {
        this();
        this.vehicleId = vehicleId;
        this.requesterId = requesterId;
        this.purpose = purpose;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Helper methods
    private void generateBookingReference() {
        this.bookingReference = "BKG-" + System.currentTimeMillis() + "-" + (1000 + (int)(Math.random() * 9000));
    }

    public void markCreated(String createdBy) {
        this.createdBy = createdBy;
        this.updatedBy = createdBy;
    }

    public void markUpdated(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public BookingType getType() {
        return type;
    }

    public void setType(BookingType type) {
        this.type = type;
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

    public VehicleEntity getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleEntity vehicle) {
        this.vehicle = vehicle;
    }

    public DriverEntity getDriver() {
        return driver;
    }

    public void setDriver(DriverEntity driver) {
        this.driver = driver;
    }

    public List<BookingEventEntity> getEvents() {
        return events;
    }

    public void setEvents(List<BookingEventEntity> events) {
        this.events = events;
    }

    // Helper methods for relationships
    public void addEvent(BookingEventEntity event) {
        events.add(event);
        event.setBooking(this);
    }

    public void removeEvent(BookingEventEntity event) {
        events.remove(event);
        event.setBooking(null);
    }

    // Enums
    public enum BookingStatus {
        PENDING("PENDING"),
        APPROVED("APPROVED"),
        REJECTED("REJECTED"),
        CONFIRMED("CONFIRMED"),
        ACTIVE("ACTIVE"),
        COMPLETED("COMPLETED"),
        CANCELLED("CANCELLED"),
        NO_SHOW("NO_SHOW");

        private final String status;

        BookingStatus(String status) {
            this.status = status;
        }

        public String getValue() {
            return status;
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
    }

    @Override
    public String toString() {
        return "BookingEntity{" +
                "id=" + id +
                "bookingReference='" + bookingReference + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", vehicleId=" + vehicleId +
                ", driverId=" + driverId +
                ", purpose='" + purpose + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}