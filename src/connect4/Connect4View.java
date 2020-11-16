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
    private final int INSETS_PADDING = 8;
    private final Color BACKGROUND_COLOR = Color.BLUE;
    private final int COLUMNS = 7;
    private final int ROWS = 6;

    private Scene scene;
    private GridPane board;
    
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

            // setting the stage
            stage.setTitle("Connect4");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        }catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    private void init(Stage stage) {
        initBoard();
        
        //TODO add board to scene
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
        
        this.scene = new Scene(board);
        
        createCircles();
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
    
    private void checkWin() {
        boolean hasWon = false;
        
        //TODO check with controller for win condition
        
        if(hasWon) {
            //TODO show alert
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
