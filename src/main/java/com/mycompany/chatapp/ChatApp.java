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

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatApp {
    public static void main(String[] args) {
        Login login = new Login();
        MessageLastPart messageSystem = new MessageLastPart();
        boolean isRunning = true;

        JOptionPane.showMessageDialog(null, "Welcome to the Chat Application",
                "Welcome", JOptionPane.INFORMATION_MESSAGE);

        boolean isAuthenticated = false;
        String loggedInUserCellPhone = null;
        while (isRunning && !isAuthenticated) {
            String[] options = {"Register", "Login", "Exit"};
            int choice = JOptionPane.showOptionDialog(null,
                    "Please select an option",
                    "Chat Application",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (choice) {
                case 0: // Register
                    String username = JOptionPane.showInputDialog(null,
                            "Enter username (must contain _ and be <=5 characters):",
                            "Registration", JOptionPane.PLAIN_MESSAGE);
                    if (username == null) continue;

                    String password = JOptionPane.showInputDialog(null,
                            "Enter password (min 8 characters, with 1 capital letter, number, special character):",
                            "Registration", JOptionPane.PLAIN_MESSAGE);
                    if (password == null) continue;

                    String cellPhone = JOptionPane.showInputDialog(null,
                            "Enter cell phone number (e.g., +27123456789):",
                            "Registration", JOptionPane.PLAIN_MESSAGE);
                    if (cellPhone == null) continue;

                    String firstName = JOptionPane.showInputDialog(null,
                            "Enter first name:",
                            "Registration", JOptionPane.PLAIN_MESSAGE);
                    if (firstName == null) continue;

                    String lastName = JOptionPane.showInputDialog(null,
                            "Enter last name:",
                            "Registration", JOptionPane.PLAIN_MESSAGE);
                    if (lastName == null) continue;

                    String registrationResult = login.registerUser(username, password, cellPhone, firstName, lastName);
                    JOptionPane.showMessageDialog(null, registrationResult,
                            "Registration Result", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case 1: // Login
                    String loginUsername = JOptionPane.showInputDialog(null,
                            "Enter username:",
                            "Login", JOptionPane.PLAIN_MESSAGE);
                    if (loginUsername == null) continue;

                    String loginPassword = JOptionPane.showInputDialog(null,
                            "Enter password:",
                            "Login", JOptionPane.PLAIN_MESSAGE);
                    if (loginPassword == null) continue;

                    String loginResult = login.returnLoginStatus(loginUsername, loginPassword);
                    JOptionPane.showMessageDialog(null, loginResult,
                            "Login Result", JOptionPane.INFORMATION_MESSAGE);

                    if (loginResult.contains("Welcome")) {
                        isAuthenticated = true;
                        loggedInUserCellPhone = login.getUser().getCellPhoneNumber();
                        if (loggedInUserCellPhone == null || loggedInUserCellPhone.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "User cell phone number not found.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            isAuthenticated = false;
                        }
                    }
                    break;

                case 2: // Exit
                    isRunning = false;
                    JOptionPane.showMessageDialog(null,
                            "Thank you for using the Chat Application. Goodbye!",
                            "Goodbye", JOptionPane.INFORMATION_MESSAGE);
                    break;

                default:
                    if (choice == -1) {
                        isRunning = false;
                        JOptionPane.showMessageDialog(null,
                                "Thank you for using the Chat Application. Goodbye!",
                                "Goodbye", JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
            }
        }

        if (isAuthenticated) {
            boolean continueMessaging = true;
            while (continueMessaging) {
                String[] options = {"New Message", "Search", "Display Sent Messages", "Exit"};
                int choice = JOptionPane.showOptionDialog(null,
                        "What would you like to do?",
                        "Messaging Options",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (choice == 3 || choice == -1) { // Exit
                    JOptionPane.showMessageDialog(null,
                            "Thank you for using the Chat Application. Goodbye!",
                            "Goodbye", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }

                if (choice == 0) { // New Message
                    String recipient = JOptionPane.showInputDialog(null,
                            "Enter recipient number (e.g., +27721384462):",
                            "Input Recipient", JOptionPane.QUESTION_MESSAGE);

                    if (recipient == null || recipient.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Recipient number is required.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    String message = JOptionPane.showInputDialog(null,
                            "Enter message (max 250 characters):",
                            "Input Message", JOptionPane.QUESTION_MESSAGE);

                    if (message == null || message.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null,
                                "Message is required.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    String[] messageOptions = {"Send Now", "Store for Later", "Disregard"};
                    int messageChoice = JOptionPane.showOptionDialog(null,
                            "Choose an action for the message:",
                            "Message Options",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            messageOptions,
                            messageOptions[0]);

                    if (messageChoice == -1) {
                        JOptionPane.showMessageDialog(null, "Action cancelled.",
                                "Cancelled", JOptionPane.WARNING_MESSAGE);
                        continue;
                    }

                    String flag;
                    switch (messageChoice) {
                        case 0: flag = "Sent"; break;
                        case 1: flag = "Stored"; break;
                        case 2: flag = "Disregard"; break;
                        default: flag = "Disregard"; break;
                    }

                    Message msg = new Message(recipient, loggedInUserCellPhone, message, flag);

                    if (msg.checkMessage() && msg.checkRecipientCell() && msg.checkSenderCell() && msg.checkMessageId()) {
                        messageSystem.addMessage(msg);

                        JOptionPane.showMessageDialog(null,
                                "Message processed:\n" + msg.printMessages(),
                                "Message Details", JOptionPane.INFORMATION_MESSAGE);

                        switch (messageChoice) {
                            case 0: // Send Now
                                String sendResult = msg.sendMessage();
                                if (sendResult.contains("successfully sent.")) {
                                  boolean storeSuccess = msg.storeMessage();  
                                if (storeSuccess) {
                                    JOptionPane.showMessageDialog(null, "Message sent and stored successfully.",
                                            "Send and Store Result", JOptionPane.INFORMATION_MESSAGE);
                                }else{
                                    JOptionPane.showMessageDialog(null, "Message sent but failed to store.",
                                            "Send and Store Error", JOptionPane.ERROR_MESSAGE);
                                }
                                }else{
                                    JOptionPane.showMessageDialog(null, sendResult, "Send Result", JOptionPane.INFORMATION_MESSAGE);
                                }
                                        
                                break;

                            case 1: // Store for Later
                                boolean storeSuccess = msg.storeMessage();
                                if (storeSuccess) {
                                    JOptionPane.showMessageDialog(null, "Message stored successfully.",
                                            "Storage Success", JOptionPane.INFORMATION_MESSAGE);
                                    String[] deleteOptions = {"Keep Message", "Delete Message"};
                                    int deleteChoice = JOptionPane.showOptionDialog(null,
                                            "Do you want to delete the stored message?",
                                            "Delete Option", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                                            null, deleteOptions, deleteOptions[0]);
                                    if (deleteChoice == 1) {
                                        boolean deleted = msg.deleteMessage();
                                        if (deleted) {
                                            JOptionPane.showMessageDialog(null, "Message deleted from storage.",
                                                    "Deletion Success", JOptionPane.INFORMATION_MESSAGE);
                                            messageSystem.deleteByHash(msg.getMessageHash());
                                        } else {
                                            JOptionPane.showMessageDialog(null, "Failed to delete message.",
                                                    "Deletion Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Failed to store message.",
                                            "Storage Error", JOptionPane.ERROR_MESSAGE);
                                }
                                break;

                            case 2: // Disregard
                                JOptionPane.showMessageDialog(null, "Message disregarded.",
                                        "Disregard", JOptionPane.INFORMATION_MESSAGE);
                                break;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Invalid input. Ensure sender and recipient are valid South African numbers (+27 followed by 9 digits) and message is <= 250 characters.",
                                "Validation Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                } else if (choice == 1) { // Search
                    String[] searchOptions = {"Search by Recipient", "Search by Message ID"};
                    int searchChoice = JOptionPane.showOptionDialog(null,
                            "How would you like to search?",
                            "Search Options",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            searchOptions,
                            searchOptions[0]);

                    if (searchChoice == -1) {
                        JOptionPane.showMessageDialog(null, "Search cancelled.",
                                "Cancelled", JOptionPane.WARNING_MESSAGE);
                        continue;
                    }

                    if (searchChoice == 0) { // Search by Recipient
                        String recipient = JOptionPane.showInputDialog(null,
                                "Enter recipient number to search (e.g., +27721384462):",
                                "Search by Recipient", JOptionPane.QUESTION_MESSAGE);

                        if (recipient == null || recipient.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Recipient number is required.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }

                        ArrayList<Message> messages = messageSystem.searchByRecipient(recipient);
                        if (messages.isEmpty()) {
                            JOptionPane.showMessageDialog(null,
                                    "No messages found for recipient: " + recipient,
                                    "Search Result", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            StringBuilder result = new StringBuilder("Messages for " + recipient + ":\n");
                            for (Message msg : messages) {
                                result.append("ID: ").append(msg.getMessageId())
                                      .append(", Sender: ").append(msg.getSender())
                                      .append(", Message: ").append(msg.getMessage())
                                      .append(", Status: ").append(msg.getFlag()).append("\n");
                            }
                            JOptionPane.showMessageDialog(null,
                                    result.toString(),
                                    "Search Result", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else if (searchChoice == 1) { // Search by Message ID
                        String messageId = JOptionPane.showInputDialog(null,
                                "Enter message ID to search:",
                                "Search by Message ID", JOptionPane.QUESTION_MESSAGE);

                        if (messageId == null || messageId.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Message ID is required.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }

                        String result = messageSystem.searchByMessageId(messageId);
                        JOptionPane.showMessageDialog(null,
                                result,
                                "Search Result", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if (choice == 2) { // Display Sent Messages
                    java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
                    System.setOut(new java.io.PrintStream(outContent));
                    messageSystem.displaySentMessages();
                    System.setOut(System.out);
                    String displayResult = outContent.toString().isEmpty() ?
                            "No sent messages to display." : outContent.toString();
                    JOptionPane.showMessageDialog(null,
                            displayResult,
                            "Sent Messages", JOptionPane.INFORMATION_MESSAGE);
                }

                int totalSent = getSentMessageCount(messageSystem);
                int totalStoredInMemory = getStoredMessageCount(messageSystem);
                int totalDisregarded = getDisregardedMessageCount(messageSystem);
                int totalStoredInFile = getStoredMessageCountFromFile();
                String statsMessage = "Statistics:\n" +
                        "Total messages sent: " + totalSent + "\n" +
                        "Total messages stored in memory: " + totalStoredInMemory + "\n" +
                        "Total messages stored in JSON: " + (totalStoredInFile >= 0 ? totalStoredInFile : "Error reading file") + "\n" +
                        "Total messages disregarded: " + totalDisregarded;
                JOptionPane.showMessageDialog(null,
                        statsMessage,
                        "Statistics", JOptionPane.INFORMATION_MESSAGE);

                java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
                System.setOut(new java.io.PrintStream(outContent));
                messageSystem.displayReport();
                System.setOut(System.out);
                JOptionPane.showMessageDialog(null,
                        "Sent Messages Report:\n" + (outContent.toString().isEmpty() ? "No sent messages." : outContent.toString()),
                        "Sent Messages Report", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private static int getSentMessageCount(MessageLastPart system) {
        ArrayList<Message> sentMessages = system.searchByRecipient("");
        return (int) sentMessages.stream().filter(msg -> "Sent".equals(msg.getFlag())).count();
    }

    private static int getStoredMessageCount(MessageLastPart system) {
        ArrayList<Message> storedMessages = system.searchByRecipient("");
        return (int) storedMessages.stream().filter(msg -> "Stored".equals(msg.getFlag())).count();
    }

    private static int getDisregardedMessageCount(MessageLastPart system) {
        ArrayList<Message> allMessages = system.searchByRecipient("");
        return (int) allMessages.stream().filter(msg -> "Disregard".equals(msg.getFlag())).count();
    }

    private static int getStoredMessageCountFromFile() {
        try {
            File file = new File(System.getProperty("java.io.tmpdir"), "test_messages.json");
            if (!file.exists()) {
                return 0;
            }
            ObjectMapper mapper = new ObjectMapper();
            List<Message> messages = mapper.readValue(file,
                    mapper.getTypeFactory().constructCollectionType(List.class, Message.class));
            return messages.size();
        } catch (IOException e) {
            return -1;
        }
    }
}

 

/*reference list
chatGPT
*/