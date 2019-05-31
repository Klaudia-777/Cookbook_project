package javaLabApp;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;


public class FinalRecipeViewActionListener implements ActionListener {
    private CookbookDBService cookbookDBService = new CookbookDBService();
    private Util service = new Util();
    private JPanel jPanel = new JPanel();
    private JPanel jPanel1 = new JPanel();
    private boolean isJFrameOpened = false;

    String refactorSpaces(String url) {
        url.replace(" ", "%20");
        return url;
    }


    String encoder(String url) {
        url = refactorSpaces(url);
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

    JFrame jf = new JFrame("Recipe");
    JTabbedPane tabbedPane = new JTabbedPane();

    void addPane(){
        jf.add(tabbedPane);
        service.setJFrame(jf, true, false, 900, 800,
                0, 0, true, Arrays.asList(new JComponent[]{}));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!isJFrameOpened){
            addPane();
            isJFrameOpened=true;
        }
        String name = e.getActionCommand();
        JPanel topJPanel = null;

        cookbookDBService.createConnection();
        cookbookDBService.createTables();

        JLabel nameLabel = new JLabel(cookbookDBService.filterByName(e.getActionCommand()).get(0));
        JLabel label = new JLabel(" ");
        JLabel imageLabel = new JLabel();

        try {
            imageLabel.setIcon(new ImageIcon(ImageIO.read(new URL(cookbookDBService.filterByName(e.getActionCommand())
                    .get(2))).getScaledInstance(600, 350, Image.SCALE_DEFAULT)));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        service.setFontOfComponent(Arrays.asList(nameLabel, label));


        StringBuilder ingridients = new StringBuilder();
        int noIngridients = cookbookDBService.filterByName(name).size() - 4;
        ingridients.append("\nSkładniki:\n");
        for (int i = 0; i < noIngridients; i++) {
            ingridients.append(cookbookDBService.filterByName(name).get(4 + i)).append("\n");
        }

        JTextArea ingridientsTextArea = new JTextArea(ingridients.toString());
        JTextArea instructionsTextArea = new JTextArea("\nWykonanie:\n" + editInstructions(cookbookDBService
                .filterByName(e.getActionCommand()).get(3)));


        ingridientsTextArea.setEditable(false);
        instructionsTextArea.setEditable(false);


        try {
            topJPanel = new ImageFromURlPanel(new URL(encoder(cookbookDBService.filterByName(name).get(2))));
            System.out.println(encoder(cookbookDBService.filterByName(name).get(2)));

            topJPanel.setSize(600, 350);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        GridLayout gridLayout = new GridLayout(1, 2);
        jPanel1.setLayout(gridLayout);

        assert topJPanel != null;
        jPanel.add(nameLabel, BorderLayout.PAGE_START);

        jPanel1.add(imageLabel);
        jPanel1.add(ingridientsTextArea);

        jPanel.add(jPanel1, BorderLayout.CENTER);
        jPanel.add(instructionsTextArea, BorderLayout.AFTER_LAST_LINE);

        tabbedPane.addTab(nameLabel.getText(), jPanel);

    }
}
