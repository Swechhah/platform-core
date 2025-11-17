package com.platform.vehicle.infrastructure.repositories;

import com.platform.vehicle.infrastructure.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingJpaRepository extends JpaRepository<BookingEntity, Long> {
    
    Optional<BookingEntity> findByBookingReference(String bookingReference);
    
    List<BookingEntity> findByRequesterId(Long requesterId);
    
    List<BookingEntity> findByVehicleId(Long vehicleId);
    
    List<BookingEntity> findByDriverId(Long driverId);
    
    List<BookingEntity> findByStatus(BookingEntity.BookingStatus status);
    
    List<BookingEntity> findByRequesterIdOrderByCreatedAtDesc(Long requesterId);
    
    @Query("SELECT b FROM BookingEntity b WHERE b.startTime >= :startDate AND b.endTime <= :endDate ORDER BY b.startTime")
    List<BookingEntity> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Current and upcoming bookings
    @Query("SELECT b FROM BookingEntity b WHERE b.status = 'ACTIVE'")
    List<BookingEntity> findCurrentBookings();
    
    @Query("SELECT b FROM BookingEntity b WHERE b.startTime > :currentTime AND b.status IN ('APPROVED', 'CONFIRMED') ORDER BY b.startTime")
    List<BookingEntity> findUpcomingBookings(@Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT b FROM BookingEntity b WHERE b.status = 'PENDING' ORDER BY b.createdAt")
    List<BookingEntity> findPendingBookings();
    
    @Query("SELECT b FROM BookingEntity b WHERE b.status = 'PENDING' AND b.managerName = :managerName ORDER BY b.createdAt")
    List<BookingEntity> findBookingsRequiringApproval(@Param("managerName") String managerName);
    
    // Conflict detection
    @Query("SELECT b FROM BookingEntity b WHERE b.vehicle.id = :vehicleId AND b.status IN ('APPROVED', 'CONFIRMED', 'ACTIVE') " +
           "AND ((b.startTime < :endTime AND b.endTime > :startTime))")
    List<BookingEntity> findConflictingBookings(@Param("vehicleId") Long vehicleId, 
                                         @Param("startTime") LocalDateTime startTime, 
                                         @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT b FROM BookingEntity b WHERE b.driver.id = :driverId AND b.status IN ('APPROVED', 'CONFIRMED', 'ACTIVE') " +
           "AND ((b.startTime < :endTime AND b.endTime > :startTime))")
    List<BookingEntity> findConflictingBookingsForDriver(@Param("driverId") Long driverId, 
                                                  @Param("startTime") LocalDateTime startTime, 
                                                  @Param("endTime") LocalDateTime endTime);
    
    // Analytics queries
    @Query("SELECT COUNT(b) FROM BookingEntity b WHERE b.vehicle.id = :vehicleId AND b.status = 'COMPLETED' " +
           "AND b.actualStartTime >= :startDate AND b.actualStartTime <= :endDate")
    long countCompletedBookingsForVehicle(@Param("vehicleId") Long vehicleId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT AVG(b.actualMileage) FROM BookingEntity b WHERE b.vehicle.id = :vehicleId AND b.status = 'COMPLETED' " +
           "AND b.actualStartTime >= :startDate AND b.actualStartTime <= :endDate AND b.actualMileage > 0")
    Double getAverageMileageForVehicle(@Param("vehicleId") Long vehicleId,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
}