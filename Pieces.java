import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
 * Edwin Yang
 */
public enum Pieces {
    AA("AA",new int[]{1, 2, 0, 2, 1, 2, 1, 0, 0}),
    AE("AE",new int[]{0, 1, 2, 1, 2, 1, 0, 0, 2}),
    BA("BA",new int[]{0, 2, 0, 0, 1, 2, 0, 2, 1}),
    BE("BE",new int[]{0, 1, 0, 1, 2, 0, 2, 1, 0});
    // other shapes...

    int[] num;
    final String name;
    Pieces(String name,int[] num) {
        this.name = name;
        this.num = num;
    }


    private static final int BOARD_PEG_LOCATIONS[] =
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
            0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
            1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
            0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
            1, 0, 1, 0, 1, 0, 1, 0, 1, 0};



    int[] locationIndices(char location){
        int[] indices = new int[9];
        int location_int=0;
        if (location >= 'A' && location <= 'Y') {
            location_int = (int) location - 65;
        } else if (location >= 'a' && location <= 'y') {
            location_int = (int) location - 72;
        }
        // Number locations into 0~49.
        indices[0] = location_int - 11;
        indices[1] = location_int - 10;
        indices[2] = location_int - 9;
        indices[3] = location_int - 1;
        indices[4] = location_int;
        indices[5] = location_int + 1;
        indices[6] = location_int + 9;
        indices[7] = location_int + 10;
        indices[8] = location_int + 11;

        for (int i=0; i<9; i++){
            if (indices[i] < 0){
                indices[i] = 0;
            }
        }

        return indices;
    }


    int[] getIndices(Pieces m, char location){
        return m.locationIndices(location);
    }

    boolean isOverlapValid(String placement){
        String[] onepiece = placement.split("(?<=\\G...)");
        String[] shape = new String[onepiece.length];
        String[] location = new String[onepiece.length];
        char[] loc = new char[onepiece.length];
        int[] lct = new int[onepiece.length];
        // 0 to 49
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

        boolean b = true;
        for (int i=0; i<onepiece.length; i++){
            for (int j = 0; j<onepiece.length; j++){
                int[] firstpiece = getIndices(Pieces.valueOf(shape[i]), loc[i]);
                int[] secondpiece = getIndices(Pieces.valueOf(shape[j]), loc[j]);
                List list = Arrays.asList(firstpiece);
                if (list.contains(secondpiece[i])){
                    int x = Pieces.valueOf(shape[i]).num[i] + Pieces.valueOf(shape[j]).num[i] + lct[i];
                    if (x<=2){
                        b = true;
                    }else{
                        b = false;
                    }
                }
            }
        }
        return b;

    }
}

// if Pieces.AA (for eg.) [0] == any Pieces with any indices (find it as Pieces.BG [3]), check if the number on that location (num: dup_location) is less than or euqal to 2,
// by adding BOARD_PEG_LOCATIONS[dup_location] + AA[0] + BG[3].
// If some of the indices are neighbour, that is, if locations of AA[0] and BG[3] are differenced by 10 or 1, they cannot be both 1 or both 2, they must be
// either both 0, 1 and 2, 0 and 1, 0 and 2. Among them only 1 and 2 need to be ranked.
// If both the above are satisfied (with a right order), then the placement is valid. The next step can be implemented.
// when the length is 24, and all above are satisfied, then the game reaches the objective.
