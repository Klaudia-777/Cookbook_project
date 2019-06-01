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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


public class FinalRecipeViewActionListener implements ActionListener {
    private CookbookDBService cookbookDBService;
    private Util service = new Util();
    private JFrame jf = new JFrame("Recipe");
    private JTabbedPane tabbedPane = new JTabbedPane();
    private boolean isJFrameOpened = false;
    private String passedUrl="";

    FinalRecipeViewActionListener(CookbookDBService cookbookDBService) {
        this.cookbookDBService = cookbookDBService;
    }

    boolean checkedIfOpened(JTabbedPane tabbedPane, String name) {

        for (int i = 0; i < tabbedPane.getComponentCount() - 1; i++) {
            if (tabbedPane.getTitleAt(i).equals(name))
                return true;
        }
        return false;
    }


    void addCloseButton(JTabbedPane tabbedPane, JLabel nameLabel) {
        int index = tabbedPane.indexOfTab(nameLabel.getText());
        JPanel pnlTab = new JPanel(new GridBagLayout());
        pnlTab.setOpaque(false);
        JLabel lblTitle = new JLabel(nameLabel.getText());
        JButton btnClose = new JButton("x");
        btnClose.setBackground(Color.RED.brighter());
        btnClose.setFont(new Font("Arial", Font.PLAIN, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        pnlTab.add(lblTitle, gbc);

        gbc.gridx++;
        gbc.weightx = 0;
        pnlTab.add(btnClose, gbc);

        tabbedPane.setTabComponentAt(index, pnlTab);
        btnClose.addActionListener(new MyCloseActionHandler(tabbedPane));
    }

    String refactorName(String name) {
        name = name.replace("• Kuchnia Lidla", " ");
        return name;
    }

    String refactorSpaces(String url) {
        url.replace(" ", "%20");
        return url;
    }

    String encoder(String url) {
        passedUrl = url;
        url = refactorSpaces(url);
        StringBuilder result = new StringBuilder();
        int iterator = url.length() - 1;
        while (url.charAt(iterator) != '/') {
            iterator--;
        }
        iterator++;
        result.append(url.substring(0, iterator));
        url = url.substring(iterator);

        try {
            result.append(URLEncoder.encode(url, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Logging.getLogger().error(e.getMessage());
        }
        if(!result.toString().equals(passedUrl)){
            Logging.getLogger().info("Image URL encoded"+"\n"+ "from:"+passedUrl+ "\n"+"to:" + result.toString());
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
        Logging.getLogger().info("Instructions text refactored.");
        return edited.toString();
    }


    void addPane() {
        service.setJFrame(jf, true, false, 900, 800,
                0, 0, true, tabbedPane);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        Logging.getLogger().info("Dish recipe chosen.");
        Logging.getLogger().info("Main recipe tab-frame opened.");

        if (!isJFrameOpened) {
            addPane();
            isJFrameOpened = true;
        }

        String name = e.getActionCommand();

        cookbookDBService.createConnection();
        cookbookDBService.createTables();

        JLabel nameLabel = new JLabel(refactorName(cookbookDBService.filterByName(e.getActionCommand()).get(0)));
        JLabel label = new JLabel(" ");
        JLabel imageLabel = new JLabel();

        try {
            imageLabel.setIcon(new ImageIcon(ImageIO.read(new URL(encoder(cookbookDBService.filterByName(e.getActionCommand())
                    .get(2)))).getScaledInstance(600, 350, Image.SCALE_DEFAULT)));
        } catch (IOException e1) {
            Logging.getLogger().error(e1.getMessage());
        }

        service.setFontOfComponent(Arrays.asList(nameLabel, label));

        List<String> remembered = new ArrayList<>();
        String str = "";

        StringBuilder ingridients = new StringBuilder();
        int noIngridients = cookbookDBService.filterByName(name).size() - 4;
        ingridients.append("\nSkładniki:\n");
        for (int i = 0; i < noIngridients; i++) {
            str = cookbookDBService.filterByName(name).get(4 + i);
            if (!remembered.contains(str)) {
                ingridients.append(cookbookDBService.filterByName(name).get(4 + i)).append("\n");
                remembered.add(str);
            } else break;
        }

        JTextArea ingridientsTextArea = new JTextArea(ingridients.toString());
        JTextArea instructionsTextArea = new JTextArea("\nWykonanie:\n" + editInstructions(cookbookDBService
                .filterByName(e.getActionCommand()).get(3)));


        ingridientsTextArea.setEditable(false);
        instructionsTextArea.setEditable(false);

        JPanel jPanel = new JPanel();
        JPanel jPanelInternal = new JPanel();

        GridLayout gridLayout = new GridLayout(1, 2);
        jPanelInternal.setLayout(gridLayout);

        jPanel.add(nameLabel, BorderLayout.PAGE_START);

        jPanelInternal.add(imageLabel);
        jPanelInternal.add(ingridientsTextArea);

        jPanel.add(jPanelInternal, BorderLayout.CENTER);
        jPanel.add(instructionsTextArea, BorderLayout.AFTER_LAST_LINE);

        if (!checkedIfOpened(tabbedPane, nameLabel.getText())) {
            tabbedPane.addTab(nameLabel.getText(), jPanel);
            addCloseButton(tabbedPane, nameLabel);
        }

    }
}
