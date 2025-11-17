package com.platform.vehicle.application.dto;

import java.time.LocalDateTime;

public class BookingResponse {
    
    private Long id;
    private String bookingReference;
    private String status;
    private String type;
    
    // Related entities
    private VehicleSummary vehicle;
    private DriverSummary driver;
    private UserSummary requester;
    private UserSummary approver;
    
    // Time information
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    
    // Location and details
    private String purpose;
    private String description;
    private String pickupLocation;
    private String destination;
    private String returnLocation;
    private int estimatedPassengers;
    private String managerName;
    private String costCenter;
    
    // Approval workflow
    private String approvalComment;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private String rejectionReason;
    private String displayStatus;
    
    // Tracking
    private String feedback;
    private double actualMileage;
    private String additionalRequirements;
    private long durationInHours;
    private boolean canBeCancelled;
    private boolean isCurrent;
    private boolean isUpcoming;
    private boolean needsApproval;

    // Constructors
    public BookingResponse() {}

    // Static helper classes for summaries
    public static class VehicleSummary {
        private Long id;
        private String plateNumber;
        private String displayName;
        private String type;
        private String status;

        public VehicleSummary() {}

        public VehicleSummary(Long id, String plateNumber, String displayName, String type, String status) {
            this.id = id;
            this.plateNumber = plateNumber;
            this.displayName = displayName;
            this.type = type;
            this.status = status;
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getPlateNumber() { return plateNumber; }
        public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class DriverSummary {
        private Long id;
        private String displayName;
        private String licenseType;
        private String phoneNumber;
        private String status;

        public DriverSummary() {}

        public DriverSummary(Long id, String displayName, String licenseType, String phoneNumber, String status) {
            this.id = id;
            this.displayName = displayName;
            this.licenseType = licenseType;
            this.phoneNumber = phoneNumber;
            this.status = status;
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public String getLicenseType() { return licenseType; }
        public void setLicenseType(String licenseType) { this.licenseType = licenseType; }
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class UserSummary {
        private Long id;
        private String username;
        private String fullName;
        private String email;
        private String department;
        private String role;

        public UserSummary() {}

        public UserSummary(Long id, String username, String fullName, String email, String department, String role) {
            this.id = id;
            this.username = username;
            this.fullName = fullName;
            this.email = email;
            this.department = department;
            this.role = role;
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    // Getters and Setters for main class
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public VehicleSummary getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleSummary vehicle) {
        this.vehicle = vehicle;
    }

    public DriverSummary getDriver() {
        return driver;
    }

    public void setDriver(DriverSummary driver) {
        this.driver = driver;
    }

    public UserSummary getRequester() {
        return requester;
    }

    public void setRequester(UserSummary requester) {
        this.requester = requester;
    }

    public UserSummary getApprover() {
        return approver;
    }

    public void setApprover(UserSummary approver) {
        this.approver = approver;
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

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
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

    public long getDurationInHours() {
        return durationInHours;
    }

    public void setDurationInHours(long durationInHours) {
        this.durationInHours = durationInHours;
    }

    public boolean isCanBeCancelled() {
        return canBeCancelled;
    }

    public void setCanBeCancelled(boolean canBeCancelled) {
        this.canBeCancelled = canBeCancelled;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public boolean isUpcoming() {
        return isUpcoming;
    }

    public void setUpcoming(boolean upcoming) {
        isUpcoming = upcoming;
    }

    public boolean isNeedsApproval() {
        return needsApproval;
    }

    public void setNeedsApproval(boolean needsApproval) {
        this.needsApproval = needsApproval;
    }

    @Override
    public String toString() {
        return "BookingResponse{" +
                "id=" + id +
                "bookingReference='" + bookingReference + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", purpose='" + purpose + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}