package com.sdg.energytracker.entity;

/**
 * Appliance.java
 *
 * This is the parent (superclass) for all household appliances.
 * It demonstrates the OOP principle of ENCAPSULATION by:
 *   - Declaring all instance variables as 'private' to hide internal state.
 *   - Providing public getter and setter methods with DATA VALIDATION
 *     to control how external code accesses and modifies the data.
 *
 * It also serves as the base class for INHERITANCE, allowing subclasses
 * (LightAppliance, CoolingAppliance) to reuse and extend its functionality.
 *
 * Related to SDG 7: Affordable and Clean Energy - tracking energy consumption
 * helps households understand and reduce their energy usage.
 *
 * @author Group 1 - WEN SIHAO (0379856)
 * @version 2.0
 */
public class Appliance {

    // ==================== ENCAPSULATION ====================
    // All attributes are declared 'private' so they cannot be accessed
    // directly from outside this class. This protects data integrity.

    /** The name of the appliance (e.g., "Air Conditioner", "LED Bulb"). */
    private String applianceName;

    /** The power rating of the appliance in watts (W). Must be non-negative. */
    private double powerRatingInWatts;

    /** The usage duration of the appliance in hours (h). Must be non-negative. */
    private double usageDurationInHours;

    // ==================== CONSTRUCTORS ====================

    /**
     * Default (no-argument) constructor.
     * Initialises the appliance with default values.
     */
    public Appliance() {
        this.applianceName = "Unknown";
        this.powerRatingInWatts = 0.0;
        this.usageDurationInHours = 0.0;
    }

    /**
     * Parameterised constructor.
     * Creates an Appliance with the specified name, power rating, and usage duration.
     * Uses setter methods internally to enforce data validation.
     *
     * @param applianceName        the name of the appliance
     * @param powerRatingInWatts   the power rating in watts (must be >= 0)
     * @param usageDurationInHours the usage duration in hours (must be >= 0)
     */
    public Appliance(String applianceName, double powerRatingInWatts, double usageDurationInHours) {
        setApplianceName(applianceName);
        setPowerRatingInWatts(powerRatingInWatts);
        setUsageDurationInHours(usageDurationInHours);
    }

    // ==================== GETTERS & SETTERS (with validation) ====================
    // Getters and Setters are a key part of ENCAPSULATION.
    // Setters include validation logic to ensure data integrity,
    // e.g., preventing negative values for power rating and duration.

    /**
     * Gets the name of the appliance.
     *
     * @return the appliance name
     */
    public String getApplianceName() {
        return applianceName;
    }

    /**
     * Sets the name of the appliance.
     * Validates that the name is not null or empty.
     *
     * @param applianceName the appliance name to set
     * @throws IllegalArgumentException if applianceName is null or empty
     */
    public void setApplianceName(String applianceName) {
        if (applianceName == null || applianceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Appliance name cannot be null or empty.");
        }
        this.applianceName = applianceName.trim();
    }

    /**
     * Gets the power rating of the appliance in watts.
     *
     * @return the power rating in watts
     */
    public double getPowerRatingInWatts() {
        return powerRatingInWatts;
    }

    /**
     * Sets the power rating of the appliance in watts.
     * Validates that the value is not negative.
     *
     * @param powerRatingInWatts the power rating to set (must be >= 0)
     * @throws IllegalArgumentException if powerRatingInWatts is negative
     */
    public void setPowerRatingInWatts(double powerRatingInWatts) {
        if (powerRatingInWatts < 0) {
            throw new IllegalArgumentException("Power rating cannot be negative. Received: " + powerRatingInWatts);
        }
        this.powerRatingInWatts = powerRatingInWatts;
    }

    /**
     * Gets the usage duration of the appliance in hours.
     *
     * @return the usage duration in hours
     */
    public double getUsageDurationInHours() {
        return usageDurationInHours;
    }

    /**
     * Sets the usage duration of the appliance in hours.
     * Validates that the value is not negative.
     *
     * @param usageDurationInHours the usage duration to set (must be >= 0)
     * @throws IllegalArgumentException if usageDurationInHours is negative
     */
    public void setUsageDurationInHours(double usageDurationInHours) {
        if (usageDurationInHours < 0) {
            throw new IllegalArgumentException("Usage duration cannot be negative. Received: " + usageDurationInHours);
        }
        this.usageDurationInHours = usageDurationInHours;
    }

    // ==================== METHODS ====================

    /**
     * Calculates the energy consumption of the appliance.
     *
     * Formula: Energy (kWh) = Power (W) * Duration (h) / 1000
     *
     * This method is designed to be OVERRIDDEN by subclasses (POLYMORPHISM).
     * Subclasses like LightAppliance and CoolingAppliance provide their own
     * specialised calculation logic while keeping the same method signature.
     *
     * @return the energy consumption in kilowatt-hours (kWh)
     */
    public double calculateEnergyConsumption() {
        // Convert watts to kilowatts by dividing by 1000
        return (powerRatingInWatts * usageDurationInHours) / 1000.0;
    }

    /**
     * Returns a string representation of the Appliance.
     * Provides a human-readable summary of the appliance's attributes.
     *
     * @return a formatted string containing the appliance details
     */
    @Override
    public String toString() {
        return String.format("Appliance [Name: %s, Power: %.2f W, Duration: %.2f h, Energy: %.4f kWh]",
                applianceName, powerRatingInWatts, usageDurationInHours, calculateEnergyConsumption());
    }
}
