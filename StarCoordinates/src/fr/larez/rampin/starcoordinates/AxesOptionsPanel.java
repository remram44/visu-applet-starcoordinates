package fr.larez.rampin.starcoordinates;

import processing.core.PGraphics;

public class AxesOptionsPanel {

    private AxisConfigPanel[] m_Panels;

    public AxesOptionsPanel(Axis[] axes, StarCoordinates app)
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

    public boolean click(int click_x, int click_y)
    {
        for(AxisConfigPanel p : m_Panels)
        {
            if(p.getX() <= click_x && p.getY() <= click_y && p.getX() + AxisConfigPanel.WIDTH > click_x && p.getY() + AxisConfigPanel.HEIGHT > click_y)
            {
                p.click(click_x, click_y);
                return true;
            }
        }
        return false;
    }

}
