package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;

public class MainTest {

    @BeforeEach
    public void setupArrays() {
        Main.sentMessagesArray = new ArrayList<>();
        Main.disregardedMessagesArray = new ArrayList<>();
        Main.storedMessagesArray = new ArrayList<>();
        Main.messageHashes = new ArrayList<>();
        Main.messageIDs = new ArrayList<>();

        Message msg1 = new Message(1000000001L, 1, "+27834557896", "It is dinner time !");
        Main.sentMessagesArray.add(msg1);
        Main.messageHashes.add(msg1.getHash());
        Main.messageIDs.add(msg1.getId());

        Message msg2 = new Message(1000000002L, 2, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        msg2.setStatus("Stored");
        Main.storedMessagesArray.add(msg2);
        Main.messageHashes.add(msg2.getHash());
        Main.messageIDs.add(msg2.getId());

        Message msg3 = new Message(1000000003L, 3, "+27834484567", "Yohoooo, I am at your gate.");
        msg3.setStatus("Disregarded");
        Main.disregardedMessagesArray.add(msg3);

        Message msg4 = new Message(838884567L, 4, "0838884567", "It is dinner time !");
        Main.sentMessagesArray.add(msg4);
        Main.messageHashes.add(msg4.getHash());
        Main.messageIDs.add(msg4.getId());

        Message msg5 = new Message(1000000005L, 5, "+27838884567", "Ok, I am leaving without you.");
        msg5.setStatus("Stored");
        Main.storedMessagesArray.add(msg5);
        Main.messageHashes.add(msg5.getHash());
        Main.messageIDs.add(msg5.getId());
    }

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

    @Test
    public void testSentMessagesArrayPopulated() {
        assertEquals(2, Main.sentMessagesArray.size());
        assertFalse(Main.sentMessagesArray.stream().anyMatch(m -> m.getContent().equals("Did you get the cake?")));
        assertTrue(Main.sentMessagesArray.stream().anyMatch(m -> m.getContent().equals("It is dinner time !")));
    }

    @Test
    public void testLongestSentMessage() {
        String longestMessage = Main.sentMessagesArray.stream()
                .map(Message::getContent)
                .max((a, b) -> Integer.compare(a.length(), b.length()))
                .orElse("");

        // Longest sent message in test data is "It is dinner time !" among sent messages
        assertEquals("It is dinner time !", longestMessage);
    }

    @Test
    public void testSearchMessageID() {
        long searchId = 838884567L; // message 4 id
        Message found = Main.sentMessagesArray.stream()
                .filter(m -> m.getId() == searchId)
                .findFirst()
                .orElse(null);

        assertNotNull(found);
        assertEquals("It is dinner time !", found.getContent());
    }

    @Test
    public void testSearchMessagesByRecipient() {
        String recipient = "+27838884567";

        long count = Main.storedMessagesArray.stream()
                .filter(m -> m.getRecipient().equals(recipient))
                .count();

        assertEquals(2, count);
    }

    @Test
    public void testDeleteMessageByHash() {
        String hashToDelete = Main.storedMessagesArray.get(0).getHash();

        boolean removed = Main.storedMessagesArray.removeIf(m -> m.getHash().equals(hashToDelete));
        Main.messageHashes.remove(hashToDelete);

        assertTrue(removed);
        assertFalse(Main.storedMessagesArray.stream().anyMatch(m -> m.getHash().equals(hashToDelete)));
        assertFalse(Main.messageHashes.contains(hashToDelete));
    }

    @Test
    public void testDisplayReportIncludesSentMessages() {
        StringBuilder report = new StringBuilder();
        for (Message m : Main.sentMessagesArray) {
            report.append(m.getHash())
                    .append(m.getRecipient())
                    .append(m.getContent());
        }
        String reportStr = report.toString();

        assertFalse(reportStr.contains("Did you get the cake?"));
        assertTrue(reportStr.contains("It is dinner time !"));
    }

}
