package org.example;

public class Message {
    private long id;
    private int number;
    private String recipient;
    private String content;
    private String hash;
    private String status;
    private static int totalMessages = 0;

    public Message(long id, int number, String recipient, String content) {
        this.id = id;
        this.number = number;
        this.recipient = recipient;
        this.content = content;
        this.hash = createMessageHash();
        this.status = "Sent";

        if (sentMessage().equals("Sent") || sentMessage().equals("Stored")) {
            totalMessages++;
        }
    }

    public boolean checkMessageID() {
        return String.valueOf(id).length() <= 10;
    }

    public int checkRecipientCell() {
        return recipient.replace("+", "").length() <= 12 ? 1 : 0;
    }

    public String createMessageHash() {
        String[] words = content.trim().split("\\s+");
        String first = words.length > 0 ? words[0].toUpperCase() : "";
        String last = words.length > 1 ? words[words.length - 1].toUpperCase() : first;
        return String.format("%02d:%d:%s%s", id % 100, number, first, last);
    }

    public String sentMessage() {
        return "Sent";
    }

    public String printMessages() {
        return String.format("""
                ─────────────────────────────
                Message #%d
                ID: %d
                Hash: %s
                Recipient: %s
                Message: %s
                ─────────────────────────────
                """, number, id, hash, recipient, content);
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    public String getHash() {
        return hash;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return status;
    }
}
