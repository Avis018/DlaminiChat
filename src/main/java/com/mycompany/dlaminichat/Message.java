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
    
    
}
