package fr.larez.rampin.starcoordinates;

public class Thing {

    private String m_Name;
    private float[] m_Coordinates;

    public Thing(String name, float[] coordinates)
    {
        m_Name = name;
        m_Coordinates = coordinates;
    }

    public float getCoordinate(int i)
    {
        return m_Coordinates[i];
    }

}
