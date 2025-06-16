/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.chatapp;





/**
 *
 * @author mnqvi
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

public class MessageLastPartTest {
    private MessageLastPart system;
    private Message msg1;
    private Message msg2;
    private static final String VALID_SENDER = "+27123456789"; // Added valid sender

    @BeforeEach
    public void setUp() {
        // Reset Message static fields
        try {
            java.lang.reflect.Field field = Message.class.getDeclaredField("numMessagesSent");
            field.setAccessible(true);
            field.set(null, 0);
            field = Message.class.getDeclaredField("messageIdCounter");
            field.setAccessible(true);
            field.set(null, 0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to reset static fields: " + e.getMessage());
        }

        system = new MessageLastPart();
        // Updated constructor calls to include sender and fixed recipient formats
        this.msg1 = new Message("+27834557896", VALID_SENDER, "Did you get the cake?", "Sent");
        this.msg2 = new Message("+27838884567", VALID_SENDER, "It is dinner time!", "Sent");
        System.out.println("msg1 messageId: " + msg1.getMessageId());
        System.out.println("msg2 messageId: " + msg2.getMessageId());
        system.addMessage(msg1);
        system.addMessage(msg2);
        system.addMessage(new Message("+27834557896", VALID_SENDER, "Did you get the cake?", "Sent"));
        system.addMessage(new Message("+27838884567", VALID_SENDER, "Where are you? You are late! I have asked you to be on time.", "Stored"));
        system.addMessage(new Message("+27834484567", VALID_SENDER, "Yohooo, I am at your gate.", "Disregard"));
        system.addMessage(new Message("+27838884567", VALID_SENDER, "It is dinner time!", "Sent"));
        system.addMessage(new Message("+27838884567", VALID_SENDER, "Ok, I am leaving without you.", "Stored"));
    }

    @Test
public void testSentMessagesArray() {
    ArrayList<Message> sentMessages = system.getSentMessages();
    assertEquals(4, sentMessages.size(), "Total sent messages should be 4");
    assertTrue(sentMessages.stream().anyMatch(msg -> 
        msg.getRecipient().equals("+27834557896") && 
        msg.getMessage().equals("Did you get the cake?") && 
        msg.getFlag().equals("Sent") && 
        msg.getSender().equals(VALID_SENDER)), "Message 1 should match");
    assertTrue(sentMessages.stream().anyMatch(msg -> 
        msg.getRecipient().equals("+27838884567") && 
        msg.getMessage().equals("It is dinner time!") && 
        msg.getFlag().equals("Sent") && 
        msg.getSender().equals(VALID_SENDER)), "Message 2 should match");
}

    @Test
    public void testLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.", system.getLongestMessage(),
                "Longest message should match");
    }

    @Test
    public void testSearchByMessageId() {
        String actual = system.searchByMessageId(msg2.getMessageId());
        String expected = "Sender: " + VALID_SENDER + ", Recipient: +27838884567, Message: It is dinner time!";
        assertEquals(expected, actual, "Search by message ID should return correct details");
    }

    @Test
    public void testSearchByMessageIdFirstSent() {
        String actual = system.searchByMessageId(msg1.getMessageId());
        String expected = "Sender: " + VALID_SENDER + ", Recipient: +27834557896, Message: Did you get the cake?";
        assertEquals(expected, actual, "Search by message ID should return correct details");
    }

    @Test
    public void testSearchByRecipient() {
        ArrayList<Message> messages = system.searchByRecipient("+27838884567");
        assertEquals(4, messages.size(), "Should find 4 messages for recipient");
        Message sentMessage = messages.stream()
                .filter(msg -> "Sent".equals(msg.getFlag()) && "It is dinner time!".equals(msg.getMessage()))
                .findFirst()
                .orElse(null);
        assertNotNull(sentMessage, "Sent message should exist");
        assertEquals("It is dinner time!", sentMessage.getMessage(), "Message content should match");
        assertEquals("Sent", sentMessage.getFlag(), "Flag should be Sent");
        assertEquals(VALID_SENDER, sentMessage.getSender(), "Sender should match");
        Message storedMessage = messages.stream()
                .filter(msg -> "Stored".equals(msg.getFlag()) && "Ok, I am leaving without you.".equals(msg.getMessage()))
                .findFirst()
                .orElse(null);
        assertNotNull(storedMessage, "Stored message should exist");
        assertEquals("Ok, I am leaving without you.", storedMessage.getMessage(), "Message content should match");
        assertEquals("Stored", storedMessage.getFlag(), "Flag should be Stored");
        assertEquals(VALID_SENDER, storedMessage.getSender(), "Sender should match");
    }

    @Test
public void testDeleteByHash() {
    ArrayList<Message> sentMessages = system.getSentMessages();
    Message messageToDelete = sentMessages.stream()
            .filter(msg -> msg.getRecipient().equals("+27838884567"))
            .findFirst()
            .orElse(null);
    assertNotNull(messageToDelete, "A Sent message for +27838884567 should exist");
    String hash = messageToDelete.getMessageHash();
    String expectedMessage = messageToDelete.getMessage();
    assertEquals("Message \"" + expectedMessage + "\" successfully deleted.", system.deleteByHash(hash),
            "Delete by hash should succeed");
    sentMessages = system.getSentMessages();
    long sentCountForRecipient = sentMessages.stream()
            .filter(msg -> msg.getRecipient().equals("+27838884567"))
            .count();
    assertEquals(1, sentCountForRecipient, "One Sent message should remain for +27838884567");
    Message remainingSentMessage = sentMessages.stream()
            .filter(msg -> msg.getRecipient().equals("+27838884567") && 
                           "Sent".equals(msg.getFlag()) && 
                           "It is dinner time!".equals(msg.getMessage()))
            .findFirst()
            .orElse(null);
    assertNotNull(remainingSentMessage, "Remaining Sent message should exist");
    assertEquals("It is dinner time!", remainingSentMessage.getMessage(), "Remaining message should match");
}

    @Test
    public void testDeleteByHash_NonExistentHash() {
        String nonExistentHash = "nonexistent:hash";
        assertEquals("Message not found", system.deleteByHash(nonExistentHash), "Non-existent hash should fail");
    }

    @Test
    public void testDisplayReport() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        system.displayReport();

        String output = outContent.toString();
        assertTrue(output.contains("Sent Message Report:"), "Report should start with header");
        assertTrue(output.contains("Sender: " + VALID_SENDER), "Report should include sender");
        assertTrue(output.contains("Recipient: +27834557896"), "Report should include recipient");
        assertTrue(output.contains("Message: Did you get the cake?"), "Report should include message");
        assertTrue(output.contains("Recipient: +27838884567"), "Report should include recipient");
        assertTrue(output.contains("Message: It is dinner time!"), "Report should include message");

        System.setOut(System.out);
    }

    @Test
    public void testDisplaySentMessages() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        system.displaySentMessages();

        String output = outContent.toString().replace("\r\n", "\n").trim();
        System.out.println("Actual output: [" + output.replace("\n", "\\n") + "]");
        assertTrue(output.contains("Sent Messages:"), "Output should start with header");
        String expectedMsg1 = "sender:" + VALID_SENDER + ", Recipient: +27834557896, Message: Did you get the cake?";
        String expectedMsg2 = "sender:" + VALID_SENDER + ", Recipient: +27838884567, Message: It is dinner time!";
        assertTrue(output.contains(expectedMsg1), "Output should include msg1 details: [" + expectedMsg1 + "]");
        assertTrue(output.contains(expectedMsg2), "Output should include msg2 details: [" + expectedMsg2 + "]");

        System.setOut(System.out);
    }

    // Helper method to convert string to hex
    private String toHex(String str) {
        StringBuilder hex = new StringBuilder();
        for (char c : str.toCharArray()) {
            hex.append(String.format("%02X", (int) c));
        }
        return hex.toString();
    }
    
 @Test
    @SuppressWarnings("ConvertToTryWithResources")
public void testLoadMessagesFromJson() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream("/messages.json");
    if (inputStream == null) {
        fail("Resource /messages.json not found in src/test/resources/. Please ensure the file exists.");
        return;
    }

    ObjectMapper mapper = new ObjectMapper();
    Message[] messages = mapper.readValue(inputStream, Message[].class);
    MessageLastPart testSystem = new MessageLastPart();
    for (Message message : messages) {
        message.initializeIfNeeded();
        testSystem.addMessage(message);
    }
    inputStream.close();

    ArrayList<Message> sentMessages = testSystem.getSentMessages();
    assertEquals(1, sentMessages.size(), "Should load 1 sent message");
    assertEquals("+27834557896", sentMessages.get(0).getRecipient(), "Recipient should match");
    assertEquals("Test message 1", sentMessages.get(0).getMessage(), "Message content should match");
    assertEquals("Sent", sentMessages.get(0).getFlag(), "Flag should be Sent");

    ArrayList<Message> storedMessages = testSystem.getStoredMessages();
    assertEquals(1, storedMessages.size(), "Should load 1 stored message");
    assertEquals("+27838884567", storedMessages.get(0).getRecipient(), "Recipient should match");
    assertEquals("Test message 2", storedMessages.get(0).getMessage(), "Message content should match");
    assertEquals("Stored", storedMessages.get(0).getFlag(), "Flag should be Stored");
}
}
