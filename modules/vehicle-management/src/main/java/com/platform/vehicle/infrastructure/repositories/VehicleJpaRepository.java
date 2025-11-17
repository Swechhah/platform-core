package com.platform.vehicle.infrastructure.repositories;

import com.platform.vehicle.infrastructure.entities.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleJpaRepository extends JpaRepository<VehicleEntity, Long> {
    
    Optional<VehicleEntity> findByPlateNumber(String plateNumber);
    
    List<VehicleEntity> findByStatus(VehicleEntity.VehicleStatus status);
    
    List<VehicleEntity> findByType(VehicleEntity.VehicleType type);
    
    List<VehicleEntity> findByLocation(String location);
    
    @Query("SELECT v FROM VehicleEntity v WHERE v.status = 'AVAILABLE' AND v.availableForBooking = true")
    List<VehicleEntity> findAvailableVehicles();
    
    @Query("SELECT v FROM VehicleEntity v WHERE v.nextMaintenance IS NOT NULL AND v.nextMaintenance < :date")
    List<VehicleEntity> findVehiclesRequiringMaintenance(@Param("date") LocalDateTime date);
    
    @Query("SELECT v FROM VehicleEntity v WHERE v.id NOT IN " +
           "(SELECT b.vehicle.id FROM BookingEntity b WHERE b.status IN ('APPROVED', 'CONFIRMED', 'ACTIVE') " +
           "AND b.startTime <= :currentTime AND b.endTime >= :currentTime)")
    List<VehicleEntity> findVehiclesCurrentlyAvailable(@Param("currentTime") LocalDateTime currentTime);
}