/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterface;

import boggle.Boggle;
import core.Board;
import core.Die;
import inputOutput.ReadDataFile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;        
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

        
/**
 *
 * @author Chris Taliaferro
 */
public class BoggleUi extends Boggle
{
    //constants
    private final static int MAX_INDEX = 3;
    private final static int MIN_INDEX = 0;
    private final static String PLUS = "+";
    private final static String MINUS = "-";
    
    private Board board;
    
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu game;
    private JMenuItem newGame;
    private JMenuItem exit;
    //Boggle Board
    private JPanel bogglePanel;
    private ArrayList<Die> dice;
    private JButton[][] diceButtons;
    //Enter found words
    private JPanel wordsPanel;
    private JScrollPane scrollPane;
    private JTextPane wordsArea;
    //Time Label
    private JLabel timeLabel;
    private JButton shakeDice;
    //Enter current word
    private JPanel currentPanel;
    private JLabel currentLabel;
    private JButton currentSubmit;
    //Player's Score
    int score = 0;
    private JLabel scoreLabel;
    //Reset game listener
    private ResetGameListener reset;
    //Timer
    private Timer timer;
    private int minutes = 3;
    private int seconds = 0;
    //Action Listeners
    private JButtonListener jButtonListener;
    private ButtonListener buttonListener;
    //Style document
    private BoggleStyleDocument document;
    //Dictionary
    private ArrayList<String> dictionaryWords = new ArrayList<String>();
    private ArrayList<String> foundWords = new ArrayList<String>();
    boolean[] randomWords;
    
    ReadDataFile dictionary = new ReadDataFile(dictionaryFileName); //read the dictionary data file

    
    public BoggleUi (Board inBoard)
    {
        board = inBoard;
        reset = new ResetGameListener();
        jButtonListener = new JButtonListener();
        buttonListener = new ButtonListener();
        dictionary.populateData();
        initComponents();
    }
    
    private void initComponents()
    {
        //Initialize the JFrame
        frame = new JFrame("Boggle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(660,500);
        
        document = new BoggleStyleDocument();
        
        //Initialize the JMenuBar and add to the JFrame
        createMenu();
        //Initialize the JPane for the current word
        setupCurrentPanel();
        //Initialize the JPanel for the word entry
        setupWordPanel();
        //Initialize the JPanel for the boggle dice
        bogglePanel = new JPanel();
        bogglePanel.setLayout(new GridLayout(4, 4));
        bogglePanel.setMinimumSize(new Dimension(400,400));
        bogglePanel.setPreferredSize(new Dimension(400,400));
        bogglePanel.setBorder(BorderFactory.createTitledBorder("Boggle Board"));
        setupBogglePanel();
        //initializes the Timer
        setupTimer();
        //Add everything to the JFrame
        frame.setJMenuBar(menuBar);
        frame.add(bogglePanel, BorderLayout.WEST);
        frame.add(wordsPanel, BorderLayout.CENTER);
        frame.add(currentPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    
    private void createMenu()
    {
        menuBar = new JMenuBar();
        game = new JMenu("Boggle");
        game.setMnemonic('B');
    
        newGame = new JMenuItem("New Game");
        newGame.setMnemonic('N');
        newGame.addActionListener(reset);
    
        exit = new JMenuItem("Exit");
        exit.setMnemonic('E');
        exit.addActionListener(new ExitListener());
    
        game.add(newGame);
        game.add(exit);
    
        menuBar.add(game);
    }

    private void setupCurrentPanel()
    {
        currentPanel = new JPanel();
        currentPanel.setBorder(BorderFactory.createTitledBorder("Current Word"));
    
        currentLabel = new JLabel();
        currentLabel.setBorder(BorderFactory.createTitledBorder("Current Word"));
        currentLabel.setMinimumSize(new Dimension(300,50));
        currentLabel.setPreferredSize(new Dimension(300,50));
        currentLabel.setHorizontalAlignment(SwingConstants.LEFT);
    
        currentSubmit = new JButton("Submit Word");
        currentSubmit.setMinimumSize(new Dimension(200,100));
        currentSubmit.setPreferredSize(new Dimension(200,50));
        currentSubmit.addActionListener(new SubmitWordListener());
    
        scoreLabel = new JLabel();
        scoreLabel.setBorder(BorderFactory.createTitledBorder("Score"));
        scoreLabel.setMinimumSize(new Dimension(100,50));
        scoreLabel.setPreferredSize(new Dimension(100,50));

        currentPanel.add(currentLabel);
        currentPanel.add(currentSubmit);
        currentPanel.add(scoreLabel);
    }

    private void setupWordPanel()
    {
        wordsPanel = new JPanel();
        wordsPanel.setLayout(new BoxLayout(wordsPanel, BoxLayout.Y_AXIS));
        wordsPanel.setBorder(BorderFactory.createTitledBorder("Enter Words Found"));
    
        wordsArea = new JTextPane();
        scrollPane = new JScrollPane(wordsArea);
        scrollPane.setPreferredSize(new Dimension(180,330));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    
        timeLabel = new JLabel("3:00");
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setFont(new Font("Serif", Font.PLAIN, 48));
        timeLabel.setPreferredSize(new Dimension(240, 100));
        timeLabel.setMinimumSize(new Dimension(240,100));
        timeLabel.setMaximumSize(new Dimension(240,100));
        timeLabel.setBorder(BorderFactory.createTitledBorder("Time Left"));
    
        shakeDice = new JButton("Shake Dice");
        shakeDice.setPreferredSize(new Dimension(240, 100));
        shakeDice.setMinimumSize(new Dimension(240,100));
        shakeDice.setMaximumSize(new Dimension(240,100));
        shakeDice.addActionListener(reset);
    
        wordsPanel.add(scrollPane);
        wordsPanel.add(timeLabel);
        wordsPanel.add(shakeDice);
    }

    private void setupBogglePanel()
    { 
        //counter for the array list (16 letters)
        int counter = 0;
        //get new letters for the game
        board.shakeDice();
        
        diceButtons = new JButton[Board.GRID][Board.GRID];
    
        for(int row = 0; row < Board.GRID; row++)
            for(int col = 0;col < Board.GRID; col++)
        {
            diceButtons[row][col] = new JButton();
            diceButtons[row][col].setText(board.getGameData().get(counter));
            diceButtons[row][col].addActionListener(jButtonListener);
            diceButtons[row][col].addActionListener(buttonListener);
            bogglePanel.add(diceButtons[row][col]);
            counter++;
        }
    }
    
//    public void setupDictionary() throws FileNotFoundException
//    {
//        ReadDataFile dictionary = new ReadDataFile(dictionaryFileName); //read the dictionary data file
//        dictionary.populateData();
//    }
    
    private void setupTimer()
    {
        timer = new Timer(1000, new TimerListener());
        timer.start();
    }
    
    private void updateTextArea(String data)
    {
        wordsArea.setText(wordsArea.getText() + "\n" + data);
        wordsArea.setCaretPosition(wordsArea.getDocument().getLength());
    }
    
    private void randomWordSelect()
    {
        //generating which words the computer found
        Random random = new Random();
        int randWord = random.nextInt(foundWords.size());
        
        if(randomWords[randWord] != true)
        {
            randomWords[randWord] = true;
        }
        else
        {
            randomWordSelect();
        }
    }
    
    private void modifyScore(String addSubtract, String currentWord)
    {
        int wordLength = currentWord.length();
        int value = 0;
        
        switch(wordLength)
        {
            case 0:
            case 1:
            case 2:
                value = 0;
                break;
            case 3:
            case 4:
                value += 1;
                break;
            case 5:
                value += 2;
                break;
            case 6:
                value += 3;
            case 7:
                value += 5;
                break;
            default:
                value += 11;
        }
        
        if(addSubtract.equals(PLUS))
        {
            score += value;
        }
        else if (addSubtract.equals(MINUS))
        {
            score -= value;
        }
        
        scoreLabel.setText(String.valueOf(score));
    }
    
    private void computerCompare()
    {
        //Notify User the computer is comparing
        JOptionPane.showMessageDialog(null, "OUT OF TIME \n The computer is comparing words");
        
        Random random = new Random();
        
        //random number of words found by the computer
        if(!foundWords.isEmpty())
        {
            int randomNum = random.nextInt(foundWords.size());

            randomWords = new boolean[foundWords.size()];
        
            JOptionPane.showMessageDialog(null, "GAME OVER! \n The computer found " + randomNum + " of Player's " + foundWords.size() + " words!");
        
            //only loop for the number of words the computer found
            for(int iter = 0; iter <randomNum; iter++)
            {
            randomWordSelect();
            }
        
            String computerWords = "";
        
            for(int j = 0; j < foundWords.size(); j++)
            {
                if(randomWords[j] == true)
                {
                    System.out.println("Word " + j + " of the player was found by the computer");
                    StyleConstants.setStrikeThrough(document.getAttrStyle(), true);
                    wordsArea.setDocument(document);
                
                    computerWords += (foundWords.get(j) + "\n");
                
                    modifyScore(MINUS, foundWords.get(j));
                }
                else
                {
                    StyleConstants.setStrikeThrough(document.getAttrStyle(), false);
                }
            
                try
                {
                    document.insertString(document.getLength(), foundWords.get(j) + "\n", null);
                }
                catch (BadLocationException ex)
                {
                    Logger.getLogger(BoggleUi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }   
        
            scoreLabel.setText(String.valueOf(score));
        }
        else
        {
         JOptionPane.showMessageDialog(null, "Please Enter at least 1 word and try again");   
        }
    }
    
    private class ButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            if(currentSubmit.isEnabled() == false)
            {
                String temp; //letter from button pressed
                temp = e.getActionCommand();    //gets that letter
                currentLabel.setText(temp);
            }
            else
            {
                String temp2;   //next letter pressed
                temp2 = e.getActionCommand();   //gets letter
                currentLabel.setText(currentLabel.getText() + temp2);   //concatenates letters
            }
        }
    }        
    private class ExitListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            int response = JOptionPane.showConfirmDialog(null, "Comfirm to exit Boggle?", "Exit?", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) System.exit(0);
        }
    }
    
    private class BoggleStyleDocument extends DefaultStyledDocument
    {
        private Style primaryStyle;
        
        public BoggleStyleDocument()
        {
            super();
            primaryStyle = this.addStyle("Primary", null);
        }
        
        public Style getAttrStyle()
        {
            return primaryStyle;
        }
        
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
        {
            super.insertString(offs, str, primaryStyle);
        }        
    }
    
    private class TimerListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(seconds == 0 && minutes == 0)
            {
                timer.stop();
                computerCompare();
            }
            else
            {
                if(seconds == 0)
                {
                    seconds = 59;
                    minutes--;
                }
                else
                {
                    seconds--;
                }
            }
            
            if(seconds < 10)
            {
                String strSeconds = "0" + String.valueOf(seconds);
                timeLabel.setText(String.valueOf(minutes) + ":" + strSeconds);
            }
            else
            {
                timeLabel.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
            }
        }
    }
    
    private class ResetGameListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            bogglePanel.removeAll();
            //resets the bogglePanel with new letters
            setupBogglePanel();
            //resets text areas
            wordsArea.setText("");
            scoreLabel.setText("0");
            currentLabel.setText("");
            timeLabel.setText("3:00");
            
            frame.add(bogglePanel, BorderLayout.WEST);

            bogglePanel.revalidate();
            bogglePanel.repaint();
            
            timer.stop();
            minutes = 3;
            seconds = 0;
            timer.start();
        }
    }
    
    private class SubmitWordListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            //compare current word to dictionary
            if(dictionary.getData().contains(currentLabel.getText().toLowerCase()) == true)
            {
                updateTextArea(currentLabel.getText());
                modifyScore(PLUS,currentLabel.getText());
                foundWords.add(currentLabel.getText());
                scoreLabel.setText(String.valueOf(score));
                currentLabel.setText("");
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Not a valid word");
                currentLabel.setText("");
            }
            
            //re-enable all buttons
            int tempRow = -1;
            int tempCol = -1;
            for(int row = 0; row <= MAX_INDEX; row++)
            {
                for(int col = 0; col <= MAX_INDEX; col++)
                {
                    diceButtons[row][col].setEnabled(true);
                    
                    if(e.getSource().equals(diceButtons[row][col]))
                    {
                        tempRow = row;
                        tempCol = col;
                    }    
                }
            }
        } 
    }
    
    private class JButtonListener implements ActionListener
    {
        int tempRow = -1;
        int tempCol = -1;

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            //grid
            for(int row = 0; row <= MAX_INDEX; row++)
            {
                for(int col = 0; col <= MAX_INDEX; col++)
                {
                    //de-enable
                    diceButtons[row][col].setEnabled(false);
                    if(e.getSource().equals(diceButtons[row][col]))
                    {
                        tempRow = row;
                        tempCol = col;
                    }
                }
            }
            //for the button on the left
            if(tempRow - 1 >= MIN_INDEX)
            {
                diceButtons[tempRow - 1][tempCol].setEnabled(true);
                if(tempCol - 1 >= MIN_INDEX)
                {
                    diceButtons[tempRow - 1][tempCol - 1].setEnabled(true);
                }
                if(tempCol + 1 <= MAX_INDEX)
                {
                    diceButtons[tempRow - 1][tempCol + 1].setEnabled(true);
                }
            }
            //for the buttons to the right
            if(tempRow + 1 <= MAX_INDEX)
            {
                diceButtons[tempRow + 1][tempCol].setEnabled(true);
                if(tempCol - 1 >= MIN_INDEX)
                {
                    diceButtons[tempRow + 1][tempCol - 1].setEnabled(true);
                }
                if(tempCol + 1 <= MAX_INDEX)
                {
                    diceButtons[tempRow + 1][tempCol + 1].setEnabled(true);
                }
            } 
            //for the buttons above
            if(tempCol - 1 >= MIN_INDEX)
            {
                diceButtons[tempRow][tempCol - 1].setEnabled(true);
            }
            if(tempCol + 1 <= MAX_INDEX)
            {
                diceButtons[tempRow][tempCol + 1].setEnabled(true);
            }            
        } 
    }
}