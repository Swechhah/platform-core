package com.platform.vehicle.infrastructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drivers")
@EntityListeners(AuditingEntityListener.class)
public class DriverEntity {
    
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull
    private DriverStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "license_type", nullable = false)
    @NotNull
    private LicenseType licenseType;

    @Column(name = "user_id")
    private Long userId; // References User entity

    @Column(name = "license_number", nullable = false, unique = true)
    @NotBlank
    @Size(max = 50)
    private String licenseNumber;

    @Column(name = "license_expiry_date")
    private LocalDate licenseExpiryDate;

    @Column(name = "phone_number")
    @Size(max = 20)
    private String phoneNumber;

    @Column(name = "emergency_contact")
    @Size(max = 100)
    private String emergencyContact;

    @Column(name = "emergency_phone")
    @Size(max = 20)
    private String emergencyPhone;

    @Column(name = "address")
    private String address;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "years_experience", nullable = false)
    private int yearsOfExperience = 0;

    @Column(name = "certifications")
    private String certifications;

    @Column(name = "available_for_booking", nullable = false)
    private boolean availableForBooking = true;

    @Column(name = "department")
    @Size(max = 100)
    private String department;

    @Column(name = "cost_center")
    @Size(max = 50)
    private String costCenter;

    @Column(name = "shift")
    @Size(max = 20)
    private String shift;

    @Column(name = "last_health_check")
    private LocalDate lastHealthCheck;

    @Column(name = "notes")
    private String notes;

    @Column(name = "total_trips_completed", nullable = false)
    private int totalTripsCompleted = 0;

    @Column(name = "total_miles_driven", nullable = false)
    private double totalMilesDriven = 0.0;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookingEntity> bookings = new ArrayList<>();

    // Constructors
    public DriverEntity() {
        this.status = DriverStatus.AVAILABLE;
        this.availableForBooking = true;
    }

    public DriverEntity(Long userId, LicenseType licenseType, String licenseNumber) {
        this();
        this.userId = userId;
        this.licenseType = licenseType;
        this.licenseNumber = licenseNumber;
    }

    // Helper methods
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

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public List<BookingEntity> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingEntity> bookings) {
        this.bookings = bookings;
    }

    // Helper methods for relationships
    public void addBooking(BookingEntity booking) {
        bookings.add(booking);
        booking.setDriver(this);
    }

    public void removeBooking(BookingEntity booking) {
        bookings.remove(booking);
        booking.setDriver(null);
    }

    // Enums
    public enum DriverStatus {
        AVAILABLE("AVAILABLE"),
        ASSIGNED("ASSIGNED"),
        ON_DUTY("ON_DUTY"),
        UNAVAILABLE("UNAVAILABLE"),
        ON_LEAVE("ON_LEAVE"),
        SICK("SICK");

        private final String status;

        DriverStatus(String status) {
            this.status = status;
        }

        public String getValue() {
            return status;
        }
    }

    public enum LicenseType {
        CLASS_1("CLASS_1"),
        CLASS_2("CLASS_2"),
        CLASS_3("CLASS_3"),
        MOTORCYCLE("MOTORCYCLE"),
        COMMERCIAL("COMMERCIAL");

        private final String type;

        LicenseType(String type) {
            this.type = type;
        }

        public String getValue() {
            return type;
        }
    }

    @Override
    public String toString() {
        return "DriverEntity{" +
                "id=" + id +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", status=" + status +
                ", licenseType=" + licenseType +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}