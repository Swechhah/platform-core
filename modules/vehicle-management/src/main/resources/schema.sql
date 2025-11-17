-- Schema for BPR Vehicle Management System
-- This file defines the database tables and their structure

-- Users table (if not using AD integration)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    department VARCHAR(100),
    job_title VARCHAR(100),
    role VARCHAR(20) NOT NULL,
    active BOOLEAN DEFAULT true,
    ad_id VARCHAR(100),
    manager_id BIGINT,
    permissions TEXT,
    last_login_at TIMESTAMP,
    email_verified BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Vehicles table
CREATE TABLE IF NOT EXISTS vehicles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    status VARCHAR(20) NOT NULL,
    type VARCHAR(20) NOT NULL,
    plate_number VARCHAR(20) UNIQUE NOT NULL,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    manufacture_year INTEGER NOT NULL,
    capacity INTEGER NOT NULL,
    color VARCHAR(50),
    description TEXT,
    location VARCHAR(100),
    last_maintenance TIMESTAMP,
    next_maintenance TIMESTAMP,
    available_for_booking BOOLEAN DEFAULT true,
    mileage DOUBLE DEFAULT 0.0,
    fuel_type VARCHAR(20),
    vehicle_group VARCHAR(50),
    cost_center VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Drivers table
CREATE TABLE IF NOT EXISTS drivers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    status VARCHAR(20) NOT NULL,
    license_type VARCHAR(20) NOT NULL,
    user_id BIGINT,
    license_number VARCHAR(50) UNIQUE NOT NULL,
    license_expiry_date DATE,
    phone_number VARCHAR(20),
    emergency_contact VARCHAR(100),
    emergency_phone VARCHAR(20),
    address TEXT,
    hire_date DATE,
    years_experience INTEGER NOT NULL DEFAULT 0,
    certifications TEXT,
    available_for_booking BOOLEAN DEFAULT true,
    department VARCHAR(100),
    cost_center VARCHAR(50),
    shift VARCHAR(20),
    last_health_check DATE,
    notes TEXT,
    total_trips_completed INTEGER NOT NULL DEFAULT 0,
    total_miles_driven DOUBLE NOT NULL DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    deleted BOOLEAN DEFAULT false
);

-- Bookings table
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_reference VARCHAR(50) UNIQUE NOT NULL,
    status VARCHAR(20) NOT NULL,
    type VARCHAR(20),
    vehicle_id BIGINT NOT NULL,
    driver_id BIGINT,
    requester_id BIGINT NOT NULL,
    approver_id BIGINT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    actual_start_time TIMESTAMP,
    actual_end_time TIMESTAMP,
    pickup_location VARCHAR(200),
    destination VARCHAR(200),
    return_location VARCHAR(200),
    estimated_passengers INTEGER DEFAULT 1,
    manager_name VARCHAR(100),
    cost_center VARCHAR(50),
    approval_comment TEXT,
    approved_at TIMESTAMP,
    rejected_at TIMESTAMP,
    rejection_reason TEXT,
    display_status VARCHAR(20),
    feedback TEXT,
    actual_mileage DOUBLE DEFAULT 0.0,
    additional_requirements TEXT,
    duration_in_hours INTEGER,
    can_be_cancelled BOOLEAN DEFAULT true,
    is_current BOOLEAN DEFAULT false,
    is_upcoming BOOLEAN DEFAULT false,
    needs_approval BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    deleted BOOLEAN DEFAULT false
);

-- Booking events table
CREATE TABLE IF NOT EXISTS booking_events (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    event_id VARCHAR(100) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    event_data TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    caused_by VARCHAR(100),
    version INTEGER NOT NULL DEFAULT 1
);

-- Add foreign key constraints (if needed)
-- ALTER TABLE drivers ADD CONSTRAINT fk_driver_user FOREIGN KEY (user_id) REFERENCES users(id);
-- ALTER TABLE bookings ADD CONSTRAINT fk_booking_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id);
-- ALTER TABLE bookings ADD CONSTRAINT fk_booking_driver FOREIGN KEY (driver_id) REFERENCES drivers(id);
-- ALTER TABLE bookings ADD CONSTRAINT fk_booking_requester FOREIGN KEY (requester_id) REFERENCES users(id);
-- ALTER TABLE bookings ADD CONSTRAINT fk_booking_approver FOREIGN KEY (approver_id) REFERENCES users(id);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_vehicles_status ON vehicles(status);
CREATE INDEX IF NOT EXISTS idx_vehicles_type ON vehicles(type);
CREATE INDEX IF NOT EXISTS idx_vehicles_available ON vehicles(available_for_booking);
CREATE INDEX IF NOT EXISTS idx_vehicles_location ON vehicles(location);

CREATE INDEX IF NOT EXISTS idx_drivers_status ON drivers(status);
CREATE INDEX IF NOT EXISTS idx_drivers_license_type ON drivers(license_type);
CREATE INDEX IF NOT EXISTS idx_drivers_available ON drivers(available_for_booking);
CREATE INDEX IF NOT EXISTS idx_drivers_user_id ON drivers(user_id);

CREATE INDEX IF NOT EXISTS idx_bookings_status ON bookings(status);
CREATE INDEX IF NOT EXISTS idx_bookings_vehicle_id ON bookings(vehicle_id);
CREATE INDEX IF NOT EXISTS idx_bookings_driver_id ON bookings(driver_id);
CREATE INDEX IF NOT EXISTS idx_bookings_requester_id ON bookings(requester_id);
CREATE INDEX IF NOT EXISTS idx_bookings_start_time ON bookings(start_time);
CREATE INDEX IF NOT EXISTS idx_bookings_end_time ON bookings(end_time);
CREATE INDEX IF NOT EXISTS idx_bookings_reference ON bookings(booking_reference);

CREATE INDEX IF NOT EXISTS idx_booking_events_booking_id ON booking_events(id);
CREATE INDEX IF NOT EXISTS idx_booking_events_type ON booking_events(event_type);
CREATE INDEX IF NOT EXISTS idx_booking_events_timestamp ON booking_events(timestamp);