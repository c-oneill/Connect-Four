package connect4;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Observable;

/**
 * This class provides test cases for the {@link Connect4Controller} with full
 * statement coverage.
 * 
 * @author Caroline O'Neill
 *
 */
public class Connect4Test 
{	
	private Connect4Controller controller;
	private int[][] expectedGrid;
	private boolean observed;

	@BeforeEach
	public void initialize()
	{
		observed = false;
		controller = new Connect4Controller();
		expectedGrid = new int[6][7];
		for (int r = 0; r < Connect4Model.ROWS; r++)
		{
			for (int c = 0; c < Connect4Model.COLUMNS; c++)
			{
				expectedGrid[r][c] = 0;
			}
		}
	}
	
	/**
	 * Tests the controller constructor initilializes an empty grid.
	 */
	@Test
	public void test_constructor()
	{
		//System.out.println(Arrays.deepToString(expectedGrid));
		//System.out.println(Arrays.deepToString(controller.getGridCopy()));
		assertArrayEquals(expectedGrid, controller.getGridCopy());
	}
	
	/**
	 * This case fills the grid right to left, top to bottom. Each disc added
	 * is checked to make sure it falls in the correct position and the state
	 * of isColumnFull. Also considers attempting to play in a column already
	 * full.
	 */
	@Test
	public void test_FillingColumns()
	{
		for (int r = Connect4Model.ROWS - 1; r >= 1; r--)
		{
			for (int c = Connect4Model.COLUMNS - 1; c >= 1; c--)
			{
				expectedGrid[r][c] = Connect4MoveMessage.YELLOW; 
				controller.humanTurn(c);
				// entered in the correct position?
				assertArrayEquals(expectedGrid, controller.getGridCopy());
				// column still empty?
				assertFalse(controller.isColumnFull(c));	
			}
		}
		// top row added
		for (int c = Connect4Model.COLUMNS - 1; c >= 1; c--)
		{
			expectedGrid[0][c] = Connect4MoveMessage.YELLOW;
			assertTrue(controller.humanTurn(c));
			// entered in the correct position?
			assertArrayEquals(expectedGrid, controller.getGridCopy());
			// column full now?
			assertTrue(controller.isColumnFull(c));
		}
		
		// playing in a column already full
		for (int c = Connect4Model.COLUMNS - 1; c >= 1; c--)
		{
			// column full now?
			// turn made?
			assertFalse(controller.humanTurn(Connect4MoveMessage.YELLOW, c));
			assertTrue(controller.isColumnFull(c));
		}
	}
	
	/**
	 * Tests conditions for a draw: isGameOver() and getWinner.
	 */
	@Test
	public void test_draw()
	{
		//empty board - draw, game not over
		assertEquals(controller.getWinner(), Connect4Model.EMPTY);
		assertFalse(controller.isGameOver());
		
		//one disc - draw, game not over
		controller.humanTurn(Connect4MoveMessage.YELLOW, 0);
		assertEquals(controller.getWinner(), Connect4Model.EMPTY);
		assertFalse(controller.isGameOver());
		
		// full board, no 4-in-a-row - draw, game over
		initialize();
		for (int r = 0; r < Connect4Model.ROWS; r++)
		{
			for (int c = 0; c < Connect4Model.COLUMNS; c++)
			{

				if (r <= 2)
				{
					if (c % 2 == 1)
						controller.humanTurn(Connect4MoveMessage.RED, c);
					else
						controller.humanTurn(Connect4MoveMessage.YELLOW, c);	
				}
				else 
				{
					if (c % 2 == 0)
						controller.humanTurn(Connect4MoveMessage.RED, c);
					else
						controller.humanTurn(Connect4MoveMessage.YELLOW, c);
				}
			}
		}
		assertEquals(controller.getWinner(), Connect4Model.EMPTY);
		assertTrue(controller.isGameOver());
	}
	
	/**
	 * test 4 in a row vertically
	 */
	@Test
	public void test_fourInRow_Vertical()
	{
		for (int i = 0; i < 4; i++)
		{
			assertNotEquals(controller.getWinner(), Connect4MoveMessage.YELLOW);
			assertFalse(controller.isGameOver());
			controller.humanTurn(Connect4MoveMessage.YELLOW, 1);	
		}
		assertEquals(controller.getWinner(), Connect4MoveMessage.YELLOW);	
		assertTrue(controller.isGameOver());
	}
	
	/**
	 * Tests 4 in a row horizontally.
	 */
	@Test
	public void test_fourInRow_Horizontal()
	{
		for (int i = 0; i < 4; i++)
		{
			assertNotEquals(controller.getWinner(), Connect4MoveMessage.RED);
			assertFalse(controller.isGameOver());
			controller.humanTurn(Connect4MoveMessage.RED, i);	
		}
		assertEquals(controller.getWinner(), Connect4MoveMessage.RED);	
		assertTrue(controller.isGameOver());
	}
	
	/**
	 * Tests 4 in a row diagonally (left/up).
	 */
	@Test
	public void test_fourInRow_LeftUp()
	{
		for (int i = 4; i > 0; i--)
		{
			assertNotEquals(controller.getWinner(), Connect4MoveMessage.RED);
			assertFalse(controller.isGameOver());
			
			controller.humanTurn(Connect4MoveMessage.RED, i);
			for (int j = 1; j <= (i - 1); j++)
			{
				controller.humanTurn(Connect4MoveMessage.YELLOW, i - j);
			}
		}
		assertEquals(controller.getWinner(), Connect4MoveMessage.RED);	
		assertTrue(controller.isGameOver());
	}
	
	/**
	 * Tests 4 in a row diagonally (right/up).
	 */
	@Test
	public void test_fourInRow_RightUp()
	{
		for (int i = 0; i < 4; i++)
		{
			assertNotEquals(controller.getWinner(), Connect4MoveMessage.RED);
			assertFalse(controller.isGameOver());
			
			controller.humanTurn(Connect4MoveMessage.RED, i);
			for (int j = 1; j <= (3 - i); j++)
			{
				controller.humanTurn(Connect4MoveMessage.YELLOW, i + j);
			}
		}
		assertEquals(controller.getWinner(), Connect4MoveMessage.RED);	
		assertTrue(controller.isGameOver());
	}
	
	/**
	 * Uses computerTurn method until win.
	 */
	@Test
	public void test_ComputerTurn()
	{
		while (!controller.isGameOver())
		{
			controller.computerTurn(Connect4MoveMessage.RED);
		}
		assertEquals(controller.getWinner(), Connect4MoveMessage.RED);
	}
	
	/**
	 * Tests Observer/Observable relationship.
	 */
	@Test
	public void test_setModelObserver()
	{
		Connect4View view = new TestView();
		controller.setModelObserver(view);
		
		assertFalse(observed);
		controller.computerTurn();
		assertTrue(observed);
	}
	
	/**
	 * TestView class that extends Connect4View for the purposes of checking the
	 * Observer/Observable relationship.
	 * 
	 * @author Caroline O'Neill
	 *
	 */
	private class TestView extends Connect4View
	{
		@Override
		public void update(Observable o, Object arg) 
		{
			observed = true;
		}
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
		int[][] grid = controller.getGridCopy();	
		for (int row = 0; row < Connect4Model.ROWS; row++)
		{
			for (int col = 0; col < Connect4Model.COLUMNS; col++)
			{
				// check down
				if (row + 3 < Connect4Model.ROWS 
					&& grid[row + 1][col] == color
					&& grid[row + 2][col] == color
					&& grid[row + 3][col] == color)
				{
					return true;
				}
		
				// check right
				if (col + 3 < Connect4Model.COLUMNS 
					&& grid[row][col + 1] == color
					&& grid[row][col + 2] == color
					&& grid[row][col + 3] == color)
				{
					return true;
				}
		
				// check left/down
				if (row - 3 >= 0 && col + 3 < Connect4Model.COLUMNS
					&& grid[row - 1][col + 1] == color
					&& grid[row - 2][col + 2] == color
					&& grid[row - 3][col + 3] == color)
				{
					return true; 
				}
		
				// check right/down
				if (row + 3 < Connect4Model.ROWS && col + 3 < Connect4Model.COLUMNS 
					&& grid[row + 1][col + 1] == color
					&& grid[row + 2][col + 2] == color
					&& grid[row + 3][col + 3] == color)
				{
					return true;
				}
			}
		}
		return false;
	}
}
