package com.platform.vehicle.infrastructure.entities;

import com.platform.common.domain.core.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
public class VehicleEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull
    private VehicleStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @NotNull
    private VehicleType type;

    @Column(name = "plate_number", nullable = false, unique = true)
    @NotBlank
    @Size(max = 20)
    private String plateNumber;

    @Column(name = "make", nullable = false)
    @NotBlank
    @Size(max = 50)
    private String make;

    @Column(name = "model", nullable = false)
    @NotBlank
    @Size(max = 50)
    private String model;

    @Column(name = "manufacture_year", nullable = false)
    @Positive
    private Integer manufactureYear;

    @Column(name = "capacity", nullable = false)
    @Positive
    private int capacity;

    @Column(name = "color")
    @Size(max = 50)
    private String color;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    @Size(max = 100)
    private String location;

    @Column(name = "last_maintenance")
    private LocalDateTime lastMaintenance;

    @Column(name = "next_maintenance")
    private LocalDateTime nextMaintenance;

    @Column(name = "available_for_booking", nullable = false)
    private boolean availableForBooking = true;

    @Column(name = "mileage", nullable = false)
    private double mileage = 0.0;

    @Column(name = "fuel_type")
    @Size(max = 20)
    private String fuelType;

    @Column(name = "vehicle_group")
    @Size(max = 50)
    private String vehicleGroup;

    @Column(name = "cost_center")
    @Size(max = 50)
    private String costCenter;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookingEntity> bookings = new ArrayList<>();

    // Constructors
    public VehicleEntity() {
        super();
        this.status = VehicleStatus.AVAILABLE;
        this.availableForBooking = true;
    }

    public VehicleEntity(String plateNumber, String make, String model, Integer manufactureYear, VehicleType type, int capacity) {
        this();
        this.plateNumber = plateNumber;
        this.make = make;
        this.model = model;
        this.manufactureYear = manufactureYear;
        this.type = type;
        this.capacity = capacity;
    }

    // Getters and Setters
    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
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

    public Integer getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(Integer manufactureYear) {
        this.manufactureYear = manufactureYear;
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

    public List<BookingEntity> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingEntity> bookings) {
        this.bookings = bookings;
    }

    // Helper methods
    public void addBooking(BookingEntity booking) {
        bookings.add(booking);
        booking.setVehicle(this);
    }

    public void removeBooking(BookingEntity booking) {
        bookings.remove(booking);
        booking.setVehicle(null);
    }

    // Enums
    public enum VehicleStatus {
        AVAILABLE("AVAILABLE"),
        BOOKED("BOOKED"),
        IN_USE("IN_USE"),
        MAINTENANCE("MAINTENANCE"),
        OUT_OF_SERVICE("OUT_OF_SERVICE");

        private final String status;

        VehicleStatus(String status) {
            this.status = status;
        }

        public String getValue() {
            return status;
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
    }

    @Override
    public String toString() {
        return "VehicleEntity{" +
                "id=" + getId() +
                ", plateNumber='" + plateNumber + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", manufactureYear=" + manufactureYear +
                ", type=" + type +
                ", status=" + status +
                ", capacity=" + capacity +
                '}';
    }
}