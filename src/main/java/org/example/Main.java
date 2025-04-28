package org.example;

import javax.swing.*;

/**
 * Main class for the Chat Application that handles user registration and login functionality.
 */
public class Main {
    // Array to store registered users with a maximum capacity of 10 users
    static User[] users = new User[10]; // Changed to package-private for testing
    // Counter to keep track of the number of registered users
    static int userCount = 0; // Changed to package-private for testing

    /**
     * Main entry point of the application that displays the main menu in a loop
     */
    public static void main(String[] args) {
        while (true) {
            // Create options for the main menu
            String[] options = {"Register", "Login", "Exit"};
            // Display the main menu dialog and get user choice
            int choice = JOptionPane.showOptionDialog(null, "Welcome to the Chat App", "Chat Application",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            // Handle user's choice
            if (choice == 0) {
                registerUser();
            } else if (choice == 1) {
                loginUser();
            } else {
                break; // Exit the application
            }
        }
    }

    /**
     * Handles the user registration process with validation for username, password, and phone number
     */
    public static void registerUser() {
        // Check if the user limit is reached
        if (userCount >= users.length) {
            JOptionPane.showMessageDialog(null, "User limit reached. Cannot register more users.");
            return;
        }

        String username;
        String password;
        String phoneNumber;
        String firstName;
        String lastName;

        // Username validation loop
        while (true) {
            username = JOptionPane.showInputDialog("Enter your username (must contain an underscore and be max 5 characters):");
            if (username == null) {
                return; // User clicked Cancel
            }

            // Validate username format
            if (isValidUsername(username)) {
                JOptionPane.showMessageDialog(null, "✓ Username successfully captured!", "Username Validation", JOptionPane.INFORMATION_MESSAGE);
                break;
            } else {
                // Display validation requirements if the username is invalid
                String validationMessage = """
                        ❌ Invalid username format
                        Username must:
                        - Be 5 characters or less
                        - Contain an underscore
                        - Only use letters, numbers, and underscore
                        """;
                JOptionPane.showMessageDialog(null, validationMessage, "“Username is not correctly formatted,please ensure that your username contains an underscore and is no more than five characters in length.", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Password validation loop
        while (true) {
            password = JOptionPane.showInputDialog("Enter your password (min 8 characters, must include: uppercase letter, number, and special character):");
            if (password == null) {
                return; // User clicked Cancel
            }

            // Validate password complexity
            if (isValidPassword(password)) {
                JOptionPane.showMessageDialog(null, "✓ Password successfully captured!", "Password Validation", JOptionPane.INFORMATION_MESSAGE);
                break;
            } else {
                // Display password requirements if invalid
                String validationMessage = """
                        ❌ Invalid password format
                        Password must:
                        - Be at least 8 characters long
                        - Contain at least one uppercase letter
                        - Contain at least one lowercase letter
                        - Contain at least one number
                        - Contain at least one special character
                        """;
                JOptionPane.showMessageDialog(null, validationMessage, "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number and a special character.", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Phone number validation loop
        while (true) {
            phoneNumber = JOptionPane.showInputDialog("Enter your phone number with country code (e.g., +27XXXXXXXXXX):");
            if (phoneNumber == null) {
                return; // User clicked Cancel
            }

            // Validate the phone number format
            if (isValidPhoneNumber(phoneNumber)) {
                JOptionPane.showMessageDialog(null, "✓ Cell phone number successfully captured!", "Phone Number Validation", JOptionPane.INFORMATION_MESSAGE);
                break;
            } else {
                // Display phone number requirements if invalid
                String validationMessage = """
                        ❌ Invalid phone number format
                        Phone number must:
                        - Start with '+' followed by country code
                        - Contain only digits after the country code
                        - Not exceed 10 digits after country code
                        - Start with 0 after the country code
                        """;
                JOptionPane.showMessageDialog(null, validationMessage, "Phone Number Validation Failed", JOptionPane.ERROR_MESSAGE);
            }
        }

        firstName = JOptionPane.showInputDialog("Enter your first name:");
        if (firstName == null) return; // User clicked Cancel

        lastName = JOptionPane.showInputDialog("Enter your last name:");
        if (lastName == null) return; // User clicked Cancel

        users[userCount] = new User(username, password, phoneNumber, firstName, lastName);
        userCount++;

        // Display successful registration details
        String registrationDetails = String.format("""
                ✅ REGISTRATION SUCCESSFUL!
                
                Your account has been created with the following details:
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                Username: %s ✓
                Password: %s ✓
                Phone Number: %s ✓
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                
                You can now log in with these credentials.""",
                username, password, phoneNumber);

        JOptionPane.showMessageDialog(null, registrationDetails,
                "Registration Complete ✅", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Handles the user login process by validating credentials
     */
    public static void loginUser() {
        String username = JOptionPane.showInputDialog("Enter your username:");
        if (username == null) return; // User clicked Cancel

        String password = JOptionPane.showInputDialog("Enter your password:");
        if (password == null) return; // User clicked Cancel

        // Check credentials against registered users
        for (int i = 0; i < userCount; i++) {
            User user = users[i];
            if (user.username().equals(username) && user.password().equals(password)) {
                // In loginUser() method, modify the success message:
                String loginDetails = String.format("Welcome %s, %s it is great to see you again.",
                        user.firstName(), user.lastName());
                JOptionPane.showMessageDialog(null, loginDetails, "Login Successful", JOptionPane.INFORMATION_MESSAGE);



                return;
            }
        }
        // Display error if credentials are invalid
        JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");

    }

    // ChatGPT regex code
    /**
     * Validates the username format:
     * - Must be 5 characters or fewer
     * - Must contain an underscore
     * - Can only contain letters, numbers, and underscore
     */
    public static boolean isValidUsername(String username) {
        return username != null && username.matches("^[A-Za-z0-9_]{1,5}$") && username.contains("_");
    }

    /**
     * Validates password complexity:
     * - Minimum 8 characters
     * - Must contain at least one uppercase letter
     * - Must contain at least one lowercase letter
     * - Must contain at least one number
     * - Must contain at least one special character
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>/?]).{8,}$");
    }

    /**
     * Validates the phone number format:
     * - Must start with +27
     * - Must be followed by exactly 10 digits
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("\\+27\\d{10}");
    }

    /**
     * Converts phone number to international format by adding the '+' prefix
     * @param phoneNumber Phone number to convert
     * @return Phone number in international format
     * @throws IllegalArgumentException if the phone number is invalid
     */
    public static String convertToInternationalFormat(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 2) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        return String.format("+%s", phoneNumber.substring(1));
    }
}