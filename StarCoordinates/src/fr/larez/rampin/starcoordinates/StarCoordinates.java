package fr.larez.rampin.starcoordinates;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class StarCoordinates extends JPanel {

    private static final long serialVersionUID = -1501548717704067038L;

    private Axis[] m_Axes;
    private Vector<Thing> m_Things;
    private String m_ObjectType;

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
    }

    @Override
    public void paintComponent(Graphics g)
    {
        // Autoscaling -- 'cause we can!
        float scale_x = getWidth() * 0.4f;
        float scale_y = getHeight() * 0.4f;
        Point2D.Float origin = new Point2D.Float(getWidth()*0.5f, getHeight()*0.5f);
        int ox = (int)origin.x;
        int oy = (int)origin.y;

        // Calibrate the axes, if they want to
        for(Axis axis : m_Axes)
            axis.calibrate();

        // The things -- gotta draw'em all
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
            int x = ox + (int)(pos.x * scale_x);
            int y = oy + (int)(pos.y * scale_y);
            g.drawLine(x-2, y, x+2, y);
            g.drawLine(x, y-2, x, y+2);
        }

        // And the axes too
        for(Axis axis : m_Axes)
        {
            if(!axis.isShown())
                continue;
            int x = ox + (int)(axis.getEndPoint().x * scale_x);
            int y = oy + (int)(axis.getEndPoint().y * scale_y);

            g.drawLine(ox, oy, x, y);
            g.drawString(axis.getLabel() + " (" + axis.getEndValue() + ")", x, y);
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

}
