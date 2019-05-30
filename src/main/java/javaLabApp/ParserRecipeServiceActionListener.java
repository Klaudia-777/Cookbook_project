package javaLabApp;

import javax.swing.*;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParserRecipeServiceActionListener implements ActionListener {
    private CookbookDBService cookbookDBService = new CookbookDBService();
    private List<String> parsedRecipes = new ArrayList<>();
    private List<String> usedUrls = new ArrayList<>();
    private Util service = new Util();
    private JTextField textField = new JTextField();
    JButton parseRecipeButton = new JButton("Add");

    /**
     * INSERTING RECIPE TO DATABASE
     * FROM URL
     */

    private ParserRecipeServiceActionListener insertRecipeToDB(String urlToCheck) {
        Pattern urlPattern = Pattern.compile("https://kuchnialidla.pl/.*");

        if (urlPattern.matcher(urlToCheck).matches()) {

            try {
//                parseRecipeNameFromWebsite(urlToCheck);
//                parseCategory(urlToCheck);

                cookbookDBService.createConnection();
//                cookbookDBService.dropTables();
                cookbookDBService.createTables();

                cookbookDBService.insertDataIntoRecipesTable(Arrays.asList(
                        parseRecipeNameFromWebsite(urlToCheck),
                        parseCategory(urlToCheck),
                        parseImageUrl(urlToCheck),
                        parseInstructionsFromWebsite(urlToCheck)));
                if (!usedUrls.contains(urlToCheck)) {
                    for (String ingridient : parseIngridientsFromWebsite(urlToCheck)) {
                        cookbookDBService.insertDataIntoIngridientsTable(Arrays.asList(
                                ingridient,
                                parseRecipeNameFromWebsite(urlToCheck)
                        ));
                    }
                }
//              cookbookDBService.dropRow();


                cookbookDBService.closeConnection();
                usedUrls.add(urlToCheck);

            } catch (HttpStatusException h) {
                service.setExceptionFrame("URL doesn't exist!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            service.setExceptionFrame("Wrong format of URL!");
        }
        return this;
    }

    /**
     * PARSING NAME
     */

    public String parseRecipeNameFromWebsite(String websiteAddress) throws IOException {
        String title = Jsoup.connect(websiteAddress)
                .get()
                .select("head").first()
                .select("title").first()
                .text();

        return title;
    }

    /**
     * PARSING CATEGORY
     */

    String parseCategory(String websiteAddress) throws IOException {
        String category = Jsoup.connect(websiteAddress).get()
                .select("body")
                .select("main")
                .select("content").first()
                .select("div[id=details]")
                .select("div[class=lead]")
                .select("ul").first()
                .select("li")
                .get(1)
                .select("a")
                .attr("abs:href")
                .substring(33);
        category = category.substring(0, category.indexOf('/'));
        return category;
    }

    /**
     * PARSING INGRIDIENTS
     */

    List<String> parseIngridientsFromWebsite(String websiteAddress) throws IOException {
        List<String> ingridients = Jsoup.connect(websiteAddress).get()
                .select("body")
                .select("main")
                .select("content").first()
                .select("div[id=\"details\"]")
                .select("div[class=\"skladniki\"]")
                .select("ul")
                .stream()
                .flatMap(n -> n.select("li").stream())
                .map(Element::text)
                .collect(Collectors.toList());

        return ingridients;
    }

    /**
     * PARSING INSTRUCTIONS
     */

    String parseInstructionsFromWebsite(String websiteAddress) throws IOException {
        List<String> instructions = Jsoup.connect(websiteAddress).get()
                .select("body")
                .select("main")
                .select("content").first()
                .select("div[class=recipe_inner right]")
                .select("div[class=recipe_main]")
                .select("div[id=opis]")
                .select("p").eachText();

        StringBuilder sb = new StringBuilder();
        sb.append(String.join("\n", instructions)).append("\n");

//        System.out.println(sb.toString() + "\n");

        return sb.toString();
    }

    /**
     * PARSING IMAGE URL
     */

    String parseImageUrl(String websiteAddress) throws IOException {
        String imageUrl = Jsoup.connect(websiteAddress).get()
                .select("body")
                .select("main")
                .select("content").first()
                .select("div").first()
                .select("div").first()
                .select("img").attr("abs:src");

        parsedRecipes.add(imageUrl);
        System.out.println(parsedRecipes.get(0));
//        System.out.println(imageUrl);
        return imageUrl;
    }

    void createAndShowGUIForParsedRecipe(ChooseAnOptionActionListener chooseAnOptionActionListener) {

        JFrame jf = new JFrame("Add a recipe from the website");
        GridLayout gridLayout = new GridLayout(7, 1);
        JPanel jPanel = new ImagePanel("picFromWebsite.jpg");

        jPanel.setLayout(gridLayout);

        parseRecipeButton.addActionListener(chooseAnOptionActionListener);
        service.setButtonColor(parseRecipeButton, Color.PINK);
        textField.addActionListener(chooseAnOptionActionListener);

        JLabel label = new JLabel("Enter the website address:");

        service.setFontOfComponent(Arrays.asList(new JComponent[]{
                parseRecipeButton,
                label,
                textField}));

        service.addToJPanel(jPanel, Arrays.asList(new JComponent[]{
                label,
                textField,
                parseRecipeButton
        }));

        service.setJFrame(jf, true, false, 400, 400,
                800, 350, false, Arrays.asList(new JComponent[]{jPanel}));
    }

    public void actionPerformed(ActionEvent e) {

        insertRecipeToDB(textField.getText());
//
//        List<String> lista= Arrays.asList(
//            "https://kuchnialidla.pl/Warzywa-pieczone-na-grillu",
//            "https://kuchnialidla.pl/koszyczki-z-ciasta-filo-z-kremem-i-owocami",
//            "https://kuchnialidla.pl/tosty-z-kremem-piankowym-i-orzechowym",
//            "https://kuchnialidla.pl/ciasto-o-smaku-coli",
//            "https://kuchnialidla.pl/gyros-z-jagnieciny-z-chlebkiem-pita",
//            "https://kuchnialidla.pl/rolada-z-boczku-z-kolorowym-pieprzem",
//            "https://kuchnialidla.pl/lody-w-miseczce-z-churros",
//            "https://kuchnialidla.pl/sniadaniowy-chlebek-marchewkowy",
//            "https://kuchnialidla.pl/burger-z-fasoli-z-kremem-z-mango",
//            "https://kuchnialidla.pl/kaczka-w-sosie-zurkowym",
//            "https://kuchnialidla.pl/koktajle-na-detoks",
//            "https://kuchnialidla.pl/lemoniada-z-woda-kokosowa",
//            "https://kuchnialidla.pl/smoothie-z-kiwi-i-ogorkow",
//            "https://kuchnialidla.pl/brzoskwiniowo-bazyliowe-smoothie",
//            "https://kuchnialidla.pl/chlebki-warzywne-w-trzech-kolorach",
//            "https://kuchnialidla.pl/sniadaniowy-chlebek-marchewkowy",
//            "https://kuchnialidla.pl/burgery-rybne-z-salatka-coleslaw",
//            "https://kuchnialidla.pl/burger-z-sandaczem-z-sosem-z-kaparow",
//            "https://kuchnialidla.pl/maslo-kasztanowe",
//            "https://kuchnialidla.pl/stek-wolowy-z-maslem-paryskim",
//            "https://kuchnialidla.pl/guacamole-z-pomidorami",
//            "https://kuchnialidla.pl/weganski-sos-tatarski",
//            "https://kuchnialidla.pl/pasztet-z-jelenia-i-mus-jablkowo-chrzanowy"
//    );
//        for (String url:lista) {
//            insertRecipeToDB(url);
//        }
        //   cookbookDBService.dropRow();
    }
}