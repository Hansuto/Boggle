/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.ArrayList;

/**
 *
 * @author Chris Taliaferro
 */
public interface IBoard 
{
    public static final int NUMBER_OF_DICE = 16; // 16 dice
    public static final int GRID = 4;   // 4x4 layout
    
    /**
     *
     * @return
     */
    public ArrayList shakeDice();   //this will invoke the rollDie method for each of the 16 dice
    
    public void populateDice(); //this method will add the data to the 16 dice
}
