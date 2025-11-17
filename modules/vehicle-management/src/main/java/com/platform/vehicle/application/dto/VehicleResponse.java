package com.platform.vehicle.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public class VehicleResponse {
    
    private Long id;
    private String plateNumber;
    private String make;
    private String model;
    private Integer year;
    private String type;
    private String status;
    private int capacity;
    private String color;
    private String description;
    private String location;
    private LocalDateTime lastMaintenance;
    private LocalDateTime nextMaintenance;
    private double mileage;
    private String fuelType;
    private String vehicleGroup;
    private String costCenter;
    private boolean available;
    private String displayName;
    
    // Current booking information
    private String currentBookingReference;
    private LocalDateTime currentBookingEndTime;
    private String currentBookingPurpose;
    
    // Upcoming bookings (next 5)
    private List<BookingSummary> upcomingBookings;

    // Constructors
    public VehicleResponse() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getLastMaintenance() {
        return lastMaintenance;
    }

    public void setLastMaintenance(LocalDateTime lastMaintenance) {
        this.lastMaintenance = lastMaintenance;
    }

    public LocalDateTime getNextMaintenance() {
        return nextMaintenance;
    }

    public void setNextMaintenance(LocalDateTime nextMaintenance) {
        this.nextMaintenance = nextMaintenance;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getVehicleGroup() {
        return vehicleGroup;
    }

    public void setVehicleGroup(String vehicleGroup) {
        this.vehicleGroup = vehicleGroup;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCurrentBookingReference() {
        return currentBookingReference;
    }

    public void setCurrentBookingReference(String currentBookingReference) {
        this.currentBookingReference = currentBookingReference;
    }

    public LocalDateTime getCurrentBookingEndTime() {
        return currentBookingEndTime;
    }

    public void setCurrentBookingEndTime(LocalDateTime currentBookingEndTime) {
        this.currentBookingEndTime = currentBookingEndTime;
    }

    public String getCurrentBookingPurpose() {
        return currentBookingPurpose;
    }

    public void setCurrentBookingPurpose(String currentBookingPurpose) {
        this.currentBookingPurpose = currentBookingPurpose;
    }

    public List<BookingSummary> getUpcomingBookings() {
        return upcomingBookings;
    }

    public void setUpcomingBookings(List<BookingSummary> upcomingBookings) {
        this.upcomingBookings = upcomingBookings;
    }

    // Static helper class for booking summaries
    public static class BookingSummary {
        private String reference;
        private String purpose;
        private String status;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String requesterName;

        public BookingSummary() {}

        public BookingSummary(String reference, String purpose, String status, 
                            LocalDateTime startTime, LocalDateTime endTime, String requesterName) {
            this.reference = reference;
            this.purpose = purpose;
            this.status = status;
            this.startTime = startTime;
            this.endTime = endTime;
            this.requesterName = requesterName;
        }

        // Getters and Setters
        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
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

        public String getRequesterName() {
            return requesterName;
        }

        public void setRequesterName(String requesterName) {
            this.requesterName = requesterName;
        }
    }

    @Override
    public String toString() {
        return "VehicleResponse{" +
                "id=" + id +
                "plateNumber='" + plateNumber + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", available=" + available +
                '}';
    }
}