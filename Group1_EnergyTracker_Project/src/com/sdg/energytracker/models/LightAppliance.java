package com.sdg.energytracker.models;

/**
 * LightAppliance.java
 * 
 * This class demonstrates INHERITANCE by extending the Appliance superclass.
 * It inherits all attributes and methods from Appliance, and adds a new
 * attribute 'isLED' specific to lighting appliances.
 * 
 * It also demonstrates POLYMORPHISM by overriding the calculateEnergyConsumption()
 * method. When the same method is called on a LightAppliance object (even through
 * an Appliance reference), the overridden version executes — this is runtime
 * polymorphism (dynamic method dispatch).
 * 
 * LED lights consume approximately 80% of the energy compared to traditional
 * lights, supporting SDG 7 by promoting energy-efficient lighting solutions.
 * 
 * @author Group Work
 * @version 1.0
 */
public class LightAppliance extends Appliance {
    // 'extends Appliance' is the keyword for INHERITANCE.
    // LightAppliance IS-A Appliance, so it inherits applianceName,
    // powerRatingInWatts, usageDurationInHours, and their getters/setters.

    // ==================== ENCAPSULATION ====================
    // Additional private attribute specific to LightAppliance.

    /** Indicates whether the light appliance uses LED technology (true = LED, false = non-LED). */
    private boolean isLED;

    // ==================== CONSTRUCTORS ====================

    /**
     * Default (no-argument) constructor.
     * Calls the parent class default constructor using super(),
     * then initialises the LED attribute to false.
     */
    public LightAppliance() {
        super(); // Calls Appliance() default constructor
        this.isLED = false;
    }

    /**
     * Parameterised constructor.
     * Uses super() to pass common attributes to the Appliance constructor,
     * demonstrating how INHERITANCE reuses the parent's initialisation logic.
     *
     * @param applianceName        the name of the light appliance
     * @param powerRatingInWatts   the power rating in watts (must be >= 0)
     * @param usageDurationInHours the usage duration in hours (must be >= 0)
     * @param isLED                true if the light uses LED technology
     */
    public LightAppliance(String applianceName, double powerRatingInWatts,
                          double usageDurationInHours, boolean isLED) {
        super(applianceName, powerRatingInWatts, usageDurationInHours);
        // super() delegates validation to the parent class setters
        this.isLED = isLED;
    }

    // ==================== GETTER & SETTER ====================

    /**
     * Checks whether this light appliance uses LED technology.
     *
     * @return true if the appliance is LED, false otherwise
     */
    public boolean getIsLED() {
        return isLED;
    }

    /**
     * Sets whether this light appliance uses LED technology.
     *
     * @param isLED true for LED, false for non-LED
     */
    public void setIsLED(boolean isLED) {
        this.isLED = isLED;
    }

    // ==================== POLYMORPHISM (Method Overriding) ====================

    /**
     * Overrides the parent class method to provide specialised energy calculation
     * for lighting appliances. This is an example of POLYMORPHISM.
     * 
     * If the light is LED, energy consumption is reduced by 20% (multiplied by 0.8),
     * simulating the real-world energy savings of LED technology.
     * 
     * The @Override annotation ensures the compiler checks that this method
     * correctly overrides a method in the superclass.
     *
     * @return the energy consumption in kilowatt-hours (kWh),
     *         with a 20% discount applied if the light is LED
     */
    @Override
    public double calculateEnergyConsumption() {
        // First, get the base energy consumption from the parent class
        double baseEnergy = super.calculateEnergyConsumption();

        if (isLED) {
            // LED lights are more efficient — apply a 20% energy saving (multiply by 0.8)
            return baseEnergy * 0.8;
        } else {
            // Non-LED lights use the standard energy consumption
            return baseEnergy;
        }
    }

    /**
     * Returns a string representation of the LightAppliance.
     * Extends the parent's toString() with LED-specific information.
     *
     * @return a formatted string containing the light appliance details
     */
    @Override
    public String toString() {
        return String.format(
                "LightAppliance [Name: %s, Power: %.2f W, Duration: %.2f h, LED: %s, Energy: %.4f kWh]",
                getApplianceName(), getPowerRatingInWatts(), getUsageDurationInHours(),
                isLED ? "Yes" : "No", calculateEnergyConsumption());
    }
}
