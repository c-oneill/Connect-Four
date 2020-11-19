package connect4;

import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.scene.shape.Circle;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

/**
 * This class serves as the UI for the Connect4 program.
 * 
 * <p>This class is an {@link Observer} of the {@link Connect4Model} class.
 * 
 * </p>
 * 
 * @author Kristopher Rangel
 * 
 * @author Caroline O'Neill (integrating multi-threading from controller)
 *
 */
public class Connect4View extends Application implements Observer{

    private final int CIRCLE_RADIUS = 20;
    private final int VGAP_PADDING = 8;
    private final int HGAP_PADDING = 8;
    private final int INSETS_PADDING = 4;
    private final Color BACKGROUND_COLOR = Color.BLUE;
    private final int COLUMNS = 7;
    private final int ROWS = 6;

    private Stage stage;
    private Scene scene;
    private VBox window;
    private GridPane board;
    private MenuBar menuBar;
    private Connect4Controller controller;
    private Connect4MoveMessage message;
    
    private boolean isGameOver;
    private boolean inputEnabled;
    private String server;
    private int port;
    private boolean isServer;
    private boolean isHuman;
    private int color;
    
    
    /**
     * <ul><b><i>start</i></b></ul>
     * <ul><ul><p><code>public void start (Stage stage) </code></p></ul>
     *
     * The main entry point for all JavaFX applications.The start method is called after 
     * the init method has returned,and after the system is ready for the application to begin running. 
     *
     * @param stage - the primary stage for this application, onto whichthe application scene can be set.
     * 
     * @author Kristopher Rangel
     */
    @Override
    public void start(Stage stage) {

        controller = new Connect4Controller();
        controller.setModelObserver(this);
        scene = new Scene(window);
        
        // Showing stage
        try {
            inputEnabled = true;
            
            // setting the stage
            stage.setTitle("Connect4");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            this.stage = stage;

        }catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    /**
     * <ul><b><i>init</i></b></ul>
     * <ul><ul><p><code>public void init () </code></p></ul>
     *
     *The application initialization method. This method is called immediately
     *after the Application class is loaded and constructed.
     *
     *<p>This method initializes the element of the scene.</p>
     *
     * @author Kristopher Rangel
     *
     */
    public void init() {
        initBoard();
        createCircles();
        initMenuBar();
        this.window = new VBox(menuBar, board);
        
        // default to human player server
        this.isServer = true;
        this.isHuman = true;
        
    }
    
    /**
     * <ul><b><i>stop</i></b></ul>
     * <ul><ul><p><code>public void stop ()</code></p></ul>
     *
     *This method is called when the application should stop, and provides a
     *convenient place to prepare for application exit and destroy resources. 
     *
     * @author Kristopher Rangel
     */
    public void stop() {
        //Network cleanup 
        controller.closeNetwork();
    }
    
    /**
     * <ul><b><i>initMenuBar</i></b></ul>
     * <ul><ul><p><code>private void initMenuBar () </code></p></ul>
     *
     * Sets up the menu bar and associated menu options.
     *
     * @author Kristopher Rangel
     */
    private void initMenuBar() {
        menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(e -> { getNewGameOptions(); });
        fileMenu.getItems().add(newGame);
        menuBar.getMenus().add(fileMenu);
    }
    
    /**
     * <ul><b><i>getNewGameOptions</i></b></ul>
     * <ul><ul><p><code>private void getNewGameOptions () </code></p></ul>
     *
     * This method launches a dialog box that allows the user
     * to enter Network Setup options.
     *
     * @author Kristopher Rangel
     */
    private void getNewGameOptions() {
        Connect4NetworkSetup ns = new Connect4NetworkSetup();
        ns.showAndWait();
        if(ns.userHitOK()) { // user hit okay to start new game
            
            // Getting user options
            server = ns.getServer();
            port = ns.getPort();
            isHuman = ns.getPlayAsSelection();
            isServer = ns.getCreateModeSelection();
            
            startNewGame();
        }
 
    }
    
    /**
     * <ul><b><i>startNewGame</i></b></ul>
     * <ul><ul><p><code>private void startNewGame () </code></p></ul>
     *
     * This function starts a new game with the options selected by the user.
     *
     * @author Kristopher Rangel
     */
    private void startNewGame() {
    	controller = new Connect4Controller();
    	controller.setModelObserver(this);
    	createCircles();
          
        boolean hasConnectionError = controller.buildNetwork(isServer, server, port);
        if(hasConnectionError) {
        	showAlert(AlertType.ERROR, controller.getNetworkBuildError());
        } else {
        	if(isServer) {
        		color = Connect4MoveMessage.YELLOW;
                stage.setTitle("Connect4 (Server)");
                inputEnabled = true; // server takes first turn
                if (!isHuman)
                {
                	// initiate computerTurn
                	controller.computerTurn(this.color);
                }
        	} else {
                color = Connect4MoveMessage.RED;
                stage.setTitle("Connect4 (Client)");
                inputEnabled = false; // client waits for server
                controller.initiateListening();
            }
        }
    }

    /**
     * <ul><b><i>initBoard</i></b></ul>
     * <ul><ul><p><code> private void initBoard () </code></p></ul>
     *
     * A run-once function that sets the initial state of the Connect4 board.
     * 
     * <p>This function sets the board background, alignment, and padding. Then it
     * invokes the {@link #createCircles()} method.
     *
     * @author Kristopher Rangel
     */
    private void initBoard() {
        board = new GridPane();
        
        BackgroundFill bgfill = new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(bgfill);
        board.setBackground(bg);
        board.setHgap(HGAP_PADDING);
        board.setVgap(VGAP_PADDING);
        board.setPadding(new Insets(INSETS_PADDING, INSETS_PADDING, INSETS_PADDING, INSETS_PADDING));
        board.setAlignment(Pos.CENTER);
        board.setOnMouseClicked(e -> { if(inputEnabled) onClick(e.getX()); } );
    }
    
    /**
     * <ul><b><i>onClick</i></b></ul>
     * <ul><ul><p><code>private void onClick (double xCoord) </code></p></ul>
     *
     * This method takes the mouse cursor x-coordinate and converts it into the 
     * appropriate column. Then it invokes the {@link #selectColumn} method to
     * complete the on click action.
     *
     * @param xCoord - the x-coordinate of the mouse cursor location on click
     * 
     * @author Kristopher Rangel
     */
    private void onClick(double xCoord) {
        
        if(isHuman && inputEnabled) {
            int position = (int) Math.ceil(xCoord); // Rounding up decimal to nearest integer
            int column = (position - 5) / (HGAP_PADDING + 2 * CIRCLE_RADIUS); // Calculating column based on column width
            column = (column >= COLUMNS) ? COLUMNS - 1 : column; // limiting to last column
            selectColumn(column);
        }
    }
    
    /**
     * <ul><b><i>selectColumn</i></b></ul>
     * <ul><ul><p><code>private void selectColumn (int column) </code></p></ul>
     *
     * Checks selected column for valid move and performs move, if available.
     *
     * @param column - the column selected to perform a move on
     * 
     * @author Kristopher Rangel
     */
    private void selectColumn(int column) {
        
        if(controller.isColumnFull(column)){  
            showAlert(AlertType.ERROR, "Column full, pick somewhere else!");
        }else { // Processing turn
           controller.humanTurn(color, column);                      
        }
        
    }

    
    /**
     * <ul><b><i>createCircles</i></b></ul>
     * <ul><ul><p><code> private void createCircles () </code></p></ul>
     *
     * Creates white circle objects and adds them to the board.
     *
     * @author Kristopher Rangel
     */
    private void createCircles() {
        board.getChildren().clear();
        for(int row = 0; row < ROWS; row++) {
            for(int col = 0; col < COLUMNS; col++) {
                Circle c = new Circle(CIRCLE_RADIUS, Color.WHITE);                
                board.add(c, col, row);
            }
        }
    }
    
    /**
     * <ul><b><i>checkGameOver</i></b></ul>
     * <ul><ul><p><code> private void checkGameOver () </code></p></ul>
     *
     * Checks with the {@link Connect4Controller} for a game over condition
     * and displays an alert box in that event.
     *
     * @author Kristopher Rangel
     */
    private void checkGameOver() {
        //System.out.println("Checking for game over");
        isGameOver = controller.isGameOver();
        if(isGameOver){
            //System.out.println("Is game over");
            String msg = "empty message";
            
            int otherPlayerColor = Connect4MoveMessage.RED;
            if (this.color == Connect4MoveMessage.RED)
            	otherPlayerColor = Connect4MoveMessage.YELLOW;
     
            	
            
            if(controller.getWinner() == this.color) { msg = "You won!"; }
            else if(controller.getWinner() == otherPlayerColor) { msg = "You lost!"; }
            else { msg = "It's a tie!"; }
            
            inputEnabled = false;
            
            showAlert(AlertType.INFORMATION, msg);
        }     
    }
    
    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
    }
    
    /**
     * <ul><b><i>update</i></b></ul>
     * <ul><ul><p><code> public void update (Observable o, Object arg) </code></p></ul>
     *
     * This updates the view based on changes to the observed {@link Connect4Model}
     * object.
     *
     * @param o - the {@link Connect4Model} being observed
     * @param arg - a {@link Connect4MoveMessage} with information related to the move
     * 
     * @author Kristopher Rangel 
     */
    public void update(Observable o, Object arg) {
        message = (Connect4MoveMessage) arg;
 
        int row = message.getRow();
        int col = message.getColumn();
        int color = message.getColor();
        int index = (row*COLUMNS) + col;
        Color paint = Color.WHITE;
        
        if(color == Connect4MoveMessage.YELLOW) { paint = Color.YELLOW; }
        if(color == Connect4MoveMessage.RED)    { paint = Color.RED; }
        
        Circle c = (Circle) board.getChildren().get(index);
        c.fillProperty().set(paint);
        //System.out.printf("Updating color of row: %d, col: %d\n", row, col);
        
        checkGameOver();
        
        // switch inputEnabled on/off
        if (inputEnabled)
        	inputEnabled = false;
        else
        {
        	inputEnabled = true;
        	if (!isHuman && !controller.isGameOver())
        	{
        		// initiate computerTurn
            	controller.computerTurn(this.color);
        	}
        }        
    }
    
}
