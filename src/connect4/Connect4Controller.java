package connect4;

import javafx.application.Platform;

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
    
    private Connect4Network network;
    
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
     * Build a client/server connection as a {@link Connect4Network}.
     * @param isServer is this instance a server
     * @param server the server to connect to (if it's a client)
     * @param port the port to connect to
     * @return start error
     */
    public boolean buildNetwork(boolean isServer, String server, int port)
    {
    	network = new Connect4Network(isServer, server, port);
    	return network.getStartError();
    }
    
    /**
     * Gets the error message associated with starting up the network.
     * @return error message
     */
    public String getNetworkBuildError()
    {
    	return network.getErrorMessage();
    }
    
    public void closeNetwork()
    {
    	network.closeConnection();
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
        return takeTurn(col, Connect4MoveMessage.YELLOW);
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
        return takeTurn(col, color);
    }
    
    /**
     * Makes a play in the indicated column in the computer player color, red.
     * @return true if the play is made, false otherwise
     */
    public boolean computerTurn()
    {
    	int randCol = ((int) (Math.random() * 100)) % Connect4Model.COLUMNS;
    	return takeTurn(randCol, Connect4MoveMessage.RED);
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
    	return takeTurn(randCol, color);
    }
    
    /**
     * Plays a disc in the next availible position in the indicated column. No
     * play made if the column is full. Message with new move is sent to the
     * other instance. 
     * <p> A new thread is created to recieve the other player's move in the
     * background. This ensures the main event loop/queue is not blocked. In the
     * new thread, Platform.runLater is used so the model updates the view in 
     * the main thread. JavaFX can only update the UI in the main thread.
     * @param col column index
     * @param color disc color
     * @return true if the disc is played in the column, false otherwise
     */
    public boolean takeTurn(int col, int color)
    {
    	// invalid moves
    	if (color == Connect4Model.EMPTY || isColumnFull(col))
    		return false;
    	
    	int row = nextOpen[col];
    	
    	// make move --> updates model --> updates view
    	nextOpen[col]--;
    	model.setPosition(row, col, color);
    	
    	// send message
    	Connect4MoveMessage sendMessage = new Connect4MoveMessage(row, col, color);
    	network.writeMessage(sendMessage);
    	
    	// create new Thread: recieve message --> update model --> updates view
    	Thread recvThread = new Thread(() -> 
    	{
    		Connect4MoveMessage recvMessage = network.readMessage();
    		if(recvMessage == null)
    			return;
    		int recvRow = recvMessage.getRow();
            int recvCol = recvMessage.getColumn();
            int recvColor = recvMessage.getColor();
    		Platform.runLater(() -> 
    		{
    			// model/view update pushed until later in the main thread
    			nextOpen[recvMessage.getColumn()]--;
    			model.setPosition(recvRow, recvCol, recvColor);
    		});
    	});
    	// new thread started -> run() started
    	recvThread.start();
    	
    	return true;
    }
    
    /**
     * Prepares client to recieve first message, then passes off respobibility 
     * to the {@link Connect4Controller#takeTurn(int, int)} method.
     */
    public void initiateListening()
    {
    	Connect4MoveMessage recvMessage = network.readMessage();
    	nextOpen[recvMessage.getColumn()]--;
    	model.setPosition(recvMessage.getRow(), recvMessage.getColumn(), recvMessage.getColor());
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
