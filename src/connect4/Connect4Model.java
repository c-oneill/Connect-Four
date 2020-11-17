package connect4;

import java.util.ArrayList;
import java.util.Observable;

/**
 * This class serves as the underlying Model for the Connect4 Program.
 * 
 * <p> Observers of a Connect4Model will be notifed of changes with each
 * position set. The {@link Observable#notifyObservers()} method will pass an
 * Object array of 4 values to Observers as 
 * [int row, int col, Status status, Status winner]
 * <p> Top left of the grid acts as the anchor at (0,0). The grid is checked for
 * four-in-a-row after each position is set.
 * <p> {@link Connect4Model} tracks the winner throughout the game. Until a
 * four-in-a-row is created, the winner is considered a draw, indicated by
 * {@value Status#EMPTY}
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
	
	private Status[][] grid; // grid[row][col]
	private Status winner;
	
	private ArrayList<int[]> redList; // lists all red positions [row, col]
	private ArrayList<int[]> yellowList; // lists all yellow positions [row, col]
	
	/**
	 * Connect4Model Constructor.
	 */
	public Connect4Model()
	{
		// setting empty grid
		grid = new Status[6][7];
		for (int r = 0; r < ROWS; r++)
		{
			for (int c = 0; c < COLUMNS; c++)
			{
				grid[r][c] = Status.EMPTY;
			}
		}
		// set as draw - no winner yet
		winner = Status.EMPTY;
		
		// initialize position lists
		redList = new ArrayList<int[]>();
		yellowList = new ArrayList<int[]>();
	}
	
	/**
	 * Get the underlying grid.
	 * @return grid
	 */
	public Status[][] getGrid()
	{
		return grid;
	}
	
	/**
	 * Get the game winner. Winner is {@value Status#YELLOW} or
	 * {@value Status#RED}, {@value Status#EMPTY} if currently at a draw.
	 * @return winner
	 */
	public Status getWinner()
	{
		return winner;
	}
	
	/**
	 * Gets the Status at position (row, col) in the grid.
	 * @param row row index
	 * @param col col index
	 * @return position status
	 */
	public Status getStatus(int row, int col)
	{
		return grid[row][col];
	}
	
	// pass list to observer (row, col, status, winner)
	/**
	 * Set the status at the position (row, col). After position is set, updates
	 * position lists, checks for four-in-a-row, and notifies observers.
	 * @param row row index
	 * @param col col index
	 * @param status status to set
	 */
	public void setPosition(int row, int col, Status status)
	{
			grid[row][col] = status;
			
			// check for winner
			if (checkForStreak(row, col, status))
				winner = status;
			
			// update disc positon lists
			if (status == Status.RED)
				redList.add(new int[] {row, col});
			if (status == Status.YELLOW)
				yellowList.add(new int[] {row, col});
			
			// notify observers of changes
			Object[] updateArr = new Object[]{row, col, status, winner};
			setChanged();
			notifyObservers(updateArr);
	}
	
	/**
	 * Checks the grid to for-four-in-a-row discs, matching the Status passed. 
	 * Only iterates through known disc positons.
	 * @param row row index
	 * @param col col index
	 * @param status which disc set is checked
	 * @return
	 */
	private boolean checkForStreak(int row, int col, Status status)
	{
		ArrayList<int[]> list;
		
		if (status == Status.RED)
			list = redList;
		else if (status == Status.YELLOW)
			list = yellowList;
		else
			return false;
		
		for (int[] pair : list)
		{
			row = pair[0];
			col = pair[1];
			// check down
			if (row + 3 < ROWS 
					&& grid[row + 1][col] == status
					&& grid[row + 2][col] == status
					&& grid[row + 3][col] == status)
			{
				return true;
			}
		
			// check right
			if (col + 3 < COLUMNS 
					&& grid[row][col + 1] == status
					&& grid[row][col + 2] == status
					&& grid[row][col + 3] == status)
			{
				return true;
			}
		
			// check left/down
			if (row - 3 >= 0 && col + 3 < COLUMNS
					&& grid[row - 1][col + 1] == status
					&& grid[row - 2][col + 2] == status
					&& grid[row - 3][col + 3] == status)
			{
				return true;
			}
		
			// check right/down
			if (row + 3 < ROWS && col + 3 < COLUMNS 
					&& grid[row + 1][col + 1] == status
					&& grid[row + 2][col + 2] == status
					&& grid[row + 3][col + 3] == status)
			{
				return true;
			}
		}
		return false;
	}
	
}
