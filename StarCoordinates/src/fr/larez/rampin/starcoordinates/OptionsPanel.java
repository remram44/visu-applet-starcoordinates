package fr.larez.rampin.starcoordinates;

import processing.core.PGraphics;

public class OptionsPanel {

    private AxisConfigPanel[] m_Panels;
    private AxisConfigPanel m_Active = null;

    public OptionsPanel(Axis[] axes, StarCoordinates app)
    {
        m_Panels = new AxisConfigPanel[axes.length];
        for(int i = 0; i < axes.length; i++)
            m_Panels[i] = new AxisConfigPanel(axes[i], app);
    }

    public void layout(int width, int height)
    {
        int y = 10;
        int x = 10;

        for(int i = 0; i < m_Panels.length; i++)
        {
            m_Panels[i].setPosition(x, y);

            y += AxisConfigPanel.HEIGHT + 10;
            if(y + AxisConfigPanel.HEIGHT > height)
            {
                x += AxisConfigPanel.WIDTH + 10;
                y = 10;
            }
        }
    }

    public void draw(PGraphics g)
    {
        // TODO : Header ?

        for(AxisConfigPanel p : m_Panels)
            p.draw(g);
    }

    public boolean active()
    {
        return m_Active != null;
    }

    public boolean click(int x, int y, int button)
    {
        for(AxisConfigPanel p : m_Panels)
        {
            if(p.getX() <= x && p.getY() <= y && p.getX() + AxisConfigPanel.WIDTH > x && p.getY() + AxisConfigPanel.HEIGHT > y)
            {
                p.click(x, y, button);
                m_Active = p;
                return true;
            }
        }
        return false;
    }

    public void drag(int x, int y)
    {
        if(m_Active != null)
            m_Active.drag(x, y);
    }

    public void release(int x, int y)
    {
        if(m_Active != null)
            m_Active.release(x, y);
    }

}
