package javaLabApp;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;


public class FinalRecipeViewActionListener implements ActionListener {
    private CookbookDBService cookbookDBService = new CookbookDBService();
    private Util service = new Util();

    String encoder(String url) {
        StringBuilder result = new StringBuilder();
        int iterator = url.length() - 1;
        while (url.charAt(iterator) != '/') {
            iterator--;
        }
        result.append(url.substring(0, iterator));
        url = url.substring(iterator);

        try {
            result.append(URLEncoder.encode(url, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

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
            topJPanel = new ImageFromURlPanel(new URL(encoder(cookbookDBService.filterByName(name).get(2))));
            System.out.println(encoder(cookbookDBService.filterByName(name).get(2)));

            topJPanel.setSize(600, 350);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }


        jf.add(nameLabel, BorderLayout.PAGE_START);
        jf.add(label, BorderLayout.WEST);
        jf.add(ingridientsTextArea, BorderLayout.WEST);
        jf.add(topJPanel, BorderLayout.CENTER);
        jf.add(instructionsTextArea, BorderLayout.AFTER_LAST_LINE);

        service.setJFrame(jf, true, true, 0, 0,
                0, 0, true, Arrays.asList(new JComponent[]{}));
    }
}
