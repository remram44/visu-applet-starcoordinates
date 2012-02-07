package fr.larez.rampin.starcoordinates;

public class Thing {

    private String m_Name;
    private float[] m_Coordinates;
    private boolean m_Brushed = false;

    public Thing(String name, float[] coordinates)
    {
        m_Name = name;
        m_Coordinates = coordinates;
    }

    public float getCoordinate(int i)
    {
        return m_Coordinates[i];
    }

    public void setBrushed(boolean b)
    {
        m_Brushed = b;
    }

    public boolean isBrushed()
    {
        return m_Brushed;
    }

}
