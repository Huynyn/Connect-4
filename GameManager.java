
/*
Description: 
Manages the running of the game and acts as a mediator between classes. Allows the loading of games, saving of games, playing of moves, stores the players, 

GameManagerClass will handle all output of the program.
For example: although board class can output what the board currently looks like, gameManager will handle this in order to keep consistency. 

Author:
Date:
 */ 

import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.util.NoSuchElementException;

public class GameManager
{
    //object stores the currentGame -- as in the current game object that will be manipulated in program run for tasks
    private Game currentGame;
    //Variable holds the save status of currentGame object
    private boolean bolSaveStatus; 

    //CONSTRUCTOR , author: Huy
    public GameManager()
    {
        //set this.currentGame to a new, basically, empty game in order to prevent errors of code trying to reference this.game 
        this.currentGame = new Game(); 
        
        //populates the byte (in game class) that tracks the number of games that have been played from file
        //this ensures no duplicates of game text file names, regardless of programming rerunning
        this.currentGame.setNumberOfGames(loadNumberOfGames()); 
        //call menu to console
        promptUser(); 
    }

    //METHOD MANAGES THE MENU SYSTEM OF THE GAME    
    public void promptUser()
    {
        //byte holds the user's selection
        byte bytSelection = 0; 
        //Names of players used for game creation
        String strPlayer1Name = "";
        String strPlayer2Name = ""; 

        //file location of game -- this is used for doing a task on a game that has been already saved to files
        String strFileLocation = ""; 

        //populate bytSelection populaetByte method.  Method handles proper range of input and error trapping. 
        bytSelection = populateByte("\nMENU\n-------------------------\nEnter in...\n(1) Play a new game\n(2) Load a game from file\n(3) Replay a game\n(4) Exit the program", (byte)1, (byte)4);

        //switch logic for bytSelection
        switch (bytSelection)
        {
            case 1: //user wants to create a new game
                System.out.println();

                //prompt and populate players for this game
                System.out.println("What is the name for player 1?");
                strPlayer1Name = new Scanner(System.in).nextLine(); 
                System.out.println("What is the name for player 2?");
                strPlayer2Name = new Scanner(System.in).nextLine(); 

                //set currentGame object to new game, passing in player variables
                this.currentGame = new Game(new Player(strPlayer1Name), new Player(strPlayer2Name)); 
                //run the game now
                runGame(); 
                break;
            case 2: //user wants to play a game for saved files
                //prompt user
                System.out.println("What is the file location of the game you want to load");
                //populate
                strFileLocation = new Scanner(System.in).nextLine(); 
                //use if statement to catch boolean returned by loadGame()
                //this ensures further running of code does not occur unless loadGame() is successful 
                if (loadGame(strFileLocation)) //populate with just populated file location
                {  
                    //run the game
                    runGame(); 
                }
                else //unsuccessful loading of game
                {
                    //an error will have been called from loadGame() to tell user what happened
                    //simply send the user back to the menu
                    System.out.println("Sending back to menu...");
                    promptUser(); 
                }

                break;
            case 3: //user wants to replay a game from text file
                //prompt user
                System.out.println("What is the file location of the game you want to replay");
                //populate
                strFileLocation = new Scanner(System.in).nextLine(); 
                //use if statement to catch boolean returned by loadGame()
                //this ensures further running of code does not occur unless loadGame() is successful 
                if (loadGame(strFileLocation))
                {  
                    //call replayGame method
                    replayGame();
                }
                else //unsuccessful loading of game
                {
                    //an error will have been called from loadGame() to tell user what happened
                    //simply send the user back to the menu
                    System.out.println("Sending back to menu...");
                    promptUser(); 
                }
                break;
            case 4: //user doesn't want to play anymore
                System.out.println("Thanks for playing");
                break;
        }

    }

    //author: huy
    //method replays the game in a way that the game looks like it's being played live
    private void replayGame()
    {
        //create a temproary 2d array with dimensions of board
        char[][] tempBoard = new char[Board.getDefaultRowLength()][Board.getDefaultColLength()];

        //for every move in arraylist of moves
        for (Move m : this.currentGame.getBoardObject().getMoves())
        {
            //wait a little bit to show gaps in time between moves (in ms, 1000 = 1 s)
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException ie)
            {
                ie.printStackTrace();
            }
            //set the location inside tempboard to the piece as stored in move, m
            tempBoard[m.getX()][m.getY()] = m.getColour(); //set the element in the tempboard to move at index
            //output the board
            formatOutputOfBoard(tempBoard); //output new board     
        }

        //send user back to the menus
        promptUser(); 
    }

    //author: huy
    //method takes in a 2d array and outputs it in a pretty format
    //this method is leveraged by output occuring in replayGame() method and outputting the board as players play
    private void formatOutputOfBoard(char[][] board)
    {
        //print out top of board
        System.out.println("+---+---+---+---+---+---+---+"); //change this if dynamic board size is implemented
        for (byte r = 0; r < board.length; r++) //loop through rows
        {
            for (byte c = 0; c < board[0].length; c++) //loop through columns
            {
                //PRINT FORMATTED BOARD
                System.out.print("| ");

                if (board[r][c] != '\u0000') //if the element at r,c is not nothing, output the elemetn
                {
                    System.out.print(board[r][c]);
                }
                else //otherwise, put a blank space there
                {
                    System.out.print(" ");
                }

                System.out.print(" ");
            } 
            System.out.println("|");
            System.out.println("+---+---+---+---+---+---+---+"); //print out bottom of each row
        }
    }

    //method runs the game
    private void runGame()
    {
        //boolean stores whether to keep looping or not
        boolean bolLoop = true; 
        //stores the index of the currentPlayer (who's turn it is)
        byte bytCurrentPlayerIndex = 0; 
        //stores user input
        byte bytSelection = 0; 

        //if indexPlayerWon is not -1, that means the game has been finished
        if (this.currentGame.getIndexPlayerWon() != -1) 
        {
            //check if game drawed (when IndexPlayerWon == 2)
            if (this.currentGame.getIndexPlayerWon() == 2)
            {
                //output that game was a draw
                System.out.println("Game was a draw!");         
            }
            else //else game had a winner
            {
                //output that game winner by accessing the players of the game and referencing the one at indexPlayerWon
                System.out.println("Winner was: " + this.currentGame.getPlayers()[this.currentGame.getIndexPlayerWon()].getName());         
            }
            //output the game to the console by passing in 2d array of board into formatOutputBoard method
            formatOutputOfBoard(this.currentGame.getBoardObject().getBoard());
        }
        //this means that currentGame is in progress... so keep playing
        else
        {
            System.out.println("\nCONNECT 4 GAME\n"); 

            //before looping through turns set the bytCurrentPlayerIndex (who's turn it is)
            if (this.currentGame.getNumMoves() % 2 == 0) //if current game move is even, that means person to play is IndexOfFirstPlayer
            {
               bytCurrentPlayerIndex = this.currentGame.getIndexOfFirstPlayer();  
            }
            else //if game move number is odd, that means taht person to play is not IndexOfFirstPlayer, so the other one
            {
                bytCurrentPlayerIndex = (byte)((this.currentGame.getIndexOfFirstPlayer() == 1) ? 0 : 1);
            }
                

            //LOOP THROUGH GAME TURNS
            while (bolLoop)
            {
                //output the game to the console by passing in 2d array of board into formatOutputBoard method
                formatOutputOfBoard(this.currentGame.getBoardObject().getBoard()); 

                //output columns undernearth the board
                System.out.print("  "); 
                //loop through all elements in array that stores the amount of rows left available in each column
                for (byte b = 1; b  <= this.currentGame.getBoardObject().getAvailableColumns().length; b++)
                {
                    //if the number of rows available in column doesn't equal 0, let users know it's a viable option
                    if (this.currentGame.getBoardObject().getAvailableColumns()[b-1] != 0)
                    {
                        System.out.print(b + "   "); 
                    }
                    else
                    {
                        //print out an x to let users know they can't select this column
                        System.out.print("X   "); 
                    }  
                }

                System.out.println(); 

                //LET USERS KNOW WHO'S TURN IT IS BY ACCESSING THE ARRAY OF PLAYERS AND GETTING THE NAME OF THE PLAYER AT bytCurrentPlayerIndex
                System.out.println("\n" + this.currentGame.getPlayers()[bytCurrentPlayerIndex].getName() + ", it's your turn!\n"); 

                do 
                {
                    //prompt useres to select a column to play in 
                    System.out.println("Please select a column that is available (doesn't have an x) to play your piece in");
                    //populate bytSelection using populateByte method that handles error trapping and range of input
                    //-1 because in reality the board stores columns at 0 -> 6 (inclusive)
                    bytSelection = (byte)(populateByte("Enter here:", (byte)1, (byte)7) - 1); 
                }
                while (this.currentGame.getBoardObject().getAvailableColumns()[bytSelection] == 0); 
                //keep looping if the column they have selected has 0 rows left in it
                //they have to choose again

                //use an if statement to catch returned value from currentGame.getBoardObject().playMove() method
                //if returned value is true, the game has been won
                //if returned value is false, do nothing, the game has not been won, logic inside if statement will not run
                //playMove method works by sending in the column player has selection to put their piece in (first parameter)
                //and by the colour, char, of the player who just played (second parameter)
                if (this.currentGame.getBoardObject().playMove(bytSelection, this.currentGame.getPlayers()[bytCurrentPlayerIndex].getColour()))
                {
                    //set the winner stores in game to the player that just won. 
                    this.currentGame.setIndexPlayerWon(bytCurrentPlayerIndex); 

                    //tell users who won
                    System.out.println("\n" + this.currentGame.getPlayers()[bytCurrentPlayerIndex].getName() + " WON THE GAME!!!\n");
                    //stop looping through turns
                    bolLoop = false; 
                    //output the final board of the game
                    formatOutputOfBoard(this.currentGame.getBoardObject().getBoard()); 

                }
                else //there is no winner after this last turn
                {
                    //DO STUFF AT THE END OF THE TURN
                    //Change turns by changing index from 1 to 0 or from 0 to 1
                    if (bytCurrentPlayerIndex == 1)
                    {
                        bytCurrentPlayerIndex = 0; 
                    }
                    else
                    {
                        bytCurrentPlayerIndex = 1; 
                    }

                    //increment the number of moves by 1
                    this.currentGame.incrementMoves(); 

                    //this means that there whole board has been filled and there is no winner
                    //6 rows * 7 columns = 42.  Minus 1 because number of moves starts at 0,not 1
                    //although logic of > 41 can be hardcoded in, it has been chosen to be derived from Board.getDefaultRowLength() * Board.getDefaultColLength() to allows
                    //this to function even if later on the board dimensions are changed
                    if (this.currentGame.getNumMoves() > (Board.getDefaultRowLength() * Board.getDefaultColLength() - 1))
                    {
                        //set the index of the winner to 2, meaning it was a draw
                        this.currentGame.setIndexPlayerWon((byte)2); 
                        //stop looping through turns
                        bolLoop = false;
                        System.out.println("IT WAS A DRAW!");

                        //output board one last time
                        formatOutputOfBoard(this.currentGame.getBoardObject().getBoard()); 

                    }  
                }

                //save the game and print out the returned boolean to let users know if game auto save was complete
                //also let users know where the game was saved to
                System.out.println("Game save status: " + saveGame(this.currentGame.getFileLocation()) + "\nSaved to " + this.currentGame.getFileLocation() + "\n");

            }
        }
        promptUser(); //recall the menu once game has been played through
    }

    //method saves a game object to files
    //This method is called after every move to act as auto save for the game being played  
    //takes in the save location
    private boolean saveGame(String strFileLocation)
    {
        //create ObjectOutputStream object
        ObjectOutputStream out;
        
        //surround with try catch
        try {
            //set out to new ObjectOutputStream with save location
            out = new ObjectOutputStream(new FileOutputStream(strFileLocation));
            //write current game out to save file
            out.writeObject(this.currentGame);
            //close
            out.close();
            //let caller know this was a success
            return true; 
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
            return false;
        } catch (IOException e) {
            System.out.println("Error: Cannot write to file");
            return false; 
        }
    }

    private boolean loadGame(String strFileLocation)
    {
        //create ObjectInputStream object
        ObjectInputStream in;
        try {
            //set out to new ObjectInputStream with save location
            in = new ObjectInputStream(new FileInputStream(strFileLocation));
            //set currentGame to read game from file location
            this.currentGame = (Game)in.readObject();
            //close
            in.close();
            //let caller know this was a success
            return true; 
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Object'c class does not match");
            //let caller know this was a failure
            return false; 
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
            //let caller know this was a failure
            return false; 
        } catch (IOException e) {
            System.out.println("Error: Cannot read from file");
            //let caller know this was a failure
            return false;
        }
    }
    
    
    //method accessed the file that stores the number of games that have been played
    //this ensures that the number of games can be tracked regardless of opening and closing the program
    private byte loadNumberOfGames()
    {
        //normally i would just return this value from its in.nextByte()
        //but this allows in to be closed before returning
        //stores number of games that was accessed form the textfile
        byte bytNumberOfGames = 0; 
        Scanner in; //scanner object
        try {
            //set to scanner object wtih file location
            in = new Scanner(new FileReader("numGames.txt"));
            //populate bytNumberOfGames from scanner with file location
            bytNumberOfGames = in.nextByte(); 
            //close
            in.close();
            //return number of games that have been played before
            return bytNumberOfGames; 
            //if error occurs, return 0, simply reseting the number of games played
            //also this errors when numGames.txt hasn't been created yet, thus the number of games played is 0... so return 0
        } catch (FileNotFoundException e) {
            return 0;
        } catch (NoSuchElementException e) {
            return 0;
        } catch (IOException e) {
            return 0;
        }
    }

    //HELPER METHOD
    //error traps byte population
    private byte populateByte(String strPrompt, byte start, byte end)
    {
        boolean bolLoop = true; //set looping to true by default
        byte bytNum = 0; //stores the input of user

        System.out.println(strPrompt); //output prompt

        while (bolLoop)
        {
            System.out.print(">>>"); 
            try //attempt population 
            {
                bolLoop = false; 
                bytNum = new Scanner(System.in).nextByte(); 
            }
            catch (Exception E) //if failed, let user know what they should do properly, and keep looping
            {
                bolLoop = true; 
                System.out.printf("Ensure you are entering in a whole number between %d and %d (inclusive)\n", start, end);
            }

            if (bolLoop == false) //if population was successful, check to see if it is in given range
            {
                if (bytNum < start || bytNum > end) // bytnum is out of given range
                {   
                    bolLoop = true; //keep looping if failed test
                    System.out.printf("Ensure you are entering in a whole number between %d and %d (inclusive)\n", start, end); //let user know their mistake
                }  
            }
        }

        return bytNum;  //return populated value
    }
}
