/*
Description: Manages the 2D array of the Connect 4 board. Initial production of the program will have set dimensions for the 2D 
array based on the real-life game: 7 columns and 6 rows. If time permits, functionality for dynamic board size will be implemented. 
Class allows for the storage, creation, and check if the board has a winner. 

Author: Matt, Huy

Date: 11/30/2023 - 12/08/2023
 */ 

import java.util.ArrayList;
public class Board implements java.io.Serializable {

    //2D array of char that stores the Connect 4 board
    private char[][] board;

    //byte constants that holds default row & column lengths based on the real game
    private static final byte DEFAULT_ROW_LENGTH = 6;
    private static byte DEFAULT_COL_LENGTH = 7;

    //stores the amount of empty space in each column (by default: size 7, every element initialized at 6) 
    //every move a player makes removes one from the column in which they put their piece
    //when an element reaches 0, there is no more space on that column, therefore players are no longer allowed to place there
    private byte[] availableColumns;

    //stores all the moves that have been played in chronological order so that they can be replayed back -- rewatching the game. 
    ArrayList<Move> moves = new ArrayList<Move>(); 

    //CONSTRUCTOR
    public Board()
    {
        //create board using final values for row and column length
        this.board = new char[DEFAULT_ROW_LENGTH][DEFAULT_COL_LENGTH];  

        //populate available columns
        availableColumns = new byte[] {6,6,6,6,6,6,6}; 
    }

    //author: huy
    //method plays a move by putting passed in piece (chrColour) into the first available slot in given column
    //it also returns the boolean from with CheckIfWIn() to let caller know if this play led to a win or not. 
    public boolean playMove(byte bytCol, char chrColour)
    {
        //we are checking from bottom to top of column, thus from the last row (this.board.length - 1) to the first (0)
        byte bytTempRow = (byte)(this.board.length - 1); 

        //Handling of column being full is fulfilled bytAvailableColumns which disallows the selection of a full column (logic in gameManager class)
        while (this.board[bytTempRow][bytCol] != '\u0000') //if this row has something, take away one from bytTempRow
        {
            bytTempRow -= 1;
        }

        //now that we have the row wiht an available slot, set the piece there
        this.board[bytTempRow][bytCol] = chrColour; 
        availableColumns[bytCol]--; //decrease the amount of available slots in this column

        //increment moves
        moves.add(new Move(bytTempRow, bytCol, chrColour)); 

        //return check if win to caller, the gamemanager, to let them know if this move that has been played lead to a win 
        return checkIfWin(bytTempRow, bytCol, chrColour); 

    }

    //checks if piece at given location is surrounded by the right pieces in order for there to be a win
    //return true if there's been a win
    //false if not
    private boolean checkIfWin(byte bytRow, byte bytCol, char chrColour)
    {
        //There are four lines in which the piece is a part of that can lead to it being a part of 4 or more pieces of same type
        //these lines are 
        //Horizontal line of piece
        //vertical line of piece
        //diagonal line from bottom left to top right of piece
        //diagonal line from top left to bottom right of piece
        //all counters for number of pieces in each of these lines start at zero, because we know there is at least 1 piece in that line -> the given piece passed into method
        byte bytDiagonalBL_TR = 1; //diagonal line form bottom left to top right
        byte bytDiagonalTL_BR = 1; //diagonal line form top left to bottom right
        byte bytHorizontal = 1; //horizontal line
        byte bytVertical = 1;  //vertical line

        //this method leverages method, recursiveCheck to discover how many pieces are in each line
        //recursiveCheck returns the amount of pieces in front of or behind given pieces of same type
        //each check of each line is comprised of two components: check in front and checking behind piece
        //we tell recursive check where to check next by passing in a rowIncrement (1st parameter) and a column increment (2nd parameter)
        //checking in front is defined as checking up and to the right of piece
        //check behidn is defined as checking below and to the left of piece
        //example: to check the horizontal line, and to check in front of the piece, we would need to incremetn the row by 0 (row does not change) and increment the column by 1
        //to check behind, increment the row by 0 and increment the column by -1
        //checking behind is achieved through passing in a multiplier, notably passing in -1, to reverse the increments and thus, check behind
        //although this can be accomplished through simply changing the increments passed in, this provides more seemlessness in code as
        //this limits the amount of differentiating hard coded values

        //if total number of pieces in line has reached 4, or greator, return true to let caller know there was a win
        //if all checks of all lines do not achieve this, return false to let caller know there wasnt a win

        //CHECK BOTTOM LEFT TO TOP RIGHT DIAGONAL
        bytDiagonalBL_TR += recursiveCheck((byte)-1, (byte)1, (byte)1, chrColour, bytRow, bytCol);
        if (bytDiagonalBL_TR >= 4)
        {
            return true; 
        }
        bytDiagonalBL_TR += recursiveCheck((byte)-1, (byte)1, (byte)-1, chrColour, bytRow, bytCol);
        if (bytDiagonalBL_TR >= 4)
        {

            return true; 
        }

        //CHECK TOP LEFT TO BOTTOM RIGHT DIAGONAL
        bytDiagonalTL_BR += recursiveCheck((byte)1, (byte)1, (byte)1, chrColour, bytRow, bytCol);
        if (bytDiagonalTL_BR >= 4)
        {
            return true; 
        }
        bytDiagonalTL_BR += recursiveCheck((byte)1, (byte)1, (byte)-1, chrColour, bytRow, bytCol);

        if (bytDiagonalTL_BR >= 4)
        {
            return true; 
        }

        //CHECK HORIZONTAL
        bytHorizontal += recursiveCheck((byte)0, (byte)1, (byte)1, chrColour, bytRow, bytCol);
        if (bytHorizontal >= 4)
        {
            return true; 
        }
        bytHorizontal += recursiveCheck((byte)0, (byte)1, (byte)-1, chrColour, bytRow, bytCol);

        if (bytHorizontal >= 4)
        {
            return true; 
        }

        //CHECK VERTICAL
        bytVertical += recursiveCheck((byte)-1, (byte)0, (byte)1, chrColour, bytRow, bytCol);
        if (bytVertical >= 4)
        {
            return true; 
        }
        bytVertical += recursiveCheck((byte)-1, (byte)0, (byte)-1, chrColour, bytRow, bytCol);

        if (bytVertical >= 4)
        {
            return true; 
        }

        return false;     
    }

    //recursively returns the amount of pieces within a line of increment
    //parameters: row increment, column increment, multiplier to multiply increments by 1 (go forward) or -1 (reverse increment), the piece to look for (chrColour), and location (bytX, bytY) of last piece in recursive call
    //on first call of recursiveCheck location of last piece is simply the location of piece that player just played
    private int recursiveCheck(byte rowIncrement, byte colIncrement, byte multiplier, char chrColour, byte bytX, byte bytY)
    {
        //surround with try catch because if an error is thrown that means that it has checked outside of bounds.
        //thus there are no more pieces along that line of increment
        //catch errror and return 0, and stop recursively checking
        try
        {
            //if the piece at incremented location in board is the same to piece we're looking for
            if (chrColour == board[bytX + (rowIncrement * multiplier)][bytY + (colIncrement * multiplier)])
            {
                //return 1 because a piece was found, and keep checking at next increment of location in baord
                return 1 + recursiveCheck(rowIncrement, colIncrement, multiplier, chrColour, (byte)(bytX + (rowIncrement * multiplier)), (byte)(bytY + (colIncrement * multiplier))); 
            }
            else
            {
                //return 0, and effectively, stop recursively searching
                return 0; 
            }
        }
        catch (Exception e)
        {
            return 0; 
        }
    }

    

    //GETTERS
    public char[][] getBoard() {
        return this.board;
    }

    public byte[] getAvailableColumns() {
        return this.availableColumns;
    }

    public ArrayList<Move> getMoves()
    {
        return this.moves; 
    }

    public static byte getDefaultRowLength()
    {
        return DEFAULT_ROW_LENGTH; 
    }

    public static byte getDefaultColLength()
    {
        return DEFAULT_COL_LENGTH; 
    }
}