package com.platform.vehicle.infrastructure.repositories;

import com.platform.vehicle.infrastructure.entities.DriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverJpaRepository extends JpaRepository<DriverEntity, Long> {
    
    Optional<DriverEntity> findByLicenseNumber(String licenseNumber);
    
    List<DriverEntity> findByUserId(Long userId);
    
    List<DriverEntity> findByStatus(DriverEntity.DriverStatus status);
    
    List<DriverEntity> findByLicenseType(DriverEntity.LicenseType licenseType);
    
    @Query("SELECT d FROM DriverEntity d WHERE d.status = 'AVAILABLE' AND d.availableForBooking = true")
    List<DriverEntity> findAvailableDrivers();
    
    @Query("SELECT d FROM DriverEntity d WHERE d.licenseExpiryDate IS NOT NULL AND d.licenseExpiryDate < :date")
    List<DriverEntity> findDriversRequiringLicenseRenewal(@Param("date") LocalDateTime date);
    
    @Query("SELECT d FROM DriverEntity d WHERE d.id NOT IN " +
           "(SELECT b.driver.id FROM BookingEntity b WHERE b.status IN ('APPROVED', 'CONFIRMED', 'ACTIVE') " +
           "AND b.startTime <= :currentTime AND b.endTime >= :currentTime AND b.driver.id IS NOT NULL)")
    List<DriverEntity> findDriversCurrentlyAvailable(@Param("currentTime") LocalDateTime currentTime);
}