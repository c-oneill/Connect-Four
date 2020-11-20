package connect4;

import java.io.Serializable;

/**
 * Connect4MoveMessage Class.
 * 
 * Code specified in program requirements.
 *
 */
public class Connect4MoveMessage implements Serializable{
    public static int YELLOW = 1;
    public static int RED = 2;
    
    private static final long serialVersionUID = 1L;
    
    private int row;
    private int col;
    private int color;
    
    /**
     * Constructor.
     * 
     * @param row - row in message
     * @param col - column in message
     * @param color - color of token in message (yellow = 1, red = 2)
     */
    public Connect4MoveMessage(int row, int col, int color) {
        this.row = row;
        this.col = col;
        this.color = color; 
    }
    
    /**
     * <ul><b><i>getRow</i></b></ul>
     * <ul><ul><p><code>public int getRow () </code></p></ul>
     *
     * Returns the row stored in the message.
     *
     * @return the row of the token
     */
    public int getRow() { return row; }
    /**
     * <ul><b><i>getColumn</i></b></ul>
     * <ul><ul><p><code>public int getColumn () </code></p></ul>
     *
     * Returns the col stored in the message.
     *
     * @return the col of the token
     */
    public int getColumn() { return col; }
    /**
     * <ul><b><i>getColor</i></b></ul>
     * <ul><ul><p><code>public int getColor () </code></p></ul>
     *
     * Returns the color stored in the message.
     *
     * @return the integer representing the color of the token (1 = yellow, 2 = red)
     */
    public int getColor() { return color; }
}
