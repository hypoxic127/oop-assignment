package com.sdg.energytracker.service;

import java.util.ArrayList;
import com.sdg.energytracker.entity.Appliance;
import com.sdg.energytracker.entity.EnergyUsageRecord;
import com.sdg.energytracker.entity.Household;

/**
 * EnergyCalculationService.java
 *
 * This SERVICE class contains all the energy calculation (business logic)
 * for the application. It was extracted from the Household class so that:
 *
 * - SINGLE RESPONSIBILITY PRINCIPLE (SRP): Household (entity) stores data,
 *   while EnergyCalculationService performs calculations on that data.
 * - SEPARATION OF CONCERNS: entity = data, service = logic,
 *   controller = user interaction flow, utilities = reusable helpers.
 *
 * *** THIS CLASS IS THE KEY DEMONSTRATION OF DYNAMIC POLYMORPHISM ***
 * Every calculation method iterates through the household's appliances via
 * superclass (Appliance) references and calls calculateEnergyConsumption().
 * The JVM decides AT RUNTIME which overridden version to execute based on
 * the ACTUAL object type (dynamic method dispatch).
 *
 * Related to SDG 7: Affordable and Clean Energy - these calculations help
 * households understand and reduce their energy consumption and cost.
 *
 * @author Group 1 - WEN SIHAO (0379856)
 * @version 2.0
 */
public class EnergyCalculationService {

    // ==================== CONSTANTS ====================

    /**
     * Malaysia TNB domestic electricity tariff rate in RM per kWh.
     * Based on the standard tariff for the first 200 kWh block.
     * This constant is used to calculate estimated electricity costs,
     * helping users understand the financial impact of their energy usage
     * in support of SDG 7: Affordable and Clean Energy.
     */
    public static final double TARIFF_RATE_RM_PER_KWH = 0.218;

    // ==================== ENERGY CALCULATION METHODS ====================

    /**
     * Calculates the total energy consumption of ALL appliances in a household.
     *
     * *** DYNAMIC POLYMORPHISM (Runtime Polymorphism) - CORE DEMONSTRATION ***
     *
     * Step 1 (UPCASTING): Every appliance is accessed through a SUPERCLASS
     * reference (Appliance). The actual objects stored could be Appliance,
     * LightAppliance, or CoolingAppliance - all stored via upcasting.
     *
     * Step 2 (DYNAMIC DISPATCH): When we call calculateEnergyConsumption()
     * on the superclass reference, the JVM does NOT simply call Appliance's
     * version. Instead, at RUNTIME, it checks the ACTUAL TYPE of the object
     * and dispatches to the correct overridden method:
     *   - Appliance object       -> Appliance.calculateEnergyConsumption()
     *   - LightAppliance object  -> LightAppliance version (LED discount)
     *   - CoolingAppliance object-> CoolingAppliance version (temp penalty)
     *
     * This is the essence of RUNTIME POLYMORPHISM: same method call,
     * different behaviour depending on the actual object type.
     *
     * @param household the household whose appliances are summed (COMPOSITION)
     * @return the total energy consumption in kilowatt-hours (kWh)
     */
    public double calculateTotalEnergy(Household household) {
        double totalEnergy = 0.0;

        for (int i = 0; i < household.getApplianceCount(); i++) {

            // UPCASTING: 'currentAppliance' is the superclass reference (Appliance),
            // but the actual object could be any subclass (LightAppliance,
            // CoolingAppliance).
            Appliance currentAppliance = household.getAppliance(i);

            // DYNAMIC DISPATCH (Runtime Method Resolution): The JVM resolves which
            // version of calculateEnergyConsumption() to call based on the ACTUAL
            // runtime type of 'currentAppliance', NOT the declared type (Appliance).
            // This decision happens at RUNTIME, not at compile time.
            double energy = currentAppliance.calculateEnergyConsumption();

            totalEnergy = totalEnergy + energy;
        }

        return totalEnergy;
    }

    /**
     * Calculates the average energy consumption per appliance in a household.
     *
     * Formula: Average Energy = Total Energy / Number of Appliances
     *
     * If the household has no appliances, returns 0.0 to avoid division by zero.
     *
     * @param household the household to calculate the average for
     * @return the average energy consumption per appliance in kilowatt-hours (kWh)
     */
    public double calculateAverageEnergy(Household household) {
        if (household.getApplianceCount() == 0) {
            return 0.0;
        }
        return calculateTotalEnergy(household) / household.getApplianceCount();
    }

    /**
     * Calculates the estimated electricity cost for a household in Malaysian
     * Ringgit (RM) based on the total energy consumption and the TNB domestic
     * tariff rate.
     *
     * Formula: Estimated Cost (RM) = Total Energy (kWh) x Tariff Rate (RM/kWh)
     *
     * The tariff rate used is RM 0.218 per kWh, which is the standard TNB
     * domestic tariff for the first 200 kWh consumption block.
     *
     * This method supports SDG 7: Affordable and Clean Energy by helping
     * households understand the financial impact of their energy consumption.
     *
     * @param household the household to estimate the cost for
     * @return the estimated electricity cost in Malaysian Ringgit (RM)
     */
    public double calculateEstimatedCost(Household household) {
        return calculateTotalEnergy(household) * TARIFF_RATE_RM_PER_KWH;
    }

    /**
     * Finds and returns a list of appliances whose energy consumption exceeds
     * the specified threshold. This helps identify high-energy-consuming
     * appliances for potential energy-saving improvements (SDG 7).
     *
     * Uses DYNAMIC POLYMORPHISM: calculateEnergyConsumption() is called on
     * each Appliance reference, and the correct overridden version executes
     * based on the actual runtime type.
     *
     * @param household    the household to search
     * @param thresholdKWh the energy threshold in kilowatt-hours
     * @return an ArrayList of appliances exceeding the threshold
     */
    public ArrayList<Appliance> getHighEnergyAppliances(Household household, double thresholdKWh) {
        ArrayList<Appliance> highEnergyList = new ArrayList<Appliance>();

        for (int i = 0; i < household.getApplianceCount(); i++) {
            // UPCASTING: superclass reference pointing to subclass object
            Appliance currentAppliance = household.getAppliance(i);

            // DYNAMIC POLYMORPHISM: calculateEnergyConsumption() is resolved
            // at RUNTIME based on the actual type of the object (dynamic dispatch).
            if (currentAppliance.calculateEnergyConsumption() > thresholdKWh) {
                highEnergyList.add(currentAppliance);
            }
        }

        return highEnergyList;
    }

    /**
     * Records energy usage for all appliances in a household and generates
     * EnergyUsageRecord objects. Each record captures the appliance and its
     * calculated energy consumption. Clears any previously generated records
     * before creating new ones.
     *
     * @param household the household whose appliance usage is recorded
     */
    public void recordAllEnergyUsage(Household household) {
        // Clear previous records to avoid duplicates
        household.getRecords().clear();

        for (int i = 0; i < household.getApplianceCount(); i++) {
            Appliance currentAppliance = household.getAppliance(i);
            // POLYMORPHISM: EnergyUsageRecord internally calls the correct
            // overridden calculateEnergyConsumption() based on actual type
            EnergyUsageRecord record = new EnergyUsageRecord(currentAppliance);
            household.getRecords().add(record);
        }
    }
}
