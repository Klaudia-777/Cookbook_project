package javaLabApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class OwnRecipeService implements ActionListener {
    private GlobalFunctions service = new GlobalFunctions();
    JButton addRecipeButton = new JButton("Add recipe");

    private JTextField nameTextField = new JTextField();
    private JTextField ingridientsTextField = new JTextField();
    private JTextField instructionsTextField = new JTextField();

    void createAndShowGUIForOwnRecipe(ChooseAnOptionActionListener chooseAnOptionActionListener) {

        JFrame jf = new JFrame("Add your own recipe");
        GridLayout gridLayout = new GridLayout(7, 1);
        JPanel jPanel = new JPanel(gridLayout);

        JLabel nameLabel = new JLabel("Enter name:");
        JLabel ingridientsLabel = new JLabel("Enter ingridients:");
        JLabel instructionsLabel = new JLabel("Enter instructions:");

        addRecipeButton.addActionListener(chooseAnOptionActionListener);

        nameTextField.addActionListener(chooseAnOptionActionListener);
        ingridientsTextField.addActionListener(chooseAnOptionActionListener);
        instructionsTextField.addActionListener(chooseAnOptionActionListener);

        List<JComponent> components = Arrays.asList(new JComponent[]{
                nameLabel,
                nameTextField,
                ingridientsLabel,
                ingridientsTextField,
                instructionsLabel,
                instructionsTextField,
                addRecipeButton
        });

        service.addToJPanel(jPanel, components);
        service.setFontOfComponent(components);

        jf.getContentPane().add(jPanel);
        service.setJFrame(jf);
        jf.setSize(400, 600);

    }

    public void actionPerformed(ActionEvent e) {
        String name = nameTextField.getText();
        String ingridients = ingridientsTextField.getText();
        String instructions = instructionsTextField.getText();

        if (!name.isEmpty() && !ingridients.isEmpty() && !instructions.isEmpty()) {
            System.out.println(name + "\n" + ingridients + "\n" + instructions);
        } else {
            JFrame fillAllFieldsFrame = new JFrame();

            JLabel label = new JLabel("You need to fill all of the fields!");
            label.setForeground(Color.red);
            service.setFont(label);

            fillAllFieldsFrame.getContentPane().add(label);
            fillAllFieldsFrame.setSize(300, 100);
            service.setJFrame(fillAllFieldsFrame);
        }
    }
}
