package fr.larez.rampin.starcoordinates;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class StarCoordinates extends JPanel implements MouseListener, MouseMotionListener {

    private static final long serialVersionUID = -1501548717704067038L;

    private Axis[] m_Axes;
    private Vector<Thing> m_Things;
    private String m_ObjectType;

    Point2D.Float m_Origin;
    float m_ScaleX;
    float m_ScaleY;

    private Axis m_ActiveAxis = null;
    private static final int ACT_NONE = 0;
    private static final int ACT_HOVER = 1;
    private static final int ACT_DRAGGING = 2;
    private int m_Action = ACT_NONE;

    public StarCoordinates()
    {
        setPreferredSize(new Dimension(800, 800));
        try
        {
            loadData("cars.csv");
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(this, "Impossible de charger le fichier", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        m_ScaleX = getWidth() * 0.4f;
        m_ScaleY = getHeight() * 0.4f;
        m_Origin = new Point2D.Float(getWidth()*0.5f, getHeight()*0.5f);

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        // Clear!
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Autoscaling -- 'cause we can!
        m_ScaleX = getWidth() * 0.4f;
        m_ScaleY = getHeight() * 0.4f;
        m_Origin = new Point2D.Float(getWidth()*0.5f, getHeight()*0.5f);
        int ox = (int)m_Origin.x;
        int oy = (int)m_Origin.y;

        // Calibrate the axes, if they want to
        for(Axis axis : m_Axes)
            axis.calibrate();

        // The things -- gotta draw'em all
        g.setColor(Color.black);
        for(Thing thing : m_Things)
        {
            // Project it someplace funny
            Point2D.Float pos = new Point2D.Float();
            for(Axis axis : m_Axes)
            {
                if(!axis.isShown())
                    continue;
                Point2D.Float proj = axis.project(thing);
                pos.x += proj.x;
                pos.y += proj.y;
            }

            // Draw it
            int x = ox + (int)(pos.x * m_ScaleX);
            int y = oy + (int)(pos.y * m_ScaleY);
            g.drawLine(x-2, y, x+2, y);
            g.drawLine(x, y-2, x, y+2);
        }

        // And the axes too
        final FontMetrics metrics = g.getFontMetrics();
        final int ascent = metrics.getAscent();
        final int descent = metrics.getDescent();
        for(Axis axis : m_Axes)
        {
            if(!axis.isShown())
                continue;

            // The axis
            int x = ox + (int)(axis.getEndPoint().x * m_ScaleX);
            int y = oy + (int)(axis.getEndPoint().y * m_ScaleY);

            g.drawLine(ox, oy, x, y);

            // The handle
            if(m_ActiveAxis == axis && m_Action == ACT_DRAGGING)
                g.setColor(Color.red);
            else if(m_ActiveAxis == axis && m_Action == ACT_HOVER)
                g.setColor(Color.yellow);
            g.fillRect(x-3, y-3, 6, 6);

            // Draw the label, a little away from the handle
            g.setColor(Color.black);
            float dx = -axis.getEndPoint().y;
            float dy = axis.getEndPoint().x;
            float il = 1.0f/(float)Math.sqrt(dx*dx + dy*dy);
            int lx = (int)(dx * il * 10.0f) + x;
            int ly = (int)(dy * il * 10.0f) + y;
            String label = axis.getLabel() + " (" + axis.getEndValue() + ")";
            lx -= g.getFontMetrics().stringWidth(label)/2;
            ly += ascent/2 - descent/2;
            g.drawString(axis.getLabel() + " (" + axis.getEndValue() + ")", lx, ly);
        }
    }

    private void loadData(String file) throws IOException
    {
        m_Things = new Vector<Thing>();

        Vector<String> lines = loadStrings(file);

        // First line contains the columns names
        StringTokenizer names = new StringTokenizer(lines.get(0), ";");
        int nb_tokens = names.countTokens();

        // Second line contains the datatypes for each column
        StringTokenizer types = new StringTokenizer(lines.get(1), ";");
        assert(types.countTokens() == nb_tokens);

        // First field should be the object tags
        String labeltype = types.nextToken();
        assert(labeltype.toLowerCase().equals("string"));
        m_ObjectType = names.nextToken();

        // Create the axes
        m_Axes = new Axis[nb_tokens-1];
        int i = 0;
        while(names.hasMoreTokens())
        {
            m_Axes[i] = new Axis(names.nextToken(), types.nextToken(), i);
            i++;
        }

        // Useful infos are useful
        System.out.println("Things are " + m_ObjectType + ". There are " + (lines.size() - 2) + " things with " + (nb_tokens - 1) + " dimensions :");
        for(int j = 0; j < m_Axes.length; j++)
        {
            final Axis a = m_Axes[j];
            System.out.print(a.getLabel() + " (" + ((a.getType() == Axis.NUMBER)?"number":"category") + ")");
            if(j != m_Axes.length - 1)
                System.out.print(", ");
            else
                System.out.println();
        }

        // Read ALL the things!
        for(i = 2; i < lines.size(); i++)
        {
            StringTokenizer fields = new StringTokenizer(lines.get(i), ";");
            assert(fields.countTokens() == nb_tokens);
            String name = fields.nextToken();
            float[] coordinates = new float[nb_tokens-1];
            int j = 0;
            while(fields.hasMoreTokens())
            {
                String c = fields.nextToken();
                if(m_Axes[j].getType() == Axis.NUMBER)
                {
                    float v = Float.parseFloat(c); // May throw NumberFormatException
                    m_Axes[j].value(v);
                    coordinates[j] = v;
                }
                else if(m_Axes[j].getType() == Axis.CATEGORY)
                    coordinates[j] = m_Axes[j].category(c);
                j++;
            }
            m_Things.add(new Thing(name, coordinates));
        }

        // Don't show all the axes -- THEY ARE OVER 9000!
        int shown = Math.min(m_Axes.length, 10);
        float angle_inc = (float)(Math.PI*2.0/shown);
        for(int j = 0; j < m_Axes.length; j++)
        {
            if(j < shown)
                m_Axes[j].setEndPoint(-(float)Math.sin(j*angle_inc), -(float)Math.cos(j*angle_inc));
            else
                m_Axes[j].setShown(false);
        }
        System.out.println(shown + " axes are displayed");
    }

    private static Vector<String> loadStrings(String file) throws IOException
    {
        Reader r = new BufferedReader(new FileReader(file));
        Vector<String> lines = new Vector<String>();

        StringBuffer line = new StringBuffer();
        int c;
        while((c = r.read()) != -1)
        {
            if(c == '\r' || c == '\n')
            {
                if(line.length() > 0)
                {
                    lines.add(line.toString());
                    line.setLength(0);
                }
            }
            else
                line.append((char)c);
        }

        r.close();
        return lines;
    }

    private Axis findAxisUnder(int x, int y)
    {
        int ox = (int)m_Origin.x;
        int oy = (int)m_Origin.y;
        for(Axis axis : m_Axes)
        {
            int ax = (int)(axis.getEndPoint().x * m_ScaleX) + ox;
            int ay = (int)(axis.getEndPoint().y * m_ScaleY) + oy;

            int dx = ax - x;
            int dy = ay - y;
            if(dx*dx + dy*dy <= 100)
                return axis;
        }
        return null;
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        // Drag axes endpoints
        if(m_Action == ACT_DRAGGING)
        {
            float x = (e.getPoint().x - m_Origin.x)/m_ScaleX;
            float y = (e.getPoint().y - m_Origin.y)/m_ScaleY;
            m_ActiveAxis.setEndPoint(x, y);
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        // Hover on axes
        if(m_Action == ACT_NONE || m_Action == ACT_HOVER)
        {
            m_ActiveAxis = findAxisUnder(e.getPoint().x, e.getPoint().y);
            if(m_ActiveAxis == null)
                m_Action = ACT_NONE;
            else
                m_Action = ACT_HOVER;
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        m_ActiveAxis = findAxisUnder(e.getPoint().x, e.getPoint().y);
        if(m_ActiveAxis == null)
            m_Action = ACT_NONE;
        else
            m_Action = ACT_DRAGGING;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(m_Action == ACT_DRAGGING)
        {
            m_Action = ACT_NONE;
            mouseMoved(e);
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

}
