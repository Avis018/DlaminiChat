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
        return username !=null && username contains("_") && username.length() <=5;
        
    }
    
    public boolean checkPasswordComplexity() {
        if (password = null || password.length() < 8){
            return false;
        }
    }
}
