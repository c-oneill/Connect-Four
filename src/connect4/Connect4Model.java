package connect4;

import java.util.ArrayList;
import java.util.Observable;

/**
 * This class serves as the underlying Model for the Connect4 Program.
 * 
 * <p> Observers of a Connect4Model will be notifed of changes with each
 * position set. The {@link Observable#notifyObservers()} method will pass an
 * {@link Connect4MoveMessage} to Observers.
 * <p> Top left of the grid acts as the anchor at (0,0). The grid is checked for
 * four-in-a-row after each position is set.
 * <p> {@link Connect4Model} tracks the winner throughout the game. Until a
 * four-in-a-row is created, the winner is considered a draw, indicated by
 * EMPTY.
 * <p> A list of all red/yellow discs in play in maintained to expedite checking
 * the grid for a winner.
 * 
 * @author Caroline O'Neill
 * 
 */
public class Connect4Model extends Observable 
{
	public static final int ROWS = 6;
	public static final int COLUMNS = 7;
	public static final int EMPTY = 0;
	
	private int[][] grid; // grid[row][col]
	private int winner;
	
	private ArrayList<int[]> redList; // lists all red positions [row, col]
	private ArrayList<int[]> yellowList; // lists all yellow positions [row, col]
	
	/**
	 * Connect4Model Constructor.
	 */
	public Connect4Model()
	{
		// setting empty grid
		grid = new int[6][7];
		for (int r = 0; r < ROWS; r++)
		{
			for (int c = 0; c < COLUMNS; c++)
			{
				grid[r][c] = EMPTY;
			}
		}
		// set as draw - no winner yet
		winner = EMPTY;
		
		// initialize position lists
		redList = new ArrayList<int[]>();
		yellowList = new ArrayList<int[]>();
	}
	
	/**
	 * Get the underlying grid.
	 * @return grid
	 */
	public int[][] getGrid()
	{
		return grid;
	}
	
	/**
	 * Get the game winner. Winner is {@value Connect4MoveMessage#RED} or
	 * {@value Connect4MoveMessage#YELLOW}, {@value Connect4Model#EMPTY} if
	 * currently at a draw.
	 * @return winner
	 */
	public int getWinner()
	{
		return winner;
	}
	
	/**
	 * Gets the color at position (row, col) in the grid. Returns 0 if empty.
	 * @param row row index
	 * @param col col index
	 * @return position color
	 */
	public int getColor(int row, int col)
	{
		return grid[row][col];
	}
	
	/**
	 * Set the color at the position (row, col). After position is set, updates
	 * position lists, checks for four-in-a-row, and notifies observers.
	 * @param row row index
	 * @param col col index
	 * @param color color to set
	 */
	public void setPosition(int row, int col, int color)
	{
		grid[row][col] = color;
			
		// update disc positon lists
		if (color == Connect4MoveMessage.RED)
			redList.add(new int[] {row, col});
		if (color == Connect4MoveMessage.YELLOW)
			yellowList.add(new int[] {row, col});
		
		// check for winner
		if (checkForStreak(color))
			winner = color;

		// notify observers of changes
		Connect4MoveMessage message = new Connect4MoveMessage(row, col, color);
		setChanged();
		notifyObservers(message);
	}
	
	/**
	 * Checks the grid to for-four-in-a-row discs, matching the color(int) passed. 
	 * Only iterates through known disc positons.
	 * @param row row index
	 * @param col col index
	 * @param color which disc set is checked
	 * @return
	 */
	private boolean checkForStreak(int color)
	{
		ArrayList<int[]> list;
		
		if (color == Connect4MoveMessage.RED)
			list = redList;
		else if (color == Connect4MoveMessage.YELLOW)
			list = yellowList;
		else
			return false;
				
		for (int[] pair : list)
		{
			int row = pair[0];
			int col = pair[1];
			// check down
			if (row + 3 < ROWS 
					&& grid[row + 1][col] == color
					&& grid[row + 2][col] == color
					&& grid[row + 3][col] == color)
			{
				return true;
			}
		
			// check right
			if (col + 3 < COLUMNS 
					&& grid[row][col + 1] == color
					&& grid[row][col + 2] == color
					&& grid[row][col + 3] == color)
			{
				return true;
			}
		
			// check left/down
			if (row - 3 >= 0 && col + 3 < COLUMNS
					&& grid[row - 1][col + 1] == color
					&& grid[row - 2][col + 2] == color
					&& grid[row - 3][col + 3] == color)
			{
				return true;
			}
		
			// check right/down
			if (row + 3 < ROWS && col + 3 < COLUMNS 
					&& grid[row + 1][col + 1] == color
					&& grid[row + 2][col + 2] == color
					&& grid[row + 3][col + 3] == color)
			{
				return true;
			}
		}
		return false;
	}
	
}
