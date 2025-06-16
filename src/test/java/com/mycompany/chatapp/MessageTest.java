/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;




/**
 *
 * @author mnqvi
 */

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.util.List;
import java.io.IOException;

public class MessageTest {
    private Message message;
    private static final String VALID_SA_NUMBER = "+27821234567"; // Corrected format
    private static final String VALID_SENDER = "+27123456789"; // Added valid sender
    private static final String VALID_MESSAGE = "Hello from South Africa";
    @TempDir
    File tempDir;

    @BeforeEach
    public void setUp() {
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
        // Updated to include sender
        message = new Message(VALID_SA_NUMBER, VALID_SENDER, VALID_MESSAGE, "Sent");
    }

    @Test
    public void testCheckRecipientCell_ValidSouthAfricanNumber() {
        assertTrue(message.checkRecipientCell(), "Valid SA number should pass");
    }

    @Test
    public void testCheckRecipientCell_InvalidSouthAfricanNumber() {
        Message invalidMessage = new Message("+12012345678", VALID_SENDER, VALID_MESSAGE, "Sent");
        assertFalse(invalidMessage.checkRecipientCell(), "Non-SA number should fail");
    }

    @Test
    public void testCheckRecipientCell_TooShortNumber() {
        Message invalidMessage = new Message("+277123456", VALID_SENDER, VALID_MESSAGE, "Sent");
        assertFalse(invalidMessage.checkRecipientCell(), "Too short number should fail");
    }

    @Test
    public void testCheckRecipientCell_NullNumber() {
        Message invalidMessage = new Message(null, VALID_SENDER, VALID_MESSAGE, "Sent");
        assertFalse(invalidMessage.checkRecipientCell(), "Null recipient should fail");
    }

    @Test
    public void testCheckSenderCell_ValidSouthAfricanNumber() {
        assertTrue(message.checkSenderCell(), "Valid SA sender should pass");
    }

    @Test
    public void testCheckSenderCell_InvalidSouthAfricanNumber() {
        Message invalidMessage = new Message(VALID_SA_NUMBER, "+12012345678", VALID_MESSAGE, "Sent");
        assertFalse(invalidMessage.checkSenderCell(), "Non-SA sender should fail");
    }

    @Test
    public void testCheckSenderCell_TooShortNumber() {
        Message invalidMessage = new Message(VALID_SA_NUMBER, "+27123456", VALID_MESSAGE, "Sent");
        assertFalse(invalidMessage.checkSenderCell(), "Too short sender should fail");
    }

    @Test
    public void testCheckSenderCell_NullNumber() {
        Message invalidMessage = new Message(VALID_SA_NUMBER, null, VALID_MESSAGE, "Sent");
        assertFalse(invalidMessage.checkSenderCell(), "Null sender should fail");
    }

    @Test
    public void testCheckMessage_ValidMessage() {
        assertTrue(message.checkMessage(), "Valid message should pass");
    }

    @Test
    public void testCheckMessage_TooLongMessage() {
        String longMessage = "This is a very long message".repeat(10);
        Message invalidMessage = new Message(VALID_SA_NUMBER, VALID_SENDER, longMessage, "Sent");
        assertFalse(invalidMessage.checkMessage(), "Too long message should fail");
    }

    @Test
    public void testCheckMessage_NullMessage() {
        Message invalidMessage = new Message(VALID_SA_NUMBER, VALID_SENDER, null, "Sent");
        assertFalse(invalidMessage.checkMessage(), "Null message should fail");
    }

    @Test
    public void testSendMessage_ValidInput() {
        assertEquals("Message successfully sent.", message.sendMessage(), "Valid message should send");
        assertEquals(1, Message.returnTotalMessages(), "Total messages should increment");
    }

    @Test
    public void testSendMessage_InvalidRecipient() {
        Message invalidMessage = new Message("+12012345678", VALID_SENDER, VALID_MESSAGE, "Sent");
        assertEquals("Failed to send: Invalid message, recipient, sender, or message ID.",
                invalidMessage.sendMessage(), "Invalid recipient should fail");
        assertEquals(0, Message.returnTotalMessages(), "Total messages should not increment");
    }

    @Test
    public void testSendMessage_InvalidSender() {
        Message invalidMessage = new Message(VALID_SA_NUMBER, "+12012345678", VALID_MESSAGE, "Sent");
        assertEquals("Failed to send: Invalid message, recipient, sender, or message ID.",
                invalidMessage.sendMessage(), "Invalid sender should fail");
        assertEquals(0, Message.returnTotalMessages(), "Total messages should not increment");
    }

    @Test
    public void testSendMessage_TooLongMessage() {
        String longMessage = "This is a very long message".repeat(10);
        Message invalidMessage = new Message(VALID_SA_NUMBER, VALID_SENDER, longMessage, "Sent");
        assertEquals("Failed to send: Invalid message, recipient, sender, or message ID.",
                invalidMessage.sendMessage(), "Too long message should fail");
        assertEquals(0, Message.returnTotalMessages(), "Total messages should not increment");
    }

    @Test
    public void testSendMessage_InvalidMessageId() throws NoSuchFieldException, IllegalAccessException {
        Message invalidMessage = new Message(VALID_SA_NUMBER, VALID_SENDER, VALID_MESSAGE, "Sent");
        java.lang.reflect.Field field = Message.class.getDeclaredField("messageId");
        field.setAccessible(true);
        field.set(invalidMessage, "12345678901"); // 11 digits invalid
        assertEquals("Failed to send: Invalid message, recipient, sender, or message ID.",
                invalidMessage.sendMessage(), "Invalid message ID should fail");
        assertEquals(0, Message.returnTotalMessages(), "Total messages should not increment");
    }

    @Test
    public void testCheckMessageId_ValidId() {
        assertTrue(message.checkMessageId(), "Valid message ID should pass");
    }

    @Test
    public void testCreateMessageHash_ValidMessage() {
        String hash = message.createMessageHash();
        assertTrue(hash.matches("\\d{2}:\\d+:HelloAfrica"), "Hash should match pattern");
    }

    @Test
    public void testCreateMessageHash_EmptyMessage() {
        Message emptyMessage = new Message(VALID_SA_NUMBER, VALID_SENDER, "", "Sent");
        String hash = emptyMessage.createMessageHash();
        assertTrue(hash.matches("\\d{2}:\\d+:"), "Empty message hash should match pattern");
    }

    @Test
    public void testCreateMessageHash_NullMessage() {
        Message nullMessage = new Message(VALID_SA_NUMBER, VALID_SENDER, null, "Sent");
        String hash = nullMessage.createMessageHash();
        assertTrue(hash.matches("\\d{2}:\\d+:"), "Null message hash should match pattern");
    }

    @Test
    public void testReturnTotalMessages_AfterValidSend() {
        message.sendMessage();
        assertEquals(1, Message.returnTotalMessages(), "Total messages should be 1 after valid send");
    }

    @Test
    public void testReturnTotalMessages_AfterInvalidMessage() {
        Message invalidMessage = new Message("+12012345678", VALID_SENDER, VALID_MESSAGE, "Sent");
        invalidMessage.sendMessage();
        assertEquals(0, Message.returnTotalMessages(), "Total messages should be 0 after invalid send");
    }

    @Test
    public void testPrintMessages() {
        String output = message.printMessages();
        assertTrue(output.contains("Message ID:") &&
                   output.contains("Recipient:" + VALID_SA_NUMBER) &&
                   output.contains("sender:" + VALID_SENDER) &&
                   output.contains("Message:" + VALID_MESSAGE) &&
                   output.contains("Flag:Sent"), "Print should include all fields");
    }

    @Test
    public void testStoreMessage_SavesToJson_ValidMessage() throws IOException {
        File tempFile = new File(tempDir, "test_messages.json");
        System.setProperty("java.io.tmpdir", tempDir.getAbsolutePath());
        
        assertTrue(message.storeMessage(), "Valid message should store successfully");

        ObjectMapper mapper = new ObjectMapper();
        List<Message> savedMessages = mapper.readValue(tempFile,
                mapper.getTypeFactory().constructCollectionType(List.class, Message.class));

        assertEquals(1, savedMessages.size(), "One message should be stored");
        Message savedMessage = savedMessages.get(0);
        assertEquals(VALID_SA_NUMBER, savedMessage.getRecipient(), "Recipient should match");
        assertEquals(VALID_SENDER, savedMessage.getSender(), "Sender should match");
        assertEquals(VALID_MESSAGE, savedMessage.getMessage(), "Message should match");
        assertEquals("Sent", savedMessage.getFlag(), "Flag should match");
    }

    @Test
    public void testStoreMessage_DoesNotSave_InvalidMessage() throws IOException {
        Message invalidMessage = new Message("+12012345678", VALID_SENDER, VALID_MESSAGE, "Sent");
        File tempFile = new File(tempDir, "test_messages.json");
        System.setProperty("java.io.tmpdir", tempDir.getAbsolutePath());
        assertFalse(invalidMessage.storeMessage(), "Invalid recipient should not store");
        assertFalse(tempFile.exists(), "File should not exist");
    }

    @Test
    public void testStoreMessage_DoesNotSave_InvalidSender() throws IOException {
        Message invalidMessage = new Message(VALID_SA_NUMBER, "+12012345678", VALID_MESSAGE, "Sent");
        File tempFile = new File(tempDir, "test_messages.json");
        System.setProperty("java.io.tmpdir", tempDir.getAbsolutePath());
        assertFalse(invalidMessage.storeMessage(), "Invalid sender should not store");
        assertFalse(tempFile.exists(), "File should not exist");
    }

    @Test
    public void testStoreMessage_DoesNotSave_InvalidMessageId() throws NoSuchFieldException,
            IllegalAccessException, IOException {
        Message invalidMessage = new Message(VALID_SA_NUMBER, VALID_SENDER, VALID_MESSAGE, "Sent");
        java.lang.reflect.Field field = Message.class.getDeclaredField("messageId");
        field.setAccessible(true);
        field.set(invalidMessage, "12345678901"); // 11 digits invalid
        File tempFile = new File(tempDir, "test_messages.json");
        System.setProperty("java.io.tmpdir", tempDir.getAbsolutePath());
        assertFalse(invalidMessage.storeMessage(), "Invalid message ID should not store");
        assertFalse(tempFile.exists(), "File should not exist");
    }

    @Test
    public void testDeleteMessage_SuccessfulDeletion() throws IOException {
        File tempFile = new File(tempDir, "test_messages.json");
        System.setProperty("java.io.tmpdir", tempDir.getAbsolutePath());
        
        assertTrue(message.storeMessage(), "Message should store successfully");

        ObjectMapper mapper = new ObjectMapper();
        List<Message> savedMessages = mapper.readValue(tempFile,
                mapper.getTypeFactory().constructCollectionType(List.class, Message.class));
        assertEquals(1, savedMessages.size(), "One message should be stored");

        assertTrue(message.deleteMessage(), "Message should delete successfully");

        savedMessages = mapper.readValue(tempFile,
                mapper.getTypeFactory().constructCollectionType(List.class, Message.class));
        assertEquals(0, savedMessages.size(), "File should be empty after deletion");
    }

    @Test
    public void testDeleteMessage_NonExistentMessage() throws IOException {
        File tempFile = new File(tempDir, "test_messages.json");
        System.setProperty("java.io.tmpdir", tempDir.getAbsolutePath());

        Message otherMessage = new Message(VALID_SA_NUMBER, VALID_SENDER, "Different message", "Sent");
        assertTrue(otherMessage.storeMessage(), "Other message should store");

        Message nonExistentMessage = new Message(VALID_SA_NUMBER, VALID_SENDER, VALID_MESSAGE, "Sent");
        assertFalse(nonExistentMessage.deleteMessage(), "Non-existent message should not delete");
    }
}
        
        
   
 

    
    
    
    
     

