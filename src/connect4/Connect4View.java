package connect4;

import java.util.Observable;
import java.util.Observer;

import com.sun.prism.paint.Color;

import javafx.application.Application;
import javafx.stage.Stage;

public class Connect4View extends Application implements Observer{

    private final int CIRCLE_RADIUS = 20;
    private final int VGAP_PADDING = 8;
    private final int HGAP_PADDING = 8;
    private final int INSETS_PADDING = 8;
    private final Color BACKGROUND_COLOR = Color.BLUE;
    private final int COLUMNS = 7;
    private final int ROWS = 6;

    @Override
    public void start(Stage stage) {

    }


    public void update(Observable o, Object arg) {

    }
}
