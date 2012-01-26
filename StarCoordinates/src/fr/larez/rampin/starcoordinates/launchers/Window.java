package fr.larez.rampin.starcoordinates.launchers;

import javax.swing.JFrame;

import fr.larez.rampin.starcoordinates.StarCoordinates;

public class Window extends JFrame {

    private static final long serialVersionUID = 1234306378347539058L;

    public Window()
    {
        setTitle("StarCoordinates");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().add(new StarCoordinates());
        pack();
    }

    public static void main(String[] args)
    {
        Window window = new Window();
        window.setVisible(true);
    }

}
