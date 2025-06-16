

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
public class UserTest {
    public static void main(String[] args) {
      
    }
    
    @Test
    public void testUserConstructionAndGetters() {
        User user = new User("Mon_1", "Ch&&sec@Ke99!", "+27721384462", "Monique", "Viljoen");
        assertEquals("Mon_1", user.getUsername());
        assertEquals("Ch&&sec@Ke99!", user.getPassword());
        assertEquals("+27721384462", user.getCellPhoneNumber());
        assertEquals("Monique", user.getFirstName());
        assertEquals("Viljoen", user.getLastName());
    }
}
