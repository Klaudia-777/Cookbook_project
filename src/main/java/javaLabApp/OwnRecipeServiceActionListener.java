package javaLabApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class OwnRecipeServiceActionListener implements ActionListener {
    private Util service = new Util();
    JButton addRecipeButton = new JButton("Add recipe");

    private JTextField nameTextField = new JTextField();
    private JTextField ingridientsTextField = new JTextField();
    private JTextField instructionsTextField = new JTextField();

    void createAndShowGUIForOwnRecipe(ChooseAnOptionActionListener chooseAnOptionActionListener) {

        JFrame jf = new JFrame("Add your own recipe");
        GridLayout gridLayout = new GridLayout(7, 1);
        JPanel jPanel = new ImagePanel("picManually.jpg");
        jPanel.setLayout(gridLayout);

        JLabel nameLabel = new JLabel("Enter name:");
        JLabel ingridientsLabel = new JLabel("Enter ingridients:");
        JLabel instructionsLabel = new JLabel("Enter instructions:");

        addRecipeButton.addActionListener(chooseAnOptionActionListener);
        service.setButtonColor(addRecipeButton,Color.PINK);

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
    //    service.setJFrame(jf,true,true,true);
        jf.setSize(400, 600);

    }

    public void actionPerformed(ActionEvent e) {
        String name = nameTextField.getText();
        String ingridients = ingridientsTextField.getText();
        String instructions = instructionsTextField.getText();

        if (!name.isEmpty() && !ingridients.isEmpty() && !instructions.isEmpty()) {
            System.out.println(name + "\n" + ingridients + "\n" + instructions);
        } else {
           service.setExceptionFrame("Fill all of the fields!");
        }
    }
}
