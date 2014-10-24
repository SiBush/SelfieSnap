package com.johncorser.selfiesnap;

import org.apache.commons.lang3.ArrayUtils;

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
            "Ate too much pizza",
            "box full of kittens",
            "sweatshirt fresh out the dryer",
            "the seat of your pants slowly fills with stool as you come to the realization that you've messed yourself",
            "Life changing realization",
            "Left the oven on",
            "Dog in the house, but I don't own a dog",
            "U2 put an album on my phone without me knowing",
            "Found out you have a kid in grade school",
            "Your uncle is actually just some guy your family's been very kind to over the years",
            "That wasn't chicken...",
            "That was some good chicken",
            "Forgot what face I was supposed to make",
            "Chicken fries are no longer being sold",
            "Breaking Bad renewed for season 6",
            "Go to pay at the register, wallet nowhere to be found",
            "The McRib is back",
            "Cake Left Out In Break Room With No Instructions",
            "North Korean Leader Kim Jong-un was arrested for drunk and disorderly conduct",
            "Redbox Partners With Vivid Entertainment, Company To Stock XXX Films In Kiosks",
            "Winning the lottery",
            "A hairy dirty old mushy gummy bear was just thrown into your mouth",
            "Found out the phone call was coming from inside the house",
            "Babysitting and the family doesn't own a clown statue",
            "Have an itch but can't reach it",
            "Babysitting and the family doesn't own a baby",

    };

    public String[] grossFaces = {
            "Has a surprising erection",
            "Has biggest erection of entire life",
            "My dick looks really small today",
            "Just walked in on someone doing it",
            "Parents just walked in on you doing it",
            "Thinking about how your grandparents had sex with each other at one point",
            "About to hit it",
            "Just had sex",
            "On a boat",
            "Hole in one",
            "Fear Boner",
            "Code Boner",
            "Just found out I'm an evil cyber bully and I don't care",
    };

    String[] allFaces = ArrayUtils.addAll(faces, grossFaces);

    public String getFaceToMake(){

        int rnd = new Random().nextInt(allFaces.length);
        return allFaces[rnd];
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

    static CharSequence[] shuffleArray(CharSequence[] ar)
    {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            CharSequence a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return ar;
    }

}
