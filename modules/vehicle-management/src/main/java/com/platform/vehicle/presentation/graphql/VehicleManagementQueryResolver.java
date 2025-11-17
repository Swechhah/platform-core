package com.platform.vehicle.presentation.graphql;

import com.platform.vehicle.application.dto.VehicleResponse;
import com.platform.vehicle.domain.Vehicle;
import com.platform.vehicle.domain.Driver;
import com.platform.vehicle.domain.Booking;
import com.platform.vehicle.infrastructure.VehicleManagementRepository;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class VehicleManagementQueryResolver {
    
    private final VehicleManagementRepository vehicleManagementRepository;

    public VehicleManagementQueryResolver(VehicleManagementRepository vehicleManagementRepository) {
        this.vehicleManagementRepository = vehicleManagementRepository;
    }

    // Vehicle Queries
    public DataFetcher<List<VehicleResponse>> getVehicles() {
        return dataFetchingEnvironment -> {
            Vehicle.VehicleStatus status = dataFetchingEnvironment.getArgument("status");
            Vehicle.VehicleType type = dataFetchingEnvironment.getArgument("type");
            Boolean availableOnly = dataFetchingEnvironment.getArgument("availableOnly");

            List<Vehicle> vehicles;
            
            if (availableOnly != null && availableOnly) {
                vehicles = vehicleManagementRepository.findAvailableVehicles();
            } else if (status != null) {
                vehicles = vehicleManagementRepository.findVehiclesByStatus(status);
            } else if (type != null) {
                vehicles = vehicleManagementRepository.findVehiclesByType(type);
            } else {
                // Return all vehicles (in a real implementation, this would be paginated)
                vehicles = vehicleManagementRepository.findAllAvailableVehiclesForBooking();
            }
            
            return vehicles.stream()
                    .map(this::convertToVehicleResponse)
                    .collect(Collectors.toList());
        };
    }

    public DataFetcher<VehicleResponse> getVehicle() {
        return dataFetchingEnvironment -> {
            Long id = Long.valueOf(dataFetchingEnvironment.getArgument("id"));
            Optional<Vehicle> vehicle = vehicleManagementRepository.findVehicleById(id);
            return vehicle.map(this::convertToVehicleResponse).orElse(null);
        };
    }

    public DataFetcher<List<VehicleResponse>> getVehiclesByLocation() {
        return dataFetchingEnvironment -> {
            String location = dataFetchingEnvironment.getArgument("location");
            List<Vehicle> vehicles = vehicleManagementRepository.findVehiclesByLocation(location);
            return vehicles.stream()
                    .map(this::convertToVehicleResponse)
                    .collect(Collectors.toList());
        };
    }

    public DataFetcher<List<VehicleResponse>> getAvailableVehicles() {
        return dataFetchingEnvironment -> {
            LocalDateTime startTime = dataFetchingEnvironment.getArgument("startTime");
            LocalDateTime endTime = dataFetchingEnvironment.getArgument("endTime");
            
            List<Vehicle> allAvailable = vehicleManagementRepository.findAllAvailableVehiclesForBooking();
            
            // Filter out vehicles that have conflicts in the requested time range
            return allAvailable.stream()
                    .filter(vehicle -> isVehicleAvailableForTimeRange(vehicle.getId(), startTime, endTime))
                    .map(this::convertToVehicleResponse)
                    .collect(Collectors.toList());
        };
    }

    public DataFetcher<List<VehicleResponse>> getVehiclesRequiringMaintenance() {
        return dataFetchingEnvironment -> {
            LocalDate date = dataFetchingEnvironment.getArgument("date");
            List<Vehicle> vehicles = vehicleManagementRepository.findVehiclesRequiringMaintenance(date.atStartOfDay());
            return vehicles.stream()
                    .map(this::convertToVehicleResponse)
                    .collect(Collectors.toList());
        };
    }

    // Driver Queries
    public DataFetcher<List<Driver>> getDrivers() {
        return dataFetchingEnvironment -> {
            Driver.DriverStatus status = dataFetchingEnvironment.getArgument("status");
            Boolean availableOnly = dataFetchingEnvironment.getArgument("availableOnly");

            if (availableOnly != null && availableOnly) {
                return vehicleManagementRepository.findAvailableDrivers();
            } else if (status != null) {
                return vehicleManagementRepository.findDriversByStatus(status);
            } else {
                return vehicleManagementRepository.findAllAvailableDriversForBooking();
            }
        };
    }

    public DataFetcher<Driver> getDriver() {
        return dataFetchingEnvironment -> {
            Long id = Long.valueOf(dataFetchingEnvironment.getArgument("id"));
            return vehicleManagementRepository.findDriverById(id).orElse(null);
        };
    }

    public DataFetcher<List<Driver>> getAvailableDrivers() {
        return dataFetchingEnvironment -> {
            LocalDateTime startTime = dataFetchingEnvironment.getArgument("startTime");
            LocalDateTime endTime = dataFetchingEnvironment.getArgument("endTime");
            
            List<Driver> allAvailable = vehicleManagementRepository.findAvailableDrivers();
            
            // Filter out drivers that have conflicts in the requested time range
            return allAvailable.stream()
                    .filter(driver -> isDriverAvailableForTimeRange(driver.getId(), startTime, endTime))
                    .collect(Collectors.toList());
        };
    }

    public DataFetcher<List<Driver>> getDriversRequiringLicenseRenewal() {
        return dataFetchingEnvironment -> {
            LocalDate date = dataFetchingEnvironment.getArgument("date");
            return vehicleManagementRepository.findDriversRequiringLicenseRenewal(date.atStartOfDay());
        };
    }

    // Booking Queries
    public DataFetcher<List<Booking>> getBookings() {
        return dataFetchingEnvironment -> {
            Booking.BookingStatus status = dataFetchingEnvironment.getArgument("status");
            Long requesterId = dataFetchingEnvironment.getArgument("requesterId");
            Long vehicleId = dataFetchingEnvironment.getArgument("vehicleId");
            Long driverId = dataFetchingEnvironment.getArgument("driverId");

            if (status != null) {
                return vehicleManagementRepository.findBookingsByStatus(status);
            } else if (requesterId != null) {
                return vehicleManagementRepository.findBookingsByRequesterId(requesterId);
            } else if (vehicleId != null) {
                return vehicleManagementRepository.findBookingsByVehicleId(vehicleId);
            } else if (driverId != null) {
                return vehicleManagementRepository.findBookingsByDriverId(driverId);
            } else {
                // Return recent bookings (in a real implementation, this would be paginated)
                return vehicleManagementRepository.findUpcomingBookings();
            }
        };
    }

    public DataFetcher<Booking> getBooking() {
        return dataFetchingEnvironment -> {
            Long id = Long.valueOf(dataFetchingEnvironment.getArgument("id"));
            return vehicleManagementRepository.findBookingById(id).orElse(null);
        };
    }

    public DataFetcher<Booking> getBookingByReference() {
        return dataFetchingEnvironment -> {
            String reference = dataFetchingEnvironment.getArgument("reference");
            return vehicleManagementRepository.findBookingByReference(reference).orElse(null);
        };
    }

    public DataFetcher<List<Booking>> getMyBookings() {
        return dataFetchingEnvironment -> {
            Long requesterId = Long.valueOf(dataFetchingEnvironment.getArgument("requesterId"));
            Boolean includeHistory = dataFetchingEnvironment.getArgument("includeHistory");
            
            if (includeHistory != null && includeHistory) {
                return vehicleManagementRepository.findBookingsByRequesterIdOrderByCreatedAtDesc(requesterId);
            } else {
                return vehicleManagementRepository.findBookingsByRequesterId(requesterId);
            }
        };
    }

    public DataFetcher<List<Booking>> getPendingApprovals() {
        return dataFetchingEnvironment -> {
            String managerName = dataFetchingEnvironment.getArgument("managerName");
            return vehicleManagementRepository.findBookingsRequiringApproval(managerName);
        };
    }

    public DataFetcher<List<Booking>> getCurrentBookings() {
        return dataFetchingEnvironment -> vehicleManagementRepository.findCurrentBookings();
    }

    public DataFetcher<List<Booking>> getUpcomingBookings() {
        return dataFetchingEnvironment -> vehicleManagementRepository.findUpcomingBookings();
    }

    // Dashboard Query
    public DataFetcher<Dashboard> getDashboard() {
        return dataFetchingEnvironment -> {
            // This would be implemented with proper aggregation queries
            // For now, return a basic dashboard
            Dashboard dashboard = new Dashboard();
            // Populate dashboard with real data
            return dashboard;
        };
    }

    // Helper methods
    private boolean isVehicleAvailableForTimeRange(Long vehicleId, LocalDateTime startTime, LocalDateTime endTime) {
        return vehicleManagementRepository.findConflictingBookings(vehicleId, startTime, endTime).isEmpty();
    }

    private boolean isDriverAvailableForTimeRange(Long driverId, LocalDateTime startTime, LocalDateTime endTime) {
        return vehicleManagementRepository.findConflictingBookingsForDriver(driverId, startTime, endTime).isEmpty();
    }

    private VehicleResponse convertToVehicleResponse(Vehicle vehicle) {
        VehicleResponse response = new VehicleResponse();
        response.setId(vehicle.getId());
        response.setPlateNumber(vehicle.getPlateNumber());
        response.setMake(vehicle.getMake());
        response.setModel(vehicle.getModel());
        response.setYear(vehicle.getYear());
        response.setType(vehicle.getType().getValue());
        response.setStatus(vehicle.getStatus().getValue());
        response.setCapacity(vehicle.getCapacity());
        response.setColor(vehicle.getColor());
        response.setDescription(vehicle.getDescription());
        response.setLocation(vehicle.getLocation());
        response.setLastMaintenance(vehicle.getLastMaintenance());
        response.setNextMaintenance(vehicle.getNextMaintenance());
        response.setMileage(vehicle.getMileage());
        response.setFuelType(vehicle.getFuelType());
        response.setVehicleGroup(vehicle.getVehicleGroup());
        response.setCostCenter(vehicle.getCostCenter());
        response.setAvailable(vehicle.isAvailable());
        response.setDisplayName(vehicle.getDisplayName());
        
        return response;
    }

    // Simple dashboard class (would be a proper DTO in real implementation)
    public static class Dashboard {
        private int totalVehicles;
        private int availableVehicles;
        private int inUseVehicles;
        private int totalDrivers;
        private int availableDrivers;
        private int onDutyDrivers;
        private int totalBookings;
        private int pendingBookings;
        private int upcomingBookings;
        private int currentBookings;
        private List<Booking> recentActivity;

        // Getters and setters
        public int getTotalVehicles() { return totalVehicles; }
        public void setTotalVehicles(int totalVehicles) { this.totalVehicles = totalVehicles; }
        public int getAvailableVehicles() { return availableVehicles; }
        public void setAvailableVehicles(int availableVehicles) { this.availableVehicles = availableVehicles; }
        public int getInUseVehicles() { return inUseVehicles; }
        public void setInUseVehicles(int inUseVehicles) { this.inUseVehicles = inUseVehicles; }
        public int getTotalDrivers() { return totalDrivers; }
        public void setTotalDrivers(int totalDrivers) { this.totalDrivers = totalDrivers; }
        public int getAvailableDrivers() { return availableDrivers; }
        public void setAvailableDrivers(int availableDrivers) { this.availableDrivers = availableDrivers; }
        public int getOnDutyDrivers() { return onDutyDrivers; }
        public void setOnDutyDrivers(int onDutyDrivers) { this.onDutyDrivers = onDutyDrivers; }
        public int getTotalBookings() { return totalBookings; }
        public void setTotalBookings(int totalBookings) { this.totalBookings = totalBookings; }
        public int getPendingBookings() { return pendingBookings; }
        public void setPendingBookings(int pendingBookings) { this.pendingBookings = pendingBookings; }
        public int getUpcomingBookings() { return upcomingBookings; }
        public void setUpcomingBookings(int upcomingBookings) { this.upcomingBookings = upcomingBookings; }
        public int getCurrentBookings() { return currentBookings; }
        public void setCurrentBookings(int currentBookings) { this.currentBookings = currentBookings; }
        public List<Booking> getRecentActivity() { return recentActivity; }
        public void setRecentActivity(List<Booking> recentActivity) { this.recentActivity = recentActivity; }
    }
}