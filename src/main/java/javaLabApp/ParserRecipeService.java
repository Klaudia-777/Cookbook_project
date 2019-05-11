package javaLabApp;

import javax.swing.*;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParserRecipeService implements ActionListener {

    private GlobalFunctions service = new GlobalFunctions();
    private JTextField textField = new JTextField();
    private String parsedInstruction="";
    JButton parseRecipeButton = new JButton("Add");

    public String getParsedInstruction() {
        return parsedInstruction;
    }

    public ParserRecipeService checkFormatOfUrl(String urlToCheck) {
        Pattern urlPattern = Pattern.compile("https://kuchnialidla.pl/.*");

        if (urlPattern.matcher(urlToCheck).matches()) {
            try {
                parseInstructionsFromWebsite(urlToCheck);
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

    String parseRecipeNameFromWebsite(String websiteAddress) throws IOException {
        Document doc = Jsoup.connect(websiteAddress).get();
        Element title = doc.select("head[id=Head1]").get(0).select("title").get(0);
        return title.text();
    }

    List<String> parseIngridientsFromWebsite(String websiteAddress) throws IOException {
        List<String> e = Jsoup.connect(websiteAddress).get()
                .select("body")
                .select("main")
                .select("content").first()
                .select("div[id=\"details\"]")
                .select("div[class=\"skladniki\"]")
                .select("ul").first()
                .select("li").eachText();

        e.stream().forEach(System.out::println);
        return e;
    }

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
        sb.append(instructions.stream().collect(Collectors.joining("\n"))).append("\n");

        System.out.println(sb.toString()+"\n");
        parsedInstruction=sb.toString();

        return sb.toString();
    }

    void createAndShowGUIForParsedRecipe(ChooseAnOptionActionListener chooseAnOptionActionListener) {

        JFrame jf = new JFrame("Add a recipe from the website");
        GridLayout gridLayout = new GridLayout(7, 1);
        JPanel jPanel = new ImagePanel("picFromWebsite.jpg");

        jPanel.setLayout(gridLayout);

        parseRecipeButton.addActionListener(chooseAnOptionActionListener);
        service.setButtonColor(parseRecipeButton,Color.PINK);
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