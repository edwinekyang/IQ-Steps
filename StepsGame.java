import java.util.*;

/**
 * This class provides the text interface for the Steps Game
 *
 * The game is based directly on Smart Games' IQ-Steps game
 * 
 * Edwin Yang
 */
public class StepsGame {

    // Pieces(0: No circle, 1: Lower circle, 2: Higher circle)
    public static final Map<String, String> pieceList = new HashMap<String, String>(){{
        put("AA", "120212100"); put("AB", "121012020"); put("AC", "001212021"); put("AD", "020210121");
        put("AE", "012121002"); put("AF", "010021212"); put("AG", "200121210"); put("AH", "212120010");
        put("BA", "020012021"); put("BB", "000212120"); put("BC", "120210020"); put("BD", "021212000");
        put("BE", "010120210"); put("BF", "210121000"); put("BG", "012021010"); put("BH", "000121012");
        put("CA", "020012120"); put("CB", "100212020"); put("CC", "021210020"); put("CD", "020212001");
        put("CE", "010120012"); put("CF", "010121200"); put("CG", "210021010"); put("CH", "002121010");
        put("DA", "020210021"); put("DB", "020212100"); put("DC", "120012020"); put("DD", "001212020");
        put("DE", "010021210"); put("DF", "200121010"); put("DG", "012120010"); put("DH", "010121002");
        put("EA", "020210120"); put("EB", "120212000"); put("EC", "021012020"); put("ED", "000212021");
        put("EE", "010021012"); put("EF", "000121210"); put("EG", "210120010"); put("EH", "012121000");
        put("FA", "001012120"); put("FB", "100210021"); put("FC", "021210100"); put("FD", "120012001");
        put("FE", "200120012"); put("FF", "012120200"); put("FG", "210021002"); put("FH", "002021210");
        put("GA", "021012120"); put("GB", "100212021"); put("GC", "021210120"); put("GD", "120212001");
        put("GE", "210120012"); put("GF", "012121200"); put("GG", "210021012"); put("GH", "002121210");
        put("HA", "021210021"); put("HB", "020212101"); put("HC", "120012120"); put("HD", "101212020");
        put("HE", "210021210"); put("HF", "202121010"); put("HG", "012120012"); put("HH", "010121202");
    }};

    // 1: No peg, 2: Peg
    public static final int[] BOARD_PEG_LOCATIONS = {1, 2, 1, 2, 1, 2, 1, 2, 1, 2,
                                                      2, 1, 2, 1, 2, 1, 2, 1, 2, 1,
                                                      1, 2, 1, 2, 1, 2, 1, 2, 1, 2,
                                                      2, 1, 2, 1, 2, 1, 2, 1, 2, 1,
                                                      1, 2, 1, 2, 1, 2, 1, 2, 1, 2};

    /**
     * Determine whether a piece placement is well-formed according to the following:
     * - it consists of exactly three characters
     * - the first character is in the range A .. H (shapes)
     * - the second character is in the range A .. H (orientations)
     * - the third character is in the range A .. Y and a .. y (locations)
     *
     * @param piecePlacement A string describing a piece placement
     * @return True if the piece placement is well-formed
     */
    static boolean isPiecePlacementWellFormed(String piecePlacement) {
        // FIXME Task 2: determine whether a piece placement is well-formed
        // Check if piecePlacement consists of exactly three characters
        // and return true if the string matches condition
        if (piecePlacement.length() == 3) {
            return piecePlacement.matches("[A-H]{2}[A-Ya-y]{1}");
        }
        return false;
    }

    public static boolean isHomeRingLocationValid(String placement){
        //check the second letter of the piece if is bettwen 'A' to 'D', then the homering needs to be located on a peg
        // if the second letter is between 'E' to 'H' then the homering must be on a location.
        char sec=placement.charAt(1);
        char hr = placement.charAt(2);
        char[] pegs = {'A', 'C', 'E', 'G', 'I', 'L', 'N', 'P', 'R', 'T', 'U', 'W', 'Y', 'b', 'd', 'g', 'i', 'k', 'm', 'o', 'p', 'r', 't', 'v', 'x'};
        char loc[] = {'B','D','F','H','J','K','M','O','Q','S','V','X','a','c','e','f','h','j','l','n','q','s','u','w','y'};
        boolean b = false;
            /* check if the second char of the piece is 'A'-'D', whether the home ring is located on a peg*/
        if(sec=='A' || sec=='B'|| sec=='C'|| sec=='D') {
            for (char c : pegs) {
                if (c==hr) {
                    b = true;
                    break;
                } else {
                    b = false;
                }
            }
        }

            /* check if the second char of the piece is 'E'-'H', whether the home ring is located on a location*/
        else if(sec=='E' || sec=='F'|| sec=='G'|| sec=='H') {
            for (char c : loc) {
                if (c == hr) {
                    b = true;
                    break;
                } else {
                    b = false;
                }
            }
        }

        return b;
    }
    /**
     * Determine whether a placement string is well-formed:
     *  - it consists of exactly N three-character piece placements (where N = 1 .. 8);
     *  - each piece placement is well-formed
     *  - no shape appears more than once in the placement
     *
     * @param placement A string describing a placement of one or more pieces
     * @return True if the placement is well-formed
     */
    public static boolean isPlacementWellFormed(String placement) {
        boolean b = false;
        if(placement.equals("") || placement==null) {
            b = false;
        } else {
            String s[] = new String[placement.length() / 3];
            String x[] = new String[placement.length() / 3];
            int m = 0;
            for (int i = 0; i < placement.length() / 3; i++) {
                if (placement.length() / 3 >= 1 && placement.length() / 3 <= 8 && placement.length() % 3 == 0) {
                    s[i] = placement.substring(m, m + 3);

                    if (isPiecePlacementWellFormed(s[i]) == true) {
                        x[i] = s[i].substring(0, s[i].length() - 1);
                        b = true;

                    } else {
                        b = false;
                        break;
                    }
                    m+=3;

                } else {
                    b = false;
                }
            }

            for (int q = 0; q < x.length; q++) {
                for (int p = q + 1; p < x.length; p++) {

                    if (x[p].equals("") || x[p].equals(null)) {
                        b = false;

                    } else if (x[p].equals(x[q])) {
                        b = false;
                    }
                }
            }
        }
        return b;
    }

    /**
     * Determine whether a placement sequence is valid.  To be valid, the placement
     * sequence must be well-formed and each piece placement must be a valid placement
     * (with the pieces ordered according to the order in which they are played).
     *
     * @param placement A placement sequence string
     * @return True if the placement sequence is valid
     */
    public static boolean isPlacementSequenceValid(String placement) {
        String[] onepiece = placement.split("(?<=\\G...)");
        String[] shape = new String[onepiece.length];
        String[] location = new String[onepiece.length];
        char[] loc = new char[onepiece.length];
        // 0 to 49
        int[] lct = new int[onepiece.length];

        for (int i = 0; i < onepiece.length; i++) {
            shape[i] = onepiece[i].substring(0, 2);
            location[i] = onepiece[i].substring(2, 3);
            loc[i] = location[i].charAt(0);
            if (loc[i] >= 'A' && loc[i] <= 'Y') {
                lct[i] = (int) loc[i] - 65;
            } else if (loc[i] >= 'a' && loc[i] <= 'y') {
                lct[i] = (int) loc[i] - 72;
            }
        }
        String s[] = new String[placement.length()/3];
        char x[] = new char[placement.length()/3];
        char y[] = {'A','B','C','D','E','F','G','H','I','J','K','T','U','e','f','o','p','q','r','s','t','u','v','w','x','y'};
        int m=0;
        boolean b=true;
        if (isPlacementWellFormed(placement)) {
            b=true;
        }
        else b=false;
        for(int r=0; r<placement.length()/3;r++){
            s[r] = placement.substring(m,m+3);
            m+=3;
        }
        for(int i=0;i<placement.length()/3;i++){
            if(s[i].charAt(0)=='A'||s[i].charAt(0)=='C'||s[i].charAt(0)=='D'||s[i].charAt(0)=='F'||s[i].charAt(0)=='G'||s[i].charAt(0)=='H'){
                for(int j=0;j<y.length;j++){
                    if (s[i].charAt(2)==y[j]){
                        b=false;
                        break;
                    }
                }
            }
            x[i] = s[i].charAt(0);
        }
        for (int q= 0; q < x.length; q++) {
            for (int p = q+1; p < x.length; p++) {
                if(x[p]==x[q]){
                    b= false;
                }
            }
        }
        // 2 steps to check 1. directions of lower circle to check except the shape itself.
        //                  2. check if the piece is placed on the direction of lower circle.
        boolean b2 = true;
        if (b) {
            //boolean b2 = true;
            int[] boardLocations = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            for (int i = 0; i < onepiece.length; i++) {
                String strPiece = pieceList.get(shape[i]);
                int lctNum = lct[i];

                // check whether next piece is overlap with other pieces placed already on the board
                b2 = isPieceOverlap(strPiece, lctNum, boardLocations);

                //COMMENT: b2=true means that there is no overlap! Then the board is occopied.
                // change boardLocations to 1 according to the piece placement if b2 is true
                if (b2) {
                    for (int j = 0; j < strPiece.length(); j++) {
                        int lctHandler = (j != 0) ? (Math.floorDiv(j, 3)) : 0;
                        int currentLoc = lctNum - 11 + 10 * lctHandler + (j - 3 * lctHandler);
                        //Commnet: got locations of all the circles
                        if (currentLoc < 0 || currentLoc > 49) {
                            continue;
                        } else if (strPiece.charAt(j) != '0') {
                            boardLocations[currentLoc] =
                                    (Character.getNumericValue(strPiece.charAt(j)) == BOARD_PEG_LOCATIONS[currentLoc]) ? 1 : 0;
                            //COMMENT: Here if the pieces do not match the board, then return 0 as not occupied.
                            // Hope to add an error test to return an "ERROR" in javaFX when putting at wrong locations.
                        }
                    }
                } else {
                    break;
                }
            }
            if(placement.contains("BBT") ||  placement.contains("BBo")||placement.contains( "EBT")||placement.contains( "EBo")){
                b=false;
            }
            if(placement.contains("BCr") ||  placement.contains("BCt")||placement.contains("BCv")||placement.contains("BCx")||placement.contains("ECr") ||  placement.contains("ECt")||placement.contains("ECv")||placement.contains("ECx")){
                b=false;
            }
        }
        return b && b2;
    }

    /**
     * Given a string value of piece strPiece, an integer value of regarding center location,
     * and an array of 0 and 1 values indicating usage of board locations,
     * return a boolean value if strPiece will overlap the current placements on the board.
     * @param strPiece piece placement value in string from final hashmap pieceList (hashmap.value)
     * @param lctNum center location of piece placement from 0 to 49
     * @param boardLocations An array of 0 and 1 values to express if the location is used
     * @return A boolean value of overlap
     */
    static boolean isPieceOverlap(String strPiece, int lctNum, int[] boardLocations) {
        //COMMENT: boardLocations is an array representing the given location-using situation with the pieces already there.
        boolean b2 = true;
        for (int j = 0; j < strPiece.length(); j++) {
            // branch to only for lower circle
            if (strPiece.charAt(j) == '1') {
                //Integer chkLocation = lct[i] + j - 4;
                int lctHandler = j != 0 ? (Math.floorDiv(j, 3)) : 0;
                //COMMENT: lctHandler decided which row is the chosen circle (each lower circles) on the piece: 0, 1, 2.
                Integer chkLocation = lctNum - 11 + 10*lctHandler + (j - 3*lctHandler);
                //COMMENT: chklocation decided the location on board of the lower circles.
                // check above
                if (chkLocation > 9) {
                    if (chkLocation - 10 > 49){
                        b2 = false;
                    }else {
                        b2 = (boardLocations[chkLocation - 10] == 0) && b2;
                    }
                }
                //COMMENT: check if the above position of the lower circle is occupied. Similar below. "true" means not overlap
                // check right
                if (chkLocation != 9 && chkLocation != 19 & chkLocation != 29 && chkLocation != 39 && chkLocation != 49) {
                    if (chkLocation + 1 < 50 && chkLocation + 1 >= 0) {
                        b2 = (boardLocations[chkLocation + 1] == 0) && b2;
                    }else{b2 = false;}
                }
                // check left
                if (chkLocation != 0 && chkLocation != 10 & chkLocation != 20 && chkLocation != 30 && chkLocation != 40) {
                    if (chkLocation - 1 <50 && chkLocation - 1 >= 0) {
                        b2 = (boardLocations[chkLocation - 1] == 0) && b2;
                    }else{b2 = false;}
                }
                // check below
                if (chkLocation < 40) {
                    if (chkLocation +10 <50 && chkLocation + 10 >= 0) {
                        b2 = (boardLocations[chkLocation + 10] == 0) && b2;
                    }else{b2 = false;}
                }

                if (chkLocation < 50 && chkLocation >= 0) {
                    if (BOARD_PEG_LOCATIONS[chkLocation] != 1){
                        b2 = false;
                    }
                }else {b2 = false;}
            }

            }
        return b2;
    }

    /**
     * Given a string describing a placement of pieces and a string describing
     * an (unordered) objective, return a set of all possible next viable
     * piece placements.   A viable piece placement must be a piece that is
     * not already placed (ie not in the placement string), and which will not
     * obstruct any other unplaced piece.
     *
     * @param placement A valid sequence of piece placements where each piece placement is drawn from the objective
     * @param objective A valid game objective, but not necessarily a valid placement string
     * @return An set of viable piece placements
     */
    public static Set<String> getViablePiecePlacements(String placement, String objective) {
        String[] onepiece = objective.split("(?<=\\G...)");
        String[] placementpiece = placement.split("(?<=\\G...)");
        String[] shape = new String[onepiece.length];
        String[] location = new String[onepiece.length];
        char[] loc = new char[onepiece.length];
        int[] lct = new int[onepiece.length];

        if (placement == "") {
            Set<String> viablePiecePlacements = new HashSet<>();
            for (int i = 0; i < onepiece.length; i++) {
                shape[i] = onepiece[i].substring(0, 2);
                location[i] = onepiece[i].substring(2, 3);
                loc[i] = location[i].charAt(0);
                if (loc[i] >= 'A' && loc[i] <= 'Y') {
                    lct[i] = (int) loc[i] - 65;
                } else if (loc[i] >= 'a' && loc[i] <= 'y') {
                    lct[i] = (int) loc[i] - 72;
                }
            }


            int a;
            for (int j = 0; j < objective.length()/3; j++) {
                a = 0;
                for (int k = 0; k < objective.length()/3; k++) {
                    if (j!=k) {
                        //
                        if (isPlacementSequenceValid(onepiece[j] + onepiece[k])) {
                            a += 1;
                        }
                    } else {
                        continue;
                    }
                }
                if (a >= 7) {
                    viablePiecePlacements.add(onepiece[j]);
                }
            }
            return viablePiecePlacements;
            //COMMENT: if the given placement is "", any piece that are valid with all the other 7 pieces are viable first placement.

        } else {
            // need to modify
            Set<String> viablePiecePlacements = new HashSet<>();
            int plIndex = Arrays.asList(onepiece).indexOf(placementpiece[placementpiece.length-1]);
            //Index of the last piece of placement
            for (int i = 1; placement.length()/3 + i < 9 ; i++) {
                if (isPlacementSequenceValid(placement + onepiece[plIndex + i])) {

                    int a;
                    for (int j = plIndex + i; j < objective.length()/3; j++) {
                        a = 0;
                        for (int k = plIndex + i; k < objective.length()/3; k++) {
                            if (j!=k) {
                                //
                                if (isPlacementSequenceValid(onepiece[j] + onepiece[k])) {
                                    a += 1;
                                }
                            } else {
                                continue;
                            }
                        }
                        if (a >= 8-2-plIndex) {
                            viablePiecePlacements.add(onepiece[j]);
                        }
                    }

                }
            }
            System.out.println(placement+" "+objective);
            System.out.println(viablePiecePlacements);
            System.out.println("=============end=============");
            //COMMENT: mistake: need not only check placement is valid with the next piece,
            // BUT ALSO check if the next piece placement do not affect other pieces!!!
            return viablePiecePlacements;
        }


    }
    /**
     * Return an array of all unique (unordered) solutions to the game, given a
     * starting placement.   A given unique solution may have more than one than
     * one placement sequence, however, only a single (unordered) solution should
     * be returned for each such case.
     *
     * @param placement  A valid piece placement string.
     * @return An array of strings, each describing a unique unordered solution to
     * the game given the starting point provided by placement.
     */
    public static String[] getSolutions(String placement) {
        String b[] = {"A","B","C","D","E","F","G","H",
                "I","J","K","L","M","N","O","P",
                "Q","R","S","T","U","V","W","X",
                "Y","a","b","c","d","e","f","g",
                "h","i","j","k","l","m","n","o",
                "p","q","r","s","t","u","v","w",
                "x","y"};
        String s = "";
        String newS = "";
        String fs = placement;
        List<String> result= new ArrayList<String>();

        for(String str: pieceList.keySet()) {
            for (int r = 0; r < b.length; r++) {
                s = str + b[r];
                newS = fs + s;
                if (isPlacementSequenceValid(newS)) {
                    fs = newS;
                    if (fs.length() == 24) {
                        result.add(fs);
                    }
                } else {s = str;}

            }
        }
        String [] value = result.toArray(new String[0]);
        return value;
    }
}
