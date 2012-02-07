package fr.larez.rampin.starcoordinates;

import processing.core.PGraphics;

/**
 * The configuration panel for a single Axis.
 *
 * @author Rémi Rampin
 */
public class AxisConfigPanel {

    private Axis m_Axis;

    private int m_PosX, m_PosY;

    private StarCoordinates m_App;

    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    private static final int CHECKBOX_X = 2;
    private static final int CHECKBOX_Y = 3;
    private static final int CHECKBOX_SIZE = 10;

    private static final int COLOR_X = 3;
    private static final int COLOR_Y = 19;
    private static final int COLOR_SIZE = 8;

    private static final int GRAPH_X = 28;
    private static final int GRAPH_Y = 18;
    private static final int GRAPH_W = 50;
    private static final int GRAPH_H = 10;

    private static final int LIMIT_MIN = 1;
    private static final int LIMIT_MAX = 2;
    private int m_Drag;

    public AxisConfigPanel(Axis axis, StarCoordinates app)
    {
        m_Axis = axis;
        m_App = app;
    }

    /**
     * Draw the panel on the given PGraphics.
     */
    public void draw(PGraphics g)
    {
        g.pushMatrix();
        g.translate(m_PosX, m_PosY);

        g.stroke(0, 0, 0);
        g.fill(223, 223, 223);
        g.rect(0, 0, WIDTH, HEIGHT);

        // Label
        g.noStroke();
        g.fill(0, 0, 0);
        String s = Utils.clipText(g, m_Axis.getLabel(), WIDTH - 12);
        g.text(s, 14, g.textAscent());

        // Checkbox
        g.stroke(0, 0, 0);
        g.fill(255, 255, 255);
        g.rect(CHECKBOX_X, CHECKBOX_Y, CHECKBOX_SIZE, CHECKBOX_SIZE);
        if(m_Axis.isShown())
        {
            final int x2 = CHECKBOX_X + CHECKBOX_SIZE;
            final int y2 = CHECKBOX_Y + CHECKBOX_SIZE;
            g.line(CHECKBOX_X, CHECKBOX_Y, x2, y2);
            g.line(x2, CHECKBOX_Y, CHECKBOX_X, y2);
        }

        // Color switch
        g.noStroke();
        g.fill(255, 255, 255);
        g.rect(COLOR_X, COLOR_Y, COLOR_SIZE, COLOR_SIZE);
        g.stroke(255, 0, 0);
        g.line(COLOR_X, COLOR_Y, COLOR_X + COLOR_SIZE, COLOR_Y);
        g.stroke(0, 255, 0);
        g.line(COLOR_X, COLOR_Y, COLOR_X, COLOR_Y + COLOR_SIZE);
        g.stroke(0, 0, 255);
        g.line(COLOR_X + COLOR_SIZE, COLOR_Y, COLOR_X + COLOR_SIZE, COLOR_Y + COLOR_SIZE);
        g.stroke(255, 255, 0);
        g.line(COLOR_X, COLOR_Y + COLOR_SIZE, COLOR_X + COLOR_SIZE, COLOR_Y + COLOR_SIZE);
        if(m_App.getColorAxis() == m_Axis)
        {
            g.stroke(0, 0, 0);
            final int x2 = COLOR_X + COLOR_SIZE;
            final int y2 = COLOR_Y + COLOR_SIZE;
            g.line(COLOR_X, COLOR_Y, x2, y2);
            g.line(x2, COLOR_Y, COLOR_X, y2);
        }

        // Graph
        g.noStroke();
        g.fill(0, 0, 0);
        g.rect(GRAPH_X, GRAPH_Y, GRAPH_W, GRAPH_H);
        g.noFill();
        g.stroke(255, 0, 0);
        if(m_Axis.getFilterMin() != null)
        {
            int l1 = (int)((m_Axis.getFilterMin() + m_Axis.getEndValue()) * GRAPH_W / (m_Axis.getEndValue() * 2.0f));
            g.line(GRAPH_X + l1, GRAPH_Y, GRAPH_X + l1, GRAPH_Y + GRAPH_H);
        }
        if(m_Axis.getFilterMax() != null)
        {
            int l2 = (int)((m_Axis.getFilterMax() + m_Axis.getEndValue()) * GRAPH_W / (m_Axis.getEndValue() * 2.0f));
            g.line(GRAPH_X + l2, GRAPH_Y, GRAPH_X + l2, GRAPH_Y + GRAPH_H);
        }

        g.popMatrix();
    }

    /**
     * Handle a click event.
     */
    public void click(int x, int y, int button)
    {
        x -= m_PosX;
        y -= m_PosY;

        // Checkbox
        if(button == 1)
        {
            final int x2 = CHECKBOX_X + CHECKBOX_SIZE;
            final int y2 = CHECKBOX_Y + CHECKBOX_SIZE;
            if(x >= CHECKBOX_X && x < x2 && y >= CHECKBOX_Y && y < y2)
            {
                m_Axis.setShown(!m_Axis.isShown());
                m_App.redraw();
                return ;
            }
        }

        // Color switch
        if(button == 1)
        {
            final int x2 = COLOR_X + COLOR_SIZE;
            final int y2 = COLOR_Y + COLOR_SIZE;
            if(x >= COLOR_X && x < x2 && y >= COLOR_Y && y < y2)
            {
                if(m_App.getColorAxis() == m_Axis)
                    m_App.setColorAxis(null);
                else
                    m_App.setColorAxis(m_Axis);
                m_App.redraw();
                return ;
            }
        }

        // Graph
        {
            final int x2 = GRAPH_X + GRAPH_W;
            final int y2 = GRAPH_Y + GRAPH_H;
            if(x >= COLOR_X && x < x2 && y >= COLOR_Y && y < y2)
            {
                if(button == 1)
                {
                    // Which limit to move?
                    Integer l1 = null;
                    if(m_Axis.getFilterMin() != null)
                        l1 = (int)((m_Axis.getFilterMin() + m_Axis.getEndValue()) * GRAPH_W / (m_Axis.getEndValue() * 2.0f));
                    Integer l2 = null;
                    if(m_Axis.getFilterMax() != null)
                        l2 = (int)((m_Axis.getFilterMax() + m_Axis.getEndValue()) * GRAPH_W / (m_Axis.getEndValue() * 2.0f));

                    int xl = x - GRAPH_X;
                    if(l1 != null && Math.abs(xl - l1) < 8)
                        m_Drag = LIMIT_MIN;
                    else if(l2 != null && Math.abs(xl - l2) < 8)
                        m_Drag = LIMIT_MAX;
                    else if(xl < GRAPH_W/2)
                        m_Drag = LIMIT_MIN;
                    else
                        m_Drag = LIMIT_MAX;

                    drag(x + m_PosX, y + m_PosY);
                }
                else if(button == 2)
                {
                    m_Axis.setFilterMin(null);
                    m_Axis.setFilterMax(null);
                    m_App.redraw();
                }
            }
        }
    }

    /**
     * A mouse dragging event.
     */
    public void drag(int x, int y)
    {
        x -= m_PosX;
        y -= m_PosY;

        int xl = x - GRAPH_X;
        if(xl < 0 || xl > GRAPH_W)
            return ;

        float value = xl * m_Axis.getEndValue() * 2.0f / GRAPH_W - m_Axis.getEndValue();
        if(m_Drag == LIMIT_MIN)
            m_Axis.setFilterMin(value);
        else if(m_Drag == LIMIT_MAX)
            m_Axis.setFilterMax(value);
        else
            return ;

        m_App.redraw();
    }

    /**
     * A mouse release event.
     */
    public void release(int x, int y)
    {
        x -= m_PosX;
        y -= m_PosY;

        m_Drag = 0;
    }

    /**
     * Repositions the panel.
     */
    public void setPosition(int x, int y)
    {
        m_PosX = x;
        m_PosY = y;
    }

    public int getX()
    {
        return m_PosX;
    }

    public int getY()
    {
        return m_PosY;
    }

}
