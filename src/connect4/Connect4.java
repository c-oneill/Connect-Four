package connect4;

import javafx.application.Application;

/**
 * This class serves as the launch point for the Connect4 program.
 * 
 * @author Kristopher Rangel
 *
 */
public class Connect4 {
    
    /**
     * <ul><b><i>main</i></b></ul>
     * <ul><ul><p><code>public static void main (String[] args) </code></p></ul>
     *
     * Launches the Connect4 game.
     *
     * @param args - command line arguements
     */
    public static void main(String[] args) {
        Application.launch(Connect4View.class, args);
    }

}
