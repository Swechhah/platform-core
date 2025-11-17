package com.platform.vehicle.domain;

import com.platform.common.domain.core.BaseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

public class Driver extends BaseEntity {
    
    public enum DriverStatus {
        AVAILABLE("AVAILABLE"),
        ASSIGNED("ASSIGNED"), // Assigned to a booking but not yet in use
        ON_DUTY("ON_DUTY"), // Currently driving
        UNAVAILABLE("UNAVAILABLE"), // Not available for booking
        ON_LEAVE("ON_LEAVE"),
        SICK("SICK");

        private final String status;

        DriverStatus(String status) {
            this.status = status;
        }

        public String getValue() {
            return status;
        }

        public static DriverStatus fromString(String status) {
            return Arrays.stream(DriverStatus.values())
                    .filter(s -> s.status.equalsIgnoreCase(status))
                    .findFirst()
                    .orElse(AVAILABLE);
        }
    }

    public enum LicenseType {
        CLASS_1("CLASS_1"), // Regular car
        CLASS_2("CLASS_2"), // Small trucks
        CLASS_3("CLASS_3"), // Large trucks
        MOTORCYCLE("MOTORCYCLE"),
        COMMERCIAL("COMMERCIAL");

        private final String type;

        LicenseType(String type) {
            this.type = type;
        }

        public String getValue() {
            return type;
        }

        public static LicenseType fromString(String type) {
            return Arrays.stream(LicenseType.values())
                    .filter(t -> t.type.equalsIgnoreCase(type))
                    .findFirst()
                    .orElse(CLASS_1);
        }
    }

    // Reference to common User entity
    private Long userId; // References User entity
    private DriverStatus status;
    private LicenseType licenseType;
    private String licenseNumber;
    private LocalDate licenseExpiryDate;
    private String phoneNumber;
    private String emergencyContact;
    private String emergencyPhone;
    private String address;
    private LocalDate hireDate;
    private int yearsOfExperience;
    private String certifications; // Comma-separated list
    private boolean availableForBooking = true;
    private String department;
    private String costCenter;
    private String shift; // Morning, Afternoon, Night
    private LocalDate lastHealthCheck;
    private String notes;
    private int totalTripsCompleted;
    private double totalMilesDriven;

    // Constructors
    public Driver() {
        super();
        this.status = DriverStatus.AVAILABLE;
        this.availableForBooking = true;
    }

    public Driver(Long userId, LicenseType licenseType, String licenseNumber) {
        this();
        this.userId = userId;
        this.licenseType = licenseType;
        this.licenseNumber = licenseNumber;
    }

    // Business Methods
    public boolean isAvailable() {
        return status == DriverStatus.AVAILABLE && availableForBooking;
    }

    public boolean isOnDuty() {
        return status == DriverStatus.ON_DUTY;
    }

    public boolean isAssigned() {
        return status == DriverStatus.ASSIGNED;
    }

    public boolean canDriveVehicle(Vehicle.VehicleType vehicleType) {
        switch (vehicleType) {
            case SEDAN:
            case SUV:
                return licenseType == LicenseType.CLASS_1;
            case VAN:
            case TRUCK:
                return licenseType == LicenseType.CLASS_2 || licenseType == LicenseType.CLASS_3;
            case MOTORCYCLE:
                return licenseType == LicenseType.MOTORCYCLE;
            default:
                return licenseType == LicenseType.COMMERCIAL;
        }
    }

    public boolean isLicenseValid() {
        return licenseExpiryDate != null && LocalDate.now().isBefore(licenseExpiryDate);
    }

    public boolean needsLicenseRenewal() {
        if (licenseExpiryDate == null) return true;
        return LocalDate.now().isAfter(licenseExpiryDate.minusDays(30)); // 30 days before expiry
    }

    public boolean isHealthCheckValid() {
        if (lastHealthCheck == null) return false;
        return LocalDate.now().isBefore(lastHealthCheck.plusYears(1)); // Health check yearly
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
        markUpdated("system");
    }

    public void setAssigned() {
        this.status = DriverStatus.ASSIGNED;
        markUpdated("system");
    }

    public void setOnDuty() {
        this.status = DriverStatus.ON_DUTY;
        markUpdated("system");
    }

    public void setAvailable() {
        this.status = DriverStatus.AVAILABLE;
        this.availableForBooking = true;
        markUpdated("system");
    }

    public void setUnavailable() {
        this.status = DriverStatus.UNAVAILABLE;
        this.availableForBooking = false;
        markUpdated("system");
    }

    public void setOnLeave() {
        this.status = DriverStatus.ON_LEAVE;
        this.availableForBooking = false;
        markUpdated("system");
    }

    public void updateLicense(LocalDate newExpiryDate) {
        this.licenseExpiryDate = newExpiryDate;
        markUpdated("system");
    }

    public void completeTrip(double miles) {
        this.totalTripsCompleted++;
        this.totalMilesDriven += miles;
        markUpdated("system");
    }

    public void updateHealthCheck(LocalDate healthCheckDate) {
        this.lastHealthCheck = healthCheckDate;
        markUpdated("system");
    }

    public String getDisplayName() {
        return "Driver #" + id + " (" + (userId != null ? "User:" + userId : "External") + ")";
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public LocalDate getLicenseExpiryDate() {
        return licenseExpiryDate;
    }

    public void setLicenseExpiryDate(LocalDate licenseExpiryDate) {
        this.licenseExpiryDate = licenseExpiryDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    public boolean isAvailableForBooking() {
        return availableForBooking;
    }

    public void setAvailableForBooking(boolean availableForBooking) {
        this.availableForBooking = availableForBooking;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public LocalDate getLastHealthCheck() {
        return lastHealthCheck;
    }

    public void setLastHealthCheck(LocalDate lastHealthCheck) {
        this.lastHealthCheck = lastHealthCheck;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getTotalTripsCompleted() {
        return totalTripsCompleted;
    }

    public void setTotalTripsCompleted(int totalTripsCompleted) {
        this.totalTripsCompleted = totalTripsCompleted;
    }

    public double getTotalMilesDriven() {
        return totalMilesDriven;
    }

    public void setTotalMilesDriven(double totalMilesDriven) {
        this.totalMilesDriven = totalMilesDriven;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return Objects.equals(licenseNumber, driver.licenseNumber) && 
               Objects.equals(userId, driver.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(licenseNumber, userId);
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                "userId=" + userId +
                ", status=" + status +
                ", licenseType=" + licenseType +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}