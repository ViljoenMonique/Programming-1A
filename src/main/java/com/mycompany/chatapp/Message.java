/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

/**
 *
 * @author mnqvi
 */


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javax.swing.JOptionPane;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class Message {
    private String messageId;
    public String messageHash;
    private String recipient;
    private String sender;
    public String message;
    private String flag;
    private static int numMessagesSent = 0;
    private static int messageIdCounter = 0;

    // Constructor
    @JsonCreator
    public Message(@JsonProperty("recipient") String recipient, @JsonProperty("sender") String sender, @JsonProperty("message") String message,
            @JsonProperty("messageId") String messageId, @JsonProperty("messageHash") String messageHash,
            @JsonProperty("flag") String flag) {
        this.recipient = recipient;
        this.sender = sender;
        this.message = message;
        this.messageId = (messageId != null) ? messageId : generateMessageId(); // Use provided ID or generate new
        this.messageHash = (messageHash != null) ? messageHash : createMessageHash(); // Use provided hash or generate new
        this.flag = flag;
        System.out.println("Constructor: messageId=" + this.messageId + ", messageHash=" + this.messageHash +
                ", recipient=" + this.recipient + ",sender=" + this.sender + ", message=" + this.message + ", flag=" + this.flag);
        if (checkMessage() && checkRecipientCell()) {
            // No action needed here, just validation
        }
    }

    // Constructor for new messages (used by application)
    public Message(String recipient, String sender, String message, String flag) {
        this(recipient, sender, message, null, null, flag);
    }

    // Check if message ID is not more than 10 characters
    public boolean checkMessageId() {
        boolean valid = messageId != null && messageId.length() <= 10;
        System.out.println("checkMessageId: messageId=" + messageId + ", valid=" + valid);
        return valid;
    }

    // Check if recipient cell number is a valid South African number (+27 followed by 9 digits)
    public final boolean checkRecipientCell() {
        boolean valid = recipient != null && recipient.matches("\\+27[0-9]{9}");
        System.out.println("checkRecipientCell: recipient=" + recipient + ", valid=" + valid);
        return valid;
    }
    
    public final boolean checkSenderCell() {
        boolean valid = sender != null && sender.matches("\\+27[0-9]{9}");
        System.out.println("checkSenderCell: sender=" +sender + ",valid=" + valid);
        return valid;
    }

    // Generate a random 10-digit message ID
    private String generateMessageId() {
        return String.format("%010d", messageIdCounter++);
    }

    // Create a message hash (first two digits of ID, colon, message number, colon, first and last words)
    public final String createMessageHash() {
        String firstTwo = (messageId != null && messageId.length() >= 2) ? messageId.substring(0, 2) : "00";
        String firstWord = (message == null || message.trim().isEmpty()) ? "" :
                message.trim().split("\\s+")[0];
        String lastWord = (message == null || message.trim().isEmpty()) ? "" :
                message.trim().split("\\s+")[message.trim().split("\\s+").length - 1];
        String hash = firstTwo + ":" + numMessagesSent + ":" + firstWord + lastWord;
        System.out.println("createMessageHash: hash=" + hash);
        return hash;
    }
    
    //Post deserialization initialization
    public void initializeIfNeeded() {
        if (messageId == null) {
            this.messageId = generateMessageId();
            }
        if (messageHash == null) {
            this.messageHash = createMessageHash();
        }
    }

    // Validate and send message
    public String sendMessage() {
        System.out.println("sendMessage: messageId=" + messageId + ", recipient=" + recipient +
                ", message=" + message + ", flag=" + flag + ", numMessagesSent=" + numMessagesSent);
        if (checkMessage() && checkRecipientCell() && checkMessageId() && checkSenderCell()) {
            numMessagesSent++;
            this.messageHash = createMessageHash();
            return "Message successfully sent.";
        }
        return "Failed to send: Invalid message, recipient, sender, or message ID.";
    }

    // Print all message details
    public String printMessages() {
        return "Message ID:" + messageId + ",Message Hash:" + messageHash + ",Recipient:" + recipient +
               ",sender:" + sender + ",Message:" + message + ",Flag:" + flag;
    }

    // Return total number of messages sent
    @JsonIgnore
    public static int returnTotalMessages() {
        return numMessagesSent;
    }

    // Store message in JSON file
    public boolean storeMessage() {
        if (!checkMessage() || !checkRecipientCell() || !checkMessageId() || !checkSenderCell()) {
            System.err.println("Cannot store: Invalid message, recipient, sender or message ID ");
            JOptionPane.showMessageDialog(null,
                    "Cannot store invalid message, recipient, sender or message ID.",
                    "Storage error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print JSON
            File file = new File(System.getProperty("java.io.tmpdir"), "test_messages.json");

            // Read existing messages or initialize empty list
            List<Message> messages;
            if (file.exists()) {
                messages = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Message.class));
            } else {
                messages = new ArrayList<>();
            }

            // Add current message
            messages.add(this);
            mapper.writeValue(file, messages);

            // Show confirmation dialog
            JOptionPane.showMessageDialog(null, "Message successfully stored in test_messages.json: " + printMessages(),
                    "Storage success", JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error storing message: " + e.getMessage(),
                    "Storage error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Delete message from JSON file by messageId
    public boolean deleteMessage() {
        try {
            File file = new File(System.getProperty("java.io.tmpdir"), "test_messages.json");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null,
                        "No stored messages to delete.",
                        "Deletion Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            ObjectMapper mapper = new ObjectMapper();
            List<Message> messages = mapper.readValue(file,
                    mapper.getTypeFactory().constructCollectionType(List.class, Message.class));
            boolean removed = messages.removeIf(msg -> msg.getMessageId().equals(this.messageId));

            if (removed) {
                mapper.writeValue(file, messages);
                JOptionPane.showMessageDialog(null,
                        "Message successfully deleted: " + printMessages(),
                        "Deletion Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Message not found in test_messages.json: " + printMessages(),
                        "Deletion Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error deleting message: " + e.getMessage(),
                    "Deletion Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Check message validity
    public final boolean checkMessage() {
        boolean valid = message != null && message.length() <= 250;
        System.out.println("checkMessage: message=" + message + ", valid=" + valid);
        return valid;
    }

    // Getters for Jackson serialization
    @JsonProperty("sender")
    public String getSender() {
        return sender;
    }
    
    @JsonProperty("messageId")
    public String getMessageId() {
        return messageId;
    }

    @JsonProperty("messageHash")
    public String getMessageHash() {
        return messageHash;
    }

    @JsonProperty("recipient")
    public String getRecipient() {
        return recipient;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("flag")
    public String getFlag() {
        return flag;
    }

    // Setters for Jackson deserialization
    @JsonProperty("sender")
    public void setSender(String sender) {
        this.sender = sender;
    }
    @JsonProperty("messageId")
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @JsonProperty("recipient")
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("flag")
    public void setFlag(String flag) {
        this.flag = flag;
    }
}
  
    
    
    
    
    
  
    
    
    
