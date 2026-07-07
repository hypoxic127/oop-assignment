package com.sdg.energytracker.utilities;

import java.util.Scanner;

/**
 * InputReader.java
 *
 * This UTILITY class encapsulates ALL robust console input handling for the
 * application. It was extracted from the main application class so that:
 *
 * - SINGLE RESPONSIBILITY PRINCIPLE (SRP): this class does one job only -
 *   reading and validating user input.
 * - CODE REUSE: every part of the program (and any future feature) reads
 *   input through the same validated methods.
 * - ENCAPSULATION: the Scanner object is a private field; external classes
 *   cannot touch it directly and must use the validated read methods.
 *
 * ROBUSTNESS: every method uses try-catch and a while loop to keep retrying
 * until the user enters valid input. The program NEVER crashes due to
 * invalid user input (e.g., entering letters where numbers are expected).
 *
 * @author Group 1 - To Tsun Kai (0373689)
 * @version 2.0
 */
public class InputReader {

    // ==================== ENCAPSULATION ====================

    /** The Scanner used to read from the console (private - COMPOSITION). */
    private Scanner scanner;

    // ==================== CONSTRUCTORS ====================

    /**
     * Constructs an InputReader that reads from standard input (System.in).
     */
    public InputReader() {
        this.scanner = new Scanner(System.in);
    }

    // ==================== INPUT METHODS ====================

    /**
     * Reads an integer from the user within a specified range.
     * Uses try-catch and a while loop to keep retrying until the user
     * enters a valid integer within [min, max].
     *
     * ROBUSTNESS: This method guarantees a valid return value.
     * It will never throw an exception or return an out-of-range value.
     *
     * @param prompt the message to display to the user
     * @param min    the minimum acceptable value (inclusive)
     * @param max    the maximum acceptable value (inclusive)
     * @return a valid integer within [min, max]
     */
    public int readInt(String prompt, int min, int max) {
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
     * @param prompt the message to display to the user
     * @return a valid non-negative double value
     */
    public double readDouble(String prompt) {
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
     * @param prompt the message to display to the user
     * @return true if the user answered yes, false if no
     */
    public boolean readYesNo(String prompt) {
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

    /**
     * Reads a non-empty string from the user.
     * Retries with the given error message until the input is not empty.
     *
     * @param prompt       the message to display to the user
     * @param errorMessage the message shown when the input is empty
     * @return a valid, trimmed, non-empty string
     */
    public String readNonEmptyString(String prompt, String errorMessage) {
        System.out.print("  " + prompt);
        String value = scanner.nextLine().trim();

        // Validate the input is not empty; retry until it is valid
        while (value.isEmpty()) {
            System.out.println("  " + errorMessage);
            System.out.print("  " + prompt);
            value = scanner.nextLine().trim();
        }

        return value;
    }

    // ==================== RESOURCE MANAGEMENT ====================

    /**
     * Closes the underlying Scanner to release system resources.
     * Should be called once when the application exits.
     */
    public void close() {
        scanner.close();
    }
}
