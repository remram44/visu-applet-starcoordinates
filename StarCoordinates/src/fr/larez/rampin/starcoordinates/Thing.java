package fr.larez.rampin.starcoordinates;

/**
 * A Thing, eg an object represented in the visualization.
 *
 * @author Rémi Rampin
 */
public class Thing {

    private String m_Name;
    private float[] m_Coordinates;
    private boolean m_Brushed = false;

    public Thing(String name, float[] coordinates)
    {
        m_Name = name;
        m_Coordinates = coordinates;
    }

    /**
     * Returns the i-th coordinate.
     */
    public float getCoordinate(int i)
    {
        return m_Coordinates[i];
    }

    /**
     * Indicates whether this Thing is highlighted.
     */
    public void setBrushed(boolean b)
    {
        m_Brushed = b;
    }

    /**
     * Indicates whether this Thing is highlighted.
     */
    public boolean isBrushed()
    {
        return m_Brushed;
    }

    /**
     * Returns the name of this Thing.
     */
    public String getName()
    {
        return m_Name;
    }

}
