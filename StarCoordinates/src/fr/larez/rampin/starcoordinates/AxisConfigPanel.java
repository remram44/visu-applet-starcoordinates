package fr.larez.rampin.starcoordinates;

import processing.core.PGraphics;

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

    public AxisConfigPanel(Axis axis, StarCoordinates app)
    {
        m_Axis = axis;
        m_App = app;
    }

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

        g.popMatrix();
    }

    public void click(int x, int y)
    {
        x -= m_PosX;
        y -= m_PosY;

        // Checkbox
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
    }

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
