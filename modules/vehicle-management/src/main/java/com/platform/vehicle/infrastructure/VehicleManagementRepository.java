package com.platform.vehicle.infrastructure;

import com.platform.vehicle.domain.Vehicle;
import com.platform.vehicle.domain.Driver;
import com.platform.vehicle.domain.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleManagementRepository {
    
    // Vehicle Repository Methods
    Optional<Vehicle> findVehicleById(Long id);
    Optional<Vehicle> findVehicleByPlateNumber(String plateNumber);
    List<Vehicle> findVehiclesByStatus(Vehicle.VehicleStatus status);
    List<Vehicle> findAvailableVehicles();
    List<Vehicle> findVehiclesByType(Vehicle.VehicleType type);
    List<Vehicle> findVehiclesByLocation(String location);
    Vehicle saveVehicle(Vehicle vehicle);
    void deleteVehicle(Long id);
    
    // Driver Repository Methods
    Optional<Driver> findDriverById(Long id);
    Optional<Driver> findDriverByLicenseNumber(String licenseNumber);
    List<Driver> findDriversByStatus(Driver.DriverStatus status);
    List<Driver> findAvailableDrivers();
    List<Driver> findDriversByLicenseType(Driver.LicenseType licenseType);
    List<Driver> findDriversByUserId(Long userId);
    Driver saveDriver(Driver driver);
    void deleteDriver(Long id);
    
    // Booking Repository Methods
    Optional<Booking> findBookingById(Long id);
    Optional<Booking> findBookingByReference(String reference);
    List<Booking> findBookingsByRequesterId(Long requesterId);
    List<Booking> findBookingsByVehicleId(Long vehicleId);
    List<Booking> findBookingsByDriverId(Long driverId);
    List<Booking> findBookingsByStatus(Booking.BookingStatus status);
    List<Booking> findBookingsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    // Current/upcoming bookings
    List<Booking> findCurrentBookings();
    List<Booking> findUpcomingBookings();
    List<Booking> findPendingBookings();
    List<Booking> findBookingsRequiringApproval(String managerName);
    
    // Conflict detection
    @Query("SELECT b FROM Booking b WHERE b.vehicleId = :vehicleId AND b.status IN ('APPROVED', 'CONFIRMED', 'ACTIVE') " +
           "AND ((b.startTime < :endTime AND b.endTime > :startTime))")
    List<Booking> findConflictingBookings(@Param("vehicleId") Long vehicleId, 
                                         @Param("startTime") LocalDateTime startTime, 
                                         @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT b FROM Booking b WHERE b.driverId = :driverId AND b.status IN ('APPROVED', 'CONFIRMED', 'ACTIVE') " +
           "AND ((b.startTime < :endTime AND b.endTime > :startTime))")
    List<Booking> findConflictingBookingsForDriver(@Param("driverId") Long driverId, 
                                                  @Param("startTime") LocalDateTime startTime, 
                                                  @Param("endTime") LocalDateTime endTime);
    
    // Analytics and reporting
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.vehicleId = :vehicleId AND b.status = 'COMPLETED' " +
           "AND b.actualStartTime >= :startDate AND b.actualStartTime <= :endDate")
    long countCompletedBookingsForVehicle(@Param("vehicleId") Long vehicleId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT AVG(b.actualMileage) FROM Booking b WHERE b.vehicleId = :vehicleId AND b.status = 'COMPLETED' " +
           "AND b.actualStartTime >= :startDate AND b.actualStartTime <= :endDate AND b.actualMileage > 0")
    Double getAverageMileageForVehicle(@Param("vehicleId") Long vehicleId,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT b FROM Booking b WHERE b.requesterId = :requesterId ORDER BY b.createdAt DESC")
    List<Booking> findBookingsByRequesterIdOrderByCreatedAtDesc(@Param("requesterId") Long requesterId);
    
    // Save operations
    Booking saveBooking(Booking booking);
    void deleteBooking(Long id);
    
    // Complex queries for dashboard
    @Query("SELECT v FROM Vehicle v WHERE v.status = 'AVAILABLE' AND v.availableForBooking = true")
    List<Vehicle> findAllAvailableVehiclesForBooking();
    
    @Query("SELECT d FROM Driver d WHERE d.status = 'AVAILABLE' AND d.availableForBooking = true")
    List<Driver> findAllAvailableDriversForBooking();
    
    // Vehicle utilization
    @Query("SELECT v FROM Vehicle v WHERE v.id NOT IN " +
           "(SELECT b.vehicleId FROM Booking b WHERE b.status IN ('APPROVED', 'CONFIRMED', 'ACTIVE') " +
           "AND b.startTime <= :currentTime AND b.endTime >= :currentTime)")
    List<Vehicle> findVehiclesCurrentlyAvailable(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT d FROM Driver d WHERE d.id NOT IN " +
           "(SELECT b.driverId FROM Booking b WHERE b.status IN ('APPROVED', 'CONFIRMED', 'ACTIVE') " +
           "AND b.startTime <= :currentTime AND b.endTime >= :currentTime AND b.driverId IS NOT NULL)")
    List<Driver> findDriversCurrentlyAvailable(@Param("currentTime") LocalDateTime currentTime);
    
    // Maintenance and expiry checks
    @Query("SELECT v FROM Vehicle v WHERE v.nextMaintenance IS NOT NULL AND v.nextMaintenance < :date")
    List<Vehicle> findVehiclesRequiringMaintenance(@Param("date") LocalDateTime date);
    
    @Query("SELECT d FROM Driver d WHERE d.licenseExpiryDate IS NOT NULL AND d.licenseExpiryDate < :date")
    List<Driver> findDriversRequiringLicenseRenewal(@Param("date") LocalDateTime date);
}