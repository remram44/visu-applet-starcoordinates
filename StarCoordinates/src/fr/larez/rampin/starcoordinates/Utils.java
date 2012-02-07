package fr.larez.rampin.starcoordinates;

import processing.core.PGraphics;

/**
 * Random functions used throughout the code.
 *
 * @author Rémi Rampin
 */
public class Utils {

    /**
     * Clip a text so that it is less than a given width.
     *
     * @param g A PGraphics on which the text would be drawn.
     * @param text The String to be truncated.
     * @param size The maximum available width.
     * @return The original String if possible, else a truncated version ending
     * with "...".
     */
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
