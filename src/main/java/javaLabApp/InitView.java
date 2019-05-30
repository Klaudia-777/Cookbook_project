package javaLabApp;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class InitView {
    private final ChooseAnOptionActionListener chooseAnOptionActionListener = new ChooseAnOptionActionListener(this);

    private Util service = new Util();
    // private JButton addManuallyButton;
    private JButton addFromSiteButton;
    private JButton addFilteringButton;

    private void setButtons() {

//        addManuallyButton = new JButton("<html>Add your<br />own recipe</html>");
//        addManuallyButton.addActionListener(chooseAnOptionActionListener);
//        addManuallyButton.setBounds(20, 60, 150, 100);
//        service.setButtonColor(addManuallyButton, Color.GREEN.brighter());

        addFromSiteButton = new JButton("<html>Add recipe<br />from the website</html>");
        addFromSiteButton.addActionListener(chooseAnOptionActionListener);
        addFromSiteButton.setBounds(20, 60, 150, 100);
        service.setButtonColor(addFromSiteButton, Color.GREEN.brighter());

        addFilteringButton = new JButton("<html>Filter</html>");
        addFilteringButton.addActionListener(chooseAnOptionActionListener);
        addFilteringButton.setBounds(220, 60, 150, 100);
        service.setButtonColor(addFilteringButton, Color.GREEN.brighter());

        service.setFontOfComponent(Arrays.asList(new JComponent[]{
//                addManuallyButton,
                        addFromSiteButton,
                        addFilteringButton})
        );
    }

    private void setPane(Container pane) {
        setButtons();
//        pane.add(addManuallyButton);
        pane.add(addFromSiteButton);
        pane.add(addFilteringButton);
    }

    private void createAndShowGUI() {
        JFrame jf = new JFrame("Add a Recipe to your base");
        JPanel jPanel = new ImagePanel("pic.jpg");

        jPanel.setLayout(null);

        setPane(jPanel);
        service.setJFrame(jf,false,false,400,400,
                800,350,false,Arrays.asList(new JComponent[]{jPanel}));

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


