package com.platform.vehicle.application.usecases;

import com.platform.vehicle.application.dto.CreateBookingRequest;
import com.platform.vehicle.application.dto.BookingResponse;
import com.platform.vehicle.domain.Booking;
import com.platform.vehicle.domain.Vehicle;
import com.platform.vehicle.domain.Driver;
import com.platform.vehicle.infrastructure.VehicleManagementRepository;
import com.platform.vehicle.domain.events.BookingEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateBookingUseCase {
    
    private final VehicleManagementRepository vehicleManagementRepository;
    private final BookingValidationService bookingValidationService;
    private final NotificationService notificationService;
    private final EventPublisher eventPublisher;

    public CreateBookingUseCase(
            VehicleManagementRepository vehicleManagementRepository,
            BookingValidationService bookingValidationService,
            NotificationService notificationService,
            EventPublisher eventPublisher) {
        this.vehicleManagementRepository = vehicleManagementRepository;
        this.bookingValidationService = bookingValidationService;
        this.notificationService = notificationService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Create a new booking request.
     * 
     * @param request The booking creation request
     * @param requesterId The ID of the user making the request
     * @return The created booking response
     * @throws BookingValidationException if validation fails
     */
    @Transactional
    public BookingResponse execute(CreateBookingRequest request, Long requesterId) throws BookingValidationException {
        // 1. Validate vehicle exists and is available
        Vehicle vehicle = vehicleManagementRepository.findVehicleById(request.getVehicleId())
                .orElseThrow(() -> new BookingValidationException("Vehicle not found: " + request.getVehicleId()));
        
        // 2. Validate driver if specified
        Driver driver = null;
        if (request.getDriverId() != null) {
            driver = vehicleManagementRepository.findDriverById(request.getDriverId())
                    .orElseThrow(() -> new BookingValidationException("Driver not found: " + request.getDriverId()));
        }

        // 3. Validate booking constraints
        bookingValidationService.validateBookingRequest(request, vehicle, driver);

        // 4. Create the booking
        Booking booking = new Booking();
        booking.setVehicleId(request.getVehicleId());
        booking.setDriverId(request.getDriverId());
        booking.setRequesterId(requesterId);
        booking.setPurpose(request.getPurpose());
        booking.setDescription(request.getDescription());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setPickupLocation(request.getPickupLocation());
        booking.setDestination(request.getDestination());
        booking.setReturnLocation(request.getReturnLocation());
        booking.setEstimatedPassengers(request.getEstimatedPassengers());
        booking.setManagerName(request.getManagerName());
        booking.setCostCenter(request.getCostCenter());
        booking.setAdditionalRequirements(request.getAdditionalRequirements());
        
        // Set booking type
        if (request.getBookingType() != null) {
            booking.setType(Booking.BookingType.fromString(request.getBookingType()));
        }
        
        // Mark created by requester
        booking.markCreated(String.valueOf(requesterId));

        // 5. Save the booking
        Booking savedBooking = vehicleManagementRepository.saveBooking(booking);

        // 6. Update vehicle status if needed
        if (savedBooking.getStatus() == Booking.BookingStatus.APPROVED) {
            vehicle.setBooked();
            vehicleManagementRepository.saveVehicle(vehicle);
        }

        // 7. Publish domain events
        publishBookingCreatedEvent(savedBooking);

        // 8. Send notifications
        sendBookingCreatedNotifications(savedBooking);

        // 9. Convert to response DTO
        return mapToBookingResponse(savedBooking);
    }

    private void publishBookingCreatedEvent(Booking booking) {
        BookingEvent event = new BookingEvent(
            booking.getBookingReference(),
            "BOOKING_CREATED",
            String.format("Booking created: %s for %s", booking.getPurpose(), booking.getStartTime()),
            String.valueOf(booking.getRequesterId())
        );
        eventPublisher.publishEvent(event);
    }

    private void sendBookingCreatedNotifications(Booking booking) {
        // Notify requester
        notificationService.sendBookingCreatedNotification(booking);
        
        // Notify approver
        if (booking.getManagerName() != null) {
            notificationService.sendApprovalRequestNotification(booking);
        }
        
        // Notify admin
        notificationService.sendNewBookingNotificationToAdmin(booking);
    }

    private BookingResponse mapToBookingResponse(Booking booking) {
        // This will be implemented with proper mapping
        // For now, return a basic response
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setBookingReference(booking.getBookingReference());
        response.setStatus(booking.getStatus().getValue());
        response.setPurpose(booking.getPurpose());
        response.setStartTime(booking.getStartTime());
        response.setEndTime(booking.getEndTime());
        response.setCanBeCancelled(booking.canBeCancelled());
        return response;
    }

    /**
     * Exception thrown when booking validation fails.
     */
    public static class BookingValidationException extends Exception {
        public BookingValidationException(String message) {
            super(message);
        }
        
        public BookingValidationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}