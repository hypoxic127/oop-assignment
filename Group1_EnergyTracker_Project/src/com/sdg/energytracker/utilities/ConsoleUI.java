package com.sdg.energytracker.utilities;

/**
 * ConsoleUI.java
 *
 * This UTILITY class centralises all "static" console output: banners,
 * menus, separators, and standard information messages. It was extracted
 * from the main application class so that:
 *
 * - SINGLE RESPONSIBILITY PRINCIPLE (SRP): this class does one job only -
 *   printing fixed user-interface elements to the console.
 * - CODE REUSE / CONSISTENCY: every screen of the program uses the same
 *   separator constants, giving the whole application a uniform look.
 *
 * All members are static because this class holds no per-object state -
 * it is a pure helper (similar to java.lang.Math). The private constructor
 * prevents accidental instantiation.
 *
 * @author Group 1 - ABDULLAH AHONAF FAHOMID ZIM (0390367)
 * @version 2.0
 */
public class ConsoleUI {

    // ==================== CONSTANTS ====================
    // Reusable separator strings for consistent, beautiful console output.

    /** Long separator for major sections (welcome banner, exit, reports). */
    public static final String SEPARATOR_THICK =
            "============================================================";

    /** Medium separator for sub-sections (menu, add appliance). */
    public static final String SEPARATOR_MEDIUM =
            "------------------------------------------------------------";

    /** Short separator for minor divisions within sections. */
    public static final String SEPARATOR_THIN =
            "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -";

    // ==================== CONSTRUCTOR ====================

    /**
     * Private constructor - this is a static utility class and should
     * never be instantiated (same pattern as java.lang.Math).
     */
    private ConsoleUI() {
        // Intentionally empty
    }

    // ==================== DISPLAY METHODS ====================

    /**
     * Prints the welcome banner when the application starts.
     * Provides a visually appealing introduction with SDG 7 branding.
     */
    public static void printWelcomeBanner() {
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
    public static void printMenu() {
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
    public static void printExitBanner() {
        System.out.println();
        System.out.println(SEPARATOR_THICK);
        System.out.println("   Thank you for using the Energy Tracker!");
        System.out.println(SEPARATOR_THIN);
        System.out.println("   Remember: Small changes in energy habits can");
        System.out.println("   make a big difference for SDG 7 - Clean Energy!");
        System.out.println(SEPARATOR_THICK);
    }

    /**
     * Prints a standard information block telling the user that no
     * appliances have been added yet.
     */
    public static void printNoAppliancesInfo() {
        System.out.println();
        System.out.println(SEPARATOR_MEDIUM);
        System.out.println("   [INFO] No appliances added yet.");
        System.out.println("   Please add appliances first (Option 1).");
        System.out.println(SEPARATOR_MEDIUM);
    }

    /**
     * Prints an unexpected-error block. Used by the controller's outer
     * try-catch to report a crash-free shutdown (ROBUSTNESS).
     *
     * @param message the exception message to display
     */
    public static void printUnexpectedError(String message) {
        System.out.println();
        System.out.println(SEPARATOR_THICK);
        System.out.println("  [UNEXPECTED ERROR] " + message);
        System.out.println("  The program will now exit safely.");
        System.out.println(SEPARATOR_THICK);
    }
}
