package com.platform.vehicle.application.dto;

import java.time.LocalDateTime;

public class CreateBookingRequest {
    
    private Long vehicleId;
    private Long driverId; // Optional, for self-driving
    private String purpose;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String pickupLocation;
    private String destination;
    private String returnLocation;
    private int estimatedPassengers = 1;
    private String managerName;
    private String costCenter;
    private String bookingType;
    private String additionalRequirements;

    // Constructors
    public CreateBookingRequest() {}

    public CreateBookingRequest(Long vehicleId, String purpose, LocalDateTime startTime, LocalDateTime endTime) {
        this.vehicleId = vehicleId;
        this.purpose = purpose;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
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

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getAdditionalRequirements() {
        return additionalRequirements;
    }

    public void setAdditionalRequirements(String additionalRequirements) {
        this.additionalRequirements = additionalRequirements;
    }

    @Override
    public String toString() {
        return "CreateBookingRequest{" +
                "vehicleId=" + vehicleId +
                ", driverId=" + driverId +
                ", purpose='" + purpose + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", pickupLocation='" + pickupLocation + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}