package javaLabApp;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * SET OF UNIVERSAL FUNCTIONS
 * USED IN THE WHOLE PROJECT
 */

class Util {

    void setButtonColor(JButton button, Color color) {
        button.setBackground(color);
        button.setOpaque(true);
    }

    void setFont(JComponent component) {
        Font namelabelFont = component.getFont().deriveFont(Font.PLAIN, 20f);
        component.setFont(namelabelFont);
    }

    void setJFrame(JFrame jframe, boolean dispose,
                   boolean pack, int width, int height,
                   int x, int y,
                   boolean setResizable, List<JComponent> componentList) {

        jframe.setVisible(true);
        jframe.setLocation(x, y);
        jframe.setResizable(setResizable);
        if (dispose)
            jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        else
            jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (!componentList.isEmpty()){
            for (JComponent com : componentList) {
                jframe.add(com);
            }
        }
        if (pack) jframe.pack();
        else if (!pack) {
            jframe.setSize(width, height);
        }
    }

    void addToJPanel(JPanel jPanel, List<JComponent> componentList) {
        for (JComponent com : componentList) {
            jPanel.add(com);
        }
    }

    void setFontOfComponent(List<JComponent> componentList) {
        for (JComponent com : componentList) {
            setFont(com);
        }
    }

    void setExceptionFrame(String message) {
        JFrame wrongUrlFrame = new JFrame();

        JLabel label = new JLabel(message);
        label.setForeground(Color.red);
        setFont(label);
        label.setBackground(Color.BLACK);
        setJFrame(wrongUrlFrame, true, true, 300, 100,
                900, 500, false, Arrays.asList(new JComponent[]{label}));
    }
}
