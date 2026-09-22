package restaurant.util;

import java.util.Scanner;

/** Wraps a single shared Scanner and gives validated read helpers. */
public class InputHelper {

    private final Scanner sc;

    public InputHelper(Scanner sc) {
        this.sc = sc;
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    public String readNonEmptyString(String prompt) {
        String value;
        do {
            System.out.print(prompt);
            value = sc.nextLine().trim();
            if (value.isEmpty()) {
                System.out.println("This field cannot be empty. Please try again.");
            }
        } while (value.isEmpty());
        return value;
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    public double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Please enter a number between " + min + " and " + max + ".");
        }
    }

    public double readPositiveDouble(String prompt) {
        while (true) {
            double value = readDouble(prompt);
            if (value >= 0) return value;
            System.out.println("Please enter a value of 0 or more.");
        }
    }

    public void pause() {
        System.out.print("\nPress ENTER to continue...");
        sc.nextLine();
    }
}
