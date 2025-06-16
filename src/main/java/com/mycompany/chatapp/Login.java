/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

/**
 *
 * @author mnqvi
 */
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class Login {
private User user;

//Method to check if username contains an underscore and  is no more than 5 characters
public boolean checkUserName(String username) {
    if (username==null) return false;
    return username.contains("_")&& username.length() <=5;
}

//Method to check password complexity
public boolean checkPasswordComplexity(String password) {
    if (password==null || password.length() <8) return false;
    
    
    boolean hasCapital=false, hasNumber=false, hasSpecial=false;
    for (char c: password.toCharArray()) {
        if (Character.isUpperCase(c)) hasCapital=true;
        if (Character.isDigit(c))hasNumber=true;
        if (!Character.isLetterOrDigit(c))hasSpecial=true;
    }
    
    return hasCapital && hasNumber && hasSpecial;
    }

//Method to check if cell phone number starts with South African country code and is correct length
public boolean checkCellPhoneNumber(String cellPhoneNumber) {
    if (cellPhoneNumber==null) return false;
    //South African country code +27, followed by 9 digits (total length 12)
    String regex="^\\+27\\d{9}$";
    Pattern pattern=Pattern.compile(regex);
    Matcher matcher=pattern.matcher(cellPhoneNumber);
    return matcher.matches();
    }

//Method to register user and return appropriate message
public String registerUser(String username, String password, String cellPhoneNumber, String firstname, String lastName){
    if (!checkUserName(username)) {
        return "Username is not correctly formatted, please ensure your username contains an underscore and is no more than five characters.";
    }
    
    if (!checkPasswordComplexity(password)) {
        return"Password not correctly formatted,please ensure password contains at least eight charachters,a capital letter,a number,and special character.";
    }
    
    if (!checkCellPhoneNumber(cellPhoneNumber)) {
        return "Cell phone number incorrectly formatted or does not contain an international code.";
    }
    
    //If all checks pass, create a new user
    
    this.user=new User(username,password,cellPhoneNumber,firstname,lastName);
    return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
}

//Method to verify login and return status
public boolean loginUser(String username, String password) {
    if (user==null) return false;
    return user.getUsername().equals(username)&& user.getPassword().equals(password);
}

//Method to return login status message
public String returnLoginStatus(String username,String password) {
    if (loginUser(username,password)) {
        return "Welcome " + user.getFirstName() + " " + user.getLastName() + " it is great to see you again.";
    }else{
        return "Username or password incorrect, please try again.";
        }
    }

//Added getter for user
public User getUser() {
    return user;
}
}
