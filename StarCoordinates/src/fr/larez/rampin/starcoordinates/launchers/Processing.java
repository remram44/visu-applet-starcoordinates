package fr.larez.rampin.starcoordinates.launchers;

import processing.core.PApplet;
import fr.larez.rampin.starcoordinates.StarCoordinates;

public class Processing {

    public static void main(String[] args)
    {
        final boolean present_mode = false;
        final String classname = StarCoordinates.class.getCanonicalName();
        if(present_mode)
            PApplet.main(new String[] {"--present", classname});
        else
            PApplet.main(new String[] {classname});
    }

}
