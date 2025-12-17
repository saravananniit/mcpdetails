package com.bank.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Represents a customer in the banking system.
 * Follows immutability and encapsulation principles.
 */
public class Customer {
    private final String customerId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final LocalDate dateOfBirth;
    private final String address;
    private final LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private boolean isActive;

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\+?[1-9]\\d{1,14}$");

    private Customer(Builder builder) {
        this.customerId = builder.customerId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
        this.dateOfBirth = builder.dateOfBirth;
        this.address = builder.address;
        this.createdAt = builder.createdAt;
        this.lastModifiedAt = builder.lastModifiedAt;
        this.isActive = builder.isActive;
    }

    // Getters
    public String getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getAge() {
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    public void deactivate() {
        this.isActive = false;
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void activate() {
        this.isActive = true;
        this.lastModifiedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", name='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    // Builder Pattern
    public static class Builder {
        private String customerId;
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private LocalDate dateOfBirth;
        private String address;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private boolean isActive;

        public Builder() {
            this.customerId = UUID.randomUUID().toString();
            this.createdAt = LocalDateTime.now();
            this.lastModifiedAt = LocalDateTime.now();
            this.isActive = true;
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder lastModifiedAt(LocalDateTime lastModifiedAt) {
            this.lastModifiedAt = lastModifiedAt;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Customer build() {
            validateFields();
            return new Customer(this);
        }

        private void validateFields() {
            if (firstName == null || firstName.trim().isEmpty()) {
                throw new IllegalStateException("First name is required");
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                throw new IllegalStateException("Last name is required");
            }
            if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
                throw new IllegalStateException("Valid email is required");
            }
            if (phoneNumber == null || !PHONE_PATTERN.matcher(phoneNumber).matches()) {
                throw new IllegalStateException("Valid phone number is required");
            }
            if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now().minusYears(18))) {
                throw new IllegalStateException("Customer must be at least 18 years old");
            }
        }
    }
}

