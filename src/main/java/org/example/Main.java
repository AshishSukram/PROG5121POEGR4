package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static User[] users = new User[10];
    static int userCount = 0;
    static List<Message> sentMessagesArray = new ArrayList<>();
    static List<Message> disregardedMessagesArray = new ArrayList<>();
    static List<Message> storedMessagesArray = new ArrayList<>();
    static List<String> messageHashes = new ArrayList<>();
    static List<Long> messageIDs = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            String[] options = {"Register", "Login", "Exit"};
            int choice = JOptionPane.showOptionDialog(null, "Welcome to the Chat App", "Chat Application",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == 0) {
                registerUser();
            } else if (choice == 1) {
                loginUser();
            } else {
                break;
            }
        }
    }

    private static void startQuickChat() {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat.");

        int maxMessages;
        while (true) {
            String input = JOptionPane.showInputDialog("How many messages would you like to send?");
            if (input == null) return;
            try {
                maxMessages = Integer.parseInt(input);
                if (maxMessages > 0) break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number greater than 0.");
            }
        }

        List<Message> sentMessages = new ArrayList<>();
        while (true) {
            String[] menuOptions = {"Send Message", "Show Recently Sent Messages", "Task Report", "Quit"};
            int choice = JOptionPane.showOptionDialog(null,
                    "Choose an option:",
                    "QuickChat Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    menuOptions,
                    menuOptions[0]);

            if (choice == 0) {
                sendMessages(maxMessages, sentMessages);
            } else if (choice == 1) {
                showSentMessages(sentMessages);
            } else if (choice == 2) {
                showTaskReportMenu();
            } else {
                break;
            }
        }

        if (!sentMessages.isEmpty()) {
            saveMessagesToJson(sentMessages);
            JOptionPane.showMessageDialog(null, "✅ Total messages handled: " + Message.returnTotalMessages());
        }
    }

    public static void sendMessages(int maxMessages, List<Message> messages) {
        if (messages.size() >= maxMessages) {
            JOptionPane.showMessageDialog(null, "Message limit reached.");
            return;
        }

        int msgNum = messages.size() + 1;

        String recipient;
        while (true) {
            recipient = JOptionPane.showInputDialog("Enter recipient number (e.g. +271234567890):");
            if (recipient == null) return;
            if (recipient.matches("^\\+\\d{11,13}$")) break;
            JOptionPane.showMessageDialog(null, "Invalid recipient format.");
        }

        String content = JOptionPane.showInputDialog("Enter your message (max 250 characters):");
        if (content == null || content.length() > 250) {
            JOptionPane.showMessageDialog(null, "Invalid message. Must be 250 characters or less.");
            return;
        }

        long messageId = (long) (Math.random() * 9_000_000_000L + 1_000_000_000L);

        String[] options = {"Send Message", "Disregard", "Store for Later"};
        int action = JOptionPane.showOptionDialog(null,
                "Choose what to do with your message:\n" + content,
                "Message Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        if (action == 0 || action == 2) {
            Message message = new Message(messageId, msgNum, recipient, content);
            messages.add(message);
            sentMessagesArray.add(message);
            messageHashes.add(message.getHash());
            messageIDs.add(message.getId());
            JOptionPane.showMessageDialog(null, message.printMessages());
        } else {
            Message discarded = new Message(messageId, msgNum, recipient, content);
            discarded.setStatus("Disregarded");
            disregardedMessagesArray.add(discarded);
        }
    }

    private static void showSentMessages(List<Message> messages) {
        if (messages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages have been sent yet.");
            return;
        }

        StringBuilder sb = new StringBuilder("\uD83D\uDCE8 Sent Messages:\n\n");
        for (Message m : messages) {
            sb.append(m.printMessages()).append("\n\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));

        JOptionPane.showMessageDialog(null, scrollPane, "Sent Messages", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void saveMessagesToJson(List<Message> messages) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("messages.json")) {
            gson.toJson(messages, writer);
            JOptionPane.showMessageDialog(null, "\uD83D\uDCC4 Messages saved to messages.json");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "\u26A0 Failed to save messages to file.");
            e.printStackTrace();
        }
    }

    public static boolean isUsernameTaken(String username) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    // ChatGPT regex code
    public static boolean isValidUsername(String username) {
        return username != null && username.matches("^[A-Za-z0-9_]{1,5}$") && username.contains("_");
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpperCase = true;
            if (Character.isLowerCase(c)) hasLowerCase = true;
            if (Character.isDigit(c)) hasDigit = true;
            if ("!@#$%^&*()-_=+[]{}|;:'\",.<>?/".indexOf(c) >= 0) hasSpecialChar = true;
        }

        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return false;
        // If phone number starts with '0', replace it with '+27'
        if (phoneNumber.startsWith("0")) {
            phoneNumber = phoneNumber.replaceFirst("0", "+27");
        }
        // Now check if it matches the format +27 followed by 10 digits
        return phoneNumber.matches("\\+27\\d{10}");
    }
    // End ChatGPT regex code

    public static void registerUser() {
        if (userCount >= users.length) {
            JOptionPane.showMessageDialog(null, "User limit reached. Cannot register more users.");
            return;
        }

        String username, password, phoneNumber, firstName, lastName;

        while (true) {
            username = JOptionPane.showInputDialog("Enter your username (must contain an underscore and be max 5 characters):");
            if (username == null) return;

            if (isValidUsername(username) && !isUsernameTaken(username)) {
                JOptionPane.showMessageDialog(null, "✓ Username successfully captured!", "Username Validation", JOptionPane.INFORMATION_MESSAGE);
                break;
            } else {
                String validationMessage = """
                        ❌ Invalid username format or username already taken
                        Username must:
                        - Be 5 characters or less
                        - Contain an underscore
                        - Only use letters, numbers, and underscore
                        """;
                JOptionPane.showMessageDialog(null, validationMessage, "Username Validation Failed", JOptionPane.ERROR_MESSAGE);
            }
        }

        while (true) {
            password = JOptionPane.showInputDialog("Enter your password (min 8 characters, must include: uppercase letter, number, and special character):");
            if (password == null) return;

            if (isValidPassword(password)) {
                JOptionPane.showMessageDialog(null, "✓ Password successfully captured!", "Password Validation", JOptionPane.INFORMATION_MESSAGE);
                break;
            } else {
                String validationMessage = """
                        ❌ Invalid password format
                        Password must:
                        - Be at least 8 characters long
                        - Contain at least one uppercase letter
                        - Contain at least one lowercase letter
                        - Contain at least one number
                        - Contain at least one special character
                        """;
                JOptionPane.showMessageDialog(null, validationMessage, "Password Validation Failed", JOptionPane.ERROR_MESSAGE);
            }
        }

        while (true) {
            phoneNumber = JOptionPane.showInputDialog("Enter your phone number with country code (e.g., +27XXXXXXXXXX):");
            if (phoneNumber == null) return;

            if (isValidPhoneNumber(phoneNumber)) {
                JOptionPane.showMessageDialog(null, "✓ Cell phone number successfully captured!", "Phone Number Validation", JOptionPane.INFORMATION_MESSAGE);
                break;
            } else {
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
        if (firstName == null) return;

        lastName = JOptionPane.showInputDialog("Enter your last name:");
        if (lastName == null) return;

        users[userCount] = new User(username, password, phoneNumber, firstName, lastName);
        userCount++;

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

        JOptionPane.showMessageDialog(null, registrationDetails, "Registration Complete ✅", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void loginUser() {
        String username = JOptionPane.showInputDialog("Enter your username:");
        if (username == null) return;

        String password = JOptionPane.showInputDialog("Enter your password:");
        if (password == null) return;

        for (int i = 0; i < userCount; i++) {
            User user = users[i];
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                String loginDetails = String.format("Welcome %s, %s it is great to see you again.",
                        user.getFirstName(), user.getLastName());
                JOptionPane.showMessageDialog(null, loginDetails, "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                startQuickChat();
                loadStoredMessagesFromJson();
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
    }
    public static void loadStoredMessagesFromJson() {
        Gson gson = new Gson();
        try (java.io.FileReader reader = new java.io.FileReader("messages.json")) {
            Message[] messages = gson.fromJson(reader, Message[].class);
            for (Message m : messages) {
                storedMessagesArray.add(m);
                messageHashes.add(m.getHash());
                messageIDs.add(m.getId());
            }
            JOptionPane.showMessageDialog(null, "Stored messages loaded from JSON.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No stored messages found.");
        }
    }
    private static void showTaskReportMenu() {
        String[] options = {
                "View Sender & Recipients",
                "View Longest Message",
                "Search by Message ID",
                "Search by Recipient",
                "Delete by Message Hash",
                "Display Full Report",
                "Back"
        };

        while (true) {
            int choice = JOptionPane.showOptionDialog(null, "Task Report Menu", "Reports",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);

            switch (choice) {
                case 0 -> viewSenderAndRecipients();
                case 1 -> showLongestMessage();
                case 2 -> searchByMessageId();
                case 3 -> searchByRecipient();
                case 4 -> deleteByMessageHash();
                case 5 -> displayFullReport();
                default -> { return; }
            }
        }
    }
    private static void viewSenderAndRecipients() {
        if (sentMessagesArray.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages to show.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessagesArray) {
            sb.append("Recipient: ").append(m.getRecipient()).append("\nMessage: ").append(m.getContent()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Sender & Recipients", JOptionPane.INFORMATION_MESSAGE);
    }
    private static void showLongestMessage() {
        if (sentMessagesArray.isEmpty()) return;

        Message longest = sentMessagesArray.stream()
                .max((m1, m2) -> Integer.compare(m1.getContent().length(), m2.getContent().length()))
                .orElse(null);

        JOptionPane.showMessageDialog(null, "Longest Message:\n" + longest.getContent());
    }
    private static void searchByMessageId() {
        String input = JOptionPane.showInputDialog("Enter message ID:");
        if (input == null) return;

        for (Message m : sentMessagesArray) {
            if (String.valueOf(m.getId()).equals(input)) {
                JOptionPane.showMessageDialog(null,
                        "Recipient: " + m.getRecipient() + "\nMessage: " + m.getContent());
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Message ID not found.");
    }
    private static void searchByRecipient() {
        String input = JOptionPane.showInputDialog("Enter recipient number:");
        if (input == null) return;

        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessagesArray) {
            if (m.getRecipient().equals(input)) {
                sb.append(m.getContent()).append("\n");
            }
        }
        for (Message m : storedMessagesArray) {
            if (m.getRecipient().equals(input)) {
                sb.append(m.getContent()).append("\n");
            }
        }

        if (sb.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages found for that recipient.");
        } else {
            JOptionPane.showMessageDialog(null, "Messages:\n" + sb);
        }
    }
    private static void deleteByMessageHash() {
        String input = JOptionPane.showInputDialog("Enter message hash to delete:");
        if (input == null) return;

        for (Message m : sentMessagesArray) {
            if (m.getHash().equals(input)) {
                sentMessagesArray.remove(m);
                messageHashes.remove(m.getHash());
                messageIDs.remove(m.getId());
                JOptionPane.showMessageDialog(null, "Message \"" + m.getContent() + "\" successfully deleted.");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "No message found with that hash.");
    }
    private static void displayFullReport() {
        if (sentMessagesArray.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages.");
            return;
        }

        StringBuilder sb = new StringBuilder("📄 Sent Message Report:\n\n");
        for (Message m : sentMessagesArray) {
            sb.append("Hash: ").append(m.getHash()).append("\n")
                    .append("Recipient: ").append(m.getRecipient()).append("\n")
                    .append("Message: ").append(m.getContent()).append("\n\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new java.awt.Dimension(500, 300));

        JOptionPane.showMessageDialog(null, scroll, "Full Sent Message Report", JOptionPane.INFORMATION_MESSAGE);
    }

}

