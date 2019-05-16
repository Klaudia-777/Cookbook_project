package javaLabApp;

import javax.swing.*;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParserRecipeService implements ActionListener {
    private ConnectingDBAndInsertingData connectingDBAndInsertingData = new ConnectingDBAndInsertingData();

    private Util service = new Util();
    private JTextField textField = new JTextField();
    JButton parseRecipeButton = new JButton("Add");

    private String parsedInstruction = "";
    private String parsedCategory = "";
    private String parsedImageUrl = "";

    public String getParsedInstruction() {
        return parsedInstruction;
    }

    public String getParsedCategory() {
        return parsedCategory;
    }

    public String getParsedImageUrl() {
        return parsedImageUrl;
    }


    public ParserRecipeService checkFormatOfUrl(String urlToCheck) {
        Pattern urlPattern = Pattern.compile("https://kuchnialidla.pl/.*");

        if (urlPattern.matcher(urlToCheck).matches()) {
            try {
                connectingDBAndInsertingData.createConnection();
                connectingDBAndInsertingData.insertData(Arrays.asList(

                        parseRecipeNameFromWebsite(urlToCheck),
                        parseCategory(urlToCheck),
                        parseImageUrl(urlToCheck),
                        parseInstructionsFromWebsite(urlToCheck)));

                connectingDBAndInsertingData.readData();
                connectingDBAndInsertingData.closeConnection();


                //parseIngridientsFromWebsite(urlToCheck);

            } catch (HttpStatusException h) {
                service.setExceptionFrame("URL doesn't exist!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                throw (new WrongUrlFormatException("Wrong URL Format!"));
            } catch (WrongUrlFormatException e) {
                e.handleException();
            }
        }
        return this;
    }

    /**
     * PARSING NAME
     */

    String parseRecipeNameFromWebsite(String websiteAddress) throws IOException {
        String title = Jsoup.connect(websiteAddress)
                .get()
                .select("head").first()
                .select("title").first()
                .text();
//        System.out.println(title);
        return title;
    }

    /**
     * PARSING CATEGORY
     */

    String parseCategory(String websiteAddress) throws IOException {
        parsedCategory = "";
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
        parsedCategory = category;
//        System.out.println(category);
        return category;
    }

    /**
     * PARSING INGRIDIENTS
     */

    List<String> parseIngridientsFromWebsite(String websiteAddress) throws IOException {
        List<String> e = Jsoup.connect(websiteAddress).get()
                .select("body")
                .select("main")
                .select("content").first()
                .select("div[id=\"details\"]")
                .select("div[class=\"skladniki\"]")
                .select("ul")
                .stream()
                .flatMap(n -> n.select("li").stream())
                .map(Element::text)
                .collect(Collectors.toList());   // zajebioza, kazdy skladnik osobno


//        e.forEach(System.out::println);
        return e;
    }

    /**
     * PARSING INSTRUCTIONS
     */

    String parseInstructionsFromWebsite(String websiteAddress) throws IOException {
        parsedInstruction = "";
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
        parsedInstruction = sb.toString();

        return sb.toString();
    }

    /**
     * PARSING IMAGE URL
     */

    String parseImageUrl(String websiteAddress) throws IOException {
        parsedImageUrl = "";
        String imageUrl = Jsoup.connect(websiteAddress).get()
                .select("body")
                .select("main")
                .select("content").first()
                .select("div").first()
                .select("div").first()
                .select("img").attr("abs:src");
        parsedImageUrl = imageUrl;
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

        jf.getContentPane().add(jPanel, BorderLayout.CENTER);
        service.setJFrame(jf);
        jf.setSize(400, 400);
    }

    public void actionPerformed(ActionEvent e) {
        checkFormatOfUrl(textField.getText());
    }
}