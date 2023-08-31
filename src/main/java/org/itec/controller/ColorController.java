/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itec.controller;

/**
 *
 * @author Usuario
 */
public class ColorController {
    
    public static int cambiarColor(){
        int max = 255;
        int min=0;
        int range = max - min + 1;        
        int rand = (int)(Math.random()*range);        
        return rand;
    }
}
