package com.platform.vehicle.infrastructure.adapters;

import com.platform.vehicle.domain.Vehicle;
import com.platform.vehicle.domain.Driver;
import com.platform.vehicle.domain.Booking;
import com.platform.vehicle.infrastructure.entities.VehicleEntity;
import com.platform.vehicle.infrastructure.entities.DriverEntity;
import com.platform.vehicle.infrastructure.entities.BookingEntity;
import com.platform.vehicle.infrastructure.entities.BookingEventEntity;
import com.platform.common.domain.core.DomainEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityMapper {

    // Vehicle mappings
    public VehicleEntity toVehicleEntity(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        
        VehicleEntity entity = new VehicleEntity();
        entity.setId(vehicle.getId());
        entity.setCreatedAt(vehicle.getCreatedAt());
        entity.setUpdatedAt(vehicle.getUpdatedAt());
        entity.setCreatedBy(vehicle.getCreatedBy());
        entity.setUpdatedBy(vehicle.getUpdatedBy());
        entity.setDeleted(vehicle.isDeleted());
        
        entity.setPlateNumber(vehicle.getPlateNumber());
        entity.setMake(vehicle.getMake());
        entity.setModel(vehicle.getModel());
        entity.setManufactureYear(vehicle.getYear());
        entity.setType(VehicleEntity.VehicleType.valueOf(vehicle.getType().name()));
        entity.setStatus(VehicleEntity.VehicleStatus.valueOf(vehicle.getStatus().name()));
        entity.setCapacity(vehicle.getCapacity());
        entity.setColor(vehicle.getColor());
        entity.setDescription(vehicle.getDescription());
        entity.setLocation(vehicle.getLocation());
        entity.setLastMaintenance(vehicle.getLastMaintenance());
        entity.setNextMaintenance(vehicle.getNextMaintenance());
        entity.setAvailableForBooking(vehicle.isAvailableForBooking());
        entity.setMileage(vehicle.getMileage());
        entity.setFuelType(vehicle.getFuelType());
        entity.setVehicleGroup(vehicle.getVehicleGroup());
        entity.setCostCenter(vehicle.getCostCenter());
        
        return entity;
    }

    public Vehicle toDomainVehicle(VehicleEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Vehicle vehicle = new Vehicle();
        vehicle.setId(entity.getId());
        vehicle.setCreatedAt(entity.getCreatedAt());
        vehicle.setUpdatedAt(entity.getUpdatedAt());
        vehicle.setCreatedBy(entity.getCreatedBy());
        vehicle.setUpdatedBy(entity.getUpdatedBy());
        vehicle.setDeleted(entity.isDeleted());
        
        vehicle.setPlateNumber(entity.getPlateNumber());
        vehicle.setMake(entity.getMake());
        vehicle.setModel(entity.getModel());
        vehicle.setYear(entity.getManufactureYear());
        vehicle.setType(Vehicle.VehicleType.valueOf(entity.getType().name()));
        vehicle.setStatus(Vehicle.VehicleStatus.valueOf(entity.getStatus().name()));
        vehicle.setCapacity(entity.getCapacity());
        vehicle.setColor(entity.getColor());
        vehicle.setDescription(entity.getDescription());
        vehicle.setLocation(entity.getLocation());
        vehicle.setLastMaintenance(entity.getLastMaintenance());
        vehicle.setNextMaintenance(entity.getNextMaintenance());
        vehicle.setAvailableForBooking(entity.isAvailableForBooking());
        vehicle.setMileage(entity.getMileage());
        vehicle.setFuelType(entity.getFuelType());
        vehicle.setVehicleGroup(entity.getVehicleGroup());
        vehicle.setCostCenter(entity.getCostCenter());
        
        return vehicle;
    }

    // Driver mappings
    public DriverEntity toDriverEntity(Driver driver) {
        if (driver == null) {
            return null;
        }
        
        DriverEntity entity = new DriverEntity();
        entity.setId(driver.getId());
        entity.setCreatedAt(driver.getCreatedAt());
        entity.setUpdatedAt(driver.getUpdatedAt());
        entity.setCreatedBy(driver.getCreatedBy());
        entity.setUpdatedBy(driver.getUpdatedBy());
        entity.setDeleted(driver.isDeleted());
        
        entity.setUserId(driver.getUserId());
        entity.setStatus(DriverEntity.DriverStatus.valueOf(driver.getStatus().name()));
        entity.setLicenseType(DriverEntity.LicenseType.valueOf(driver.getLicenseType().name()));
        entity.setLicenseNumber(driver.getLicenseNumber());
        entity.setLicenseExpiryDate(driver.getLicenseExpiryDate());
        entity.setPhoneNumber(driver.getPhoneNumber());
        entity.setEmergencyContact(driver.getEmergencyContact());
        entity.setEmergencyPhone(driver.getEmergencyPhone());
        entity.setAddress(driver.getAddress());
        entity.setHireDate(driver.getHireDate());
        entity.setYearsOfExperience(driver.getYearsOfExperience());
        entity.setCertifications(driver.getCertifications());
        entity.setAvailableForBooking(driver.isAvailableForBooking());
        entity.setDepartment(driver.getDepartment());
        entity.setCostCenter(driver.getCostCenter());
        entity.setShift(driver.getShift());
        entity.setLastHealthCheck(driver.getLastHealthCheck());
        entity.setNotes(driver.getNotes());
        entity.setTotalTripsCompleted(driver.getTotalTripsCompleted());
        entity.setTotalMilesDriven(driver.getTotalMilesDriven());
        
        return entity;
    }

    public Driver toDomainDriver(DriverEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Driver driver = new Driver();
        driver.setId(entity.getId());
        driver.setCreatedAt(entity.getCreatedAt());
        driver.setUpdatedAt(entity.getUpdatedAt());
        driver.setCreatedBy(entity.getCreatedBy());
        driver.setUpdatedBy(entity.getUpdatedBy());
        driver.setDeleted(entity.isDeleted());
        
        driver.setUserId(entity.getUserId());
        driver.setStatus(Driver.DriverStatus.valueOf(entity.getStatus().name()));
        driver.setLicenseType(Driver.LicenseType.valueOf(entity.getLicenseType().name()));
        driver.setLicenseNumber(entity.getLicenseNumber());
        driver.setLicenseExpiryDate(entity.getLicenseExpiryDate());
        driver.setPhoneNumber(entity.getPhoneNumber());
        driver.setEmergencyContact(entity.getEmergencyContact());
        driver.setEmergencyPhone(entity.getEmergencyPhone());
        driver.setAddress(entity.getAddress());
        driver.setHireDate(entity.getHireDate());
        driver.setYearsOfExperience(entity.getYearsOfExperience());
        driver.setCertifications(entity.getCertifications());
        driver.setAvailableForBooking(entity.isAvailableForBooking());
        driver.setDepartment(entity.getDepartment());
        driver.setCostCenter(entity.getCostCenter());
        driver.setShift(entity.getShift());
        driver.setLastHealthCheck(entity.getLastHealthCheck());
        driver.setNotes(entity.getNotes());
        driver.setTotalTripsCompleted(entity.getTotalTripsCompleted());
        driver.setTotalMilesDriven(entity.getTotalMilesDriven());
        
        return driver;
    }

    // Booking mappings
    public BookingEntity toBookingEntity(Booking booking) {
        if (booking == null) {
            return null;
        }
        
        BookingEntity entity = new BookingEntity();
        entity.setId(booking.getId());
        entity.setCreatedAt(booking.getCreatedAt());
        entity.setUpdatedAt(booking.getUpdatedAt());
        entity.setCreatedBy(booking.getCreatedBy());
        entity.setUpdatedBy(booking.getUpdatedBy());
        entity.setDeleted(booking.isDeleted());
        
        entity.setBookingReference(booking.getBookingReference());
        entity.setStatus(BookingEntity.BookingStatus.valueOf(booking.getStatus().name()));
        entity.setType(BookingEntity.BookingType.valueOf(booking.getType().name()));
        entity.setVehicleId(booking.getVehicleId());
        entity.setDriverId(booking.getDriverId());
        entity.setRequesterId(booking.getRequesterId());
        entity.setApproverId(booking.getApproverId());
        entity.setStartTime(booking.getStartTime());
        entity.setEndTime(booking.getEndTime());
        entity.setActualStartTime(booking.getActualStartTime());
        entity.setActualEndTime(booking.getActualEndTime());
        entity.setPickupLocation(booking.getPickupLocation());
        entity.setDestination(booking.getDestination());
        entity.setReturnLocation(booking.getReturnLocation());
        entity.setPurpose(booking.getPurpose());
        entity.setDescription(booking.getDescription());
        entity.setEstimatedPassengers(booking.getEstimatedPassengers());
        entity.setManagerName(booking.getManagerName());
        entity.setCostCenter(booking.getCostCenter());
        entity.setApprovalComment(booking.getApprovalComment());
        entity.setApprovedAt(booking.getApprovedAt());
        entity.setRejectedAt(booking.getRejectedAt());
        entity.setRejectionReason(booking.getRejectionReason());
        entity.setInternalNotes(booking.getInternalNotes());
        entity.setFeedback(booking.getFeedback());
        entity.setActualMileage(booking.getActualMileage());
        entity.setAdditionalRequirements(booking.getAdditionalRequirements());
        entity.setApprovalLevel(booking.getApprovalLevel());
        entity.setRecurring(booking.isRecurring());
        entity.setRecurringPattern(booking.getRecurringPattern());
        entity.setEstimatedCost(booking.getEstimatedCost());
        
        // Map events if available
        if (booking.getEventHistory() != null && !booking.getEventHistory().isEmpty()) {
            List<BookingEventEntity> events = booking.getEventHistory().stream()
                    .map(this::toBookingEventEntity)
                    .collect(Collectors.toList());
            events.forEach(entity::addEvent);
        }
        
        return entity;
    }

    public Booking toDomainBooking(BookingEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Booking booking = new Booking();
        booking.setId(entity.getId());
        booking.setCreatedAt(entity.getCreatedAt());
        booking.setUpdatedAt(entity.getUpdatedAt());
        booking.setCreatedBy(entity.getCreatedBy());
        booking.setUpdatedBy(entity.getUpdatedBy());
        booking.setDeleted(entity.isDeleted());
        
        booking.setBookingReference(entity.getBookingReference());
        booking.setStatus(Booking.BookingStatus.valueOf(entity.getStatus().name()));
        booking.setType(Booking.BookingType.valueOf(entity.getType().name()));
        booking.setVehicleId(entity.getVehicleId());
        booking.setDriverId(entity.getDriverId());
        booking.setRequesterId(entity.getRequesterId());
        booking.setApproverId(entity.getApproverId());
        booking.setStartTime(entity.getStartTime());
        booking.setEndTime(entity.getEndTime());
        booking.setActualStartTime(entity.getActualStartTime());
        booking.setActualEndTime(entity.getActualEndTime());
        booking.setPickupLocation(entity.getPickupLocation());
        booking.setDestination(entity.getDestination());
        booking.setReturnLocation(entity.getReturnLocation());
        booking.setPurpose(entity.getPurpose());
        booking.setDescription(entity.getDescription());
        booking.setEstimatedPassengers(entity.getEstimatedPassengers());
        booking.setManagerName(entity.getManagerName());
        booking.setCostCenter(entity.getCostCenter());
        booking.setApprovalComment(entity.getApprovalComment());
        booking.setApprovedAt(entity.getApprovedAt());
        booking.setRejectedAt(entity.getRejectedAt());
        booking.setRejectionReason(entity.getRejectionReason());
        booking.setInternalNotes(entity.getInternalNotes());
        booking.setFeedback(entity.getFeedback());
        booking.setActualMileage(entity.getActualMileage());
        booking.setAdditionalRequirements(entity.getAdditionalRequirements());
        booking.setApprovalLevel(entity.getApprovalLevel());
        booking.setRecurring(entity.isRecurring());
        booking.setRecurringPattern(entity.getRecurringPattern());
        booking.setEstimatedCost(entity.getEstimatedCost());
        
        // Map events if available
        if (entity.getEvents() != null && !entity.getEvents().isEmpty()) {
            List<DomainEvent> events = entity.getEvents().stream()
                    .map(this::toDomainEvent)
                    .collect(Collectors.toList());
            booking.setEventHistory(events);
        }
        
        return booking;
    }

    // Event mappings
    public BookingEventEntity toBookingEventEntity(DomainEvent domainEvent) {
        if (domainEvent == null) {
            return null;
        }
        
        BookingEventEntity entity = new BookingEventEntity();
        entity.setEventId(domainEvent.getEventId());
        entity.setEventType(domainEvent.getEventType());
        entity.setEventData(domainEvent.getEventData());
        entity.setCausedBy(domainEvent.getCausedBy());
        entity.setVersion(domainEvent.getVersion());
        entity.setTimestamp(domainEvent.getTimestamp());
        entity.setBookingId(Long.valueOf(domainEvent.getAggregateId()));
        
        return entity;
    }

    public DomainEvent toDomainEvent(BookingEventEntity entity) {
        if (entity == null) {
            return null;
        }
        
        // Create a simple domain event for now
        return new DomainEvent(
            entity.getBookingId().toString(),
            entity.getEventType(),
            entity.getEventData(),
            entity.getCausedBy()
        ) {
            @Override
            public void setEventId(String eventId) {
                // Override to set from entity
            }
            
            @Override
            public void setTimestamp(java.time.LocalDateTime timestamp) {
                // Override to set from entity
            }
        };
    }
}