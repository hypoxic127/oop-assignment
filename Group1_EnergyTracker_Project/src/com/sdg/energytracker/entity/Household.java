package com.sdg.energytracker.entity;

import java.util.ArrayList;

/**
 * Household.java
 *
 * This ENTITY class demonstrates the OOP principle of COMPOSITION
 * (HAS-A relationship). A Household HAS-A list of Appliances. Unlike
 * INHERITANCE (IS-A), composition models a "whole-part" relationship:
 * a household is composed of multiple appliances.
 *
 * By encapsulating the ArrayList&lt;Appliance&gt; inside this class, we achieve:
 * - ENCAPSULATION: The internal lists are private; external code can only
 *   interact with them through well-defined public methods.
 * - SINGLE RESPONSIBILITY PRINCIPLE (SRP): After refactoring, this class is
 *   responsible ONLY for storing and managing household data (name,
 *   appliances, records). All calculations were moved to
 *   EnergyCalculationService and all report formatting to ReportService
 *   in the 'service' package.
 * - POLYMORPHISM (via UPCASTING): The ArrayList is declared with the
 *   superclass type 'Appliance', allowing it to hold ANY subclass object
 *   (LightAppliance, CoolingAppliance, etc.).
 *
 * Related to SDG 7: Affordable and Clean Energy - this class stores the
 * data needed to track and analyse household energy consumption patterns.
 *
 * @author Group 1 - Su Tingwei (0384175)
 * @version 2.0
 */
public class Household {

    // ==================== COMPOSITION (HAS-A Relationship) ====================
    // A Household HAS-A list of Appliances.
    // This is COMPOSITION - the Household "owns" its appliances.
    // The ArrayList is declared with the superclass type 'Appliance', allowing
    // it to hold ANY subclass object (LightAppliance, CoolingAppliance, etc.).

    /** The name of the household (e.g., "Smith Family Home"). */
    private String householdName;

    /** The list of appliances in this household (COMPOSITION). */
    private ArrayList<Appliance> appliances;

    /** The list of energy usage records generated for this household. */
    private ArrayList<EnergyUsageRecord> records;

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
        // duplicating the validation logic - ensures consistency.
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
     * object can be passed in - this is POLYMORPHISM through upcasting.
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
     * ENCAPSULATION: external classes (e.g., the services) access the
     * private appliance list ONLY through this controlled method.
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

    /**
     * Returns a string representation of the Household.
     *
     * @return a formatted string containing the household summary
     */
    @Override
    public String toString() {
        return String.format("Household [Name: %s, Appliances: %d]",
                householdName, appliances.size());
    }
}
