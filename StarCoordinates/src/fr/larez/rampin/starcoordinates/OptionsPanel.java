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

    private static final int BRUSH_CHECKBOX_X = 5;
    private static final int BRUSH_CHECKBOX_Y = 10;
    private static final int BRUSH_CHECKBOX_SIZE = 8;

    private static final int BRUSH_LABEL_X = 15;
    private static final int BRUSH_LABEL_Y = 14;
    private static final int BRUSH_RIGHT = BRUSH_LABEL_X + 90; // FIXME

    private static final int RESET_X = 12;
    private static final int RESET_Y = 22;
    private static final int RESET_W = 90;
    private static final int RESET_H = 14;

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
        int y = 50;
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
            g.noStroke();
            g.rect(BRUSH_CHECKBOX_X, BRUSH_CHECKBOX_Y, BRUSH_RIGHT - BRUSH_CHECKBOX_X, BRUSH_CHECKBOX_SIZE);
            g.fill(255, 255, 255);
            g.stroke(0, 0, 0);
            g.rect(BRUSH_CHECKBOX_X, BRUSH_CHECKBOX_Y, BRUSH_CHECKBOX_SIZE, BRUSH_CHECKBOX_SIZE);
            if(m_App.isBrushing())
            {
                final int x2 = BRUSH_CHECKBOX_X + BRUSH_CHECKBOX_SIZE;
                final int y2 = BRUSH_CHECKBOX_Y + BRUSH_CHECKBOX_SIZE;
                g.line(BRUSH_CHECKBOX_X, BRUSH_CHECKBOX_Y, x2, y2);
                g.line(x2, BRUSH_CHECKBOX_Y, BRUSH_CHECKBOX_X, y2);
            }

            // Label
            g.fill(0, 0, 0);
            g.text("Brushing mode", BRUSH_LABEL_X, BRUSH_LABEL_Y + g.textAscent()/2 - g.textDescent()/2);

            // Button
            g.fill(255, 255, 255);
            g.noStroke();
            g.rect(RESET_X, RESET_Y, RESET_W, RESET_H);
            g.fill(0, 0, 0);
            g.text("Clear brushing", RESET_X, RESET_Y + g.textAscent());
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
        if(x >= BRUSH_CHECKBOX_X && y >= BRUSH_CHECKBOX_Y && x < BRUSH_RIGHT && y < BRUSH_CHECKBOX_Y + BRUSH_CHECKBOX_SIZE)
        {
            m_App.setBrushing(!m_App.isBrushing());
            m_App.redraw();
            return true;
        }

        if(x >= RESET_X && y >= RESET_Y && x < RESET_X + RESET_W && y < RESET_Y + RESET_H)
        {
            m_App.resetBrushing();
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
