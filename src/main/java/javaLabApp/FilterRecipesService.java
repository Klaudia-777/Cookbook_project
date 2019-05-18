package javaLabApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class FilterRecipesService implements ActionListener {
    private CookbookDBService cookbookDBService = new CookbookDBService();
    private Util service = new Util();

    private JTextField filterByCategory = new JTextField();
    private JTextField filterByIngridient = new JTextField();

    JButton filterButton = new JButton("Search");

    void createAndShowGUIForParsedRecipe(ChooseAnOptionActionListener chooseAnOptionActionListener) {

        JFrame jf = new JFrame("Filter");
        GridLayout gridLayout = new GridLayout(8, 1);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(gridLayout);

        filterButton.addActionListener(chooseAnOptionActionListener);
        service.setButtonColor(filterButton, Color.PINK);
        filterByCategory.addActionListener(chooseAnOptionActionListener);
        filterByIngridient.addActionListener(chooseAnOptionActionListener);

        JLabel labelCategory = new JLabel("Filter by category");
        JLabel labelIngridient = new JLabel("Filter by ingridient");

        service.setFontOfComponent(Arrays.asList(new JComponent[]{
                labelCategory,
                labelIngridient,
                filterByCategory,
                filterByIngridient,
                filterButton
        }));

        service.addToJPanel(jPanel, Arrays.asList(new JComponent[]{
                labelCategory,
                filterByCategory,
                labelIngridient,
                filterByIngridient,
                filterButton
        }));

        jf.getContentPane().add(jPanel, BorderLayout.CENTER);
        service.setJFrame(jf);
        jf.setSize(400, 400);
    }

    public void actionPerformed(ActionEvent e) {
        cookbookDBService.createConnection();
        cookbookDBService.createTables();
        if(!filterByCategory.getText().equals("") && filterByIngridient.getText().equals("")){
            cookbookDBService.filterRecipesByCategory(filterByCategory.getText());
        }else  if (filterByCategory.getText().equals("") && !filterByIngridient.getText().equals("")){
            cookbookDBService.filterRecipesByIngridients(filterByIngridient.getText());
        }else if(!filterByCategory.getText().equals("") && !filterByIngridient.getText().equals("")){
            cookbookDBService.filterByCategoriesAndIngridients(
                    filterByCategory.getText(),
                    filterByIngridient.getText()
            );
        }else{
            System.out.println("Fill in at least one field!");
        }
        cookbookDBService.closeConnection();
    }
}
