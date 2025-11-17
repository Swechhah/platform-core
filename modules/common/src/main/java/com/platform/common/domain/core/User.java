package com.platform.common.domain.core;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class User extends BaseEntity {
    
    public enum UserRole {
        ADMIN("ADMIN"),
        EMPLOYEE("EMPLOYEE"),
        MANAGER("MANAGER"),
        DRIVER("DRIVER");

        private final String role;

        UserRole(String role) {
            this.role = role;
        }

        public String getValue() {
            return role;
        }

        public static UserRole fromString(String role) {
            return Arrays.stream(UserRole.values())
                    .filter(r -> r.role.equalsIgnoreCase(role))
                    .findFirst()
                    .orElse(EMPLOYEE);
        }
    }

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String department;
    private String jobTitle;
    private UserRole role;
    private boolean active = true;
    private String adId; // Active Directory ID
    private String managerId;
    private List<String> permissions;
    private LocalDateTime lastLoginAt;
    private boolean emailVerified;
    private String phoneNumber;

    // Constructors
    public User() {
        super();
    }

    public User(String username, String email, String firstName, String lastName, UserRole role) {
        this();
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    // Business Methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public boolean isManager() {
        return role == UserRole.MANAGER || role == UserRole.ADMIN;
    }

    public boolean isDriver() {
        return role == UserRole.DRIVER;
    }

    public boolean canApprove() {
        return isManager();
    }

    public void markLogin() {
        this.lastLoginAt = LocalDateTime.now();
        markUpdated(username);
    }

    public void verifyEmail() {
        this.emailVerified = true;
        markUpdated(username);
    }

    public void updateProfile(String firstName, String lastName, String department, String jobTitle) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.jobTitle = jobTitle;
        markUpdated(username);
    }

    public void addPermission(String permission) {
        if (permissions == null) {
            permissions = new java.util.ArrayList<>();
        }
        if (!permissions.contains(permission)) {
            permissions.add(permission);
        }
    }

    public void removePermission(String permission) {
        if (permissions != null) {
            permissions.remove(permission);
        }
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(adId, user.adId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, adId);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", role=" + role +
                ", active=" + active +
                ", adId='" + adId + '\'' +
                '}';
    }
}