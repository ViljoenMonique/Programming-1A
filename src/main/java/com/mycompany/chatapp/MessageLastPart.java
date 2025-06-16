/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

/**
 *
 * @author mnqvi
 */

import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.File;

public class MessageLastPart {
    private final ArrayList<Message> sentMessages;
    private final ArrayList<Message> disregardedMessages;
    private final ArrayList<Message> storedMessages;
    private final ArrayList<String> messageHashes;
    private final ArrayList<String> messageIds;

    public MessageLastPart() {
        sentMessages = new ArrayList<>();
        disregardedMessages = new ArrayList<>();
        storedMessages = new ArrayList<>();
        messageHashes = new ArrayList<>();
        messageIds = new ArrayList<>();
    }

    // Add a message to appropriate arrays based on flag
    @SuppressWarnings("ConvertToStringSwitch")
    public void addMessage(Message message) {
        if (message.getFlag().equals("Sent")) {
            sentMessages.add(message);
        } else if (message.getFlag().equals("Disregard")) {
            disregardedMessages.add(message);
        } else if (message.getFlag().equals("Stored")) {
            storedMessages.add(message);
        }
        messageHashes.add(message.getMessageHash());
        messageIds.add(message.getMessageId());
    }

    // Display sender and recipient of all sent messages
    public void displaySentMessages() {
        System.out.println("Sent Messages:");
        for (Message msg : sentMessages) {
            System.out.println("sender:" + msg.getSender() + ", Recipient: " + msg.getRecipient() + ", Message: " + msg.getMessage());
        }
        
    }

    // Display the longest sent message
    public String getLongestMessage() {
        Message longest = null;
        int maxLength = 0;
        //Check sentMessages
        for (Message msg : sentMessages) {
            if (msg.getMessage() != null && msg.getMessage().length() > maxLength) {
                maxLength = msg.getMessage().length();
                longest = msg;
            }
        }
        //Check storedMessages
        for (Message msg : storedMessages) {
            if (msg.getMessage() != null && msg.getMessage().length() > maxLength) {
                maxLength = msg.getMessage().length();
                longest = msg;
            }
        }
        return longest != null ? longest.getMessage() : "No message found";
    }

    // Search for message by ID
   public String searchByMessageId(String messageId) {
        System.out.println("Searching for messageId: " + messageId);
        for (Message msg : sentMessages) {
            System.out.println("Checking sentMessages: messageId=" + msg.getMessageId() + ", recipient=" + msg.getRecipient() + ", message=" + msg.getMessage());
            if (msg.getMessageId().equals(messageId)) {
                System.out.println("Match found in sentMessages: " + msg.getRecipient() + ", " + msg.getMessage());
                return "Sender: " + msg.getSender() + ", Recipient: " + msg.getRecipient() + ", Message: " + msg.getMessage();
            }
        }
        for (Message msg : storedMessages) {
            System.out.println("Checking storedMessages: messageId=" + msg.getMessageId() + ", recipient=" + msg.getRecipient() + ", message=" + msg.getMessage());
            if (msg.getMessageId().equals(messageId)) {
                System.out.println("Match found in storedMessages: " + msg.getRecipient() + ", " + msg.getMessage());
                return "Sender: " + msg.getSender() + "Recipient: " + msg.getRecipient() + ", Message: " + msg.getMessage();
            }
        }
        System.out.println("No match found for messageId: " + messageId);
        return "Message not found";
    }

    // Search messages by recipient
    public ArrayList<Message> searchByRecipient(String recipient) {
        ArrayList<Message> result = new ArrayList<>();
        // If recipient is empty, return all messages
        if (recipient == null || recipient.isEmpty()) {
            result.addAll(sentMessages);
            result.addAll(storedMessages);
        } else {
            // Otherwise, filter by recipient
            System.out.println("Searching for recipient: " + recipient);
            for (Message msg : sentMessages) {
                System.out.println("Checking sent message: " + msg.getRecipient() + ", " + msg.getMessage());
                if (msg.getRecipient().equals(recipient)) {
                    result.add(msg);
                }
            }
        
            for (Message msg : storedMessages) {
                System.out.println("Checking stored message: " + msg.getRecipient() + ", " + msg.getMessage());
                if (msg.getRecipient().equals(recipient)) {
                    result.add(msg);
                }
            }
    }
        System.out.println("Found " + result.size() + "message for " + recipient);
        return result;
    }
    //Adding a getter
    public ArrayList<Message> getSentMessages() {
        return new ArrayList<>(sentMessages);
    }
    
      public ArrayList<Message> getStoredMessages() {
        return new ArrayList<>(storedMessages);
    }
    // Delete message by hash
    public String deleteByHash(String messageHash) {
        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i).getMessageHash().equals(messageHash)) {
                String content = sentMessages.get(i).getMessage();
                sentMessages.remove(i);
                messageHashes.remove(messageHash);
                return "Message \"" +content+ "\" successfully deleted.";
            }
        }
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).getMessageHash().equals(messageHash)) {
                String content = storedMessages.get(i).getMessage();
                storedMessages.remove(i);
                messageHashes.remove(messageHash);
                return "Message \"" + content + "\" successfully deleted.";
            }
        }
        return "Message not found";
    }

    // Display report of all sent messages
    public void displayReport() {
        System.out.println("Sent Message Report:");
        for (Message msg : sentMessages) {
            System.out.println("Message Hash: " + msg.getMessageHash());
            System.out.println("Sender: " + msg.getSender());
            System.out.println("Recipient: " + msg.getRecipient());
            System.out.println("Message: " + msg.getMessage());
            System.out.println("--------------");
        }
    }
    
    //Method to load JSON
    public void loadMessagesFromJson(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Message[] messages = mapper.readValue(new File(filePath), Message[].class);
        for (Message message : messages) {
            message.initializeIfNeeded(); 
            addMessage(message);
        }
    }
}