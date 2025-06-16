/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author mnqvi
 */
public class LoginTest {
    
    @Test
    public void testUsernameCorrectlyFormatted() {
        Login login = new Login();
        String result = login.registerUser("mon_1","Ch&&sec@Ke99!","+27721384462", "Monique","Viljoen");
        assertTrue(result.contains("Username successfully captured."));
    }
    
    @Test
    public void testUsernameIncorrectlyformatted() {
        Login login = new Login();
        String result = login.registerUser("Monique!!!!","Ch&&sec@Ke99!","+27721384462","Monique","Viljoen");
        assertEquals("Username is not correctly formatted, please ensure your username contains an underscore and is no more than five characters.",result);     
    }
    
    @Test
    public void testPasswordMeetsComplexityRequirements() {
        Login login = new Login();
        String result=login.registerUser("Mon_1","Ch&&sec@Ke99!", "+27721384462","Monique","Viljoen");
        assertTrue(result.contains("Password successfully captured."));    
    }
    
    @Test
    public void testPasswordDoesNotMeetComplexityRequirements() {
        Login login = new Login();
        String result=login.registerUser("Mon_1", "password", "+27721384462", "Monique", "Viljoen");
        assertEquals("Password not correctly formatted,please ensure password contains at least eight charachters,a capital letter,a number,and special character.",result);
    }
    
    @Test
    public void testCellPhoneNumberCorrectlyFormatted() {
        Login login = new Login();
        String result=login.registerUser("Mon_1", "Ch&&sec@Ke99!", "+27721384462", "Monique", "Viljoen");
        assertTrue(result.contains("Cell phone number successfully added."));
    }
    
    @Test
    public void testCellPhoneNumberIncorrectlyFormatted() {
        Login login = new Login();
        String result=login.registerUser("Mon_1", "Ch&&sec@Ke99!", "08966553", "Monique", "Viljoen");
        assertEquals("Cell phone number incorrectly formatted or does not contain an international code.",result);
    }
    
    @Test
    public void testLoginSuccessful() {
        Login login = new Login();
        login.registerUser("Mon_1", "Ch&&sec@Ke99!", "+27721384462", "Monique", "Viljoen");
        String result = login.returnLoginStatus("Mon_1", "Ch&&sec@Ke99!");
        assertEquals("Welcome Monique Viljoen it is great to see you again.",result);
    }
    
    @Test
    public void testLoginFailed() {
        Login login = new Login();
        login.registerUser("Mon_1", "Ch&&sec@Ke99!", "+27721384462", "Monique", "Viljoen");
        String result = login.returnLoginStatus("Mon_1", "Wrongpassword");
        assertEquals("Username or password incorrect, please try again.",result);
    }
    
}
