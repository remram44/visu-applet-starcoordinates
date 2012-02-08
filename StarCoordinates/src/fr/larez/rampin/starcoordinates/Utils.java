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

    public static void drawTooltip(PGraphics g, String text, float x, float y)
    {
        float tx = x + 10;
        float ty = y + 15 + g.textAscent();
        float tw = g.textWidth(text);
        if(tx + tw >= g.width)
            tx = x - tw - 10;
        if(ty + g.textDescent() >= g.height)
            ty = y - 10 - g.textDescent();
        g.fill(240, 240, 150, 240);
        g.stroke(0, 0, 0, 191);
        g.rect(tx - 2, ty - g.textAscent() - 2, tw + 4, g.textAscent() + g.textDescent() + 4);
        g.fill(0, 0, 0, 255);
        g.noStroke();
        g.text(text, tx, ty);
    }

    public static void centeredText(PGraphics g, String text, float x, float y)
    {
        x -= g.textWidth(text)/2;
        y += g.textAscent()/2 - g.textDescent()/2;
        g.text(text, x, y);
    }

    public static void centeredText(PGraphics g, String text, float x, float y, int bgcolor)
    {
        float ascent = g.textAscent(), descent = g.textDescent();
        float width = g.textWidth(text);
        x -= width/2;
        y += ascent/2 - descent/2;
        int fgcolor = g.fillColor;
        g.fill(bgcolor);
        g.rect(x, y - ascent, width, ascent + descent);
        g.fill(fgcolor);
        g.text(text, x, y);
    }

}
