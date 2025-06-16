/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

/**
 *
 * @author mnqvi
 */
@SuppressWarnings("FieldMayBeFinal")
public class User {
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;
    
public User(String username, String password, String cellPhoneNumber, String firstName, String lastName ){
    this.username=username;
    this.password=password;
    this.cellPhoneNumber=cellPhoneNumber;
    this.firstName=firstName;
    this.lastName=lastName;
}

//Getters
public String getUsername() {
    return username;
}

public String getPassword() {
    return password;
}

public String getCellPhoneNumber() {
    return cellPhoneNumber;
}

public String getFirstName() {
    return firstName;
}

public String getLastName(){
  return lastName;  
 }
}

