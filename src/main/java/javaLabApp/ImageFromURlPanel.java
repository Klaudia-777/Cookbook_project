package javaLabApp;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.io.IOException;
import java.net.URL;

/**
 *  CLASS BEING EXTENSION TO JPANEL
 *  ADDING PICTURE
 *  FROM GIVEN URL ENABLED
 */


public class ImageFromURlPanel extends JPanel {

    private Image image;

    public ImageFromURlPanel(URL pathname) {
        super();
        try {
            image = ImageIO.read(pathname).getScaledInstance(600, 350, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            System.err.println("Blad odczytu obrazka");
            e.printStackTrace();
        }
        JLabel jl = new JLabel();
        jl.setIcon(new ImageIcon(image));
        this.add(jl,BorderLayout.CENTER);

    }

//    @Override
//    public void paintComponent(Graphics g) {
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.drawImage(image, 0, 50, this);
//    }
}
