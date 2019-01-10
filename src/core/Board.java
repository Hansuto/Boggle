/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Chris Taliaferro
 */
public class Board implements IBoard 
{
    private ArrayList<String> boggleData;   //stores letter data from data file
    private ArrayList<String> dictionaryData;   //stores dictionary data
    private ArrayList<Die> boggleDice;  //the collection of 16 dice
    private ArrayList<String> gameData = new ArrayList(); //stores game data
    
    public Board(ArrayList<String> boggle, ArrayList<String> dictionary)
    {
        boggleData = boggle;
        dictionaryData = dictionary;
        boggleDice = new ArrayList<Die>();
    }

    @Override
    public void populateDice() 
    {
        Die die;
        int counter = 0;
        
        for(int dice =0; dice < NUMBER_OF_DICE; dice++) //Loops through each die
        {
            die = new Die();    //Creates an instance of Die
            
            for(int side = 0; side < die.NUMBER_OF_SIDES; side++)   //Loops through each side of every die and populates it
            {
                die.addLetter(boggleData.get(counter));
                counter++;
            }
            
            //System.out.print("Die " + dice + ": "); //Assignment printout
            //die.displayLetters();
            //System.out.println();
            
            boggleDice.add(die);
        }
    }
    
    @Override
    public ArrayList shakeDice() 
    {
        ArrayList<Integer> usedDie = new ArrayList();   //Makes variabe that keeps track of used dice
        gameData.clear();
        int dieCounter = 0;
        Random randomDie = new Random();
        while(dieCounter < NUMBER_OF_DICE)
        {
            int numberofDie = randomDie.nextInt(NUMBER_OF_DICE);    //Grabs a value 0-15
        
            if(!usedDie.contains(numberofDie))
            {
                Die die = boggleDice.get(numberofDie);  //Creates instance of type die and grabs the randomly selected die
                gameData.add(die.rollDie());    //Rolls die and stores it in game data
                usedDie.add(new Integer(numberofDie));
                dieCounter++;
            }
        }
        
        return gameData;
    }
    

    public void displayGameData() 
    {
        int format = 0;
        for(String value : gameData) 
        {
            System.out.print(value + " ");  //Prints the gameData
            format++;
            if (format == GRID) //Formats the 4x4 layout
            {
                System.out.println(); 
                format = 0;
            }
        }    
    }

    /**
     * @return the boggleData
     */
    public ArrayList<String> getBoggleData() {
        return boggleData;
    }

    /**
     * @param boggleData the boggleData to set
     */
    public void setBoggleData(ArrayList<String> boggleData) {
        this.boggleData = boggleData;
    }

    /**
     * @return the dictionaryData
     */
    public ArrayList<String> getDictionaryData() {
        return dictionaryData;
    }

    /**
     * @param dictionaryData the dictionaryData to set
     */
    public void setDictionaryData(ArrayList<String> dictionaryData) {
        this.dictionaryData = dictionaryData;
    }

    /**
     * @return the boggleDice
     */
    public ArrayList<Die> getBoggleDice() {
        return boggleDice;
    }

    /**
     * @param boggleDice the boggleDice to set
     */
    public void setBoggleDice(ArrayList<Die> boggleDice) {
        this.boggleDice = boggleDice;
    }

    /**
     * @return the gameData
     */
    public ArrayList<String> getGameData() {
        return gameData;
    }

}
