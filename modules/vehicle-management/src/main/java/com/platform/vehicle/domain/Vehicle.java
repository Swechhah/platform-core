package com.platform.vehicle.domain;

import com.platform.common.domain.core.BaseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

public class Vehicle extends BaseEntity {
    
    public enum VehicleStatus {
        AVAILABLE("AVAILABLE"),
        BOOKED("BOOKED"), // Reserved but not yet in use
        IN_USE("IN_USE"), // Currently being used
        MAINTENANCE("MAINTENANCE"),
        OUT_OF_SERVICE("OUT_OF_SERVICE");

        private final String status;

        VehicleStatus(String status) {
            this.status = status;
        }

        public String getValue() {
            return status;
        }

        public static VehicleStatus fromString(String status) {
            return Arrays.stream(VehicleStatus.values())
                    .filter(s -> s.status.equalsIgnoreCase(status))
                    .findFirst()
                    .orElse(AVAILABLE);
        }
    }

    public enum VehicleType {
        SEDAN("SEDAN"),
        SUV("SUV"),
        VAN("VAN"),
        TRUCK("TRUCK"),
        MOTORCYCLE("MOTORCYCLE"),
        OTHER("OTHER");

        private final String type;

        VehicleType(String type) {
            this.type = type;
        }

        public String getValue() {
            return type;
        }

        public static VehicleType fromString(String type) {
            return Arrays.stream(VehicleType.values())
                    .filter(t -> t.type.equalsIgnoreCase(type))
                    .findFirst()
                    .orElse(SEDAN);
        }
    }

    private String plateNumber;
    private String make;
    private String model;
    private Integer year;
    private VehicleType type;
    private VehicleStatus status;
    private int capacity; // Number of passengers
    private String color;
    private String description;
    private String location; // Where the vehicle is currently located
    private LocalDateTime lastMaintenance;
    private LocalDateTime nextMaintenance;
    private boolean availableForBooking = true;
    private double mileage;
    private String fuelType;
    private String vehicleGroup; // For grouping similar vehicles
    private String costCenter;

    // Constructors
    public Vehicle() {
        super();
        this.status = VehicleStatus.AVAILABLE;
        this.availableForBooking = true;
    }

    public Vehicle(String plateNumber, String make, String model, Integer year, VehicleType type, int capacity) {
        this();
        this.plateNumber = plateNumber;
        this.make = make;
        this.model = model;
        this.year = year;
        this.type = type;
        this.capacity = capacity;
    }

    // Business Methods
    public boolean isAvailable() {
        return status == VehicleStatus.AVAILABLE && availableForBooking;
    }

    public boolean isInUse() {
        return status == VehicleStatus.IN_USE;
    }

    public boolean isBooked() {
        return status == VehicleStatus.BOOKED;
    }

    public boolean canBeBooked() {
        return isAvailable() && nextMaintenance != null && 
               LocalDateTime.now().isBefore(nextMaintenance.minusDays(7)); // 7 days before maintenance
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
        markUpdated("system");
    }

    public void setInUse() {
        this.status = VehicleStatus.IN_USE;
        markUpdated("system");
    }

    public void setAvailable() {
        this.status = VehicleStatus.AVAILABLE;
        this.availableForBooking = true;
        markUpdated("system");
    }

    public void setBooked() {
        this.status = VehicleStatus.BOOKED;
        this.availableForBooking = false;
        markUpdated("system");
    }

    public void setMaintenance() {
        this.status = VehicleStatus.MAINTENANCE;
        this.availableForBooking = false;
        markUpdated("system");
    }

    public void setOutOfService() {
        this.status = VehicleStatus.OUT_OF_SERVICE;
        this.availableForBooking = false;
        markUpdated("system");
    }

    public void updateMileage(double newMileage) {
        if (newMileage > this.mileage) {
            this.mileage = newMileage;
            markUpdated("system");
        }
    }

    public void updateMaintenanceSchedule() {
        this.lastMaintenance = LocalDateTime.now();
        this.nextMaintenance = lastMaintenance.plusMonths(6); // Maintenance every 6 months
        markUpdated("system");
    }

    public boolean needsMaintenance() {
        if (nextMaintenance == null) return true;
        return LocalDateTime.now().isAfter(nextMaintenance.minusDays(7)) || 
               LocalDateTime.now().isAfter(nextMaintenance);
    }

    public String getDisplayName() {
        return String.format("%s %s %s (%s)", year, make, model, plateNumber);
    }

    // Getters and Setters
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

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public VehicleStatus getStatus() {
        return status;
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

    public boolean isAvailableForBooking() {
        return availableForBooking;
    }

    public void setAvailableForBooking(boolean availableForBooking) {
        this.availableForBooking = availableForBooking;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(plateNumber, vehicle.plateNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plateNumber);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                "plateNumber='" + plateNumber + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", type=" + type +
                ", status=" + status +
                ", capacity=" + capacity +
                ", location='" + location + '\'' +
                '}';
    }
}