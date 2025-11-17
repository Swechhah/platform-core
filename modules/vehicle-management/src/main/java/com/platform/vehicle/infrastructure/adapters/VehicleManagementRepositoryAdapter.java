package com.platform.vehicle.infrastructure.adapters;

import com.platform.vehicle.domain.Vehicle;
import com.platform.vehicle.domain.Driver;
import com.platform.vehicle.domain.Booking;
import com.platform.vehicle.infrastructure.VehicleManagementRepository;
import com.platform.vehicle.infrastructure.entities.BookingEntity;
import com.platform.vehicle.infrastructure.entities.DriverEntity;
import com.platform.vehicle.infrastructure.entities.VehicleEntity;
import com.platform.vehicle.infrastructure.repositories.VehicleJpaRepository;
import com.platform.vehicle.infrastructure.repositories.DriverJpaRepository;
import com.platform.vehicle.infrastructure.repositories.BookingJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository adapter implementing the VehicleManagementRepository interface.
 * Bridges between the domain layer and JPA infrastructure.
 */
@Component
public class VehicleManagementRepositoryAdapter implements VehicleManagementRepository {
    
    private final VehicleJpaRepository vehicleJpaRepository;
    private final DriverJpaRepository driverJpaRepository;
    private final BookingJpaRepository bookingJpaRepository;
    private final EntityMapper entityMapper;

    public VehicleManagementRepositoryAdapter(
            VehicleJpaRepository vehicleJpaRepository,
            DriverJpaRepository driverJpaRepository,
            BookingJpaRepository bookingJpaRepository,
            EntityMapper entityMapper) {
        this.vehicleJpaRepository = vehicleJpaRepository;
        this.driverJpaRepository = driverJpaRepository;
        this.bookingJpaRepository = bookingJpaRepository;
        this.entityMapper = entityMapper;
    }

    // Vehicle Repository Methods
    @Override
    public Optional<Vehicle> findVehicleById(Long id) {
        return vehicleJpaRepository.findById(id)
                .map(entityMapper::toDomainVehicle);
    }

    @Override
    public Optional<Vehicle> findVehicleByPlateNumber(String plateNumber) {
        return vehicleJpaRepository.findByPlateNumber(plateNumber)
                .map(entityMapper::toDomainVehicle);
    }

    @Override
    public List<Vehicle> findVehiclesByStatus(Vehicle.VehicleStatus status) {
        return vehicleJpaRepository.findByStatus(
                VehicleEntity.VehicleStatus.valueOf(status.name())
        ).stream()
                .map(entityMapper::toDomainVehicle)
                .collect(Collectors.toList());
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        return vehicleJpaRepository.findAvailableVehicles().stream()
                .map(entityMapper::toDomainVehicle)
                .collect(Collectors.toList());
    }

    @Override
    public List<Vehicle> findVehiclesByType(Vehicle.VehicleType type) {
        return vehicleJpaRepository.findByType(
                VehicleEntity.VehicleType.valueOf(type.name())
        ).stream()
                .map(entityMapper::toDomainVehicle)
                .collect(Collectors.toList());
    }

    @Override
    public List<Vehicle> findVehiclesByLocation(String location) {
        return vehicleJpaRepository.findByLocation(location).stream()
                .map(entityMapper::toDomainVehicle)
                .collect(Collectors.toList());
    }

    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {
        VehicleEntity entity = entityMapper.toVehicleEntity(vehicle);
        VehicleEntity savedEntity = vehicleJpaRepository.save(entity);
        return entityMapper.toDomainVehicle(savedEntity);
    }

    @Override
    public void deleteVehicle(Long id) {
        vehicleJpaRepository.deleteById(id);
    }

    // Driver Repository Methods
    @Override
    public Optional<Driver> findDriverById(Long id) {
        return driverJpaRepository.findById(id)
                .map(entityMapper::toDomainDriver);
    }

    @Override
    public Optional<Driver> findDriverByLicenseNumber(String licenseNumber) {
        return driverJpaRepository.findByLicenseNumber(licenseNumber)
                .map(entityMapper::toDomainDriver);
    }

    @Override
    public List<Driver> findDriversByStatus(Driver.DriverStatus status) {
        return driverJpaRepository.findByStatus(
                DriverEntity.DriverStatus.valueOf(status.name())
        ).stream()
                .map(entityMapper::toDomainDriver)
                .collect(Collectors.toList());
    }

    @Override
    public List<Driver> findAvailableDrivers() {
        return driverJpaRepository.findAvailableDrivers().stream()
                .map(entityMapper::toDomainDriver)
                .collect(Collectors.toList());
    }

    @Override
    public List<Driver> findDriversByLicenseType(Driver.LicenseType licenseType) {
        return driverJpaRepository.findByLicenseType(
                DriverEntity.LicenseType.valueOf(licenseType.name())
        ).stream()
                .map(entityMapper::toDomainDriver)
                .collect(Collectors.toList());
    }

    @Override
    public List<Driver> findDriversByUserId(Long userId) {
        return driverJpaRepository.findByUserId(userId).stream()
                .map(entityMapper::toDomainDriver)
                .collect(Collectors.toList());
    }

    @Override
    public Driver saveDriver(Driver driver) {
        DriverEntity entity = entityMapper.toDriverEntity(driver);
        DriverEntity savedEntity = driverJpaRepository.save(entity);
        return entityMapper.toDomainDriver(savedEntity);
    }

    @Override
    public void deleteDriver(Long id) {
        driverJpaRepository.deleteById(id);
    }

    // Booking Repository Methods
    @Override
    public Optional<Booking> findBookingById(Long id) {
        return bookingJpaRepository.findById(id)
                .map(entityMapper::toDomainBooking);
    }

    @Override
    public Optional<Booking> findBookingByReference(String reference) {
        return bookingJpaRepository.findByBookingReference(reference)
                .map(entityMapper::toDomainBooking);
    }

    @Override
    public List<Booking> findBookingsByRequesterId(Long requesterId) {
        return bookingJpaRepository.findByRequesterId(requesterId).stream()
                .map(entityMapper::toDomainBooking)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findBookingsByVehicleId(Long vehicleId) {
        return bookingJpaRepository.findByVehicleId(vehicleId).stream()
                .map(entityMapper::toDomainBooking)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findBookingsByDriverId(Long driverId) {
        return bookingJpaRepository.findByDriverId(driverId).stream()
                .map(entityMapper::toDomainBooking)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findBookingsByStatus(Booking.BookingStatus status) {
        return bookingJpaRepository.findByStatus(
                BookingEntity.BookingStatus.valueOf(status.name())
        ).stream()
                .map(entityMapper::toDomainBooking)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findBookingsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return bookingJpaRepository.findByDateRange(startDate, endDate).stream()
                .map(entityMapper::toDomainBooking)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findCurrentBookings() {
        return bookingJpaRepository.findCurrentBookings().stream()
                .map(entityMapper::toDomainBooking)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findUpcomingBookings() {
        return bookingJpaRepository.findUpcomingBookings(LocalDateTime.now()).stream()
                .map(entityMapper::toDomainBooking)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findPendingBookings() {
        return bookingJpaRepository.findPendingBookings().stream()
                .map(entityMapper::toDomainBooking)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findBookingsRequiringApproval(String managerName) {
        return bookingJpaRepository.findBookingsRequiringApproval(managerName).stream()
                .map(entityMapper::toDomainBooking)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findConflictingBookings(Long vehicleId, LocalDateTime startTime, LocalDateTime endTime) {
        return bookingJpaRepository.findConflictingBookings(vehicleId, startTime, endTime).stream()
                .map(entityMapper::toDomainBooking)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findConflictingBookingsForDriver(Long driverId, LocalDateTime startTime, LocalDateTime endTime) {
        return bookingJpaRepository.findConflictingBookingsForDriver(driverId, startTime, endTime).stream()
                .map(entityMapper::toDomainBooking)
                .collect(Collectors.toList());
    }

    @Override
    public long countCompletedBookingsForVehicle(Long vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        return bookingJpaRepository.countCompletedBookingsForVehicle(vehicleId, startDate, endDate);
    }

    @Override
    public Double getAverageMileageForVehicle(Long vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        return bookingJpaRepository.getAverageMileageForVehicle(vehicleId, startDate, endDate);
    }

    @Override
    public List<Booking> findBookingsByRequesterIdOrderByCreatedAtDesc(Long requesterId) {
        return bookingJpaRepository.findByRequesterIdOrderByCreatedAtDesc(requesterId).stream()
                .map(entityMapper::toDomainBooking)
                .collect(Collectors.toList());
    }

    @Override
    public Booking saveBooking(Booking booking) {
        BookingEntity entity = entityMapper.toBookingEntity(booking);
        BookingEntity savedEntity = bookingJpaRepository.save(entity);
        return entityMapper.toDomainBooking(savedEntity);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingJpaRepository.deleteById(id);
    }

    @Override
    public List<Vehicle> findAllAvailableVehiclesForBooking() {
        return vehicleJpaRepository.findAvailableVehicles().stream()
                .map(entityMapper::toDomainVehicle)
                .collect(Collectors.toList());
    }

    @Override
    public List<Driver> findAllAvailableDriversForBooking() {
        return driverJpaRepository.findAvailableDrivers().stream()
                .map(entityMapper::toDomainDriver)
                .collect(Collectors.toList());
    }

    @Override
    public List<Vehicle> findVehiclesCurrentlyAvailable(LocalDateTime currentTime) {
        return vehicleJpaRepository.findVehiclesCurrentlyAvailable(currentTime).stream()
                .map(entityMapper::toDomainVehicle)
                .collect(Collectors.toList());
    }

    @Override
    public List<Driver> findDriversCurrentlyAvailable(LocalDateTime currentTime) {
        return driverJpaRepository.findDriversCurrentlyAvailable(currentTime).stream()
                .map(entityMapper::toDomainDriver)
                .collect(Collectors.toList());
    }

    @Override
    public List<Vehicle> findVehiclesRequiringMaintenance(LocalDateTime date) {
        return vehicleJpaRepository.findVehiclesRequiringMaintenance(date).stream()
                .map(entityMapper::toDomainVehicle)
                .collect(Collectors.toList());
    }

    @Override
    public List<Driver> findDriversRequiringLicenseRenewal(LocalDateTime date) {
        return driverJpaRepository.findDriversRequiringLicenseRenewal(date).stream()
                .map(entityMapper::toDomainDriver)
                .collect(Collectors.toList());
    }
}