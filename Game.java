/*
Description: Object stores a game, an instance of connect 4. 
Stores all pertinent attributes to the game (ex: players, the board), thereby allowing the program (in this case: the gameManager class) to reference the game and access its state.  Therefore, if a Game object is saved to file, it can be saved and loaded from save. 

Author: Ahmad, Matt

Date: 2023/11/30
 */

import java.util.ArrayList;
import java.util.Random;
import java.io.*;

public class Game implements java.io.Serializable {

    //declare a board object that is associated with this instance of Game
    private Board board;

    //declare Player objects that stores the 2 players that plays the game
    private Player player1;
    private Player player2;

    //declare a static variable of type byte to store the number of games played by the players
    //this variable is used for setting the file location of each game
    private static byte bytNumGames  = 0;

    //declare a variable of type String to store the name of the text file where the game is saved
    //set in the constructor, after by bytNumGames has been incremented because it is derived from the new number of bytNumGames
    private String strFileName;

    //declare an ArrayList to store the players in the game
    private Player[] players = new Player[2]; 

    //declare a variable of type byte to store the nth move in the game
    private byte bytMoveNum = 0;

    //declare a variable of type byte to store the index of the index ArrayList<Player> players that is the first player of the game
    private byte bytIndexOfFirstPlayer;

    //declare a variable of type byte to store the index of index ArrayList<Player> players that is the winner
    //if -1, game has not finished
    //if 2, game was a draw
    private byte bytIndexPlayerWon = -1;
    
    //default constructor
    public Game()
    {
                
    }
    
    //implement a constructor method that brings in parameters - 
    public Game(Player p1, Player p2) {
        //create new board object
        this.board = new Board();
        //increment static variable for number of games by 1
        bytNumGames++;
        //save this new number to textfile that tracks number of games
        saveNumberOfGames(this.bytNumGames);
        //set file save location, derived from number of games
        this.strFileName = "Game" + this.bytNumGames + ".txt";
        
        //set player 1 and 2 in array
        this.players[0] = p1; 
        this.players[1] = p2; 
    
        //set player 1 and 2's colours
        this.players[0].setColour('R'); 
        this. players[1].setColour('Y');
    
        // randomly select which player goes first by setting indexOfFirstPlayer
        this.bytIndexOfFirstPlayer = (byte)randomizePlayer(); 

    }
    
    //randomizes either 0 or 1, to be able to randomize between players
    private static int randomizePlayer()
    {
        Random random = new Random();

        int intRandomIndex = random.nextInt(2);

        return intRandomIndex;
    }
    
    //called to increment the number of moves played
    public void incrementMoves()
    {
        this.bytMoveNum++; 
    }
    
    
    //used to save number of games played to text file
    private void saveNumberOfGames(byte bytNumGames)
    {
        //create printwriter object
        PrintWriter out;

        try {
            //set out object to new printwriter with file location
            out = new PrintWriter(new FileWriter("numGames.txt"));
            //printout bytNumGames
            out.println(this.bytNumGames);
            //close
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
        } catch (IOException e) {
            System.out.println("Error: Cannot write to file");
        }  
    }


    //GETTERS
    public String getFileLocation() {
        return this.strFileName;     
    }

    public Board getBoardObject() {
        return this.board; 
    }

    public byte getIndexPlayerWon() {
        return this.bytIndexPlayerWon; 
    }

    public byte getIndexOfFirstPlayer()
    {
        return this.bytIndexOfFirstPlayer; 
    }

    public byte getNumMoves()
    {
        return this.bytMoveNum; 
    }

    public Player[] getPlayers()
    {
        return this.players; 
    }
    
    
    //SETTERS
    public void setIndexPlayerWon(byte b)    
    {
        this.bytIndexPlayerWon = b; 
    }
    
    public void setNumberOfGames(byte bytNumGames)
    {
        this.bytNumGames = bytNumGames; 
    }
}