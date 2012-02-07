package fr.larez.rampin.starcoordinates;

import processing.core.PGraphics;

public class Utils {

    public static String clipText(PGraphics g, String text, float size)
    {
        if(g.textWidth(text) < size)
            return text;
        else
        {
            // Dichotomy!
            int a = 1, b = text.length();
            while(a != b-1)
            {
                int c = (a+b)/2;
                if(g.textWidth(text.substring(0, c) + "...") < size)
                    a = c;
                else
                    b = c;
            }
            return text.substring(0, a) + "...";
        }
    }

}
