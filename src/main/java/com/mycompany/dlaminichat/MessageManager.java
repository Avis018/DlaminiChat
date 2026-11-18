package com.mycompany.dlaminichat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Student
 */
public class MessageManager {
    
    public static final int MAX_MESSAGES = 1000;
    
    public static final String NO_STORED_MESSAGES = "No stored messages.";
    public static final String NO_MESSAGES = "No messages to display.";
    public static final String ID_NOT_FOUND = "Message ID not found.";
    public static final String HASH_NOT_FOUND = "Message hash not found.";
    
    // Parallel arrays holding every message
    private String[] messageIDs = new String[MAX_MESSAGES];
    private String[] messageHashes = new String[MAX_MESSAGES];
    private String[] recipients = new String[MAX_MESSAGES];
    private String[] messageTexts = new String[MAX_MESSAGES];
    private int[] flags = new int[MAX_MESSAGES];   // Message.SEND, DISREGARD or STORE
    private int totalMessages = 0;
    
    // Arrays: the text of the sent, disregarded and stored messages
    private String[] sentMessages = new String[MAX_MESSAGES];
    private String[] disregardedMessages = new String[MAX_MESSAGES];
    private String[] storedMessages = new String[MAX_MESSAGES];
    private int sentCount = 0;
    private int disregardedCount = 0;
    private int storedCount = 0;
    
    private int jsonPosition = 0;
    
    //====== Populate the arrays ======

    // flag must be Message.SEND, Message.DISREGARD or Message.STORE
    public boolean addMessage(String messageID, String messageHash, String recipient,
            String messageText, int flag) {
        if (totalMessages >= MAX_MESSAGES) {
            return false;
        }

        if (flag == Message.SEND) {
            sentMessages[sentCount] = messageText;
            ++sentCount;
        } else if (flag == Message.DISREGARD) {
            disregardedMessages[disregardedCount] = messageText;
            ++disregardedCount;
        } else if (flag == Message.STORE) {
            storedMessages[storedCount] = messageText;
            ++storedCount;
        } else {
            return false;
        }

        messageIDs[totalMessages] = messageID;
        messageHashes[totalMessages] = messageHash;
        recipients[totalMessages] = recipient;
        messageTexts[totalMessages] = messageText;
        flags[totalMessages] = flag;
        ++totalMessages;
        return true;
    }
    
    public boolean addMessage(Message message, int flag) {
        return addMessage(message.getMessageID(), message.getMessageHash(),
                message.getRecipient(), message.getMessageText(), flag);
    }

    public String[] getSentMessages() {
        return copyElements(sentMessages, sentCount);
    }

    public String[] getDisregardedMessages() {
        return copyElements(disregardedMessages, disregardedCount);
    }

    public String[] getStoredMessages() {
        return copyElements(storedMessages, storedCount);
    }

    public String[] getMessageHashes() {
        return copyElements(messageHashes, totalMessages);
    }

    public String[] getMessageIDs() {
        return copyElements(messageIDs, totalMessages);
    }
    
    // Copies the first count elements, so callers cannot change the arrays
    private String[] copyElements(String[] source, int count) {
        String[] copy = new String[count];
        for (int x = 0; x < count; ++x) {
            copy[x] = source[x];
        }
        return copy;
    }
    
    //====== Read the JSON file into the stored messages array ======
    
    /*
     * Attribution:
     * - Reading a text file line by line with BufferedReader: Farrell (2019), Java
     *   Programming 9th ed., Chapter 13 "File Input and Output" (Using Java's IO Classes).
     * - JSON layout (objects, "key": value pairs, escaped quotes inside strings):
     *   https://www.json.org/json-en.html
     */
    
   public int loadStoredMessages(String fileName) {
        String json = readFile(fileName);
        if (json == null) {
            return -1;
        }

        int added = 0;
        jsonPosition = 0;
        String id = readJsonValue(json, "messageID");

        while (id != null) {
            String recipient = readJsonValue(json, "recipient");
            String text = readJsonValue(json, "message");
            String hash = readJsonValue(json, "messageHash");

            if (recipient == null || text == null || hash == null) {
                return added;
            }
            if (findIndexByID(id) == -1 && addMessage(id, hash, recipient, text, Message.STORE)) {
                ++added;
            }
            id = readJsonValue(json, "messageID");
        }
        return added;
    }
   
   private String readFile(String fileName) {
        StringBuilder contents = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                contents.append(line).append("\n");
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            return null;
        }
        return contents.toString();
    }
   
   private String readJsonValue(String json, String key) {
        int keyStart = json.indexOf("\"" + key + "\"", jsonPosition);
        if (keyStart == -1) {
            return null;
        }

        int start = json.indexOf(':', keyStart) + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            ++start;
        }

        StringBuilder value = new StringBuilder();
        int x = start;
        if (start < json.length() && json.charAt(start) == '"') {
            x = start + 1;
            while (x < json.length() && json.charAt(x) != '"') {
                char letter = json.charAt(x);
                if (letter == '\\' && x + 1 < json.length()) {
                    ++x;
                    letter = unescape(json.charAt(x));
                }
                value.append(letter);
                ++x;
            }
            ++x;   // skip the closing quote
        } else {
            while (x < json.length() && json.charAt(x) != ',' && json.charAt(x) != '\n') {
                value.append(json.charAt(x));
                ++x;
            }
        }
        jsonPosition = x;
        return value.toString().trim();
    }
   
   // Turns the letter after a backslash back into the character it stands for
    private char unescape(char letter) {
        if (letter == 't') {
            return '\t';
        } else if (letter == 'n') {
            return '\n';
        }
        return letter;   // \" and \\ become " and \
    }
    
    //====== Display the stored messages ======

    // a. Sender and recipient of all stored messages
    public String displaySendersAndRecipients(String sender) {
        String result = "";
        for (int x = 0; x < totalMessages; ++x) {
            if (flags[x] == Message.STORE) {
                result = result + "Sender: " + sender + "\n"
                        + "Recipient: " + recipients[x] + "\n\n";
            }
        }
        if (result.equals("")) {
            return NO_STORED_MESSAGES;
        }
        return result;
    }
    
    // Numbered list of the stored messages array
    public String listStoredMessages() {
        String result = "";
        for (int x = 0; x < storedCount; ++x) {
            result = result + (x + 1) + ") " + storedMessages[x] + "\n";
        }
        if (result.equals("")) {
            return NO_STORED_MESSAGES;
        }
        return result;
    }
    
    // b. The longest stored message
    public String longestStoredMessage() {
        if (storedCount == 0) {
            return NO_STORED_MESSAGES;
        }
        String longest = storedMessages[0];
        for (int x = 1; x < storedCount; ++x) {
            if (storedMessages[x].length() > longest.length()) {
                longest = storedMessages[x];
            }
        }
        return longest;
    }
    
    //====== Search ======

    // c. Message text for a message ID
    public String searchByMessageID(String messageID) {
        int index = findIndexByID(messageID);
        if (index == -1) {
            return ID_NOT_FOUND;
        }
        return messageTexts[index];
    }
    
    // c. Recipient and message for a message ID
    public String displayByMessageID(String messageID) {
        int index = findIndexByID(messageID);
        if (index == -1) {
            return ID_NOT_FOUND;
        }
        return "Recipient: " + recipients[index] + "\nMessage: " + messageTexts[index];
    }
    
    // d. Every sent or stored message for one recipient (disregarded messages are not included)
    public String[] searchByRecipient(String recipient) {
        int matches = 0;
        for (int x = 0; x < totalMessages; ++x) {
            if (recipients[x].equals(recipient) && flags[x] != Message.DISREGARD) {
                ++matches;
            }
        }

        String[] found = new String[matches];
        int next = 0;
        for (int x = 0; x < totalMessages; ++x) {
            if (recipients[x].equals(recipient) && flags[x] != Message.DISREGARD) {
                found[next] = messageTexts[x];
                ++next;
            }
        }
        return found;
    }
    
    private int findIndexByID(String messageID) {
        for (int x = 0; x < totalMessages; ++x) {
            if (messageIDs[x].equals(messageID)) {
                return x;
            }
        }
        return -1;
    }
    
    private int findIndexByHash(String messageHash) {
        for (int x = 0; x < totalMessages; ++x) {
            if (messageHashes[x].equals(messageHash)) {
                return x;
            }
        }
        return -1;
    }
    
    //====== Delete ======

    // e. Removes the message with this hash from every array
    public String deleteByHash(String messageHash) {
        int index = findIndexByHash(messageHash);
        if (index == -1) {
            return HASH_NOT_FOUND;
        }

        String deletedText = messageTexts[index];
        removeFromCategoryArray(flags[index], positionInCategory(index));

        for (int x = index; x < totalMessages - 1; ++x) {
            messageIDs[x] = messageIDs[x + 1];
            messageHashes[x] = messageHashes[x + 1];
            recipients[x] = recipients[x + 1];
            messageTexts[x] = messageTexts[x + 1];
            flags[x] = flags[x + 1];
        }
        --totalMessages;

        // the brief's wording has no full stop inside the closing quote
        if (deletedText.endsWith(".")) {
            deletedText = deletedText.substring(0, deletedText.length() - 1);
        }
        return "Message: \"" + deletedText + "\" successfully deleted.";
    }
    
    // How many messages with the same flag come before this subscript
    private int positionInCategory(int index) {
        int position = 0;
        for (int x = 0; x < index; ++x) {
            if (flags[x] == flags[index]) {
                ++position;
            }
        }
        return position;
    }
    
    private void removeFromCategoryArray(int flag, int position) {
        if (flag == Message.SEND) {
            sentCount = removeElement(sentMessages, sentCount, position);
        } else if (flag == Message.DISREGARD) {
            disregardedCount = removeElement(disregardedMessages, disregardedCount, position);
        } else {
            storedCount = removeElement(storedMessages, storedCount, position);
        }
    }
    
    // Shifts the later elements left and returns the new count
    private int removeElement(String[] array, int count, int position) {
        for (int x = position; x < count - 1; ++x) {
            array[x] = array[x + 1];
        }
        return count - 1;
    }
    
    //====== Report ======

    // f. Full details of every message with this flag (Message.STORE for the stored report)
    public String buildReport(int flag) {
        String report = "";
        for (int x = 0; x < totalMessages; ++x) {
            if (flags[x] == flag) {
                report = report + "Message ID: " + messageIDs[x] + "\n"
                        + "Message Hash: " + messageHashes[x] + "\n"
                        + "Recipient: " + recipients[x] + "\n"
                        + "Message: " + messageTexts[x] + "\n\n";
            }
        }
        if (report.equals("")) {
            return NO_MESSAGES;
        }
        return report;
    }
}
