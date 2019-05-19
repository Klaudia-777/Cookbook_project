package javaLabApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class FilterRecipesActionListener implements ActionListener {
    private CookbookDBService cookbookDBService = new CookbookDBService();
    private Util service = new Util();
    private JFrame jf = new JFrame("Filter");

    JButton filterButton = new JButton("Search");
    private JTextField filterByIngridient = new JTextField();
    private JComboBox<String> filterByCategory = new JComboBox<>(new String[]{"dania-glowne",
            "zupy",
            "salatki",
            "napoje",
            "przetwory",
            "sniadania",
            "fast-food",
            "przekaski",
            "ciasta",
            "ciastka",
            "desery"});


    /**
     *  CREATING GUI FOR FILTERING FRAME
     */


    void createAndShowGUIForParsedRecipe(ChooseAnOptionActionListener chooseAnOptionActionListener) {

        GridLayout gridLayout = new GridLayout(8, 1);
        JPanel jPanel =new ImagePanel("picFromWebsite.jpg");
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

    /**
     *  PERFORMING ACTION FROM FILTERS
     */

    public void actionPerformed(ActionEvent e) {
        cookbookDBService.createConnection();
        cookbookDBService.createTables();
        String category = filterByCategory.getSelectedItem().toString();

        if(!category.equals("") && filterByIngridient.getText().equals("")){
            cookbookDBService.filterRecipesByCategory(category);
//        }else  if (category.equals("") && !filterByIngridient.getText().equals("")){
//            cookbookDBService.filterRecipesByIngridients(category);
        }else if(!category.equals("") && !filterByIngridient.getText().equals("")){
            cookbookDBService.filterByCategoriesAndIngridients(
                    category,
                    filterByIngridient.getText()
            );
        }else{
            service.setExceptionFrame("You need to fill at least one field!");
        }
        cookbookDBService.closeConnection();

    }
}
