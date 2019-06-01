package javaLabApp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ChooseAnOptionActionListener implements ActionListener {
    private ParserRecipeServiceActionListener parserRecipeService = new ParserRecipeServiceActionListener();
    private FilterRecipesActionListener filterRecipesService = new FilterRecipesActionListener();

    ChooseAnOptionActionListener(InitView initView) {
        InitView initView1 = initView;
    }

    public void actionPerformed(ActionEvent e) {
        final String pressedButton = e.getActionCommand();
        if ("<html>Add recipe<br />from the website</html>".equals(pressedButton)) {
            Logging.getLogger().info("Add recipe button pressed");
            parserRecipeService.createAndShowGUIForParsedRecipe(this);
            parserRecipeService.parseRecipeButton.addActionListener(parserRecipeService);
        }
        else if ("<html>Filter</html>".equals(pressedButton)) {
            Logging.getLogger().info("Filter recipes button pressed");
            filterRecipesService.createAndShowGUIForParsedRecipe(this);
            filterRecipesService.filterButton.addActionListener(filterRecipesService);
        }
    }
}
