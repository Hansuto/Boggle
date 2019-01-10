/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boggle;

import core.Board;
import inputOutput.ReadDataFile;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import userInterface.BoggleUi;

 
/**
 *
 * @author Chris Taliaferro
 */
public class Boggle  
{    
    private static  ArrayList<String> boggleData = new ArrayList(); //Array list that stores data value of each die
    private static  ArrayList<String> dictionaryData = new ArrayList(); //Array list to store data from dictionary file
    private static String dataFileName = new String("../data/BoggleData.txt");  //Boggle data file using relative pathing
    public static String dictionaryFileName = new String("../data/Dictionary.txt");    //Dictionary data file using relative pathing

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        System.out.println("Boggle Board");   // Prints explicit text
        JOptionPane.showMessageDialog(null,"Let's Play Boggle!");   // Displays dialog box
        
        ReadDataFile data = new ReadDataFile(dataFileName); //read the dice data file
        data.populateData();
        
        ReadDataFile dictionary = new ReadDataFile(dictionaryFileName); //read the dictionary data file
        dictionary.populateData();
        
        Board board = new Board(data.getData(), dictionary.getData());  //creates instance of Board passing BoggleData
        board.populateDice();
        
        System.out.println("There are " + dictionary.getData().size() + " entries in the Dictionary");
        
        board.shakeDice();
        board.displayGameData();    //Displays the game data
        System.out.println("");
        board.shakeDice();
        board.displayGameData();    //Displays the game data
        
        boggleData = board.getGameData();   //Sets the dice values equal to boggleData
        
        BoggleUi ui = new BoggleUi(board); 
    }

}
    

