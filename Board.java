import StepsGame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.lang.reflect.Array;
import java.util.*;

public class Board extends Application {
    private static final int BOARD_WIDTH = 1200;
    private static final int BOARD_HEIGHT = 1000;



    private static final int SQUARE_SIZE = 65;

    private static final int MARGIN_X = 30;
    private static final int BOARD_X = MARGIN_X;
    private static final int MARGIN_Y = 65;
    private static final int BOARD_Y = MARGIN_Y;

    /* marker for unplaced piece */
    private static final char NOT_PLACED = ' ';

    private static final int XSIDE = 10;
    private static final int YSIDE = 5; // dimensions of overall board
    private static final int PLACES = XSIDE * YSIDE;// number of places in overall board

    private final Group root = new Group();
    private final Group controls = new Group();
    private TextField textField;


    private final Group board = new Group();
    private final Group piece = new Group();
    private final Group TF = new Group();

    private static final String URI_BASE = "assets/";

    private static final Paint SUBBOARD_FILL = Color.HONEYDEW;
    private static final Paint SUBBOARD_STROKE = Color.GREY;

    private static final String[] GOOD_PAIRS = {
            "CEQ",
            "GEO",
            "BGK",
            "BHF",
            "CHS",
            "GDL",
            "BGK",
            "DFO",
            "CGO",
            "HBL",
            "GFS",
            "BBG",
            "DFQ",
            "FDN",
            "GGS",
            "GHS",
            "EAo",
            "FDN",
            "EGO",
            "AAL",
            "ADg"
    };

    /* the state of the pieces */
    char[] piecestate = new char[8];   //  all off screen to begin with
    private final Text completionText = new Text("Well done!");

    // FIXME Task 7: Implement a basic playable Steps Game in JavaFX that only allows pieces to be placed in valid places

    private void hideCompletion() {
        completionText.toBack();
        completionText.setOpacity(0);
    }

    public class Pieces extends ImageView {
        int pieces;
        int p2;
        Pieces(char pieces,char p2) {
            if (!(pieces >= 'A' && pieces <= 'H')) {
                throw new IllegalArgumentException("Bad mask: \"" +pieces + "\"");
            }
            if(p2=='A') {
                setImage(new Image(Viewer.class.getResource(URI_BASE + pieces + 'A' + ".png").toString()));
            }
            else {
                setImage(new Image(Viewer.class.getResource(URI_BASE + pieces + 'E' + ".png").toString()));
            }
            this.pieces = pieces -'A';
            setFitHeight(SQUARE_SIZE);
            setFitWidth(SQUARE_SIZE);

        }
        Pieces(char p1, char p2, int pos){
            if (!(p1 >= 'A' && p1 <= 'H')) {
                throw new IllegalArgumentException("Bad mask: \"" +pieces + "\"");
            }
            setImage(new Image(Viewer.class.getResource(URI_BASE + p1 + p2 + ".png").toString()));

            setFitHeight(MARGIN_X*7.7);
            setFitWidth(MARGIN_X*7.7);
            //Comment: can't use SQUARE_SIZE to set the size of the shapes. Use MARGIN_X instend and enlarge the size by 7.7
//            setPreserveRatio(true);
//            if (pos < -11 || pos >= PLACES) {
//                throw new IllegalArgumentException("Bad tile position: " + pos);
//            }
//            pos -= 'A';
//            int o = (pos % 4);
//            int rowminor = pos / XSIDE;
//            int colminor = pos % XSIDE;
//            // int rowmajor = (pos / ONEPIECE_PLACES) / ONEPIECE_SIDE;
//            // int colmajor = (pos / ONEPIECE_PLACES) % ONEPIECE_SIDE;
//
//            setLayoutX(BOARD_X + (colminor) * SQUARE_SIZE-20);
//            setLayoutY(BOARD_Y + (rowminor) * SQUARE_SIZE-15);
//            setRotate(90 * o);

            int NTindex = pos;
            int TLindex = NTindex - 11;
            if (TLindex >= 0) {
                Text t2 = texts.get(TLindex);
                setLayoutX(t2.getX() - SQUARE_SIZE + 20);
                setLayoutY(t2.getY() - SQUARE_SIZE + 5);
            }else{
                Text t1 = texts.get(NTindex);
                setLayoutX(t1.getX() - 2 * SQUARE_SIZE + 20);
                setLayoutY(t1.getY() - 2* SQUARE_SIZE + 5);
            }
        }

    }

    private String placement0;
    private String placement;
    private String pl = "";
    private char p2aschar = ' ';
    double p2calindex = 0;

    class DraggableFXPieces extends Pieces {
        int homeX=300, homeY;           // the position in the window where the mask should be when not on the board
        double mouseX, mouseY;      // the last known mouse positions (used when dragging)

        /**
         * Construct a draggable piece
         *
         * @param pieces The pieces identifier ('AA' - 'HE')
         */
        DraggableFXPieces(char pieces,char p2) {
            super(pieces,p2);
            piecestate[pieces- 'A'] = NOT_PLACED;
            if(pieces=='A' && p2 == 'A'){
                homeX = 700;
                setLayoutX(homeX);
                homeY = 20;
                setLayoutY(homeY);
            }
            if(pieces=='A' && p2 == 'E') {
                homeX = 900;
                setLayoutX(homeX);
                homeY = 20;
                setLayoutY(homeY);
            }
            if(pieces=='B' && p2 == 'A'){
                homeX = 700;
                setLayoutX(homeX);
                homeY = 200;
                setLayoutY(homeY);
            }
            if(pieces=='B' && p2 == 'E') {
                homeX = 900;
                setLayoutX(homeX);
                homeY = 200;
                setLayoutY(homeY);
            }

            if(pieces>='C' && pieces<='E' && p2 == 'A'){
                homeX = 10 + 370*(pieces-'C');
                setLayoutX(homeX);
                homeY = 400;
                setLayoutY(homeY);
            }
            if(pieces>='C' && pieces<='E' && p2 == 'E') {
                homeX = 200 + 370*(pieces-'C');
                setLayoutX(homeX);
                homeY = 400;
                setLayoutY(homeY);
            }
            if(pieces>='F' && pieces<='H' && p2 == 'A'){
                homeX = 10 + 370*(pieces-'F');
                setLayoutX(homeX);
                homeY = 600;
                setLayoutY(homeY);
            }
            if(pieces>='F' && pieces<='H' && p2 == 'E') {
                homeX = 200+ 370*(pieces-'F');
                setLayoutX(homeX);
                homeY = 600;
                setLayoutY(homeY);
            }


            setFitHeight(MARGIN_X * 7.7);
            setFitWidth(MARGIN_X * 7.7);
            // setting starting postions

            /* event handlers */
            setOnScroll(event -> {            // scroll to change orientation
                hideCompletion();
                rotate();
                event.consume();
            });
            setOnMousePressed(event -> {      // mouse press indicates begin of drag
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                TF.getChildren().clear();
                System.out.println("The random starting placement is: "+placement0);
                //if (!canRotate())             // apply rotation constraint
                   // setRotate(StepsGame.getObjective().getMaskWConstraint() * 90);
            });
            setOnMouseDragged(event -> {      // mouse is being dragged
                hideCompletion();
                toFront();
                double movementX = event.getSceneX() - mouseX;
                double movementY = event.getSceneY() - mouseY;
                setLayoutX(getLayoutX() + movementX);
                setLayoutY(getLayoutY() + movementY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                event.consume();
            });
            setOnMouseReleased(event -> {     // drag is complete
                Text t1 = findNearestText(mouseX,mouseY);
                String nearesttext = t1.getText();
                p2calindex = getRotate()/90;
                int temInt = (int) p2calindex;
                int temchar = p2+temInt;
                p2aschar = (char) temchar;
                placement = String.valueOf(pieces) + p2aschar + nearesttext;
                //p2calindex = 0;

//                System.out.println(getLayoutX());
//                System.out.println(getLayoutY());
                snapToGrid();
                //setLayoutX(t1.getX());
                //setLayoutY(t1.getY());

                //setLayoutX(getLayoutX() + event.getSceneX() - mouseX);
                //setLayoutY(getLayoutY() + event.getSceneY() - mouseY);
            });
        }

        /**
         * @return true if the mask is on the board
         */

        private void snapToGrid() {
            if (onBoard()) {
                String pl1 = placement0 + placement;
                System.out.println("Now the string placement to check valid is: " + pl1);
                if (StepsGame.isPlacementSequenceValid(pl1)) {
                    placement0 = pl1;
                    System.out.println(pl1 +" is valid, thus now the placement is: " + placement0);
                    String x1 = pl1 + " is valid, thus now the placement is: " + placement0;
                    Text t4 = new Text(BOARD_X + SQUARE_SIZE/3, BOARD_Y - 10, x1);
                    t4.setFill(Color.BLACK);
                    t4.setFont(new Font(20));
                    TF.getChildren().add(t4);

                    if(placement0.length() == 24){
                        Text success = new Text(BOARD_WIDTH/2, BOARD_HEIGHT/2,"Success!");
                        success.setFill(Color.RED);
                        success.setFont(new Font(80));
                        success.toFront();
                        TF.getChildren().add(success);
                    }

                    Text t1 = findNearestText(mouseX, mouseY);
                    int NTindex = texts.indexOf(t1);
                    int TLindex = NTindex - 11;
                    if (TLindex >= 0) {
                        Text t2 = texts.get(TLindex);
                        setLayoutX(t2.getX() - SQUARE_SIZE + 20);
                        setLayoutY(t2.getY() - SQUARE_SIZE + 5);
                    }else{
                        setLayoutX(t1.getX() - 2 * SQUARE_SIZE + 20);
                        setLayoutY(t1.getY() - 2* SQUARE_SIZE + 5);
                    }
                    //setLayoutX((BOARD_WIDTH/2) + (((getLayoutX() + (1.5*SQUARE_SIZE))> BOARD_WIDTH/2 ? 0 : -3) * SQUARE_SIZE));
                    //setLayoutY((BOARD_HEIGHT/2) + ((getLayoutY() + (1.5*SQUARE_SIZE) > BOARD_HEIGHT/2 ? 0 : -3) * SQUARE_SIZE ));
                    setPosition();
                }else{
                    System.out.println(pl1 + " is NOT valid, thus now the placement is still: "+ placement0);
                    String x2 = pl1 + " is NOT valid, thus now the placement is still: " + placement0;
                    Text t5 = new Text(BOARD_X + SQUARE_SIZE/3, BOARD_Y - 10, x2);
                    t5.setFill(Color.BLACK);
                    t5.setFont(new Font(20));
                    TF.getChildren().add(t5);
                    snapToHome();
                }
            } else {
                System.out.println(placement.substring(0, 2) + " is not on Board!");
                String x2 = placement.substring(0, 2) + " is not on Board!";
                Text t5 = new Text(BOARD_X + SQUARE_SIZE/3, BOARD_Y - 10, x2);
                t5.setFill(Color.BLACK);
                t5.setFont(new Font(20));
                TF.getChildren().add(t5);
                snapToHome();
            }
           // makeExposed();
        }

        private boolean onBoard() {
            System.out.println("----------------- new placement -------------------");
            System.out.println("The new placement is: " + placement);
            String x = "The new placement is: " + placement;
            Text t3 = new Text(BOARD_X + SQUARE_SIZE/3, BOARD_Y - 35, x);
            t3.setFill(Color.BLACK);
            t3.setFont(new Font(20));
            TF.getChildren().add(t3);

//            String[] solutions = StepsGame.getSolutions(placement);
//            System.out.println("Now you have make placement of "+ placement + ", you have the following solutions: ");
//            for(int i=0; i<solutions.length; i++){
//                System.out.println(solutions[i]);
//                Set<String> firstplacements = StepsGame.getViablePiecePlacements(placement, solutions[i]);
//                System.out.println("And for solution " + solutions[i] + " you have the following first possible placement: ");
//                System.out.println(firstplacements);
//            }
            return getLayoutX() > (BOARD_X-XSIDE*SQUARE_SIZE/2-77*3) && (getLayoutX() < (BOARD_X+XSIDE*SQUARE_SIZE/2+77*3))
                   && getLayoutY() > (BOARD_Y-YSIDE*SQUARE_SIZE/2-77) && (getLayoutY() < (BOARD_Y+YSIDE*SQUARE_SIZE/2+77*3));

        }

        private void snapToHome() {
            setLayoutX(homeX);
            setLayoutY(homeY);
            setRotate(0);
            piecestate[pieces] = NOT_PLACED;
        }
        /**
         * Snap the mask to its home position (if it is not on the grid)
         */

        /**
         * @return true if this mask can be rotated
         */
        /**
         * Rotate the mask by 90 degrees (unless this is mask zero and there is a constraint on mask zero)
         */
        private void rotate() {
            setRotate((getRotate() + 90) % 360);
            setPosition();
        }

        /**
         * @return true if this mask can be rotated
         */
      //  private boolean canRotate() { return !(mask == 0 && hide.getObjective().getMaskWConstraint() != -1); }


        /**
         * Determine the grid-position of the origin of the mask (0 .. 15)
         * or -1 if it is off the grid, taking into account its rotation.
         */
        private void setPosition() {
            int x = (int) (getLayoutX() - BOARD_X) / SQUARE_SIZE;
            int y = (int) (getLayoutY() - BOARD_Y) / SQUARE_SIZE;
            int rotate = (int) getRotate() / 90;
            char val = (char) ('A' + (4 * (x + (2*y)) + rotate));
         //   maskstate[mask] = val;
        }


        /** @return the mask placement represented as a string */
        public String toString() {

            return null; //""+maskstate[p[0]];
        }

    }
    private ArrayList<Text> texts = new ArrayList<>();
    private void makeBoard(String b) {
        if (!StepsGame.isPlacementWellFormed(b)) {
            throw new IllegalArgumentException("Hide board incorrect length: " + b);
        }

        String[] onepiece = b.split("(?<=\\G...)");
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
            //NUMBER the positions into 0-49.
            String tile = shape[i];
            char tile0 = tile.charAt(0);
            char tile1 = tile.charAt(1);
            if (tile != "")
                root.getChildren().add(new Pieces(tile0, tile1, lct[i]));
        }
        root.toBack();
    }

    private void makePieces() {
        piece.getChildren().clear();
        char[] p2 = {'A','E'};
        for (char m = 'A'; m <= 'H'; m++) {
            for(int i=0;i<=1;i++) {
                piece.getChildren().add(new DraggableFXPieces(m, p2[i]));
            }
        }
    }


        // FIXME Task 8: Implement starting placements
    private String makeStartingPlacements() {
        Random r = new Random();
        String startingPlacement = GOOD_PAIRS[r.nextInt(GOOD_PAIRS.length-1)];
        return startingPlacement;
    }


        // FIXME Task 10: Implement hints

        // FIXME Task 11: Generate interesting starting placements



    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField ();
        textField.setPrefWidth(300);

        // Task8 Start the game with the randomly selected piece placement
        String startPlacement = makeStartingPlacements();
        placement0 = startPlacement;
        textField.setText(startPlacement);
        makeBoard(startPlacement);

        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                makeBoard(textField.getText());
                // Task8 Do not clear the textField in order to place pieces step by step.
                // textField.clear();
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(BOARD_HEIGHT - 50);
        controls.getChildren().add(hb);
    }
    private void resetPieces() {
        piece.toFront();
        for (Node n : piece.getChildren()) {
            ((DraggableFXPieces) n).snapToHome();
        }
    }
    private void newGame() {
        try {
            makePieces();

        } catch (IllegalArgumentException e) {
            System.err.println("Uh oh. "+ e);
            e.printStackTrace();
            Platform.exit();
        }
        resetPieces();
    }

    private Text findNearestText(double x, double y){
        int minidex = 0;
        int[] textx = new int[50];
        int[] texty = new int[50];
        textx[0] = BOARD_X + SQUARE_SIZE/3;
        texty[0] = BOARD_Y + 2*SQUARE_SIZE/3;
        double[] eachdistance = new double[50];
        eachdistance[0] = Math.sqrt((textx[0] - x)*(textx[0] - x) + (texty[0] - y)*(texty[0] - y));
        double mindistance = eachdistance[0];
        for (int i = 1; i<50; i++){
            textx[i] = BOARD_X + SQUARE_SIZE/3 + (i %XSIDE) * SQUARE_SIZE;
            texty[i] = BOARD_Y + 2*SQUARE_SIZE/3 +(i / XSIDE) * SQUARE_SIZE;
            eachdistance[i] = Math.sqrt((textx[i] - x)*(textx[i] - x) + (texty[i] - y)*(texty[i] - y));
            if (eachdistance[i] < mindistance){
                minidex = i;
                mindistance = eachdistance[i];
            }
        }
//        System.out.println(minidex);
//        System.out.println(texts.get(minidex).getX());
//        System.out.println(texts.get(minidex).getY());
//        System.out.println(texts.get(minidex).getText());
        return texts.get(minidex);
    }


    @Override
        public void start(Stage primaryStage) throws Exception {
            primaryStage.setTitle("StepsGame");
            Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT);
            //////////////// ADDING BOARD /////////////////////////
            Rectangle r = new Rectangle(BOARD_X,  BOARD_Y, XSIDE*SQUARE_SIZE, YSIDE*SQUARE_SIZE);
            r.setFill(SUBBOARD_FILL);
            r.setStroke(SUBBOARD_STROKE);
            root.getChildren().add(r);
        for (int i = 0; i<10; i=i+2){
            Circle c = new Circle();
            c.setCenterX(BOARD_X + SQUARE_SIZE/3 + (i %XSIDE) * SQUARE_SIZE);
            c.setCenterY(BOARD_Y + 2*SQUARE_SIZE/3 +(i / XSIDE) * SQUARE_SIZE);
            c.setRadius(25.0);
            c.setFill(Color.GREY);
            root.getChildren().add(c);
        }
        for (int i = 11; i<20; i=i+2){
            Circle c = new Circle();
            c.setCenterX(BOARD_X + SQUARE_SIZE/3 + (i %XSIDE) * SQUARE_SIZE);
            c.setCenterY(BOARD_Y + 2*SQUARE_SIZE/3 +(i / XSIDE) * SQUARE_SIZE);
            c.setRadius(25.0);
            c.setFill(Color.GREY);
            root.getChildren().add(c);
        }
        for (int i = 20; i<30; i=i+2){
            Circle c = new Circle();
            c.setCenterX(BOARD_X + SQUARE_SIZE/3 + (i %XSIDE) * SQUARE_SIZE);
            c.setCenterY(BOARD_Y + 2*SQUARE_SIZE/3 +(i / XSIDE) * SQUARE_SIZE);
            c.setRadius(25.0);
            c.setFill(Color.GREY);
            root.getChildren().add(c);
        }
        for (int i = 31; i<40; i=i+2){
            Circle c = new Circle();
            c.setCenterX(BOARD_X + SQUARE_SIZE/3 + (i %XSIDE) * SQUARE_SIZE);
            c.setCenterY(BOARD_Y + 2*SQUARE_SIZE/3 +(i / XSIDE) * SQUARE_SIZE);
            c.setRadius(25.0);
            c.setFill(Color.GREY);
            root.getChildren().add(c);
        }
        for (int i = 40; i<50; i=i+2){
            Circle c = new Circle();
            c.setCenterX(BOARD_X + SQUARE_SIZE/3 + (i %XSIDE) * SQUARE_SIZE);
            c.setCenterY(BOARD_Y + 2*SQUARE_SIZE/3 +(i / XSIDE) * SQUARE_SIZE);
            c.setRadius(25.0);
            c.setFill(Color.GREY);
            root.getChildren().add(c);
        }
            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYabcdefghijklmnopqrstuvwxy";
            for (int i = 0; i<50; i++){
                Text t = new Text(BOARD_X + SQUARE_SIZE/3 + (i %XSIDE) * SQUARE_SIZE - 5, BOARD_Y + 2*SQUARE_SIZE/3 +(i / XSIDE) * SQUARE_SIZE + 10, alphabet.substring(i, i+1));
                t.setFont(new Font(20));
                t.setFill(Color.BLACK);
                texts.add(t);
                root.getChildren().add(t);
            }
        /////////////////// ADDING BOARD ///////////////////////////////
            makeControls();
            root.getChildren().add(piece);
            root.getChildren().add(board);
            root.getChildren().add(controls);
            root.getChildren().add(TF);
            TF.toFront();
            //makeBoard("AAO");


            // makeControls();
            newGame();
            primaryStage.setScene(scene);
            primaryStage.show();

        }
    }



////COMMENT: left to do:
// make the texts appear in the Scene. Need task 6 and 9.
// make reset
// make success as "cheers" or so...