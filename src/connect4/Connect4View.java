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
 */
public class Connect4View extends Application implements Observer{

    private final int CIRCLE_RADIUS = 20;
    private final int VGAP_PADDING = 8;
    private final int HGAP_PADDING = 8;
    private final int INSETS_PADDING = 4;
    private final Color BACKGROUND_COLOR = Color.BLUE;
    private final int COLUMNS = 7;
    private final int ROWS = 6;

    private Scene scene;
    private VBox window;
    private GridPane board;
    private MenuBar menuBar;
    
    private boolean inputEnabled;
    
    /* This main function used for unit testing */
    public static void main(String[] args) {
        launch(args);
    }
    /* **************************************** */
    
    @Override
    public void start(Stage stage) {

        // Initialization 
        init(stage);

        // Showing stage
        try {
            inputEnabled = true;
            
            // setting the stage
            stage.setTitle("Connect4");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        }catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    /**
     * <ul><b><i>init</i></b></ul>
     * <ul><ul><p><code> private void init () </code></p></ul>
     *
     * This method initializes the scene elements and adds them to the scene
     *
     * @param stage
     */
    private void init(Stage stage) {
        initBoard();
        createCircles();
        initMenuBar();
        this.window = new VBox(menuBar, board);
        this.scene = new Scene(window);
    }
    
    private void initMenuBar() {
        menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        menuBar.getMenus().add(fileMenu);
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
     */
    private void onClick(double xCoord) {
        int position = (int) Math.ceil(xCoord);
        int column = (position - 5) / (HGAP_PADDING + 2 * CIRCLE_RADIUS);
        column = (column >= COLUMNS) ? COLUMNS - 1 : column; 
        //System.out.printf("Mouse x = %.1f \tPosition = %d \t Column = %d\n", xCoord, position, column);
        selectColumn(column);
    }
    
    /**
     * <ul><b><i>selectColumn</i></b></ul>
     * <ul><ul><p><code> void selectColumn () </code></p></ul>
     *
     * Checks selected column for valid move and performs move, if available.
     *
     * @param col
     */
    private void selectColumn(int col) {
        boolean columnFull = false;
        
        //TODO check for full column
        
        
        if(columnFull) {
            Alert alert = new Alert(AlertType.INFORMATION, "Column full, pick somewhere else!");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
        }
    }
    
    /**
     * <ul><b><i>createCircles</i></b></ul>
     * <ul><ul><p><code> private void createCircles () </code></p></ul>
     *
     * Creates white circle objects and adds them to the board.
     *
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
     */
    private void checkGameOver() {
        boolean isGameOver = false;
        boolean hasWon = false;
        //TODO check with controller for win/loss condition
        //TODO set win flag if won
        if(isGameOver) {
            String msg = "You lost!";
            if(hasWon)
                msg = "You won!";
            Alert alert = new Alert(AlertType.INFORMATION, msg);
            alert.showAndWait().filter(response -> response == ButtonType.OK);

        }
        
    }
    
    
    
    
    
    /**
     * <ul><b><i>update</i></b></ul>
     * <ul><ul><p><code> public void update (Observable o, Object arg) </code></p></ul>
     *
     * This updates the view based on changes to the observed {@link Connect4Model}
     * object.
     *
     * @param o - the {@link Connect4Model} being observed
     * @param arg
     */
    public void update(Observable o, Object arg) {

    }
}
