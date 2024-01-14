/*
Description: Prompts the user to enter the players' names so each player can correspond to a letter within the game. 
Then, each player selects from the given colour options. After each game, ask the user if they wish to continue playing, 
display statistics or exit.

Author: Huy, Matt

Date: 11/30/2023 - 12/08/2023
 */ 

public class Player implements java.io.Serializable {

    //INSTANCE VARIABLES
    private String strName; //stores the name of the player
    private char chrColour; //stores the colour of the pieces that belongs to the player

    //CONSTRUCTOR
    public Player(String s) {
        this.strName = s; //initializes the name of the player
    }

    //GETTERS
    public char getColour() {
        return this.chrColour;
    }

    //SETTERS
    public void setColour(char c) {
        this.chrColour = c;
    }
    
    //GETTERS
    public String getName()
    {
        return this.strName; 
    }
  
}