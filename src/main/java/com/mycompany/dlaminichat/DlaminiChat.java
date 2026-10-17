/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.dlaminichat;
import java.util.Scanner;
/**
 *
 * @author Student
 */
public class DlaminiChat {
    
    //Part 2: Quick Chat menu options 
    private static final int MENU_SEND = 1;
    private static final int MENU_RECENT = 2;
    private static final int MENU_QUIT = 3;
    
    private static final String SCREEN_TOO_LONG = "Please enter a message of less than 250 characters.";
    private static final String SCREEN_MESSAGE_OK = "Message sent";
    private static final String SCREEN_SENT = "Message successfully sent";
    private static final String SCREEN_DISREGARD = "Press 0 to delete the message";
    private static final String SCREEN_STORED = "Message successfully stored";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login user = null;
        boolean isRegistered = false;
        
        System.out.println("====Dlamini Chat Registration====");
        
        while (!isRegistered){
            System.out.println("Enter first name: ");
            String firstName = input.nextLine();
            
            System.out.println("Enter last name: ");
            String lastName = input.nextLine();
            
            System.out.println("Enter a username (should be no more than 5 characters and have an underscore): ");
            String username = input.nextLine();
            
            System.out.println("Enter your password (should contain 8 characters, 1 capital letter, 1 number & special character): ");
            String password = input.nextLine();
            
            System.out.println("Enter your South African cell phone number, e.g. +27833025474: ");
            String cellPhoneNumber = input.nextLine();
            
            user = new Login(firstName, lastName, username, password, cellPhoneNumber);
            
            
            String message = user.registerUser();
            System.out.println(message);
            isRegistered = user.isRegistered();
            
            if (!isRegistered) {
                System.out.println("Please try registering again.\n");
                
            }
        }
        System.out.println("\n==== Login ====");
        boolean loggedIn = false;
        
        while(!loggedIn) {
            System.out.println("Enter username: ");
            String loginUsername = input.nextLine();
            
            System.out.println("Enter password: ");
            String loginPassword = input.nextLine();
            
            loggedIn = user.loginUser(loginUsername, loginPassword);
            System.out.println(user.returnLoginStatus());
        }
        runQuickChat(input);
        
        input.close();
    }
    //=============== Part 2: QuickChat (DlaminiChat) ================
    
    private static void runQuickChat(Scanner input) {
        System.out.println("\nWelcome to QuickChat.");
        int numMessages = askNumberOfMessages(input);
        boolean messagesEntered = false;
        int choice = 0;
        
        while (choice !=MENU_QUIT) {
             printMenu();
            choice = readNumber(input);
            
            if (choice == MENU_SEND) {
                if (messagesEntered) {
                    System.out.println("You have already entered all " + numMessages + " messages.");
                }else{
                    sendMessages(input, numMessages);
                    messagesEntered = true;
                }
            }else if (choice == MENU_RECENT) {
                System.out.println("Coming Soon.");
            }else if (choice != MENU_QUIT) {
                System.out.println("Invalid option, please enter 1, 2 or 3.");
            }
        }
        System.out.println("Goodbye.");
    }
    
    private static void printMenu() {
        System.out.println("\n Please choose an option: ");
        System.out.println("1) Send Messages");
        System.out.println("2) Show recently sent messages");
        System.out.println("3) Quit");
    }
    
    //user should decide how many messages they want to enter
    private static int askNumberOfMessages(Scanner input) {
        System.out.println("How many messages do you wish to enter?");
        int numMessages = readNumber(input);
        
        while (numMessages < 1) {
            System.out.println("Please enter a whole number greater than 0.");
            System.out.println("How many messages do you wish to enter?");
            numMessages = readNumber(input);
        }
        return numMessages;
    }
    
    
    
    private static void sendMessages(Scanner input, int numMessages) {
        for (int x = 0; x < numMessages; ++x) {
            System.out.println("\n--- Message " + (x + 1) + " of " + numMessages + " ---");
            
            String recipient = askRecipient(input);
            String text = askMessageText(input);
            Message message = new Message(x, recipient, text);
            
            System.out.println(message.messageIDMessage());
            chooseWhatToDo(input, message);
        }
        System.out.println("\n Total number of messages sent: " + Message.returnTotalMessages());
    }
    
    private static String askRecipient(Scanner input) {
        System.out.println("Enter the recipient's cell number(eg. +27898764567): ");
        String recipient = input.nextLine();
        
        while (!Message.isValidRecipient(recipient)) {
            System.out.println(Message.CELL_FAILURE);
            System.out.println("Enter the recipient's cell number (e.g. +27718693002): ");
            recipient = input.nextLine();
        }
        System.out.println(Message.CELL_SUCCESS);
        return recipient;
    }
    
    private static String askMessageText(Scanner input) {
        System.out.println("Enter your message (maximum 250 characters): ");
        String text = input.nextLine();
        
        while (!Message.isMessageLengthValid(text)) {
            System.out.println(SCREEN_TOO_LONG);
            System.out.println("Enter your message (maximum 250 characters): ");
            text = input.nextLine();
        }
        System.out.println(SCREEN_MESSAGE_OK);
        return text;
    }
    
    //The user must choose to send, store or disregard the message they entered
    private static void chooseWhatToDo(Scanner input, Message message) {
        int choice = 0;
        
        while (choice < Message.SEND || choice > Message.STORE) {
            System.out.println("What would you like to do with this message?");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            choice = readNumber(input);
        }
        
        String result = message.sentMessage(choice);
        
        if (choice == Message.SEND) {
            System.out.println(SCREEN_SENT);
            System.out.println("\n" + message.getDetails());
        }else if (choice == Message.DISREGARD) {
            System.out.println(SCREEN_DISREGARD);
            waitForDelete(input);
        }else if (result.equals(Message.MESSAGE_STORED)) {
            System.out.println(SCREEN_STORED);
        }else {
            System.out.println(result);
        }
    }
    
    //For when user wants to delete the message
    private static void waitForDelete(Scanner input) {
        int key = readNumber(input);
        
        while (key !=0) {
            System.out.println(SCREEN_DISREGARD);
            key = readNumber(input);
        }
        System.out.println("Message deleted.");
    }
    
    private static int readNumber(Scanner input) {
        String entry = input.nextLine();
        boolean allDigits = entry.length() > 0 && entry.length() < 9;
        
        for (int x = 0; x < entry.length(); ++x) {
            if (!Character.isDigit(entry.charAt(x))) {
                allDigits = false;
            }
        }
        
        if (allDigits) {
            return Integer.parseInt(entry);
        }
        return -1;
    }
}
