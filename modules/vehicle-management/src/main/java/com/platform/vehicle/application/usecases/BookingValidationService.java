package com.platform.vehicle.application.usecases;

import com.platform.vehicle.application.dto.CreateBookingRequest;
import com.platform.vehicle.domain.Booking;
import com.platform.vehicle.domain.Vehicle;
import com.platform.vehicle.domain.Driver;
import com.platform.vehicle.infrastructure.VehicleManagementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingValidationService {
    
    private final VehicleManagementRepository vehicleManagementRepository;

    public BookingValidationService(VehicleManagementRepository vehicleManagementRepository) {
        this.vehicleManagementRepository = vehicleManagementRepository;
    }

    /**
     * Validate a booking request against business rules.
     * 
     * @param request The booking request to validate
     * @param vehicle The vehicle for the booking
     * @param driver The driver for the booking (nullable)
     * @throws CreateBookingUseCase.BookingValidationException if validation fails
     */
    public void validateBookingRequest(CreateBookingRequest request, Vehicle vehicle, Driver driver) 
            throws CreateBookingUseCase.BookingValidationException {
        
        // 1. Validate vehicle availability
        validateVehicleAvailability(vehicle, request.getStartTime(), request.getEndTime());
        
        // 2. Validate driver availability if driver is specified
        if (driver != null) {
            validateDriverAvailability(driver, request.getStartTime(), request.getEndTime());
            validateDriverCanDriveVehicle(driver, vehicle);
        }
        
        // 3. Validate time constraints
        validateTimeConstraints(request);
        
        // 4. Validate business rules
        validateBusinessRules(request, vehicle);
    }

    private void validateVehicleAvailability(Vehicle vehicle, LocalDateTime startTime, LocalDateTime endTime) 
            throws CreateBookingUseCase.BookingValidationException {
        
        if (!vehicle.canBeBooked()) {
            throw new CreateBookingUseCase.BookingValidationException(
                "Vehicle is not available for booking. Status: " + vehicle.getStatus());
        }
        
        if (vehicle.needsMaintenance()) {
            throw new CreateBookingUseCase.BookingValidationException(
                "Vehicle requires maintenance before booking");
        }
        
        // Check for overlapping bookings
        List<Booking> conflictingBookings = vehicleManagementRepository.findConflictingBookings(
            vehicle.getId(), startTime, endTime);
        
        if (!conflictingBookings.isEmpty()) {
            throw new CreateBookingUseCase.BookingValidationException(
                "Vehicle is already booked for the requested time period");
        }
    }

    private void validateDriverAvailability(Driver driver, LocalDateTime startTime, LocalDateTime endTime) 
            throws CreateBookingUseCase.BookingValidationException {
        
        if (!driver.isAvailable()) {
            throw new CreateBookingUseCase.BookingValidationException(
                "Driver is not available. Status: " + driver.getStatus());
        }
        
        if (!driver.isLicenseValid()) {
            throw new CreateBookingUseCase.BookingValidationException(
                "Driver's license has expired or is invalid");
        }
        
        if (driver.needsLicenseRenewal()) {
            throw new CreateBookingUseCase.BookingValidationException(
                "Driver's license needs renewal within 30 days");
        }
        
        if (!driver.isHealthCheckValid()) {
            throw new CreateBookingUseCase.BookingValidationException(
                "Driver requires health check before booking");
        }
        
        // Check for overlapping driver bookings
        List<Booking> conflictingBookings = vehicleManagementRepository.findConflictingBookingsForDriver(
            driver.getId(), startTime, endTime);
        
        if (!conflictingBookings.isEmpty()) {
            throw new CreateBookingUseCase.BookingValidationException(
                "Driver is already assigned to another booking during this time");
        }
    }

    private void validateDriverCanDriveVehicle(Driver driver, Vehicle vehicle) 
            throws CreateBookingUseCase.BookingValidationException {
        
        if (!driver.canDriveVehicle(vehicle.getType())) {
            throw new CreateBookingUseCase.BookingValidationException(
                "Driver does not have the required license type for this vehicle. " +
                "Required: " + vehicle.getType() + ", Driver has: " + driver.getLicenseType());
        }
    }

    private void validateTimeConstraints(CreateBookingRequest request) 
            throws CreateBookingUseCase.BookingValidationException {
        
        if (request.getStartTime().isBefore(LocalDateTime.now())) {
            throw new CreateBookingUseCase.BookingValidationException(
                "Start time cannot be in the past");
        }
        
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new CreateBookingUseCase.BookingValidationException(
                "End time must be after start time");
        }
        
        // Validate minimum and maximum booking duration
        long durationHours = request.getStartTime().until(request.getEndTime(), java.time.temporal.ChronoUnit.HOURS);
        
        if (durationHours < 1) {
            throw new CreateBookingUseCase.BookingValidationException(
                "Minimum booking duration is 1 hour");
        }
        
        if (durationHours > 168) { // 7 days
            throw new CreateBookingUseCase.BookingValidationException(
                "Maximum booking duration is 7 days");
        }
        
        // Validate advance booking limits
        long advanceDays = LocalDateTime.now().until(request.getStartTime(), java.time.temporal.ChronoUnit.DAYS);
        
        if (advanceDays > 90) { // 3 months advance
            throw new CreateBookingUseCase.BookingValidationException(
                "Bookings cannot be made more than 3 months in advance");
        }
    }

    private void validateBusinessRules(CreateBookingRequest request, Vehicle vehicle) 
            throws CreateBookingUseCase.BookingValidationException {
        
        // Validate purpose is provided
        if (request.getPurpose() == null || request.getPurpose().trim().isEmpty()) {
            throw new CreateBookingUseCase.BookingValidationException(
                "Purpose of travel is required");
        }
        
        // Validate pickup and destination locations
        if (request.getPickupLocation() == null || request.getPickupLocation().trim().isEmpty()) {
            throw new CreateBookingUseCase.BookingValidationException(
                "Pickup location is required");
        }
        
        if (request.getDestination() == null || request.getDestination().trim().isEmpty()) {
            throw new CreateBookingUseCase.BookingValidationException(
                "Destination is required");
        }
        
        // Validate passenger capacity
        if (request.getEstimatedPassengers() <= 0 || request.getEstimatedPassengers() > vehicle.getCapacity()) {
            throw new CreateBookingUseCase.BookingValidationException(
                "Estimated passengers must be between 1 and " + vehicle.getCapacity());
        }
        
        // Validate cost center if provided
        if (request.getCostCenter() != null && request.getCostCenter().trim().length() > 50) {
            throw new CreateBookingUseCase.BookingValidationException(
                "Cost center cannot exceed 50 characters");
        }
    }

    /**
     * Check if a vehicle and driver are available for a specific time period.
     * 
     * @param vehicleId The vehicle ID
     * @param driverId The driver ID (nullable)
     * @param startTime The start time
     * @param endTime The end time
     * @return true if both are available, false otherwise
     */
    public boolean isAvailable(Long vehicleId, Long driverId, LocalDateTime startTime, LocalDateTime endTime) {
        try {
            Vehicle vehicle = vehicleManagementRepository.findVehicleById(vehicleId).orElse(null);
            if (vehicle == null || !vehicle.canBeBooked()) {
                return false;
            }
            
            // Check for vehicle conflicts
            List<Booking> conflictingVehicleBookings = vehicleManagementRepository.findConflictingBookings(
                vehicleId, startTime, endTime);
            
            if (!conflictingVehicleBookings.isEmpty()) {
                return false;
            }
            
            // Check for driver conflicts if driver is specified
            if (driverId != null) {
                Driver driver = vehicleManagementRepository.findDriverById(driverId).orElse(null);
                if (driver == null || !driver.isAvailable() || !driver.isLicenseValid()) {
                    return false;
                }
                
                List<Booking> conflictingDriverBookings = vehicleManagementRepository.findConflictingBookingsForDriver(
                    driverId, startTime, endTime);
                
                if (!conflictingDriverBookings.isEmpty()) {
                    return false;
                }
            }
            
            return true;
            
        } catch (Exception e) {
            // Log error and return false
            return false;
        }
    }
}