package com.platform.vehicle.infrastructure;

import com.platform.vehicle.infrastructure.entities.VehicleEntity;
import com.platform.vehicle.infrastructure.entities.DriverEntity;
import com.platform.vehicle.infrastructure.entities.BookingEntity;
import com.platform.vehicle.infrastructure.repositories.VehicleJpaRepository;
import com.platform.vehicle.infrastructure.repositories.DriverJpaRepository;
import com.platform.vehicle.infrastructure.repositories.BookingJpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@Profile("!production")
public class SampleDataLoader {

    @Value("${sample-data.enabled:true}")
    private boolean sampleDataEnabled;

    @Bean
    @Transactional
    public CommandLineRunner loadSampleData(
            VehicleJpaRepository vehicleRepository,
            DriverJpaRepository driverRepository,
            BookingJpaRepository bookingRepository) {
        
        return args -> {
            // Skip data loading if disabled via property
            if (!sampleDataEnabled) {
                System.out.println("Sample data loading is disabled. Using SQL script approach instead.");
                return;
            }
            
            // Only load data if repositories are empty
            if (vehicleRepository.count() == 0) {
                System.out.println("Loading sample data...");
                
                // Create sample users (IDs 1-10)
                List<Long> userIds = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
                
                // Create sample vehicles
                List<VehicleEntity> vehicles = createSampleVehicles();
                vehicles = vehicleRepository.saveAll(vehicles);
                vehicleRepository.flush(); // Force ID generation
                System.out.println("Created " + vehicles.size() + " vehicles");
                System.out.println("First vehicle ID: " + (vehicles.isEmpty() ? "none" : vehicles.get(0).getId()));
                
                // Create sample drivers
                List<DriverEntity> drivers = createSampleDrivers(userIds);
                drivers = driverRepository.saveAll(drivers);
                driverRepository.flush(); // Force ID generation
                System.out.println("Created " + drivers.size() + " drivers");
                System.out.println("First driver ID: " + (drivers.isEmpty() ? "none" : drivers.get(0).getId()));
                
                // Create sample bookings
                List<BookingEntity> bookings = createSampleBookings(vehicles, drivers, userIds);
                bookingRepository.saveAll(bookings);
                System.out.println("Created " + bookings.size() + " bookings");
                
                System.out.println("Sample data loading completed!");
            } else {
                System.out.println("Sample data already exists, skipping data loading.");
            }
        };
    }
    
    private List<VehicleEntity> createSampleVehicles() {
        List<VehicleEntity> vehicles = new ArrayList<>();
        
        // Sample vehicle data: make, model, year, type, capacity, color
        Object[][] vehicleData = {
            // Sedans (8 vehicles)
            {"Toyota", "Camry", 2022, VehicleEntity.VehicleType.SEDAN, 5, "Silver"},
            {"Honda", "Accord", 2021, VehicleEntity.VehicleType.SEDAN, 5, "Black"},
            {"Mazda", "6", 2023, VehicleEntity.VehicleType.SEDAN, 5, "White"},
            {"Nissan", "Altima", 2020, VehicleEntity.VehicleType.SEDAN, 5, "Blue"},
            {"Ford", "Fusion", 2019, VehicleEntity.VehicleType.SEDAN, 5, "Gray"},
            {"Chevrolet", "Malibu", 2022, VehicleEntity.VehicleType.SEDAN, 5, "Red"},
            {"Hyundai", "Sonata", 2021, VehicleEntity.VehicleType.SEDAN, 5, "Beige"},
            {"Kia", "Optima", 2020, VehicleEntity.VehicleType.SEDAN, 5, "Green"},
            
            // SUVs (7 vehicles)
            {"Toyota", "RAV4", 2023, VehicleEntity.VehicleType.SUV, 5, "White"},
            {"Honda", "CR-V", 2022, VehicleEntity.VehicleType.SUV, 5, "Black"},
            {"Ford", "Escape", 2021, VehicleEntity.VehicleType.SUV, 5, "Silver"},
            {"Nissan", "Rogue", 2020, VehicleEntity.VehicleType.SUV, 5, "Blue"},
            {"Chevrolet", "Equinox", 2022, VehicleEntity.VehicleType.SUV, 5, "Red"},
            {"Hyundai", "Tucson", 2021, VehicleEntity.VehicleType.SUV, 5, "Gray"},
            {"Kia", "Sportage", 2023, VehicleEntity.VehicleType.SUV, 5, "Green"},
            
            // Vans (4 vehicles)
            {"Ford", "Transit", 2022, VehicleEntity.VehicleType.VAN, 12, "White"},
            {"Chevrolet", "Express", 2021, VehicleEntity.VehicleType.VAN, 15, "Silver"},
            {"Mercedes", "Sprinter", 2023, VehicleEntity.VehicleType.VAN, 10, "Black"},
            {"Dodge", "Grand Caravan", 2020, VehicleEntity.VehicleType.VAN, 7, "Blue"},
            
            // Trucks (4 vehicles)
            {"Ford", "F-150", 2022, VehicleEntity.VehicleType.TRUCK, 3, "Red"},
            {"Chevrolet", "Silverado", 2021, VehicleEntity.VehicleType.TRUCK, 3, "Blue"},
            {"Ram", "1500", 2023, VehicleEntity.VehicleType.TRUCK, 3, "Black"},
            {"Toyota", "Tundra", 2020, VehicleEntity.VehicleType.TRUCK, 3, "White"},
            
            // Motorcycle (1 vehicle)
            {"Harley-Davidson", "Street Glide", 2022, VehicleEntity.VehicleType.MOTORCYCLE, 1, "Black"},
            {"Yamaha", "Golf Cart", 2023, VehicleEntity.VehicleType.OTHER, 4, "Green"}
        };
        
        // Create vehicles
        for (int i = 0; i < vehicleData.length; i++) {
            Object[] data = (Object[]) vehicleData[i];
            String make = (String) data[0];
            String model = (String) data[1];
            Integer year = (Integer) data[2];
            VehicleEntity.VehicleType type = (VehicleEntity.VehicleType) data[3];
            Integer capacity = (Integer) data[4];
            String color = (String) data[5];
            
            VehicleEntity vehicle = new VehicleEntity();
            vehicle.setPlateNumber("BPR-" + String.format("%03d", i + 1));
            vehicle.setMake(make);
            vehicle.setModel(model);
            vehicle.setManufactureYear(year);
            vehicle.setType(type);
            vehicle.setCapacity(capacity);
            vehicle.setColor(color);
            vehicle.setDescription(make + " " + model + " - " + year);
            vehicle.setLocation(getRandomLocation());
            vehicle.setMileage(ThreadLocalRandom.current().nextDouble(500, 150000));
            vehicle.setFuelType(getRandomFuelType());
            vehicle.setVehicleGroup(getVehicleGroup(type));
            vehicle.setCostCenter(getRandomCostCenter());
            
            // Set maintenance dates
            vehicle.setLastMaintenance(LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(30, 180)));
            vehicle.setNextMaintenance(LocalDateTime.now().plusDays(ThreadLocalRandom.current().nextInt(30, 90)));
            
            // Set random status (70% available, 20% in use, 5% maintenance, 5% out of service)
            int statusRoll = ThreadLocalRandom.current().nextInt(100);
            if (statusRoll < 70) {
                vehicle.setStatus(VehicleEntity.VehicleStatus.AVAILABLE);
            } else if (statusRoll < 90) {
                vehicle.setStatus(VehicleEntity.VehicleStatus.IN_USE);
            } else if (statusRoll < 95) {
                vehicle.setStatus(VehicleEntity.VehicleStatus.MAINTENANCE);
            } else {
                vehicle.setStatus(VehicleEntity.VehicleStatus.OUT_OF_SERVICE);
            }
            
            vehicle.setAvailableForBooking(vehicle.getStatus() == VehicleEntity.VehicleStatus.AVAILABLE);
            
            vehicles.add(vehicle);
        }
        
        return vehicles;
    }
    
    private List<DriverEntity> createSampleDrivers(List<Long> userIds) {
        List<DriverEntity> drivers = new ArrayList<>();
        
        // Sample driver data: firstName, lastName, licenseType, phone, department
        Object[][] driverData = {
            {"John", "Smith", DriverEntity.LicenseType.CLASS_1, "555-0101", "Operations"},
            {"Sarah", "Johnson", DriverEntity.LicenseType.CLASS_2, "555-0102", "Sales"},
            {"Michael", "Brown", DriverEntity.LicenseType.COMMERCIAL, "555-0103", "Logistics"},
            {"Emily", "Davis", DriverEntity.LicenseType.CLASS_1, "555-0104", "Marketing"},
            {"David", "Wilson", DriverEntity.LicenseType.CLASS_3, "555-0105", "Human Resources"},
            {"Jessica", "Miller", DriverEntity.LicenseType.CLASS_2, "555-0106", "Finance"},
            {"James", "Taylor", DriverEntity.LicenseType.CLASS_1, "555-0107", "Operations"},
            {"Lisa", "Anderson", DriverEntity.LicenseType.MOTORCYCLE, "555-0108", "Security"},
            {"Robert", "Thomas", DriverEntity.LicenseType.COMMERCIAL, "555-0109", "Maintenance"},
            {"Amanda", "Jackson", DriverEntity.LicenseType.CLASS_1, "555-0110", "Operations"},
            {"Christopher", "White", DriverEntity.LicenseType.CLASS_2, "555-0111", "Sales"},
            {"Jennifer", "Harris", DriverEntity.LicenseType.CLASS_3, "555-0112", "Administration"},
            {"Daniel", "Martin", DriverEntity.LicenseType.CLASS_1, "555-0113", "IT"},
            {"Rachel", "Thompson", DriverEntity.LicenseType.CLASS_2, "555-0114", "Customer Service"},
            {"Kevin", "Garcia", DriverEntity.LicenseType.COMMERCIAL, "555-0115", "Transportation"}
        };
        
        // Create drivers
        for (int i = 0; i < driverData.length; i++) {
            Object[] data = (Object[]) driverData[i];
            String firstName = (String) data[0];
            String lastName = (String) data[1];
            DriverEntity.LicenseType licenseType = (DriverEntity.LicenseType) data[2];
            String phone = (String) data[3];
            String department = (String) data[4];
            
            DriverEntity driver = new DriverEntity();
            driver.setUserId(userIds.get(i % userIds.size()));
            driver.setLicenseType(licenseType);
            driver.setLicenseNumber("DL" + String.format("%08d", 10000000 + i));
            driver.setLicenseExpiryDate(LocalDate.now().plusYears(ThreadLocalRandom.current().nextInt(1, 8)));
            driver.setPhoneNumber(phone);
            driver.setEmergencyContact(firstName + " " + lastName + " Emergency");
            driver.setEmergencyPhone("555-999" + String.format("%02d", i + 1));
            driver.setAddress(getRandomAddress());
            driver.setHireDate(LocalDate.now().minusYears(ThreadLocalRandom.current().nextInt(1, 15)));
            driver.setYearsOfExperience(ThreadLocalRandom.current().nextInt(2, 20));
            driver.setCertifications(getRandomCertifications(licenseType));
            driver.setDepartment(department);
            driver.setCostCenter(getRandomCostCenter());
            driver.setShift(getRandomShift());
            driver.setLastHealthCheck(LocalDate.now().minusMonths(ThreadLocalRandom.current().nextInt(1, 12)));
            driver.setNotes("Experienced driver, excellent safety record");
            driver.setTotalTripsCompleted(ThreadLocalRandom.current().nextInt(50, 500));
            driver.setTotalMilesDriven(ThreadLocalRandom.current().nextInt(10000, 100000));
            
            // Set random status (80% available, 10% on duty, 5% on leave, 5% sick)
            int statusRoll = ThreadLocalRandom.current().nextInt(100);
            if (statusRoll < 80) {
                driver.setStatus(DriverEntity.DriverStatus.AVAILABLE);
            } else if (statusRoll < 90) {
                driver.setStatus(DriverEntity.DriverStatus.ON_DUTY);
            } else if (statusRoll < 95) {
                driver.setStatus(DriverEntity.DriverStatus.ON_LEAVE);
            } else {
                driver.setStatus(DriverEntity.DriverStatus.SICK);
            }
            
            driver.setAvailableForBooking(driver.getStatus() == DriverEntity.DriverStatus.AVAILABLE);
            
            drivers.add(driver);
        }
        
        return drivers;
    }
    
    private List<BookingEntity> createSampleBookings(List<VehicleEntity> vehicles, 
                                                   List<DriverEntity> drivers, 
                                                   List<Long> userIds) {
        List<BookingEntity> bookings = new ArrayList<>();
        
        // Create 20 sample bookings with various statuses and times
        for (int i = 0; i < 20; i++) {
            BookingEntity booking = new BookingEntity();
            booking.setBookingReference("BKG-" + String.format("%08d", 10000000 + i));
            
            // Select random vehicle and driver
            VehicleEntity vehicle = vehicles.get(ThreadLocalRandom.current().nextInt(vehicles.size()));
            DriverEntity driver = drivers.get(ThreadLocalRandom.current().nextInt(drivers.size()));
            Long requesterId = userIds.get(ThreadLocalRandom.current().nextInt(userIds.size()));
            Long approverId = userIds.get(ThreadLocalRandom.current().nextInt(userIds.size()));
            
            // Debug output for first few bookings
            if (i < 3) {
                System.out.println("Booking " + i + " - Vehicle ID: " + vehicle.getId() + ", Driver ID: " + driver.getId());
            }
            
            // Set booking time (some past, some current, some future)
            LocalDateTime baseTime = LocalDateTime.now();
            int daysOffset = ThreadLocalRandom.current().nextInt(-30, 30);
            int hoursOffset = ThreadLocalRandom.current().nextInt(8, 17);
            
            LocalDateTime startTime = baseTime.plusDays(daysOffset).withHour(hoursOffset).withMinute(0);
            LocalDateTime endTime = startTime.plusHours(ThreadLocalRandom.current().nextInt(2, 8));
            
            booking.setVehicleId(vehicle.getId());
            booking.setDriverId(driver.getId());
            booking.setRequesterId(requesterId);
            booking.setApproverId(approverId);
            booking.setStartTime(startTime);
            booking.setEndTime(endTime);
            
            // Set booking type and purpose
            BookingEntity.BookingType[] types = BookingEntity.BookingType.values();
            booking.setType(types[ThreadLocalRandom.current().nextInt(types.length)]);
            booking.setPurpose(getRandomPurpose(booking.getType()));
            booking.setDescription("Business trip for " + booking.getPurpose());
            booking.setPickupLocation(getRandomLocation());
            booking.setDestination(getRandomDestination());
            booking.setReturnLocation(booking.getPickupLocation());
            booking.setEstimatedPassengers(ThreadLocalRandom.current().nextInt(1, 6));
            booking.setManagerName("Manager " + (approverId % 5 + 1));
            booking.setCostCenter(getRandomCostCenter());
            
            // Set status based on timing
            LocalDateTime now = LocalDateTime.now();
            BookingEntity.BookingStatus status;
            
            if (now.isBefore(startTime)) {
                // Future booking
                status = ThreadLocalRandom.current().nextInt(2) == 0 ? 
                    BookingEntity.BookingStatus.CONFIRMED : BookingEntity.BookingStatus.APPROVED;
            } else if (now.isAfter(endTime)) {
                // Past booking
                status = ThreadLocalRandom.current().nextInt(3) == 0 ? 
                    BookingEntity.BookingStatus.COMPLETED : BookingEntity.BookingStatus.CANCELLED;
            } else {
                // Current booking
                status = BookingEntity.BookingStatus.ACTIVE;
                booking.setActualStartTime(startTime);
            }
            
            booking.setStatus(status);
            
            // Set approval info
            if (status.ordinal() >= BookingEntity.BookingStatus.APPROVED.ordinal()) {
                booking.setApprovedAt(startTime.minusDays(1));
                booking.setApprovalComment("Approved for business use");
            }
            
            // Set some additional fields
            booking.setActualMileage(ThreadLocalRandom.current().nextDouble(50, 500));
            booking.setAdditionalRequirements("None");
            
            bookings.add(booking);
        }
        
        return bookings;
    }
    
    // Helper methods for random data generation
    private String getRandomLocation() {
        String[] locations = {
            "Main Office - Floor 1", "Main Office - Floor 2", "Main Office - Floor 3",
            "Branch Office - North", "Branch Office - South", "Branch Office - East", 
            "Branch Office - West", "Warehouse - Downtown", "Warehouse - Industrial",
            "Client Site - Tech Park", "Client Site - Business District"
        };
        return locations[ThreadLocalRandom.current().nextInt(locations.length)];
    }
    
    private String getRandomDestination() {
        String[] destinations = {
            "Airport - Terminal 1", "Airport - Terminal 2", "City Center",
            "Business Park", "Client Office - North", "Client Office - South",
            "Conference Center", "Hotel - Downtown", "University Campus",
            "Government Building", "Medical Center", "Shopping Mall"
        };
        return destinations[ThreadLocalRandom.current().nextInt(destinations.length)];
    }
    
    private String getRandomFuelType() {
        String[] fuelTypes = {"Gasoline", "Hybrid", "Electric", "Diesel"};
        return fuelTypes[ThreadLocalRandom.current().nextInt(fuelTypes.length)];
    }
    
    private String getVehicleGroup(VehicleEntity.VehicleType type) {
        switch (type) {
            case SEDAN: return "Executive";
            case SUV: return "Fleet";
            case VAN: return "Transport";
            case TRUCK: return "Utility";
            case MOTORCYCLE: return "Special";
            default: return "General";
        }
    }
    
    private String getRandomCostCenter() {
        String[] costCenters = {"CC-001", "CC-002", "CC-003", "CC-004", "CC-005"};
        return costCenters[ThreadLocalRandom.current().nextInt(costCenters.length)];
    }
    
    private String getRandomAddress() {
        return ThreadLocalRandom.current().nextInt(100, 999) + " " +
               ThreadLocalRandom.current().nextInt(1000, 9999) + " " +
               ThreadLocalRandom.current().nextInt(1, 9) + "th Street, " +
               "City, State " + ThreadLocalRandom.current().nextInt(10000, 99999);
    }
    
    private String getRandomShift() {
        String[] shifts = {"Morning", "Afternoon", "Evening", "Night"};
        return shifts[ThreadLocalRandom.current().nextInt(shifts.length)];
    }
    
    private String getRandomCertifications(DriverEntity.LicenseType licenseType) {
        switch (licenseType) {
            case COMMERCIAL: return "DOT Certified, Hazmat License";
            case MOTORCYCLE: return "Motorcycle Safety Course";
            case CLASS_1: return "Defensive Driving, First Aid";
            case CLASS_2: return "Defensive Driving";
            case CLASS_3: return "Basic Driving Course";
            default: return "Standard License";
        }
    }
    
    private String getRandomPurpose(BookingEntity.BookingType type) {
        switch (type) {
            case BUSINESS_TRIP: return "Client meeting in downtown";
            case MEETING: return "Department coordination meeting";
            case DELIVERY: return "Document delivery to client";
            case MAINTENANCE_TRIP: return "Vehicle maintenance check";
            case TRAINING: return "Safety training session";
            default: return "General business travel";
        }
    }
}