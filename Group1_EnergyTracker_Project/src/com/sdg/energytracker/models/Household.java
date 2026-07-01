package com.sdg.energytracker.models;

import java.util.ArrayList;

/**
 * Household.java
 * 
 * This class demonstrates the OOP principle of COMPOSITION (HAS-A
 * relationship).
 * A Household HAS-A list of Appliances. Unlike INHERITANCE (IS-A), composition
 * models a "whole-part" relationship: a household is composed of multiple
 * appliances.
 * 
 * By encapsulating the ArrayList<Appliance> inside this class, we achieve:
 * - ENCAPSULATION: The internal list is private; external code can only
 * interact
 * with it through well-defined public methods.
 * - SEPARATION OF CONCERNS: All appliance management and energy calculation
 * logic is centralised here, keeping the main application class
 * (EnergyTrackerApp)
 * focused purely on user interaction (Scanner / menu).
 * - POLYMORPHISM: When iterating through the appliance list to calculate total
 * energy, the correct overridden calculateEnergyConsumption() method is called
 * for each appliance based on its actual runtime type (dynamic dispatch).
 * 
 * This design follows the Single Responsibility Principle (SRP):
 * - Household is responsible for appliance management and energy calculations.
 * - EnergyTrackerApp is responsible for user interface and input/output.
 * 
 * Related to SDG 7: Affordable and Clean Energy — this class helps track
 * and analyse household energy consumption patterns.
 * 
 * @author Group Work
 * @version 1.0
 */
public class Household {

    // ==================== COMPOSITION (HAS-A Relationship) ====================
    // A Household HAS-A list of Appliances.
    // This is COMPOSITION — the Household "owns" its appliances.
    // The ArrayList is declared with the superclass type 'Appliance', allowing
    // it to hold ANY subclass object (LightAppliance, CoolingAppliance, etc.).

    /** The name of the household (e.g., "Smith Family Home"). */
    private String householdName;

    /** The list of appliances in this household (COMPOSITION). */
    private ArrayList<Appliance> appliances;

    /** The list of energy usage records generated for this household. */
    private ArrayList<EnergyUsageRecord> records;

    // ==================== CONSTANTS ====================

    /**
     * Malaysia TNB domestic electricity tariff rate in RM per kWh.
     * Based on the standard tariff for the first 200 kWh block.
     * This constant is used to calculate estimated electricity costs,
     * helping users understand the financial impact of their energy usage
     * in support of SDG 7: Affordable and Clean Energy.
     */
    private static final double TARIFF_RATE_RM_PER_KWH = 0.218;

    // ==================== CONSTRUCTORS ====================

    /**
     * Default (no-argument) constructor.
     * Initialises the household with a default name and empty lists.
     */
    public Household() {
        this.householdName = "My Household";
        this.appliances = new ArrayList<Appliance>();
        this.records = new ArrayList<EnergyUsageRecord>();
    }

    /**
     * Parameterised constructor.
     * Creates a household with the specified name and empty appliance/record lists.
     *
     * @param householdName the name of the household
     * @throws IllegalArgumentException if householdName is null or empty
     */
    public Household(String householdName) {
        // ENCAPSULATION: Using setter method for validation instead of
        // duplicating the validation logic — ensures consistency.
        setHouseholdName(householdName);
        this.appliances = new ArrayList<Appliance>();
        this.records = new ArrayList<EnergyUsageRecord>();
    }

    // ==================== GETTERS & SETTERS ====================

    /**
     * Gets the name of the household.
     *
     * @return the household name
     */
    public String getHouseholdName() {
        return householdName;
    }

    /**
     * Sets the name of the household.
     *
     * @param householdName the household name to set
     * @throws IllegalArgumentException if householdName is null or empty
     */
    public void setHouseholdName(String householdName) {
        if (householdName == null || householdName.trim().isEmpty()) {
            throw new IllegalArgumentException("Household name cannot be null or empty.");
        }
        this.householdName = householdName.trim();
    }

    /**
     * Gets the number of appliances currently in the household.
     *
     * @return the number of appliances
     */
    public int getApplianceCount() {
        return appliances.size();
    }

    /**
     * Gets the list of energy usage records.
     *
     * @return the ArrayList of EnergyUsageRecord objects
     */
    public ArrayList<EnergyUsageRecord> getRecords() {
        return records;
    }

    // ==================== APPLIANCE MANAGEMENT METHODS ====================

    /**
     * Adds an appliance to the household.
     * The parameter type is 'Appliance' (the superclass), so ANY subclass
     * object can be passed in — this is POLYMORPHISM through upcasting.
     *
     * @param appliance the appliance to add (can be any Appliance subclass)
     * @throws IllegalArgumentException if appliance is null
     */
    public void addAppliance(Appliance appliance) {
        if (appliance == null) {
            throw new IllegalArgumentException("Cannot add a null appliance.");
        }
        appliances.add(appliance);
    }

    /**
     * Gets an appliance at the specified index.
     *
     * @param index the index of the appliance (0-based)
     * @return the Appliance at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Appliance getAppliance(int index) {
        if (index < 0 || index >= appliances.size()) {
            throw new IndexOutOfBoundsException("Invalid appliance index: " + index);
        }
        return appliances.get(index);
    }

    // ==================== ENERGY CALCULATION METHODS ====================

    /**
     * Calculates the total energy consumption of ALL appliances in the household.
     * 
     * *** THIS METHOD IS THE KEY DEMONSTRATION OF DYNAMIC POLYMORPHISM ***
     * 
     * We iterate through the ArrayList<Appliance> using a basic for loop.
     * Each element is accessed through an 'Appliance' reference (the superclass).
     * When we call calculateEnergyConsumption() on each reference, the JVM
     * determines the ACTUAL type of the object at runtime and calls the
     * correct overridden version:
     * - If the object is a LightAppliance → LightAppliance's version (LED discount)
     * - If the object is a CoolingAppliance → CoolingAppliance's version (temp
     * penalty)
     * - If the object is a plain Appliance → the base version
     * 
     * This is DYNAMIC METHOD DISPATCH — the method that gets executed is
     * decided at runtime, not at compile time.
     *
     * @return the total energy consumption in kilowatt-hours (kWh)
     */
    public double calculateTotalEnergy() {
        double totalEnergy = 0.0;

        // =====================================================================
        // *** DYNAMIC POLYMORPHISM (Runtime Polymorphism) - CORE DEMONSTRATION ***
        // =====================================================================
        // Step 1 (UPCASTING): The ArrayList is declared as ArrayList<Appliance>,
        // so every element is accessed through a SUPERCLASS reference.
        // The actual objects stored could be Appliance, LightAppliance,
        // or CoolingAppliance — all stored via upcasting.
        //
        // Step 2 (DYNAMIC DISPATCH): When we call calculateEnergyConsumption()
        // on the superclass reference 'currentAppliance', the JVM does
        // NOT simply call Appliance's version. Instead, at RUNTIME, it
        // checks the ACTUAL TYPE of the object and dispatches to the
        // correct overridden method:
        // - Appliance object → Appliance.calculateEnergyConsumption()
        // - LightAppliance object → LightAppliance.calculateEnergyConsumption() (LED
        // discount)
        // - CoolingAppliance object → CoolingAppliance.calculateEnergyConsumption()
        // (temp penalty)
        //
        // This is the essence of RUNTIME POLYMORPHISM: same method call,
        // different behaviour depending on the actual object type.
        // =====================================================================
        for (int i = 0; i < appliances.size(); i++) {

            // UPCASTING: 'currentAppliance' is the superclass reference (Appliance),
            // but the actual object could be any subclass (LightAppliance,
            // CoolingAppliance).
            // This is equivalent to: Appliance currentAppliance = new LightAppliance(...);
            Appliance currentAppliance = appliances.get(i);

            // DYNAMIC DISPATCH (Runtime Method Resolution): The JVM resolves which version
            // of
            // calculateEnergyConsumption() to call based on the ACTUAL runtime
            // type of 'currentAppliance', NOT the declared type (Appliance).
            // This decision happens at RUNTIME, not at compile time.
            double energy = currentAppliance.calculateEnergyConsumption();

            totalEnergy = totalEnergy + energy;
        }

        return totalEnergy;
    }

    /**
     * Calculates the average energy consumption per appliance in the household.
     *
     * Formula: Average Energy = Total Energy / Number of Appliances
     *
     * If the household has no appliances, returns 0.0 to avoid division by zero.
     *
     * @return the average energy consumption per appliance in kilowatt-hours (kWh)
     */
    public double calculateAverageEnergy() {
        if (appliances.size() == 0) {
            return 0.0;
        }
        return calculateTotalEnergy() / appliances.size();
    }

    /**
     * Calculates the estimated electricity cost for the household in Malaysian
     * Ringgit (RM) based on the total energy consumption and the TNB domestic
     * tariff rate.
     *
     * Formula: Estimated Cost (RM) = Total Energy (kWh) × Tariff Rate (RM/kWh)
     *
     * The tariff rate used is RM 0.218 per kWh, which is the standard TNB
     * domestic tariff for the first 200 kWh consumption block.
     *
     * This method supports SDG 7: Affordable and Clean Energy by helping
     * households understand the financial impact of their energy consumption.
     *
     * @return the estimated electricity cost in Malaysian Ringgit (RM)
     */
    public double calculateEstimatedCost() {
        return calculateTotalEnergy() * TARIFF_RATE_RM_PER_KWH;
    }

    /**
     * Records energy usage for all appliances and generates EnergyUsageRecord
     * objects.
     * Each record captures the appliance and its calculated energy consumption.
     * Clears any previously generated records before creating new ones.
     */
    public void recordAllEnergyUsage() {
        // Clear previous records to avoid duplicates
        records.clear();

        for (int i = 0; i < appliances.size(); i++) {
            Appliance currentAppliance = appliances.get(i);
            // POLYMORPHISM: EnergyUsageRecord internally calls the correct
            // overridden calculateEnergyConsumption() based on actual type
            EnergyUsageRecord record = new EnergyUsageRecord(currentAppliance);
            records.add(record);
        }
    }

    // ==================== REPORTING METHODS ====================

    /**
     * Finds and returns a list of appliances whose energy consumption exceeds
     * the specified threshold. This helps identify high-energy-consuming
     * appliances for potential energy-saving improvements (SDG 7).
     * 
     * Uses DYNAMIC POLYMORPHISM: calculateEnergyConsumption() is called on
     * each Appliance reference, and the correct overridden version executes
     * based on the actual runtime type.
     *
     * @param thresholdKWh the energy threshold in kilowatt-hours
     * @return an ArrayList of appliances exceeding the threshold
     */
    public ArrayList<Appliance> getHighEnergyAppliances(double thresholdKWh) {
        ArrayList<Appliance> highEnergyList = new ArrayList<Appliance>();

        for (int i = 0; i < appliances.size(); i++) {
            // UPCASTING: superclass reference pointing to subclass object
            Appliance currentAppliance = appliances.get(i);

            // DYNAMIC POLYMORPHISM: calculateEnergyConsumption() is resolved
            // at RUNTIME based on the actual type of the object (dynamic dispatch).
            if (currentAppliance.calculateEnergyConsumption() > thresholdKWh) {
                highEnergyList.add(currentAppliance);
            }
        }

        return highEnergyList;
    }

    /**
     * Generates a complete energy consumption report for the household.
     * Displays each appliance's details and energy usage, followed by
     * the total household energy consumption.
     *
     * @return a formatted string containing the full energy report
     */
    public String generateEnergyReport() {
        StringBuilder report = new StringBuilder();

        report.append("\n========== ENERGY CONSUMPTION REPORT ==========\n");
        report.append(String.format("  Household: %s%n", householdName));
        report.append(String.format("  Total Appliances: %d%n", appliances.size()));

        if (appliances.size() == 0) {
            report.append("  No appliances added yet. Please add appliances first.\n");
            report.append("===============================================\n");
            return report.toString();
        }

        // ==================== DYNAMIC POLYMORPHISM (Runtime Polymorphism) IN ACTION
        // ====================
        double totalEnergy = 0.0;

        for (int i = 0; i < appliances.size(); i++) {

            // UPCASTING: Access each element through the SUPERCLASS reference.
            // The declared type is Appliance, but the actual object could be
            // LightAppliance or CoolingAppliance (subclass objects).
            Appliance currentAppliance = appliances.get(i);

            // DYNAMIC DISPATCH (Runtime Method Resolution): Calling
            // calculateEnergyConsumption()
            // on the superclass reference. The JVM determines which overridden
            // version to execute based on the ACTUAL runtime type of the object.
            double energy = currentAppliance.calculateEnergyConsumption();
            totalEnergy = totalEnergy + energy;

            // Display individual appliance information
            report.append(String.format("%n  --- Appliance #%d ---%n", (i + 1)));
            report.append(String.format("  Name           : %s%n", currentAppliance.getApplianceName()));
            report.append(String.format("  Power Rating   : %.2f W%n", currentAppliance.getPowerRatingInWatts()));
            report.append(String.format("  Usage Duration : %.2f h%n", currentAppliance.getUsageDurationInHours()));

            // Use instanceof to display type-specific information
            // This demonstrates that the actual type is preserved at runtime
            if (currentAppliance instanceof LightAppliance) {
                LightAppliance light = (LightAppliance) currentAppliance;
                report.append(String.format("  Type           : Light Appliance%n"));
                report.append(String.format("  LED            : %s%n",
                        light.getIsLED() ? "Yes (20% energy saving applied)" : "No"));
            } else if (currentAppliance instanceof CoolingAppliance) {
                CoolingAppliance cooling = (CoolingAppliance) currentAppliance;
                report.append(String.format("  Type           : Cooling Appliance%n"));
                report.append(String.format("  Temperature    : %d \u00B0C%s%n",
                        cooling.getTemperatureSetting(),
                        cooling.getTemperatureSetting() < 24 ? " (20% energy penalty applied)" : ""));
            } else {
                report.append(String.format("  Type           : General Appliance%n"));
            }

            // Display the calculated energy — result of POLYMORPHIC method call
            report.append(String.format("  Energy Used    : %.4f kWh%n", energy));
        }

        // Calculate average usage and estimated cost for the summary
        double averageEnergy = totalEnergy / appliances.size();
        double estimatedCost = totalEnergy * TARIFF_RATE_RM_PER_KWH;

        // Summary — includes total energy, average usage, and estimated cost
        // as required by the functional requirements
        report.append("\n================================================\n");
        report.append("              SUMMARY REPORT\n");
        report.append("------------------------------------------------\n");
        report.append(String.format("  Total Energy Used    : %.4f kWh%n", totalEnergy));
        report.append(String.format("  Average Usage        : %.4f kWh/appliance%n", averageEnergy));
        report.append(String.format("  Estimated Cost       : RM %.2f%n", estimatedCost));
        report.append(String.format("  (Tariff: RM %.3f per kWh \u2014 TNB Domestic Rate)%n", TARIFF_RATE_RM_PER_KWH));
        report.append("================================================\n");
        report.append("  SDG 7 Reminder: Use LED lights and set your\n");
        report.append("  air conditioner to 24\u00B0C or above to save energy!\n");
        report.append("================================================\n");

        return report.toString();
    }

    /**
     * Generates a report of high-energy-consuming appliances.
     *
     * @param thresholdKWh the energy threshold in kilowatt-hours
     * @return a formatted string listing appliances above the threshold
     */
    public String generateHighEnergyReport(double thresholdKWh) {
        ArrayList<Appliance> highEnergyList = getHighEnergyAppliances(thresholdKWh);
        StringBuilder report = new StringBuilder();

        report.append(String.format("%n========== HIGH ENERGY APPLIANCES (> %.4f kWh) ==========%n", thresholdKWh));

        if (highEnergyList.size() == 0) {
            report.append("  No appliances exceed the energy threshold. Great job!\n");
        } else {
            report.append(String.format("  Found %d appliance(s) exceeding the threshold:%n", highEnergyList.size()));
            for (int i = 0; i < highEnergyList.size(); i++) {
                Appliance appliance = highEnergyList.get(i);
                report.append(String.format("  %d. %s \u2014 %.4f kWh%n",
                        (i + 1), appliance.getApplianceName(), appliance.calculateEnergyConsumption()));
            }
            report.append("  Consider replacing these with energy-efficient alternatives!\n");
        }

        report.append("==========================================================\n");
        return report.toString();
    }

    /**
     * Returns a string representation of the Household.
     *
     * @return a formatted string containing the household summary
     */
    @Override
    public String toString() {
        return String.format("Household [Name: %s, Appliances: %d, Total Energy: %.4f kWh]",
                householdName, appliances.size(), calculateTotalEnergy());
    }
}
