import StepsGame;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Arrays;
import java.util.List;




/**
 * A very simple viewer for piece placements in the steps game.
 *
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */
public class Viewer extends Application {

    private static final int ONEPIECE_SIDE = 1;                         // dimensions of sub-board
    private static final int ONEPIECE_PLACES = ONEPIECE_SIDE*ONEPIECE_SIDE;   // number of places in sub-board
    private static final int XSIDE = 10;
    private static final int YSIDE = 5; // dimensions of overall board
    private static final int PLACES = XSIDE * YSIDE;// number of places in overall board

    /* board layout */
    private static final int SQUARE_SIZE =65;
    private static final int PIECE_IMAGE_SIZE = (int) ((3*SQUARE_SIZE)*1.33);
    private static final int VIEWER_WIDTH = 750;
    private static final int VIEWER_HEIGHT = 500;

    private static final int MARGIN_X = 30;
    private static final int BOARD_X = MARGIN_X;
    private static final int MARGIN_Y = SQUARE_SIZE;
    private static final int BOARD_Y = MARGIN_Y;

    private static final int MINI_TILE_SIZE = 30;
    private static final int MINI_TILE_PITCH = 30;

    private static final Paint SUBBOARD_FILL = Color.HONEYDEW;
    private static final Paint SUBBOARD_STROKE = Color.GREY;

    private final Group board = new Group();

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group controls = new Group();
    TextField textField;

    static class Square extends ImageView {

        Square(String id){
            //String n[] = {"AA", "AE", "BA", "BE", "CA", "CE", "DA", "DE", "EA", "EE", "FA", "FE", "GA", "GE", "HA", "HE"};
            //List<String> list = Arrays.asList(n);
            if (id.equals("")) {
                throw new IllegalArgumentException("Bad tile id: '" + id +"'");
            }
            setImage(new Image(Viewer.class.getResource(URI_BASE + id + ".png").toString()));

            // Take shapes from assets with their addresses.

        }

        Square(String id, int pos){
                this(id);

                setFitHeight(MARGIN_X*7.7);
                setFitWidth(MARGIN_X*7.7);
                //Comment: can't use SQUARE_SIZE to set the size of the shapes. Use MARGIN_X instend and enlarge the size by 7.7
                setPreserveRatio(true);
                if (pos < -11 || pos >= PLACES) {
                    throw new IllegalArgumentException("Bad tile position: " + pos);
                }
                int rowminor = pos / XSIDE;
                int colminor = pos % XSIDE;
               // int rowmajor = (pos / ONEPIECE_PLACES) / ONEPIECE_SIDE;
               // int colmajor = (pos / ONEPIECE_PLACES) % ONEPIECE_SIDE;

            setLayoutX(BOARD_X + (colminor) * SQUARE_SIZE-25);
            setLayoutY(BOARD_Y + (rowminor) * SQUARE_SIZE-15);
            }




        Square(String id, int pos, int x, int y){
            this(id);

            setFitHeight(MINI_TILE_SIZE);
            setFitWidth(MINI_TILE_SIZE);
            if (pos < 0 || pos >= PLACES) {
                throw new IllegalArgumentException("Bad tile position: " + pos);
            }

            setLayoutX(x);
            setLayoutY(y);

        }
        // get the shapes from its sources as well as their positions. See assignment 1 'Square'
    }




    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement  A valid placement string
     */
    void makePlacement(String placement) {

        // Task8 Do not clear the textField in order to place pieces step by step.
        // board.getChildren().clear();
        if (!StepsGame.isPlacementWellFormed(placement)) {
            throw new IllegalArgumentException("Hide board incorrect length: " + placement);
        }
        Rectangle r = new Rectangle(BOARD_X,  BOARD_Y, XSIDE*SQUARE_SIZE, YSIDE*SQUARE_SIZE);
        r.setFill(SUBBOARD_FILL);
        r.setStroke(SUBBOARD_STROKE);
        board.getChildren().add(r);
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYabcdefghijklmnopqrstuvwxy";
        for (int i = 0; i<50; i++){
            double x = 1.0/3;
            Text t = new Text(BOARD_X + SQUARE_SIZE/3 + (i %XSIDE) * SQUARE_SIZE - 5, BOARD_Y + 2*SQUARE_SIZE/3 +(i / XSIDE) * SQUARE_SIZE + 10, alphabet.substring(i, i+1));
            t.setFont(new Font(20));
            t.setFill(Color.GREY);
            board.getChildren().add(t);
        }
        // Making board and adding the letters.


        String[] onepiece = placement.split("(?<=\\G...)");
        String[] shape = new String[onepiece.length];
        String[] location = new String[onepiece.length];
        char[] loc = new char[onepiece.length];
        int[] lct = new int[onepiece.length];
        for (int i = 0; i<onepiece.length; i++){
            shape[i] = onepiece[i].substring(0, 2);
            location[i] = onepiece[i].substring(2, 3);
            loc[i] = location[i].charAt(0);
            if(loc[i] >= 'A' && loc[i] <= 'Y'){
                lct[i] = (int)loc[i] - 65;
            }else if(loc[i] >= 'a' && loc[i] <= 'y'){
                lct[i] = (int)loc[i] - 72;
            }
            //Split String into 3 letters and NUMBER the positions A-y into 0-49.


            String tile = shape[i];
            if (tile != "")
                board.getChildren().add(new Square(tile, lct[i]-11));
        }
        // Adding shapes
        board.toBack();

        // FIXME Task 4: implement the simple placement viewer
    }


    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField ();
        textField.setPrefWidth(300);

        // Task8 Start the game with the randomly selected piece placement
        textField.setText("AAO");
        makePlacement("AAO");

        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                makePlacement(textField.getText());
                // Task8 Do not clear the textField in order to place pieces step by step.
                // textField.clear();
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("StepsGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);
        root.getChildren().add(board);

        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
