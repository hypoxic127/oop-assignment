package com.sdg.energytracker.controller;

import com.sdg.energytracker.entity.Appliance;
import com.sdg.energytracker.entity.CoolingAppliance;
import com.sdg.energytracker.entity.Household;
import com.sdg.energytracker.entity.LightAppliance;
import com.sdg.energytracker.service.EnergyCalculationService;
import com.sdg.energytracker.service.ReportService;
import com.sdg.energytracker.utilities.ConsoleUI;
import com.sdg.energytracker.utilities.InputReader;

/**
 * EnergyTrackerController.java
 *
 * This CONTROLLER class coordinates the whole application flow: it shows
 * the menu, reads the user's choice, and dispatches to the correct handler.
 * It connects the three layers of the architecture:
 *
 *   entity    (Household, Appliance...)        - the data
 *   service   (EnergyCalculationService,
 *              ReportService)                  - the business logic
 *   utilities (InputReader, ConsoleUI)         - reusable input/output helpers
 *
 * This layered design demonstrates SEPARATION OF CONCERNS and mirrors the
 * classic MVC (Model-View-Controller) pattern.
 *
 * It is also the clearest example of COMPOSITION in the project: the
 * controller HAS-A Household, HAS-A InputReader, HAS-A
 * EnergyCalculationService, and HAS-A ReportService, and delegates every
 * task to the appropriate collaborator.
 *
 * ROBUSTNESS: the menu loop is protected by try-catch, and every input
 * goes through the validated InputReader methods, so the program never
 * crashes on invalid user input.
 *
 * @author Group 1 - Su Tingwei (0384175)
 * @version 2.0
 */
public class EnergyTrackerController {

    // ==================== COMPOSITION (HAS-A Relationships) ====================
    // The controller owns its collaborators and delegates work to them.

    /** The household whose appliances are being tracked (Model). */
    private Household household;

    /** Robust, validated console input (utility). */
    private InputReader inputReader;

    /** Business logic for all energy calculations (service). */
    private EnergyCalculationService calculationService;

    /** Business logic for report formatting (service). */
    private ReportService reportService;

    // ==================== CONSTRUCTORS ====================

    /**
     * Default constructor: the controller creates and owns all its parts.
     * COMPOSITION: when the controller dies, its parts die with it
     * (a strong HAS-A relationship).
     */
    public EnergyTrackerController() {
        this.household = new Household("My Home");
        this.inputReader = new InputReader();
        this.calculationService = new EnergyCalculationService();
        // DEPENDENCY INJECTION: the report service shares the same
        // calculation service instead of creating a duplicate
        this.reportService = new ReportService(calculationService);
    }

    /**
     * Parameterised constructor: all collaborators are supplied from
     * outside (CONSTRUCTOR INJECTION). This is the constructor used by
     * EnergyTrackerApp.main(), so that the main() method itself creates
     * objects from the different classes of the application.
     *
     * @param household          the household to track (entity)
     * @param inputReader        validated console input (utility)
     * @param calculationService energy calculation logic (service)
     * @param reportService      report formatting logic (service)
     * @throws IllegalArgumentException if any collaborator is null
     */
    public EnergyTrackerController(Household household, InputReader inputReader,
            EnergyCalculationService calculationService, ReportService reportService) {
        if (household == null || inputReader == null
                || calculationService == null || reportService == null) {
            throw new IllegalArgumentException("Controller collaborators cannot be null.");
        }
        this.household = household;
        this.inputReader = inputReader;
        this.calculationService = calculationService;
        this.reportService = reportService;
    }

    // ==================== MAIN APPLICATION LOOP ====================

    /**
     * Runs the menu-driven application until the user chooses to exit.
     *
     * ROBUSTNESS: The entire menu loop is protected by try-catch, and
     * each input uses the validated InputReader methods that retry on
     * invalid input instead of crashing. The finally block guarantees
     * the Scanner resource is released no matter how the loop ends.
     */
    public void run() {

        // Variable to store the user's menu choice
        int choice = 0;

        // ==================== WELCOME BANNER ====================
        ConsoleUI.printWelcomeBanner();

        // ==================== MAIN MENU LOOP ====================
        // The program runs in a loop until the user chooses to exit.
        // The outer try-catch ensures NO uncaught exception crashes the program.
        try {
            while (choice != 4) {

                // Display the menu options
                ConsoleUI.printMenu();

                // Read user input with ROBUST validation
                // readInt() uses try-catch internally and loops until valid
                choice = inputReader.readInt("Please enter your choice (1-4): ", 1, 4);

                // ==================== MENU HANDLING ====================
                if (choice == 1) {
                    // ========== OPTION 1: Add an Appliance ==========
                    handleAddAppliance();

                } else if (choice == 2) {
                    // ========== OPTION 2: Record & View Total Energy ==========
                    handleEnergyReport();

                } else if (choice == 3) {
                    // ========== OPTION 3: View High Energy Appliances ==========
                    handleHighEnergyReport();

                } else if (choice == 4) {
                    // ========== OPTION 4: Exit ==========
                    ConsoleUI.printExitBanner();
                }
            }
        } catch (Exception e) {
            // ROBUSTNESS: Catch any unexpected runtime exception to prevent crash
            ConsoleUI.printUnexpectedError(e.getMessage());
        } finally {
            // Close the input reader to release system resources (always executed)
            inputReader.close();
        }
    }

    // =====================================================================
    //                       MENU HANDLER METHODS
    // =====================================================================
    // These methods handle ONLY the user interaction flow.
    // Business logic is delegated to the services (COMPOSITION), and
    // input/output details are delegated to the utilities.
    // =====================================================================

    /**
     * Handles user interaction for adding a new appliance.
     * Collects user input via InputReader, creates the appropriate Appliance
     * subclass object, and delegates storage to the Household object.
     *
     * This method demonstrates:
     *   - SEPARATION OF CONCERNS: handles ONLY the interaction flow.
     *   - UPCASTING: subclass objects assigned to superclass reference.
     *   - COMPOSITION: storage is delegated to Household.addAppliance().
     *   - ROBUSTNESS: all inputs validated via the InputReader methods.
     */
    private void handleAddAppliance() {

        System.out.println();
        System.out.println(ConsoleUI.SEPARATOR_MEDIUM);
        System.out.println("              ADD A NEW APPLIANCE");
        System.out.println(ConsoleUI.SEPARATOR_MEDIUM);
        System.out.println("   Select appliance type:");
        System.out.println("   1. General Appliance");
        System.out.println("   2. Light Appliance");
        System.out.println("   3. Cooling Appliance (e.g., Air Conditioner)");
        System.out.println(ConsoleUI.SEPARATOR_THIN);

        // ROBUST input: readInt retries until user enters 1, 2, or 3
        int typeChoice = inputReader.readInt("Your choice (1-3): ", 1, 3);

        // ---------- Collect common attributes with ROBUST validation ----------
        System.out.println();
        System.out.println(ConsoleUI.SEPARATOR_THIN);
        System.out.println("   Enter appliance details:");
        System.out.println(ConsoleUI.SEPARATOR_THIN);

        // ROBUST: retries until the name is not empty
        String name = inputReader.readNonEmptyString("Appliance name: ",
                "[Invalid] Appliance name cannot be empty.");

        // ROBUST: readDouble retries on invalid or negative input
        double power = inputReader.readDouble("Power rating (Watts, >= 0): ");
        double duration = inputReader.readDouble("Usage duration (Hours, >= 0): ");

        // ---------- Create the appropriate object based on type ----------
        // *** UPCASTING (Subclass-to-Superclass Reference) ***
        // The variable 'newAppliance' is declared as the SUPERCLASS type (Appliance).
        // We assign SUBCLASS objects (LightAppliance, CoolingAppliance) to it.
        // This is UPCASTING - a key prerequisite for DYNAMIC POLYMORPHISM.
        // Pattern: Appliance ref = new SubclassAppliance(...);
        Appliance newAppliance = null;

        if (typeChoice == 1) {
            // General Appliance - uses the base class directly
            newAppliance = new Appliance(name, power, duration);

        } else if (typeChoice == 2) {
            // Light Appliance - ask for LED information with ROBUST yes/no input
            boolean isLED = inputReader.readYesNo("Is this an LED light? (yes/no): ");

            // UPCASTING (Subclass-to-Superclass): Superclass reference = new Subclass object
            // The LightAppliance object is stored as an Appliance reference.
            // Later, when calculateEnergyConsumption() is called on this reference,
            // DYNAMIC POLYMORPHISM ensures the LightAppliance version executes.
            newAppliance = new LightAppliance(name, power, duration, isLED);

        } else if (typeChoice == 3) {
            // Cooling Appliance - ask for temperature with ROBUST range validation
            int temp = inputReader.readInt("Temperature setting (16-30 C): ", 16, 30);

            // UPCASTING (Subclass-to-Superclass): Superclass reference = new Subclass object
            // The CoolingAppliance object is stored as an Appliance reference.
            // Later, DYNAMIC POLYMORPHISM ensures the CoolingAppliance
            // version of calculateEnergyConsumption() is called at runtime.
            newAppliance = new CoolingAppliance(name, power, duration, temp);
        }

        // COMPOSITION: delegate storage to the Household object
        if (newAppliance != null) {
            household.addAppliance(newAppliance);

            // Display success confirmation with details
            System.out.println();
            System.out.println(ConsoleUI.SEPARATOR_THIN);
            System.out.println("   [SUCCESS] Appliance added successfully!");
            System.out.println(ConsoleUI.SEPARATOR_THIN);
            System.out.println("   " + newAppliance.toString());
            System.out.println("   Total appliances in household: " + household.getApplianceCount());
        }

        System.out.println(ConsoleUI.SEPARATOR_MEDIUM);
    }

    /**
     * Handles the energy report display.
     * Delegates record generation to the EnergyCalculationService and
     * report formatting to the ReportService (SEPARATION OF CONCERNS).
     * Checks if appliances exist before generating the report.
     */
    private void handleEnergyReport() {

        if (household.getApplianceCount() == 0) {
            ConsoleUI.printNoAppliancesInfo();
            return;
        }

        // COMPOSITION: delegate all logic to the service layer
        calculationService.recordAllEnergyUsage(household);
        System.out.println(reportService.generateEnergyReport(household));
    }

    /**
     * Handles user interaction for viewing high-energy-consuming appliances.
     * Asks the user for an energy threshold (with ROBUST input validation)
     * and delegates the filtering and formatting to the service layer.
     */
    private void handleHighEnergyReport() {

        if (household.getApplianceCount() == 0) {
            ConsoleUI.printNoAppliancesInfo();
            return;
        }

        System.out.println();
        System.out.println(ConsoleUI.SEPARATOR_MEDIUM);
        System.out.println("          HIGH ENERGY CONSUMPTION FILTER");
        System.out.println(ConsoleUI.SEPARATOR_MEDIUM);

        // ROBUST: readDouble retries on invalid or negative input
        double threshold = inputReader.readDouble("Enter energy threshold (kWh, >= 0): ");

        // COMPOSITION: delegate filtering and reporting to the service layer
        System.out.println(reportService.generateHighEnergyReport(household, threshold));
    }
}
