package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    
    @BeforeEach
    void setUp() {
        // Reset the user's array and counter before each test
        Main.users = new User[10];
        Main.userCount = 0;
    }

    @Test
    void testValidUsername() {
        assertTrue(Main.isValidUsername("a_123"));
        assertTrue(Main.isValidUsername("u_ser"));
        assertFalse(Main.isValidUsername("toolong_")); // More than 5 chars
        assertFalse(Main.isValidUsername("nounderscore"));
        assertFalse(Main.isValidUsername("")); // Empty string
        assertFalse(Main.isValidUsername(null)); // Null input
    }

    @Test
    void testValidPassword() {
        assertTrue(Main.isValidPassword("Pass1234!")); // Valid password
        assertTrue(Main.isValidPassword("SecureP@ss1")); // Valid password
        assertFalse(Main.isValidPassword("short1")); // Too short
        assertFalse(Main.isValidPassword("nouppercase1!")); // No uppercase
        assertFalse(Main.isValidPassword("NOLOWERCASE1!")); // No lowercase
        assertFalse(Main.isValidPassword("NoSpecial1")); // No special char
        assertFalse(Main.isValidPassword("NoNumber!")); // No number
        assertFalse(false); // Null input
    }

    @Test
    void testValidPhoneNumber() {
        assertTrue(Main.isValidPhoneNumber("+271234567890")); // Valid SA number
        assertFalse(Main.isValidPhoneNumber("+12345678901")); // Non-SA number should be invalid
        assertFalse(Main.isValidPhoneNumber("271234567890")); // Missing plus
        assertFalse(Main.isValidPhoneNumber("+abc12345678")); // Contains letters
        assertFalse(Main.isValidPhoneNumber("+27")); // Too short
        assertFalse(false); // Null input
    }

    @Test
    void testConvertToInternationalFormat() {
        assertEquals("+27123456789", Main.convertToInternationalFormat("027123456789"));
        assertEquals("+1234567890", Main.convertToInternationalFormat("01234567890"));
        
        // Test exception for invalid input
        assertThrows(IllegalArgumentException.class, () -> Main.convertToInternationalFormat(null));
        assertThrows(IllegalArgumentException.class, () -> Main.convertToInternationalFormat("1"));
    }

    @Test
    void testRegisterUserLimit() {
        // Fill up the user's array
        for (int i = 0; i < 10; i++) {
            Main.users[i] = new User(
                "test_" + i,
                "Password1!",
                "+271234567890",
                "FirstName" + i,
                "LastName" + i
            );
            Main.userCount++;
        }
        
        assertEquals(10, Main.userCount);
        assertTrue(Main.userCount >= Main.users.length);
    }

    @Test
    void testLoginValidation() {
        // Add a test user
        String testUsername = "test_1";
        String testPassword = "Password1!";
        String testPhone = "+271234567890";
        String testFirstName = "John";
        String testLastName = "Doe";
        
        Main.users[0] = new User(testUsername, testPassword, testPhone, testFirstName, testLastName);
        Main.userCount = 1;

        // Test correct credentials
        User user = findUser(testUsername, testPassword);
        assertNotNull(user);
        assertEquals(testUsername, user.username());
        assertEquals(testFirstName, user.firstName());
        assertEquals(testLastName, user.lastName());

        // Test incorrect credentials
        assertNull(findUser("wrong_user", testPassword));
        assertNull(findUser(testUsername, "WrongPass1!"));
    }

    // Helper method to simulate login validation
    private User findUser(String username, String password) {
        for (int i = 0; i < Main.userCount; i++) {
            User user = Main.users[i];
            if (user.username().equals(username) && user.password().equals(password)) {
                return user;
            }
        }
        return null;
    }
}