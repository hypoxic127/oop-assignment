package com.sdg.energytracker.main;

import java.util.Scanner;
import com.sdg.energytracker.models.Appliance;
import com.sdg.energytracker.models.LightAppliance;
import com.sdg.energytracker.models.CoolingAppliance;
import com.sdg.energytracker.models.Household;

/**
 * EnergyTrackerApp.java
 *
 * Main application class for the Household Energy Consumption Tracker.
 * This class is responsible ONLY for user interaction (Scanner menu,
 * input/output). All business logic has been moved to the Household class.
 *
 * This design demonstrates SEPARATION OF CONCERNS:
 *   - EnergyTrackerApp: handles the console-based user interface (View/Controller)
 *   - Household: manages appliance data and energy calculations (Model)
 *
 * It also demonstrates COMPOSITION: EnergyTrackerApp creates and uses a
 * Household object (HAS-A relationship) to delegate all appliance management
 * and energy calculation tasks.
 *
 * DYNAMIC POLYMORPHISM is demonstrated when the Household iterates through
 * its ArrayList&lt;Appliance&gt; and calls calculateEnergyConsumption() - the JVM
 * dispatches to the correct overridden method based on actual object type.
 *
 * ROBUSTNESS: All user inputs are wrapped in try-catch blocks and validated
 * with retry loops. The program never crashes due to invalid user input.
 *
 * Related to SDG 7: Affordable and Clean Energy - this tracker helps
 * households monitor and reduce their energy consumption.
 *
 * @author Group Work
 * @version 2.0
 */
public class EnergyTrackerApp {

    // ==================== CONSTANTS ====================
    // Reusable separator strings for consistent, beautiful console output.

    /** Long separator for major sections (welcome banner, exit, reports). */
    private static final String SEPARATOR_THICK =
            "============================================================";

    /** Medium separator for sub-sections (menu, add appliance). */
    private static final String SEPARATOR_MEDIUM =
            "------------------------------------------------------------";

    /** Short separator for minor divisions within sections. */
    private static final String SEPARATOR_THIN =
            "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -";

    // ==================== MAIN METHOD ====================

    /**
     * The main entry point of the application.
     * Displays a menu-driven interface for managing household appliances
     * and calculating their total energy consumption.
     *
     * The main method delegates all business logic to a Household object,
     * keeping this class focused purely on user interaction.
     *
     * ROBUSTNESS: The entire menu loop is protected by try-catch, and
     * each input uses validated helper methods (readInt / readDouble)
     * that retry on invalid input instead of crashing.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        // ==================== COMPOSITION ====================
        // EnergyTrackerApp HAS-A Household object.
        // Instead of managing the ArrayList<Appliance> directly in main(),
        // we delegate that responsibility to the Household class.
        Household household = new Household("My Home");

        // Scanner for reading user input from the console
        Scanner scanner = new Scanner(System.in);

        // Variable to store the user's menu choice
        int choice = 0;

        // ==================== WELCOME BANNER ====================
        printWelcomeBanner();

        // ==================== MAIN MENU LOOP ====================
        // The program runs in a loop until the user chooses to exit.
        // The outer try-catch ensures NO uncaught exception crashes the program.
        try {
            while (choice != 4) {

                // Display the menu options
                printMenu();

                // Read user input with ROBUST validation
                // readInt() uses try-catch internally and loops until valid
                choice = readInt(scanner, "Please enter your choice (1-4): ", 1, 4);

                // ==================== MENU HANDLING ====================
                if (choice == 1) {
                    // ========== OPTION 1: Add an Appliance ==========
                    handleAddAppliance(scanner, household);

                } else if (choice == 2) {
                    // ========== OPTION 2: Record & View Total Energy ==========
                    handleEnergyReport(household);

                } else if (choice == 3) {
                    // ========== OPTION 3: View High Energy Appliances ==========
                    handleHighEnergyReport(scanner, household);

                } else if (choice == 4) {
                    // ========== OPTION 4: Exit ==========
                    printExitBanner();
                }
            }
        } catch (Exception e) {
            // ROBUSTNESS: Catch any unexpected runtime exception to prevent crash
            System.out.println();
            System.out.println(SEPARATOR_THICK);
            System.out.println("  [UNEXPECTED ERROR] " + e.getMessage());
            System.out.println("  The program will now exit safely.");
            System.out.println(SEPARATOR_THICK);
        } finally {
            // Close the Scanner to release system resources (always executed)
            scanner.close();
        }
    }

    // =====================================================================
    //                       INPUT HELPER METHODS
    // =====================================================================
    // These methods encapsulate ROBUST input handling logic.
    // They use try-catch and while loops to ensure the program NEVER
    // crashes due to invalid user input (e.g., entering letters for numbers).
    // =====================================================================

    /**
     * Reads an integer from the user within a specified range.
     * Uses try-catch and a while loop to keep retrying until the user
     * enters a valid integer within [min, max].
     *
     * ROBUSTNESS: This method guarantees a valid return value.
     * It will never throw an exception or return an out-of-range value.
     *
     * @param scanner the Scanner object for reading user input
     * @param prompt  the message to display to the user
     * @param min     the minimum acceptable value (inclusive)
     * @param max     the maximum acceptable value (inclusive)
     * @return a valid integer within [min, max]
     */
    private static int readInt(Scanner scanner, String prompt, int min, int max) {
        int value = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("  " + prompt);
            try {
                // Attempt to read the input as a string and parse it
                String input = scanner.nextLine().trim();
                value = Integer.parseInt(input);

                // Check if the value is within the acceptable range
                if (value >= min && value <= max) {
                    validInput = true;
                } else {
                    System.out.println("  [Invalid] Please enter a number between "
                            + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                // ROBUSTNESS: Catch non-numeric input (e.g., "abc", "12.5")
                System.out.println("  [Invalid] That is not a valid number. Please try again.");
            }
        }

        return value;
    }

    /**
     * Reads a non-negative double value from the user.
     * Uses try-catch and a while loop to keep retrying until the user
     * enters a valid non-negative decimal number.
     *
     * ROBUSTNESS: This method guarantees a valid, non-negative return value.
     *
     * @param scanner the Scanner object for reading user input
     * @param prompt  the message to display to the user
     * @return a valid non-negative double value
     */
    private static double readDouble(Scanner scanner, String prompt) {
        double value = -1.0;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("  " + prompt);
            try {
                String input = scanner.nextLine().trim();
                value = Double.parseDouble(input);

                if (value >= 0) {
                    validInput = true;
                } else {
                    // ENCAPSULATION of validation: negative values are rejected
                    System.out.println("  [Invalid] Value cannot be negative. Please try again.");
                }
            } catch (NumberFormatException e) {
                // ROBUSTNESS: Catch non-numeric input
                System.out.println("  [Invalid] That is not a valid number. Please try again.");
            }
        }

        return value;
    }

    /**
     * Reads a yes/no response from the user.
     * Accepts "yes", "y", "no", "n" (case-insensitive).
     * Retries until a valid response is given.
     *
     * @param scanner the Scanner object for reading user input
     * @param prompt  the message to display to the user
     * @return true if the user answered yes, false if no
     */
    private static boolean readYesNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print("  " + prompt);
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("yes") || input.equals("y")) {
                return true;
            } else if (input.equals("no") || input.equals("n")) {
                return false;
            } else {
                System.out.println("  [Invalid] Please enter 'yes' or 'no'.");
            }
        }
    }

    // =====================================================================
    //                       DISPLAY HELPER METHODS
    // =====================================================================

    /**
     * Prints the welcome banner when the application starts.
     * Provides a visually appealing introduction with SDG 7 branding.
     */
    private static void printWelcomeBanner() {
        System.out.println();
        System.out.println(SEPARATOR_THICK);
        System.out.println("       HOUSEHOLD ENERGY CONSUMPTION TRACKER");
        System.out.println("            SDG 7: Clean Energy");
        System.out.println(SEPARATOR_MEDIUM);
        System.out.println("   Track your appliances. Save energy. Save the planet.");
        System.out.println(SEPARATOR_THICK);
    }

    /**
     * Prints the main menu options.
     * Uses clear formatting with separators for readability.
     */
    private static void printMenu() {
        System.out.println();
        System.out.println(SEPARATOR_MEDIUM);
        System.out.println("                    MAIN MENU");
        System.out.println(SEPARATOR_MEDIUM);
        System.out.println("   1.  Add an Appliance");
        System.out.println("   2.  Record & View Total Energy Consumption");
        System.out.println("   3.  View High Energy Consumption Appliances");
        System.out.println("   4.  Exit Program");
        System.out.println(SEPARATOR_MEDIUM);
    }

    /**
     * Prints the exit banner when the user chooses to quit.
     * Includes an SDG 7 reminder message.
     */
    private static void printExitBanner() {
        System.out.println();
        System.out.println(SEPARATOR_THICK);
        System.out.println("   Thank you for using the Energy Tracker!");
        System.out.println(SEPARATOR_THIN);
        System.out.println("   Remember: Small changes in energy habits can");
        System.out.println("   make a big difference for SDG 7 - Clean Energy!");
        System.out.println(SEPARATOR_THICK);
    }

    // =====================================================================
    //                       UI HANDLER METHODS
    // =====================================================================
    // These methods handle ONLY user input/output.
    // All business logic is delegated to the Household object (COMPOSITION).
    // =====================================================================

    /**
     * Handles user interaction for adding a new appliance.
     * Collects user input via Scanner, creates the appropriate Appliance
     * subclass object, and delegates storage to the Household object.
     *
     * This method demonstrates:
     *   - SEPARATION OF CONCERNS: handles ONLY the user interface (I/O).
     *   - UPCASTING: subclass objects assigned to superclass reference.
     *   - COMPOSITION: storage is delegated to Household.addAppliance().
     *   - ROBUSTNESS: all inputs validated via readInt/readDouble/readYesNo.
     *
     * @param scanner   the Scanner object for reading user input
     * @param household the Household object to add the appliance to (COMPOSITION)
     */
    private static void handleAddAppliance(Scanner scanner, Household household) {

        System.out.println();
        System.out.println(SEPARATOR_MEDIUM);
        System.out.println("              ADD A NEW APPLIANCE");
        System.out.println(SEPARATOR_MEDIUM);
        System.out.println("   Select appliance type:");
        System.out.println("   1. General Appliance");
        System.out.println("   2. Light Appliance");
        System.out.println("   3. Cooling Appliance (e.g., Air Conditioner)");
        System.out.println(SEPARATOR_THIN);

        // ROBUST input: readInt retries until user enters 1, 2, or 3
        int typeChoice = readInt(scanner, "Your choice (1-3): ", 1, 3);

        // ---------- Collect common attributes with ROBUST validation ----------
        System.out.println();
        System.out.println(SEPARATOR_THIN);
        System.out.println("   Enter appliance details:");
        System.out.println(SEPARATOR_THIN);

        System.out.print("  Appliance name: ");
        String name = scanner.nextLine().trim();

        // Validate name is not empty
        while (name.isEmpty()) {
            System.out.println("  [Invalid] Appliance name cannot be empty.");
            System.out.print("  Appliance name: ");
            name = scanner.nextLine().trim();
        }

        // ROBUST: readDouble retries on invalid or negative input
        double power = readDouble(scanner, "Power rating (Watts, >= 0): ");
        double duration = readDouble(scanner, "Usage duration (Hours, >= 0): ");

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
            boolean isLED = readYesNo(scanner, "Is this an LED light? (yes/no): ");

            // UPCASTING (Subclass-to-Superclass): Superclass reference = new Subclass object
            // Appliance newAppliance = new LightAppliance(...);
            // The LightAppliance object is stored as an Appliance reference.
            // Later, when calculateEnergyConsumption() is called on this reference,
            // DYNAMIC POLYMORPHISM ensures the LightAppliance version executes.
            newAppliance = new LightAppliance(name, power, duration, isLED);

        } else if (typeChoice == 3) {
            // Cooling Appliance - ask for temperature with ROBUST range validation
            int temp = readInt(scanner, "Temperature setting (16-30 C): ", 16, 30);

            // UPCASTING (Subclass-to-Superclass): Superclass reference = new Subclass object
            // Appliance newAppliance = new CoolingAppliance(...);
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
            System.out.println(SEPARATOR_THIN);
            System.out.println("   [SUCCESS] Appliance added successfully!");
            System.out.println(SEPARATOR_THIN);
            System.out.println("   " + newAppliance.toString());
            System.out.println("   Total appliances in household: " + household.getApplianceCount());
        }

        System.out.println(SEPARATOR_MEDIUM);
    }

    /**
     * Handles the energy report display.
     * Delegates record generation and report formatting to the Household object.
     * Checks if appliances exist before generating the report.
     *
     * @param household the Household object to generate the report for (COMPOSITION)
     */
    private static void handleEnergyReport(Household household) {

        if (household.getApplianceCount() == 0) {
            System.out.println();
            System.out.println(SEPARATOR_MEDIUM);
            System.out.println("   [INFO] No appliances added yet.");
            System.out.println("   Please add appliances first (Option 1).");
            System.out.println(SEPARATOR_MEDIUM);
            return;
        }

        // COMPOSITION: delegate all logic to the Household object
        household.recordAllEnergyUsage();
        System.out.println(household.generateEnergyReport());
    }

    /**
     * Handles user interaction for viewing high-energy-consuming appliances.
     * Asks the user for an energy threshold (with ROBUST input validation)
     * and delegates the filtering logic to the Household object.
     *
     * @param scanner   the Scanner object for reading user input
     * @param household the Household object to query (COMPOSITION)
     */
    private static void handleHighEnergyReport(Scanner scanner, Household household) {

        if (household.getApplianceCount() == 0) {
            System.out.println();
            System.out.println(SEPARATOR_MEDIUM);
            System.out.println("   [INFO] No appliances added yet.");
            System.out.println("   Please add appliances first (Option 1).");
            System.out.println(SEPARATOR_MEDIUM);
            return;
        }

        System.out.println();
        System.out.println(SEPARATOR_MEDIUM);
        System.out.println("          HIGH ENERGY CONSUMPTION FILTER");
        System.out.println(SEPARATOR_MEDIUM);

        // ROBUST: readDouble retries on invalid or negative input
        double threshold = readDouble(scanner, "Enter energy threshold (kWh, >= 0): ");

        // COMPOSITION: delegate filtering and reporting to Household
        System.out.println(household.generateHighEnergyReport(threshold));
    }
}
