import javaLabApp.ParserRecipeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class ParsingInstructionsTest {
    @RunWith(Parameterized.class)
    public static class ListenerTests {
        private String input;
        private boolean expectedResult;
        private ParserRecipeService parserRecipeService;

        public ListenerTests(Pair parameters) {
            this.input = parameters.getInput();
            this.expectedResult = parameters.getExpextedResult();
        }

        @Before
        public void initialize() {
            parserRecipeService = new ParserRecipeService();
        }

        @Parameterized.Parameters
        public static Collection pressingScenarios() {
            return Arrays.asList(
                    new Pair("https://kuchnialidla.pl/koszyczki-z-ciasta-filo-z-kremem-i-owocami", true),
                    new Pair("https://kuchnialidla.pl/Warzywa-pieczone-na-grillu", true),
                    new Pair("https://kuchnialidla.pl/tosty-z-kremem-piankowym-i-orzechowym", true),
                    new Pair("https://kuchnialidla.pl/ciasto-o-smaku-coli", true),
                    new Pair("https://kuchnialidla.pl/gyros-z-jagnieciny-z-chlebkiem-pita", true),
                    new Pair("https://kuchnialidla.pl/rolada-z-boczku-z-kolorowym-pieprzem", true),
                    new Pair("https://kuchnialidla.pl/tosty-z-kremem-piankowym-i-orzechowym", true),
                    new Pair("https://kuchnialidla.pl/sniadaniowy-chlebek-marchewkowy", true));
        }

        @Test
        public void testMyActionListenerScenarios() throws IOException {
            Assertions.assertEquals(expectedResult, !parserRecipeService.parseRecipeNameFromWebsite(input).equals(""));
        }
    }
}
