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
    
    private static final String SCREEN_TOO_LONG = "Please enter a message of less than 250 characters";
    private static final String SCREEN_MESSAGE_OK = "Message sent";
    private static final String SCREEN_SENT = "Message successfully sent";
    private static final String SCREEN_DISREGARD = "Press 0 to delete the message";
    private static final String SCREEN_STORED = "Message succesfuly stored";

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
        
        input.close();
    }
    //=============== Part 2: QuickChat (DlaminiChat) ================
    
    private static void runQuickChat(Scanner input) {
        System.out.println("\n Welcome to QuickChat.");
        int numMessages = askNumberOfMessages(input);
        boolean messagesEntered = false;
        int choice = 0;
        
        while (choice !=MENU_QUIT) {
            printMENU();
            choice = readNumber(input);
        }
    }
}
