/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author Chris Taliaferro
 */
public interface IDie 
{
    public static final int NUMBER_OF_SIDES = 6; //Constant for # of sides on die
    
    public void displayLetters(); //This will display the letters on the die
    
    public void addLetter(String letter); //This will populate the Die with letters
    
    public String rollDie(); //This will return the current letter on the die
}
