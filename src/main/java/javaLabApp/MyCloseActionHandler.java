package javaLabApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyCloseActionHandler implements ActionListener {
    JTabbedPane tabbedPane;

    public MyCloseActionHandler(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {

        Logging.getLogger().info("Tab closed.");

        Component selected = tabbedPane.getSelectedComponent();
        if (selected != null) {
            tabbedPane.remove(selected);
        }

    }

}