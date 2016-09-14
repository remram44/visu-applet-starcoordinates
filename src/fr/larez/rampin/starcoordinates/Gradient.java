package fr.larez.rampin.starcoordinates;

import java.util.Map;
import java.util.TreeMap;

public class Gradient {

    private static class RGB {
        int r, g, b;
        RGB(int r_, int g_, int b_)
        {
            r = r_;
            g = g_;
            b = b_;
        }
    }

    TreeMap<Float, RGB> m_Values = new TreeMap<Float, RGB>();

    public void addPoint(float pos, int r, int g, int b)
    {
        m_Values.put(pos, new RGB(r, g, b));
    }

    public int color(float pos)
    {
        Map.Entry<Float, RGB> a, b;
        if(pos < 0.001f)
        {
            a = m_Values.floorEntry(0.0f);
            b = m_Values.ceilingEntry(0.001f);
        }
        else if(pos > 0.999f)
        {
            a = m_Values.floorEntry(0.999f);
            b = m_Values.ceilingEntry(1.0f);
        }
        else
        {
            a = m_Values.floorEntry(pos - 0.001f);
            b = m_Values.ceilingEntry(pos);
        }

        assert(a != null && b != null);

        float pa = (b.getKey() - pos)/(b.getKey() - a.getKey());
        float pb = (pos - a.getKey())/(b.getKey() - a.getKey());

        int rr = (int)(a.getValue().r * pa + b.getValue().r * pb);
        int rg = (int)(a.getValue().g * pa + b.getValue().g * pb);
        int rb = (int)(a.getValue().b * pa + b.getValue().b * pb);

        return 0xFF000000 | (rr << 16) | (rg << 8) | rb;
    }

}
