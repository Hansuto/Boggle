package core;

import java.util.ArrayList;
import java.util.Random;
import boggle.Boggle;
import core.Board;

/**
 *
 * @author Chris Taliaferro
 */
public class Die implements IDie 
{
    private ArrayList<String> letters = new ArrayList<String>();    //creates Array List for letters
    
    public void displayLetters() 
    {
        for(String value : letters) //creats instance of class String, and loops through letters
        {
            System.out.print(value + " ");
        }    
    }
    
    @Override
    public void addLetter(String letter) 
    {
        letters.add(letter);
    }
    
    @Override
    public String rollDie()
    {
        Random randomSide = new Random();
        int side = randomSide.nextInt(NUMBER_OF_SIDES);   //randomly selects one side of the die
        String thisLetter = letters.get(side); //sets "thisLetter" to the chosen side of the die
        return thisLetter;        
    }

}
