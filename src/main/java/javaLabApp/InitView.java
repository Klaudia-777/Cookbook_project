package javaLabApp;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class InitView {
    private final ChooseAnOptionActionListener chooseAnOptionActionListener = new ChooseAnOptionActionListener(this);
    private GlobalFunctions service = new GlobalFunctions();
    private JButton addManuallyButton;
    private JButton addFromSiteButton;

    private void setButtons() {
        addManuallyButton = new JButton("<html>Add your<br />own recipe</html>");
        addManuallyButton.addActionListener(chooseAnOptionActionListener);
        addManuallyButton.setBounds(20, 100, 150, 100);

        addFromSiteButton = new JButton("<html>Add recipe<br />from the website</html>");
        addFromSiteButton.addActionListener(chooseAnOptionActionListener);
        addFromSiteButton.setBounds(220, 100, 150, 100);

        service.setFontOfComponent(Arrays.asList(new JComponent[]{
                addManuallyButton,
                addFromSiteButton})
        );
    }

    private void setPane(Container pane) {
        setButtons();
        pane.add(addManuallyButton);
        pane.add(addFromSiteButton);
    }

    private void createAndShowGUI() {
        JFrame jf = new JFrame("Add a Recipe to your base");
        JPanel jPanel = new JPanel();

        jPanel.setLayout(null);
        setPane(jPanel);
        jf.getContentPane().add(jPanel);

        jf.pack();
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setSize(400, 400);
        jf.setLocation(dim.width / 2 - jf.getSize().width / 2, dim.height / 2 - jf.getSize().height / 2);
        jf.setResizable(false);

    }

    private InitView() {
        createAndShowGUI();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater((new Runnable() {
            public void run() {
                new InitView();
            }
        }));
    }
}


