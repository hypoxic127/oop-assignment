package com.sdg.energytracker.service;

import java.util.ArrayList;
import com.sdg.energytracker.entity.Appliance;
import com.sdg.energytracker.entity.CoolingAppliance;
import com.sdg.energytracker.entity.Household;
import com.sdg.energytracker.entity.LightAppliance;

/**
 * ReportService.java
 *
 * This SERVICE class is responsible ONLY for formatting energy reports as
 * text. It was extracted from the Household class so that:
 *
 * - SINGLE RESPONSIBILITY PRINCIPLE (SRP): Household (entity) stores data,
 *   EnergyCalculationService computes values, and ReportService turns those
 *   values into human-readable reports.
 * - COMPOSITION between services: ReportService HAS-A
 *   EnergyCalculationService and delegates all numeric work to it instead
 *   of duplicating the calculation logic.
 *
 * The report methods also demonstrate:
 * - DYNAMIC POLYMORPHISM: calculateEnergyConsumption() is called through
 *   superclass (Appliance) references; the overridden subclass version runs.
 * - RTTI with instanceof + DOWNCASTING: to show type-specific details
 *   (LED flag, temperature setting) in the report.
 *
 * Related to SDG 7: Affordable and Clean Energy - clear reports help users
 * see where their energy (and money) goes.
 *
 * @author Group 1 - MOHAMMAD SHAHNAWAZ (0389395)
 * @version 2.0
 */
public class ReportService {

    // ==================== COMPOSITION (HAS-A Relationship) ====================

    /**
     * The calculation service used for all numeric work (COMPOSITION).
     * ReportService HAS-A EnergyCalculationService - it delegates instead
     * of duplicating calculation logic (code reuse without inheritance).
     */
    private EnergyCalculationService calculationService;

    // ==================== CONSTRUCTORS ====================

    /**
     * Constructs a ReportService with its own EnergyCalculationService.
     */
    public ReportService() {
        this.calculationService = new EnergyCalculationService();
    }

    /**
     * Constructs a ReportService that shares an existing calculation service.
     * DEPENDENCY INJECTION: the collaborating object is passed in from
     * outside, which makes the class easier to test and reuse.
     *
     * @param calculationService the calculation service to delegate to
     * @throws IllegalArgumentException if calculationService is null
     */
    public ReportService(EnergyCalculationService calculationService) {
        if (calculationService == null) {
            throw new IllegalArgumentException("Calculation service cannot be null.");
        }
        this.calculationService = calculationService;
    }

    // ==================== REPORTING METHODS ====================

    /**
     * Generates a complete energy consumption report for a household.
     * Displays each appliance's details and energy usage, followed by
     * the total household energy consumption, average usage, and
     * estimated cost.
     *
     * @param household the household to generate the report for
     * @return a formatted string containing the full energy report
     */
    public String generateEnergyReport(Household household) {
        StringBuilder report = new StringBuilder();

        report.append("\n========== ENERGY CONSUMPTION REPORT ==========\n");
        report.append(String.format("  Household: %s%n", household.getHouseholdName()));
        report.append(String.format("  Total Appliances: %d%n", household.getApplianceCount()));

        if (household.getApplianceCount() == 0) {
            report.append("  No appliances added yet. Please add appliances first.\n");
            report.append("===============================================\n");
            return report.toString();
        }

        // ========== DYNAMIC POLYMORPHISM (Runtime Polymorphism) IN ACTION ==========
        double totalEnergy = 0.0;

        for (int i = 0; i < household.getApplianceCount(); i++) {

            // UPCASTING: Access each element through the SUPERCLASS reference.
            // The declared type is Appliance, but the actual object could be
            // LightAppliance or CoolingAppliance (subclass objects).
            Appliance currentAppliance = household.getAppliance(i);

            // DYNAMIC DISPATCH (Runtime Method Resolution): Calling
            // calculateEnergyConsumption() on the superclass reference. The JVM
            // determines which overridden version to execute based on the
            // ACTUAL runtime type of the object.
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
                // DOWNCASTING: safe because instanceof confirmed the actual type
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

            // Display the calculated energy - result of POLYMORPHIC method call
            report.append(String.format("  Energy Used    : %.4f kWh%n", energy));
        }

        // COMPOSITION: delegate average and cost calculations to the
        // EnergyCalculationService instead of duplicating the formulas here
        double averageEnergy = totalEnergy / household.getApplianceCount();
        double estimatedCost = totalEnergy * EnergyCalculationService.TARIFF_RATE_RM_PER_KWH;

        // Summary - includes total energy, average usage, and estimated cost
        // as required by the functional requirements
        report.append("\n================================================\n");
        report.append("              SUMMARY REPORT\n");
        report.append("------------------------------------------------\n");
        report.append(String.format("  Total Energy Used    : %.4f kWh%n", totalEnergy));
        report.append(String.format("  Average Usage        : %.4f kWh/appliance%n", averageEnergy));
        report.append(String.format("  Estimated Cost       : RM %.2f%n", estimatedCost));
        report.append(String.format("  (Tariff: RM %.3f per kWh \u2014 TNB Domestic Rate)%n",
                EnergyCalculationService.TARIFF_RATE_RM_PER_KWH));
        report.append("================================================\n");
        report.append("  SDG 7 Reminder: Use LED lights and set your\n");
        report.append("  air conditioner to 24\u00B0C or above to save energy!\n");
        report.append("================================================\n");

        return report.toString();
    }

    /**
     * Generates a report of high-energy-consuming appliances.
     * COMPOSITION: the filtering logic is delegated to the
     * EnergyCalculationService; this method only formats the result.
     *
     * @param household    the household to query
     * @param thresholdKWh the energy threshold in kilowatt-hours
     * @return a formatted string listing appliances above the threshold
     */
    public String generateHighEnergyReport(Household household, double thresholdKWh) {
        ArrayList<Appliance> highEnergyList =
                calculationService.getHighEnergyAppliances(household, thresholdKWh);
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
}
