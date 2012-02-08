package fr.larez.rampin.starcoordinates;

import processing.core.PGraphics;

/**
 * The panel displayed on top of the visualization.
 *
 * @author Rémi Rampin
 */
public class OptionsPanel {

    private StarCoordinates m_App;
    private AxisConfigPanel[] m_Panels;
    private AxisConfigPanel m_Active = null;

    private static final int CHECKBOX_X = 10;
    private static final int CHECKBOX_Y = 10;
    private static final int CHECKBOX_SIZE = 8;

    private static final int LABEL_X = 20;
    private static final int LABEL_Y = 14;
    private static final int RIGHT = LABEL_X + 100; // FIXME

    public OptionsPanel(Axis[] axes, StarCoordinates app)
    {
        m_Panels = new AxisConfigPanel[axes.length];
        for(int i = 0; i < axes.length; i++)
            m_Panels[i] = new AxisConfigPanel(axes[i], app);
        m_App = app;
    }

    /**
     * Recompute the position of each AxisConfigPanel on size change.
     */
    public void layout(int width, int height)
    {
        int y = 30;
        int x = 10;

        for(int i = 0; i < m_Panels.length; i++)
        {
            m_Panels[i].setPosition(x, y);

            y += AxisConfigPanel.HEIGHT + 10;
            if(y + AxisConfigPanel.HEIGHT > height)
            {
                x += AxisConfigPanel.WIDTH + 10;
                y = 30;
            }
        }
    }

    /**
     * Draw the panel with the given PGraphics.
     */
    public void draw(PGraphics g)
    {
        // Header
        {
            // Checkbox
            g.fill(255, 255, 255);
            g.stroke(0, 0, 0);
            g.rect(CHECKBOX_X, CHECKBOX_Y, CHECKBOX_SIZE, CHECKBOX_SIZE);
            if(m_App.isBrushing())
            {
                final int x2 = CHECKBOX_X + CHECKBOX_SIZE;
                final int y2 = CHECKBOX_Y + CHECKBOX_SIZE;
                g.line(CHECKBOX_X, CHECKBOX_Y, x2, y2);
                g.line(x2, CHECKBOX_Y, CHECKBOX_X, y2);
            }

            // Label
            g.fill(0, 0, 0);
            g.text("Brushing mode", LABEL_X, LABEL_Y + g.textAscent()/2 - g.textDescent()/2);
        }

        // Panels
        for(AxisConfigPanel p : m_Panels)
            p.draw(g);
    }

    /**
     * Indicate whether the panel currently uses mouse input.
     *
     * If true, dragging and releasing events will be sent to draw() and
     * release().
     */
    public boolean active()
    {
        return m_Active != null;
    }

    /**
     * Handles a click.
     *
     * @param button Mouse button used (1, 2 or 3).
     * @return True if the event was handled, false if it should be processed
     * by the caller.
     */
    public boolean click(int x, int y, int button)
    {
        if(x >= CHECKBOX_X && y >= CHECKBOX_Y && x < RIGHT && y < CHECKBOX_Y + CHECKBOX_SIZE)
        {
            m_App.setBrushing(!m_App.isBrushing());
            m_App.redraw();
            return true;
        }

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

    /**
     * A mouse dragging event.
     */
    public void drag(int x, int y)
    {
        if(m_Active != null)
            m_Active.drag(x, y);
    }

    /**
     * A mouse release event.
     */
    public void release(int x, int y)
    {
        if(m_Active != null)
            m_Active.release(x, y);
    }

}
