/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itec.controller;

/**
 *
 * @author Usuario
 */
public class LoginController {
    
    public Boolean login(String user){
        
        if (user.equals("admin")) {
            return true;
        }
        
        return false;
    }
    
}
