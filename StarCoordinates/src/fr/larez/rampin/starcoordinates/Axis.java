package fr.larez.rampin.starcoordinates;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

/**
 * One of the axes of the object space.
 *
 * First, the Axis is constructed from a label and typename, and is assigned a
 * unique dimension number.
 * Then, all the coordinates of objects along this Axis are made known to it
 * using either {@link category()}, which also converts the category name to a
 * number (which can be used as coordinate) or {@link value}
 */
class Axis {

    private boolean m_Shown = true;
    private int m_Coordinate;
    private String m_Label;
    private int m_Type;
    private Point2D.Float m_EndPoint = new Point2D.Float(1.0f, 0.0f);
    private float m_EndValue = 1.0f;
    private float m_MaxValue = 0.0f;

    private Float m_FilterMin = null;
    private Float m_FilterMax = null;

    private Map<String, Integer> m_Categories = null;

    public static final int NUMBER = 1;
    public static final int CATEGORY = 2;

    public Axis(String label, String typename, int coordinate)
    {
        m_Label = label;
        m_Coordinate = coordinate;
        String typenameLC = typename.toLowerCase();
        if(typenameLC.equals("number") || typenameLC.equals("int") || typenameLC.equals("integer") || typenameLC.equals("double") || typenameLC.equals("float") || typenameLC.equals("real"))
            m_Type = NUMBER;
        else if(typenameLC.equals("string") || typenameLC.equals("cat") || typenameLC.equals("category"))
        {
            m_Type = CATEGORY;
            m_Categories = new HashMap<String, Integer>();
        }
        else
        {
            System.err.println("Stumbled on unknown typename \"" + typenameLC + "\"");
            assert(false); // WHAT!?
        }
    }

    /**
     * Returns the readable name for this dimension.
     */
    public String getLabel()
    {
        return m_Label;
    }

    /**
     * Returns the type of dimension.
     *
     * The type might be either NUMBER (for any kind of numerical data,
     * discrete or real) or CATEGORY (for other kinds of data, like strings).
     */
    public int getType()
    {
        return m_Type;
    }

    /**
     * Registers a numerical value on this axis; used for calibration.
     */
    public void value(float v)
    {
        assert(m_Type == NUMBER && m_Categories == null);
        v = Math.abs(v);
        if(v > m_MaxValue)
            m_MaxValue = v;
        // Calibration is done elsewhere
    }

    /**
     * Registers a category on this axis, and assigns a number for it.
     */
    public int category(String value)
    {
        assert(m_Type == CATEGORY && m_Categories != null);
        Integer i = m_Categories.get(value);
        if(i != null)
            // We know about this category already
            return i;
        else
        {
            // New category -- generate an ID
            int j = m_Categories.size();
            m_Categories.put(value, j);
            m_EndValue = j;
            return j;
        }
    }

    public void setShown(boolean shown)
    {
        m_Shown = shown;
    }

    public boolean isShown()
    {
        return m_Shown;
    }

    /**
     * Set the location of the endpoint of this axis.
     */
    public void setEndPoint(float x, float y)
    {
        m_EndPoint.setLocation(x, y);
    }

    /**
     * Returns the location of the endpoint of this axis.
     */
    public Point2D.Float getEndPoint()
    {
        return (Point2D.Float)m_EndPoint.clone();
    }

    /**
     * Returns the maximum value on this axis, eg the scale.
     */
    public float getEndValue()
    {
        return m_EndValue;
    }

    /**
     * Returns the dimension number of this axis.
     */
    public int coordinate()
    {
        return m_Coordinate;
    }

    /**
     * Projects a Thing on this Axis.
     *
     * Adding the projection of a Thing on each Axis gives the position where
     * it should be displayed.
     */
    public Point2D.Float project(Thing thing)
    {
        float coordinate = thing.getCoordinate(m_Coordinate) / m_EndValue;
        return new Point2D.Float(m_EndPoint.x * coordinate, m_EndPoint.y * coordinate);
    }

    /**
     * Calibrates this Axis, eg computes the scale from the values.
     */
    public void calibrate()
    {
        if(m_MaxValue > 0.0f)
        {
            // Compute m_EndValue from m_MaxValue

            // Ex : m_MaxValue = 73
            // inc = 10
            // m_EndValue = 80
            /*float inc = (float)Math.pow(10.0, (int)Math.log10(m_MaxValue));
            m_EndValue = inc * (int)(m_MaxValue/inc + 0.99);*/

            // Ex : m_MaxValue = 73
            // m_EndValue = 100
            m_EndValue = (float)Math.pow(10.0, (int)(Math.log10(m_MaxValue) + 1));

            m_MaxValue = 0.0f;
        }
    }

    /**
     * Indicates whether a Thing should be filtered out.
     */
    public boolean filter(Thing thing)
    {
        float c = thing.getCoordinate(m_Coordinate);
        if( (m_FilterMin == null || m_FilterMin <= c)
         && (m_FilterMax == null || m_FilterMax >= c) )
            return false;
        else
            return true;
    }

    /**
     * Returns the minimum value under which Things should be filtered out, or
     * null.
     */
    public Float getFilterMin()
    {
        return m_FilterMin;
    }

    /**
     * Returns the maximum value over which Things should be filtered out, or
     * null.
     */
    public Float getFilterMax()
    {
        return m_FilterMax;
    }

    /**
     * Sets the minimum value of the filter.
     */
    public void setFilterMin(Float value)
    {
        m_FilterMin = value;
        if(value != null && m_FilterMax != null && m_FilterMax < value)
            m_FilterMax = null;
    }

    /**
     * Sets the maximum value of the filter.
     */
    public void setFilterMax(Float value)
    {
        m_FilterMax = value;
        if(value != null && m_FilterMin != null && m_FilterMin > value)
            m_FilterMin = null;
    }

}
