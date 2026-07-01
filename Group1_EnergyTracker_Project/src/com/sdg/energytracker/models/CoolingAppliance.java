package com.sdg.energytracker.models;

/**
 * CoolingAppliance.java
 * 
 * This class demonstrates INHERITANCE by extending the Appliance superclass.
 * It inherits all attributes and methods from Appliance, and adds a new
 * attribute 'temperatureSetting' specific to cooling appliances.
 * 
 * It also demonstrates POLYMORPHISM by overriding the calculateEnergyConsumption()
 * method. When the temperature is set below 24°C, the appliance works harder
 * and consumes 20% more energy — reflecting real-world air conditioning behaviour.
 * 
 * This supports SDG 7 by encouraging users to set higher temperature thresholds
 * to reduce unnecessary energy consumption from cooling systems.
 * 
 * @author Group Work
 * @version 1.0
 */
public class CoolingAppliance extends Appliance {
    // 'extends Appliance' establishes an IS-A relationship (INHERITANCE).
    // CoolingAppliance IS-A Appliance, inheriting all its members.

    // ==================== ENCAPSULATION ====================
    // Additional private attribute specific to CoolingAppliance.

    /** The temperature setting in degrees Celsius for this cooling appliance. */
    private int temperatureSetting;

    /** The energy-efficient temperature threshold in degrees Celsius. */
    private static final int EFFICIENT_TEMP_THRESHOLD = 24;

    /** The penalty multiplier applied when temperature is below the threshold. */
    private static final double LOW_TEMP_PENALTY = 1.2; // 20% increase

    // ==================== CONSTRUCTORS ====================

    /**
     * Default (no-argument) constructor.
     * Calls the parent class default constructor and sets a default
     * temperature of 24°C (the energy-efficient threshold).
     */
    public CoolingAppliance() {
        super(); // Calls Appliance() default constructor — INHERITANCE in action
        this.temperatureSetting = EFFICIENT_TEMP_THRESHOLD;
    }

    /**
     * Parameterised constructor.
     * Uses super() to delegate common attribute initialisation to the parent class,
     * demonstrating INHERITANCE and code reuse.
     *
     * @param applianceName        the name of the cooling appliance
     * @param powerRatingInWatts   the power rating in watts (must be >= 0)
     * @param usageDurationInHours the usage duration in hours (must be >= 0)
     * @param temperatureSetting   the temperature setting in degrees Celsius
     */
    public CoolingAppliance(String applianceName, double powerRatingInWatts,
                            double usageDurationInHours, int temperatureSetting) {
        super(applianceName, powerRatingInWatts, usageDurationInHours);
        setTemperatureSetting(temperatureSetting);
    }

    // ==================== GETTER & SETTER ====================

    /**
     * Gets the temperature setting of the cooling appliance.
     *
     * @return the temperature setting in degrees Celsius
     */
    public int getTemperatureSetting() {
        return temperatureSetting;
    }

    /**
     * Sets the temperature setting of the cooling appliance.
     * Validates that the temperature is within a reasonable range (16–30°C).
     *
     * @param temperatureSetting the temperature to set in degrees Celsius
     * @throws IllegalArgumentException if the temperature is outside the valid range
     */
    public void setTemperatureSetting(int temperatureSetting) {
        if (temperatureSetting < 16 || temperatureSetting > 30) {
            throw new IllegalArgumentException(
                    "Temperature setting must be between 16 and 30 degrees Celsius. Received: "
                            + temperatureSetting);
        }
        this.temperatureSetting = temperatureSetting;
    }

    // ==================== POLYMORPHISM (Method Overriding) ====================

    /**
     * Overrides the parent class method to provide specialised energy calculation
     * for cooling appliances. This is an example of POLYMORPHISM.
     * 
     * If the temperature setting is below 24°C, the cooling appliance must work
     * harder to maintain a lower temperature, resulting in a 20% increase in
     * energy consumption (multiplied by 1.2).
     * 
     * The @Override annotation is a compile-time check to ensure this method
     * properly overrides the parent's calculateEnergyConsumption().
     *
     * @return the energy consumption in kilowatt-hours (kWh),
     *         with a 20% penalty if temperature is set below 24°C
     */
    @Override
    public double calculateEnergyConsumption() {
        // Call the parent class method to get the base energy consumption
        double baseEnergy = super.calculateEnergyConsumption();

        if (temperatureSetting < EFFICIENT_TEMP_THRESHOLD) {
            // Low temperature setting increases energy usage by 20%
            return baseEnergy * LOW_TEMP_PENALTY;
        } else {
            // At or above the efficient threshold, standard consumption applies
            return baseEnergy;
        }
    }

    /**
     * Returns a string representation of the CoolingAppliance.
     * Extends the parent's toString() with temperature-specific information.
     *
     * @return a formatted string containing the cooling appliance details
     */
    @Override
    public String toString() {
        return String.format(
                "CoolingAppliance [Name: %s, Power: %.2f W, Duration: %.2f h, Temp: %d\u00B0C, Energy: %.4f kWh]",
                getApplianceName(), getPowerRatingInWatts(), getUsageDurationInHours(),
                temperatureSetting, calculateEnergyConsumption());
    }
}
