/*
Description: Simply a custom “variable”, used for storage. 
Stores a char for character. 
Stores an x and y location inside 2d Array for move.

Author: Matt, Ahmad

Date: 11/30/2023 - 12/08/2023
 */ 

// create a class of type public called move to store a character location
public class Move implements java.io.Serializable {

    //declare a variable of type char to store the letter representation of the colour
    private char chrColour;

    //declare variables of type byte to store the x and x coordinates
    private byte bytX;
    private byte bytY;

    //implement a constructor method that brings in parameters - char c, byte x, byte y
    public Move (byte x, byte y, char chrColour) 
    {
        //populate the colour variable with the parameter char c
        this.chrColour = chrColour;
        //populate the x coordinate
        this.bytX = x;
        //populate the y coordinate
        this.bytY = y;
    }

    //GETTERS
    public char getColour() {
        return this.chrColour;
    }
    public byte getX() {
        return this.bytX;
    }
    public byte getY() {
        return this.bytY;
    }

}