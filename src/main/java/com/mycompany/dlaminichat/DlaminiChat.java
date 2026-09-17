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
        }
    }
}
