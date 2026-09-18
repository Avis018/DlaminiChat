/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dlaminichat;
import java.util.regex.Pattern;
/**
 *
 * @author Student
 */
public class Login {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;
    
    
    private boolean registered;
    private boolean lastLoginSuccessful;
    
    //South African cell number "+27", followed by 9 digits.
    private static final String CELL_NUMBER_REGEX = "^\\+27[0-9]{9}$";
    
    //Special characters for password verification
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:'\",.<>/?`~";
    
    public Login(String firstName, String lastName, String username, String password, String cellPhoneNumber){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.registered = false;
        this.lastLoginSuccessful = false;
    }
    
    //======== Individual Checks ========
    
    public boolean checkUserName(){
        return username != null && username.contains("_") && username.length() <=5;
        
    }
    
    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8){
            return false;
        }
        
        boolean hasCapital = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (int i = 0; i < password.length(); i++){
            char currentChar = password.charAt(i);
            
            if (Character.isUpperCase(currentChar)) {
                hasCapital = true;
            }else if (Character.isDigit(currentChar)) {
                hasDigit = true;
            }else if (SPECIAL_CHARACTERS.indexOf(currentChar) >=0) {
                hasSpecial = true;
            }
        }return hasCapital && hasDigit && hasSpecial;
    }
        
        public boolean checkCellPhoneNumber() {
            return cellPhoneNumber != null && Pattern.matches(CELL_NUMBER_REGEX, cellPhoneNumber);
        }
        
        //============== Registration ============
        
        public String registerUser() {
            if (!checkUserName()) {
                return "Username is not correctly formatted, please make sure username has no more than 5 "
                + "characters and has an underscore";
            }
            
            if (!checkPasswordComplexity()){
                return "Password is not correctly formatted, please enure your "
                        + "password contains at least 8 characters, 1 capital letter "
                        + "1 special character, and a number";
            }
            
            registered = true;
            return "Welcome " + firstName + ", " + lastName + " it is great to see you.";
        }
        
        public String cellPhoneMessage() {
            if (checkCellPhoneNumber()) {
                return "Cell number is successfully captured.";
            }
            return "Cell phone number is incorrectly formatted and/or does not contain"
                    + " an international code, please fix your error and try again.";
        }
        
        //====== Login ======
        
        public boolean loginUser(String enteredUsername, String enteredPassword) {
            lastLoginSuccessful = registered
                    && username.equals(enteredUsername)
                    && password.equals(enteredPassword);
            return lastLoginSuccessful;
        }
        
        public String returnLoginStatus(){
            if (lastLoginSuccessful) {
                return "Welcome " + firstName + ", " + lastName + ", it is great to see you again.";
            }
            return "Username or password incorrect, please try again.";
        }
        
        public String getFirstName() {
            return firstName;
        }
        
        public String getLastName() {
            return lastName;
        }
        
        public String getUserName() {
            return username;
        }
        
        public String getCellPhoneNumber() {
            return cellPhoneNumber;
        }
        
        public boolean isRegistered() {
            return registered;
        }
        
}
