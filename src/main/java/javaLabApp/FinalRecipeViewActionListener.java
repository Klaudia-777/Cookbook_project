package javaLabApp;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;


public class FinalRecipeViewActionListener implements ActionListener {
    private CookbookDBService cookbookDBService = new CookbookDBService();
    private Util service = new Util();


    String editInstructions(String instructions) {
        StringBuilder edited = new StringBuilder();
        int count = 0;
        for (int i = 0; i < instructions.length(); i++) {
            if (count >= 130 && instructions.charAt(i) == ' ') {
                edited.append("\n");
                count = 0;
            }
            edited.append(instructions.charAt(i));
            count++;
        }
        return edited.toString();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String name = e.getActionCommand();

        JFrame jf = new JFrame("Recipe");
        jf.pack();
        jf.setVisible(true);
        jf.setSize(600, 800);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.setResizable(true);

        JPanel topJPanel = null;

        cookbookDBService.createConnection();
        cookbookDBService.createTables();

        JLabel nameLabel = new JLabel(cookbookDBService.filterByName(e.getActionCommand()).get(0));
        JLabel label = new JLabel(" ");
        service.setFontOfComponent(Arrays.asList(nameLabel, label));


        StringBuilder ingridients = new StringBuilder();
        int noIngridients = cookbookDBService.filterByName(name).size() - 4;
        ingridients.append("\nSkładniki:\n");
        for (int i = 0; i < noIngridients; i++) {
            ingridients.append(cookbookDBService.filterByName(name).get(4 + i) + "\n");
        }

        JTextArea ingridientsTextArea = new JTextArea(ingridients.toString());
        JTextArea instructionsTextArea = new JTextArea("\nWykonanie:\n" + editInstructions(cookbookDBService.filterByName(e.getActionCommand()).get(3)));


        ingridientsTextArea.setEditable(false);
        instructionsTextArea.setEditable(false);

        try {
            topJPanel = new ImageFromURlPanel(new URL(cookbookDBService.filterByName(name).get(2)));
            topJPanel.setSize(600, 350);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        jf.setBackground(Color.PINK);
        jf.add(nameLabel, BorderLayout.PAGE_START);
        jf.add(label, BorderLayout.WEST);
        jf.add(ingridientsTextArea, BorderLayout.WEST);
        jf.add(topJPanel, BorderLayout.CENTER);
        jf.add(instructionsTextArea, BorderLayout.AFTER_LAST_LINE);
        jf.setLocation(0, 0);

//        for (String str:cookbookDBService.filterByName(e.getActionCommand())) {
//            System.out.println(str);
//        }

    }
}
