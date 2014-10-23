package com.johncorser.selfiesnap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jcorser on 10/22/14.
 */
public class RidiculousFaces {

    public String[] faces = {
            "You've just stepped on a frog!",
            "Just saw Hitler's ghost",
            "Quicksand!",
            "Ate too much pizza"
    };

    public String getFaceToMake(){

        int rnd = new Random().nextInt(faces.length);
        return faces[rnd];
    }

    public CharSequence[] getGuessesToMake(String faceMade){
        List<String> list = new ArrayList<String>();
        list.add(faceMade);
        while (list.size() < 4){
            String toAdd = getFaceToMake();
            if (!list.contains(toAdd)){
                list.add(toAdd);
            }
        }
        CharSequence[] result = new String[list.size()];
        result = list.toArray(result);
        return result;
    }
}
