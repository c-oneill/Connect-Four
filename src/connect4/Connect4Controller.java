package connect4;

import java.util.Arrays;

/**
 * This class serves as the Controller in the Connect 4 Program, allowing the 
 * {@link Connect4View} to indirectly interact with the 
 * {@link Connect4Model}.
 * 
 * <p> The controller holds a model and handles the logic for making valid 
 * human and computer turns. Also determines game status.
 * 
 * @author Caroline O'Neill
 *
 */
public class Connect4Controller 
{
    private Connect4Model model;
    private int[] nextOpen;
    
    /**
     * Connect4Controller Constructor.
     */
    public Connect4Controller()
    {
        model = new Connect4Model();
        nextOpen = new int[Connect4Model.COLUMNS];
        for (int i = 0; i < nextOpen.length; i++)
        {
        	nextOpen[i] = 5;
        }
    }
    
    /**
     * Indicates if the game is over, either by red or yellow winning, or the 
     * board filling up.
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver()
    { 
    	for (int i = 0; i < nextOpen.length; i++)
    	{
    		if (!isColumnFull(i))
    			return model.getWinner() != Connect4Model.EMPTY;
    	}
        return true;
    }
    
    /**
     * Get's the winner's color. YELLOW (1), RED (2), no winner yet/draw (0).
     * @return color
     */
    public int getWinner()
    {
        return model.getWinner();
    }
    
	/**
	 * Get a copy of the underlying model's grid.
	 * @return grid
	 */
	public int[][] getGridCopy()
	{
		return model.getGridCopy();
	}
    
    /**
     * Checks if the indicated column is empty.
     * @param col column checked
     * @return true if full, false otherwise
     */
    public boolean isColumnFull(int col)
    {
        return nextOpen[col] < 0;
    }
    
    /**
     * Makes a play in the indicated column in the human player color, yellow.
     * @param col column index
     * @return true if the play is made, false otherwise
     */
    public boolean humanTurn(int col)
    {
        return playInColumn(col, Connect4MoveMessage.YELLOW);
    }
    
    /**
     * Overloads {@link Connect4Controller#humanTurn(int)} to allow color 
     * selection.
     * @param col column index
     * @param color color to play
     * @return true if the play is made, false otherwise
     */
    public boolean humanTurn(int color, int col)
    {
        return playInColumn(col, color);
    }
    
    /**
     * Makes a play in the indicated column in the computer player color, red.
     * @return true if the play is made, false otherwise
     */
    public boolean computerTurn()
    {
    	int randCol = ((int) (Math.random() * 100)) % Connect4Model.COLUMNS;
    	return playInColumn(randCol, Connect4MoveMessage.RED);
    }
    
    /**
     * Overloads {@link Connect4Controller#computerTurn(int)} to allow color 
     * selection.
     * @param color color to play
     * @return true if the play is made, false otherwise
     */
    public boolean computerTurn(int color)
    {
    	int randCol = ((int) (Math.random() * 100)) % Connect4Model.COLUMNS;
    	return playInColumn(randCol, color);
    }
    
    /**
     * Plays a disc in the next availible position in the indicated column. No
     * play made if the column is full.
     * @param col column index
     * @param color disc color
     * @return true if the disc is played in the column, false otherwise
     */
    private boolean playInColumn(int col, int color)
    {
    	if (color == Connect4Model.EMPTY)
    		return false;
    	
    	int row = nextOpen[col];
    	 
    	if (isColumnFull(col))
    		return false;
    	
    	nextOpen[col]--;
    	model.setPosition(row, col, color);
    	return true;
    }
    
    /**
     * <ul><b><i>setModelObserver</i></b></ul>
     * <ul><ul><p><code> void setModelObserver () </code></p></ul>
     *
     * Adds a {@link Connect4View} as an observer of the {@link Connect4Model}.
     *
     * @param view - the <code>Connect4View</code> to observer the <code>Connect4Model</code>
     * 
     * @author Kristopher Rangel
     */
    public void setModelObserver(Connect4View view) {
        model.addObserver(view);
    }
    
}
