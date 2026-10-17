
import java.io.FileWriter;
import java.io.IOException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Student
 */
public class Message {
    
    public static final int MAX_LENGTH = 250;
    public static final int ID_LENGTH = 10;
    
    //the 3 choices after a message has been entered
    public static final int SEND = 1;
    public static final int DISREGARD = 2;
    public static final int STORE = 3;
    
    
    //for unit tests
    public static final String MESSAGE_READY = "Message ready to send";
    public static final String CELL_SUCCESS = "Cell phone number successfully captured";
    public static final String CELL_FAILURE = "Cell phone number is incorrectly formatted "
            + "or does not contain an international code. "
            + "Please correct the number and try again.";
    public static final String MESSAGE_SENT = "Message succesfully sent.";
    public static final String MESSAGE_DISREGARDED = "Press 0 to delete the message.";
    public static final String MESSAGE_STORED = "Message successfully stored.";
    public static final String MESSAGE_NOT_STORED = "Message could not be stored";
    public static final String INVALID_CHOICE = "Invalid option, please select 1, 2, or 3";
    
    private static StringBuilder sentMessageDetails = new StringBuilder();
    private static int totalMessagesSent = 0;
    private static StringBuilder storedMessagesJson = new StringBuilder();
    private static int totalMessagesStored = 0;
    private static String jsonFileName = "messages.json";
    
    
    //Data for one message
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    
    
    public Message(int messageNumber, String recipient, String messageText) {
        this(createMessageID(), messageNumber, recipient, messageText);
    }
    
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = messageHash;
    }
    
    //======= Checks =======
    
    //MessageID must not be more than 10 characters
    public boolean checkMessageID() {
        return messageID.length() <= ID_LENGTH;
    }
    
    // True when the number is +27 followed by 9 digits (the +27 counts as one character).
    public static boolean isValidRecipient(String cellNumber) {
        Login recipientCheck = new Login("", "", "", "", cellNumber);
        return recipientCheck.checkCellPhoneNumber();
    }
    
     public String checkRecipientCell() {
        if (isValidRecipient(recipient)) {
            return CELL_SUCCESS;
        }
        return CELL_FAILURE;
    }
     
    public static boolean isMessageLengthValid(String text) {
        return text.length() <= MAX_LENGTH;
    }
    
    public static String checkMessageLength(String text) {
        int tooMany = text.length() - MAX_LENGTH;
        if (tooMany > 0) {
            return "Message exceeds " + MAX_LENGTH + " characters by " + tooMany
                    + "; please reduce the size.";
        }
        return MESSAGE_READY;
    }
    
    private static String createMessageID() {
        String id = "";
        for (int x = 0; x < ID_LENGTH; ++x) {
            id = id + (int) (Math.random() * 10);
        }
        return id;
    }
    
    public String messageIDMessage() {
        return "Message ID generated: " + messageID;
    }
    
    //====== Message Hash ======
    
    public String createMessageHash() {
        return buildHash(messageID, messageNumber, messageText);
    }
    
    private static String buildHash(String id, int number, String text) {
        String cleanText = removeOuterSpaces(text);
        
        String firstWord = cleanText;
        int firstSpace = cleanText.indexOf(' ');
        if (firstSpace != -1) {
            firstWord = cleanText.substring(0, firstSpace);
        }
        
        int lastSpace = -1;
        for (int x = 0; x < cleanText.length(); ++x) {
            if (cleanText.charAt(x) == ' ') {
                lastSpace = x;
            }
        }
        String lastWord = cleanText.substring(lastSpace + 1);

        String idStart = id;
        if (id.length() >= 2) {
            idStart = id.substring(0, 2);
        }
        String words = keepLettersAndDigits(firstWord) + keepLettersAndDigits(lastWord);

        return (idStart + ":" + number + ":" + words).toUpperCase();
    }
    
    private static String removeOuterSpaces(String text) {
        int start = 0;
        int end = text.length();
        while (start < end && Character.isWhitespace(text.charAt(start))) {
            ++start;
        }
        while (end > start && Character.isWhitespace(text.charAt(end - 1))) {
            --end;
        }
        return text.substring(start, end);
    }
    
    // Keeps only letters and digits, so "tonight?" becomes "tonight"
    private static String keepLettersAndDigits(String word) {
        StringBuilder cleanWord = new StringBuilder();
        for (int x = 0; x < word.length(); ++x) {
            char letter = word.charAt(x);
            if (Character.isLetterOrDigit(letter)) {
                cleanWord.append(letter);
            }
        }
        return cleanWord.toString();
    }
    
    // ======== Send, disregard or store =======
    
    public String sentMessage(int choice) {
        String result;
        
        if (choice == SEND) {
            sentMessageDetails.append(getDetails()).append("\n");
            ++totalMessagesSent;
            result = MESSAGE_SENT;
        }else if (choice == DISREGARD) {
            result = MESSAGE_DISREGARDED;
        }else if (choice == STORE) {
            if (storeMessage()) {
                result = MESSAGE_STORED;
            }else {
                result MESSAGE_NOT_STORED;
            }
        } else {
            result = INVALID_CHOICE;
        }
        return result;
    }
    
    // ====== Display =======
    
    public String getDetails() {
        return "MessageID: " + messageID + "\n"
                + "MessageHash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText + "\n";
    }
    
    public static String printMessages() {
        return sentMessageDetails.toString();
    }
    
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }
    
    //====== Store in a JSON file =======
    
    public boolean storeMessage() {
        if (totalMessagesStored > 0) {
            storedMessagesJson.append(",\n");
        }
        storedMessagesJson.append("  {\n");
        storedMessagesJson.append("    \"messageID\": \"" + escapeJson(messageID) + "\",\n");
        storedMessagesJson.append("    \"messageNumber\": " + messageNumber + ",\n");
        storedMessagesJson.append("    \"recipient\": \"" + escapeJson(recipient) + "\",\n");
        storedMessagesJson.append("    \"message\": \"" + escapeJson(messageText) + "\",\n");
        storedMessagesJson.append("    \"messageHash\": \"" + escapeJson(messageHash) + "\"\n");
        storedMessagesJson.append("  }");
        ++totalMessagesStored;

        return writeJsonFile();
    }
    
    private static String escapeJson(String text) {
        StringBuilder safeText = new StringBuilder();
        for (int x = 0; x < text.length(); ++x) {
            char letter = text.charAt(x);
            if (letter == '"' || letter == '\\') {
                safeText.append('\\');
                safeText.append(letter);
            } else if (letter == '\t') {
                safeText.append("\\t");
            } else {
                safeText.append(letter);
            }
        }
        return safeText.toString();
    }
    
    private static boolean writeJsonFile() {
        boolean saved = false;
        try {
            FileWriter writer = new FileWriter(jsonFileName);
            writer.write("[\n" + storedMessagesJson.toString() + "\n]\n");
            writer.close();
            saved = true;
        } catch (IOException e) {
            saved = false;
        }
        return saved;
    }
    
    
    public static void setJsonFileName(String fileName) {
        jsonFileName = fileName;
    }
    
    public static void clearMessages() {
        sentMessageDetails = new StringBuilder();
        totalMessagesSent = 0;
        storedMessagesJson = new StringBuilder();
        totalMessagesStored = 0;
    }
    
    
    // ======= Accessors =======
    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }
}
