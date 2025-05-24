package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void testMessageIDCheck_ValidID() {
        Message msg = new Message(1234567890L, 1, "+271234567890", "Hello world");
        assertTrue(msg.checkMessageID());
    }

    @Test
    public void testMessageIDCheck_InvalidID() {
        Message msg = new Message(1234567890123L, 1, "+271234567890", "Hello world");
        assertFalse(msg.checkMessageID());
    }

    @Test
    public void testRecipientCheck_Valid() {
        Message msg = new Message(1234567890L, 1, "+271234567890", "Hi there");
        assertEquals(1, msg.checkRecipientCell());
    }

    @Test
    public void testRecipientCheck_Invalid() {
        Message msg = new Message(1234567890L, 1, "+2712345678901234", "Hi there");
        assertEquals(0, msg.checkRecipientCell());
    }

    @Test
    public void testMessageHashGeneration() {
        Message msg = new Message(101L, 1, "+271234567890", "Hi tonight");
        // Based on your logic: 101 % 100 = 1 -> "01", number = 1, first word = HI, last = TONIGHT
        assertEquals("01:1:HITONIGHT", msg.getHash());
    }

    @Test
    public void testTotalMessagesCount() {
        int before = Message.returnTotalMessages();
        new Message(1001L, 1, "+271234567890", "Test message");
        assertEquals(before + 1, Message.returnTotalMessages());
    }

    @Test
    public void testMessagePrintingFormat() {
        Message msg = new Message(9999L, 3, "+271234567890", "This is a test");
        String output = msg.printMessages();
        assertTrue(output.contains("Message #3"));
        assertTrue(output.contains("ID: 9999"));
        assertTrue(output.contains("Recipient: +271234567890"));
        assertTrue(output.contains("Message: This is a test"));
    }

    @Test
    public void testMessageHashUniquePerMessage() {
        Message msg1 = new Message(1000L, 1, "+271234567890", "Hello world");
        Message msg2 = new Message(1001L, 2, "+271234567890", "Hello world");
        assertNotEquals(msg1.getHash(), msg2.getHash());
    }

    @Test
    public void testMessageSentStatus() {
        Message msg = new Message(123L, 1, "+271234567890", "Status test");
        msg.setStatus("Send");
        assertEquals("Send", msg.getStatusMessage());
    }

    @Test
    public void testMessageStatusStored() {
        Message msg = new Message(456L, 2, "+271234567890", "Store test");
        msg.setStatus("Store");
        assertEquals("Store", msg.getStatusMessage());
    }
}
