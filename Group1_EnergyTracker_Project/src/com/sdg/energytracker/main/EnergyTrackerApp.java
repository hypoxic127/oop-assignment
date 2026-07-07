package com.sdg.energytracker.main;

import com.sdg.energytracker.controller.EnergyTrackerController;
import com.sdg.energytracker.entity.Household;
import com.sdg.energytracker.service.EnergyCalculationService;
import com.sdg.energytracker.service.ReportService;
import com.sdg.energytracker.utilities.InputReader;

/**
 * EnergyTrackerApp.java
 *
 * Main application class (entry point) for the Household Energy
 * Consumption Tracker. The main() method creates one object from each
 * layer of the architecture, wires them together, and hands control to
 * the controller's menu loop:
 *
 *   main       (EnergyTrackerApp)              - program entry point
 *   controller (EnergyTrackerController)       - menu flow / dispatching
 *   service    (EnergyCalculationService,
 *               ReportService)                 - business logic
 *   entity     (Household, Appliance,
 *               LightAppliance,
 *               CoolingAppliance,
 *               EnergyUsageRecord)             - data model
 *   utilities  (InputReader, ConsoleUI)        - reusable I/O helpers
 *
 * This layered architecture demonstrates SEPARATION OF CONCERNS and the
 * Single Responsibility Principle (SRP): each class has exactly one job,
 * which also makes it easy to see each group member's contribution.
 *
 * Related to SDG 7: Affordable and Clean Energy - this tracker helps
 * households monitor and reduce their energy consumption.
 *
 * @author Group 1 - To Tsun Kai (0373689)
 * @version 3.0
 */
public class EnergyTrackerApp {

    /**
     * The main entry point of the application.
     *
     * As required, main() CREATES OBJECTS FROM DIFFERENT CLASSES (entity,
     * utility, service, and controller layers) and wires them together
     * using CONSTRUCTOR INJECTION - a form of COMPOSITION where the
     * collaborators are supplied from outside.
     *
     * The program run that follows demonstrates, at runtime:
     *   - Object creation        (appliance objects built from user input)
     *   - Getter/setter usage    (validated setters in the entity classes)
     *   - INHERITANCE            (LightAppliance / CoolingAppliance extend Appliance)
     *   - POLYMORPHIC BEHAVIOUR  (calculateEnergyConsumption() resolved by
     *                             the ACTUAL object type via dynamic dispatch)
     * and displays clear output showing total energy usage and insights.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        // ==================== OBJECT CREATION ====================
        // main() creates one object from each layer of the application.

        // 1. ENTITY layer: the household (Model) that will own the appliances.
        Household household = new Household("My Home");

        // 2. UTILITIES layer: robust, validated console input (wraps Scanner).
        InputReader inputReader = new InputReader();

        // 3. SERVICE layer: business logic for all energy calculations.
        EnergyCalculationService calculationService = new EnergyCalculationService();

        // 4. SERVICE layer: report formatting. DEPENDENCY INJECTION - the
        //    report service SHARES the same calculation service instance
        //    instead of creating a duplicate of its own.
        ReportService reportService = new ReportService(calculationService);

        // 5. CONTROLLER layer: receives all collaborators through its
        //    constructor (COMPOSITION via constructor injection).
        EnergyTrackerController controller = new EnergyTrackerController(
                household, inputReader, calculationService, reportService);

        // ==================== PROGRAM EXECUTION ====================
        // Hand over control to the menu loop. Everything the user does from
        // here (adding appliances, viewing reports) exercises the getters/
        // setters, inheritance hierarchy, and polymorphic method dispatch
        // described in the class documentation.
        controller.run();
    }
}
