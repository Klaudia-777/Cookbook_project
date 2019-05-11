package javaLabApp;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class GlobalFunctions {

    void setButtonColor(JButton button,Color color){
        button.setBackground(color);
        button.setOpaque(true);
    }

    void setFont(JComponent component) {
        Font namelabelFont = component.getFont().deriveFont(Font.PLAIN, 20f);
        component.setFont(namelabelFont);
    }

    void setJFrame(JFrame jframe) {
        jframe.pack();
        jframe.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jframe.setLocation(dim.width / 2 - jframe.getSize().width / 2, dim.height / 2 - jframe.getSize().height / 2);
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jframe.setResizable(true);
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

    void setExceptionFrame(String message){
        JFrame wrongUrlFrame = new JFrame();

        JLabel label = new JLabel(message);
        label.setForeground(Color.red);
        setFont(label);

        wrongUrlFrame.getContentPane().add(label);
        wrongUrlFrame.setSize(300, 100);
        setJFrame(wrongUrlFrame);
    }
}
