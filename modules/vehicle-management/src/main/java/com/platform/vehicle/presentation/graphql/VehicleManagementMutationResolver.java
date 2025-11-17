package com.platform.vehicle.presentation.graphql;

import com.platform.vehicle.application.dto.CreateBookingRequest;
import com.platform.vehicle.application.dto.BookingResponse;
import com.platform.vehicle.application.usecases.CreateBookingUseCase;
import com.platform.vehicle.application.usecases.BookingValidationService;
import com.platform.vehicle.domain.Booking;
import com.platform.vehicle.domain.Vehicle;
import com.platform.vehicle.domain.Driver;
import com.platform.vehicle.infrastructure.VehicleManagementRepository;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class VehicleManagementMutationResolver {
    
    private final CreateBookingUseCase createBookingUseCase;
    private final VehicleManagementRepository vehicleManagementRepository;
    private final BookingValidationService bookingValidationService;

    public VehicleManagementMutationResolver(
            CreateBookingUseCase createBookingUseCase,
            VehicleManagementRepository vehicleManagementRepository,
            BookingValidationService bookingValidationService) {
        this.createBookingUseCase = createBookingUseCase;
        this.vehicleManagementRepository = vehicleManagementRepository;
        this.bookingValidationService = bookingValidationService;
    }

    // Booking Mutations
    public DataFetcher<CompletableFuture<BookingResponse>> createBooking() {
        return dataFetchingEnvironment -> {
            try {
                CreateBookingRequest request = dataFetchingEnvironment.getArgument("request");
                Long requesterId = Long.valueOf(dataFetchingEnvironment.getArgument("requesterId"));
                
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        return createBookingUseCase.execute(request, requesterId);
                    } catch (CreateBookingUseCase.BookingValidationException e) {
                        throw new RuntimeException("Booking validation failed: " + e.getMessage(), e);
                    }
                });
            } catch (Exception e) {
                CompletableFuture<BookingResponse> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        };
    }

    public DataFetcher<CompletableFuture<Booking>> updateBooking() {
        return dataFetchingEnvironment -> {
            try {
                Long id = Long.valueOf(dataFetchingEnvironment.getArgument("id"));
                Object request = dataFetchingEnvironment.getArgument("request");
                
                return CompletableFuture.supplyAsync(() -> {
                    // TODO: Implement update booking logic
                    // This would involve validation, updating the booking, and saving
                    throw new UnsupportedOperationException("Update booking not yet implemented");
                });
            } catch (Exception e) {
                CompletableFuture<Booking> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        };
    }

    public DataFetcher<CompletableFuture<Boolean>> cancelBooking() {
        return dataFetchingEnvironment -> {
            try {
                Long id = Long.valueOf(dataFetchingEnvironment.getArgument("id"));
                String reason = dataFetchingEnvironment.getArgument("reason");
                
                return CompletableFuture.supplyAsync(() -> {
                    Optional<Booking> bookingOpt = vehicleManagementRepository.findBookingById(id);
                    if (bookingOpt.isEmpty()) {
                        throw new RuntimeException("Booking not found: " + id);
                    }
                    
                    Booking booking = bookingOpt.get();
                    if (!booking.canBeCancelled()) {
                        throw new RuntimeException("Booking cannot be cancelled in current status: " + booking.getStatus());
                    }
                    
                    booking.cancel(reason);
                    vehicleManagementRepository.saveBooking(booking);
                    return true;
                });
            } catch (Exception e) {
                CompletableFuture<Boolean> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        };
    }

    public DataFetcher<CompletableFuture<Booking>> approveBooking() {
        return dataFetchingEnvironment -> {
            try {
                Long id = Long.valueOf(dataFetchingEnvironment.getArgument("id"));
                Long approverId = Long.valueOf(dataFetchingEnvironment.getArgument("approverId"));
                String comment = dataFetchingEnvironment.getArgument("comment");
                
                return CompletableFuture.supplyAsync(() -> {
                    Optional<Booking> bookingOpt = vehicleManagementRepository.findBookingById(id);
                    if (bookingOpt.isEmpty()) {
                        throw new RuntimeException("Booking not found: " + id);
                    }
                    
                    Booking booking = bookingOpt.get();
                    booking.approve(String.valueOf(approverId), comment);
                    Booking savedBooking = vehicleManagementRepository.saveBooking(booking);
                    
                    // Update vehicle status if needed
                    Optional<Vehicle> vehicleOpt = vehicleManagementRepository.findVehicleById(savedBooking.getVehicleId());
                    if (vehicleOpt.isPresent()) {
                        Vehicle vehicle = vehicleOpt.get();
                        vehicle.setBooked();
                        vehicleManagementRepository.saveVehicle(vehicle);
                    }
                    
                    return savedBooking;
                });
            } catch (Exception e) {
                CompletableFuture<Booking> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        };
    }

    public DataFetcher<CompletableFuture<Booking>> rejectBooking() {
        return dataFetchingEnvironment -> {
            try {
                Long id = Long.valueOf(dataFetchingEnvironment.getArgument("id"));
                Long approverId = Long.valueOf(dataFetchingEnvironment.getArgument("approverId"));
                String reason = dataFetchingEnvironment.getArgument("reason");
                
                return CompletableFuture.supplyAsync(() -> {
                    Optional<Booking> bookingOpt = vehicleManagementRepository.findBookingById(id);
                    if (bookingOpt.isEmpty()) {
                        throw new RuntimeException("Booking not found: " + id);
                    }
                    
                    Booking booking = bookingOpt.get();
                    booking.reject(String.valueOf(approverId), reason);
                    return vehicleManagementRepository.saveBooking(booking);
                });
            } catch (Exception e) {
                CompletableFuture<Booking> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        };
    }

    public DataFetcher<CompletableFuture<Booking>> activateBooking() {
        return dataFetchingEnvironment -> {
            try {
                Long id = Long.valueOf(dataFetchingEnvironment.getArgument("id"));
                
                return CompletableFuture.supplyAsync(() -> {
                    Optional<Booking> bookingOpt = vehicleManagementRepository.findBookingById(id);
                    if (bookingOpt.isEmpty()) {
                        throw new RuntimeException("Booking not found: " + id);
                    }
                    
                    Booking booking = bookingOpt.get();
                    booking.activate();
                    return vehicleManagementRepository.saveBooking(booking);
                });
            } catch (Exception e) {
                CompletableFuture<Booking> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        };
    }

    public DataFetcher<CompletableFuture<Booking>> completeBooking() {
        return dataFetchingEnvironment -> {
            try {
                Long id = Long.valueOf(dataFetchingEnvironment.getArgument("id"));
                String feedback = dataFetchingEnvironment.getArgument("feedback");
                double actualMileage = dataFetchingEnvironment.getArgument("actualMileage");
                
                return CompletableFuture.supplyAsync(() -> {
                    Optional<Booking> bookingOpt = vehicleManagementRepository.findBookingById(id);
                    if (bookingOpt.isEmpty()) {
                        throw new RuntimeException("Booking not found: " + id);
                    }
                    
                    Booking booking = bookingOpt.get();
                    booking.complete(feedback, actualMileage);
                    Booking savedBooking = vehicleManagementRepository.saveBooking(booking);
                    
                    // Reset vehicle and driver status
                    Optional<Vehicle> vehicleOpt = vehicleManagementRepository.findVehicleById(savedBooking.getVehicleId());
                    if (vehicleOpt.isPresent()) {
                        Vehicle vehicle = vehicleOpt.get();
                        vehicle.setAvailable();
                        vehicle.updateMileage(actualMileage);
                        vehicleManagementRepository.saveVehicle(vehicle);
                    }
                    
                    if (savedBooking.getDriverId() != null) {
                        Optional<Driver> driverOpt = vehicleManagementRepository.findDriverById(savedBooking.getDriverId());
                        if (driverOpt.isPresent()) {
                            Driver driver = driverOpt.get();
                            driver.setAvailable();
                            driver.completeTrip(actualMileage);
                            vehicleManagementRepository.saveDriver(driver);
                        }
                    }
                    
                    return savedBooking;
                });
            } catch (Exception e) {
                CompletableFuture<Booking> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        };
    }

    // Vehicle Mutations
    public DataFetcher<CompletableFuture<Vehicle>> createVehicle() {
        return dataFetchingEnvironment -> {
            try {
                Object request = dataFetchingEnvironment.getArgument("request");
                
                return CompletableFuture.supplyAsync(() -> {
                    // TODO: Implement vehicle creation
                    // This would involve mapping the request to domain entity and saving
                    throw new UnsupportedOperationException("Create vehicle not yet implemented");
                });
            } catch (Exception e) {
                CompletableFuture<Vehicle> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        };
    }

    public DataFetcher<CompletableFuture<Vehicle>> updateVehicle() {
        return dataFetchingEnvironment -> {
            try {
                Long id = Long.valueOf(dataFetchingEnvironment.getArgument("id"));
                Object request = dataFetchingEnvironment.getArgument("request");
                
                return CompletableFuture.supplyAsync(() -> {
                    // TODO: Implement vehicle update
                    throw new UnsupportedOperationException("Update vehicle not yet implemented");
                });
            } catch (Exception e) {
                CompletableFuture<Vehicle> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        };
    }

    public DataFetcher<CompletableFuture<Boolean>> deleteVehicle() {
        return dataFetchingEnvironment -> {
            try {
                Long id = Long.valueOf(dataFetchingEnvironment.getArgument("id"));
                
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        vehicleManagementRepository.deleteVehicle(id);
                        return true;
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to delete vehicle: " + e.getMessage(), e);
                    }
                });
            } catch (Exception e) {
                CompletableFuture<Boolean> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        };
    }

    public DataFetcher<CompletableFuture<Vehicle>> setVehicleStatus() {
        return dataFetchingEnvironment -> {
            try {
                Long id = Long.valueOf(dataFetchingEnvironment.getArgument("id"));
                Vehicle.VehicleStatus status = dataFetchingEnvironment.getArgument("status");
                
                return CompletableFuture.supplyAsync(() -> {
                    Optional<Vehicle> vehicleOpt = vehicleManagementRepository.findVehicleById(id);
                    if (vehicleOpt.isEmpty()) {
                        throw new RuntimeException("Vehicle not found: " + id);
                    }
                    
                    Vehicle vehicle = vehicleOpt.get();
                    switch (status) {
                        case AVAILABLE:
                            vehicle.setAvailable();
                            break;
                        case IN_USE:
                            vehicle.setInUse();
                            break;
                        case MAINTENANCE:
                            vehicle.setMaintenance();
                            break;
                        case OUT_OF_SERVICE:
                            vehicle.setOutOfService();
                            break;
                        case BOOKED:
                            vehicle.setBooked();
                            break;
                    }
                    
                    return vehicleManagementRepository.saveVehicle(vehicle);
                });
            } catch (Exception e) {
                CompletableFuture<Vehicle> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        };
    }

    // Driver Mutations
    public DataFetcher<CompletableFuture<Driver>> createDriver() {
        return dataFetchingEnvironment -> {
            try {
                Object request = dataFetchingEnvironment.getArgument("request");
                
                return CompletableFuture.supplyAsync(() -> {
                    // TODO: Implement driver creation
                    throw new UnsupportedOperationException("Create driver not yet implemented");
                });
            } catch (Exception e) {
                CompletableFuture<Driver> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        };
    }

    public DataFetcher<CompletableFuture<Driver>> updateDriver() {
        return dataFetchingEnvironment -> {
            try {
                Long id = Long.valueOf(dataFetchingEnvironment.getArgument("id"));
                Object request = dataFetchingEnvironment.getArgument("request");
                
                return CompletableFuture.supplyAsync(() -> {
                    // TODO: Implement driver update
                    throw new UnsupportedOperationException("Update driver not yet implemented");
                });
            } catch (Exception e) {
                CompletableFuture<Driver> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        };
    }

    public DataFetcher<CompletableFuture<Boolean>> deleteDriver() {
        return dataFetchingEnvironment -> {
            try {
                Long id = Long.valueOf(dataFetchingEnvironment.getArgument("id"));
                
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        vehicleManagementRepository.deleteDriver(id);
                        return true;
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to delete driver: " + e.getMessage(), e);
                    }
                });
            } catch (Exception e) {
                CompletableFuture<Boolean> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        };
    }

    public DataFetcher<CompletableFuture<Driver>> setDriverStatus() {
        return dataFetchingEnvironment -> {
            try {
                Long id = Long.valueOf(dataFetchingEnvironment.getArgument("id"));
                Driver.DriverStatus status = dataFetchingEnvironment.getArgument("status");
                
                return CompletableFuture.supplyAsync(() -> {
                    Optional<Driver> driverOpt = vehicleManagementRepository.findDriverById(id);
                    if (driverOpt.isEmpty()) {
                        throw new RuntimeException("Driver not found: " + id);
                    }
                    
                    Driver driver = driverOpt.get();
                    switch (status) {
                        case AVAILABLE:
                            driver.setAvailable();
                            break;
                        case ASSIGNED:
                            driver.setAssigned();
                            break;
                        case ON_DUTY:
                            driver.setOnDuty();
                            break;
                        case UNAVAILABLE:
                            driver.setUnavailable();
                            break;
                        case ON_LEAVE:
                            driver.setOnLeave();
                            break;
                        case SICK:
                            driver.setStatus(Driver.DriverStatus.SICK);
                            break;
                    }
                    
                    return vehicleManagementRepository.saveDriver(driver);
                });
            } catch (Exception e) {
                CompletableFuture<Driver> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        };
    }
}