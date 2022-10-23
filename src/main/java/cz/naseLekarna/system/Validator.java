package cz.naseLekarna.system;

import cz.naseLekarna.gui.application.InfoBoxController;
import cz.naseLekarna.gui.application.StageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Matěj Vaník
 * @created 06.03.2022
 */
public class Validator {


    /**
     * Method checks if String is number.
     *
     * @param string Any string.
     * @return True if is number, false if not.
     */
    public static boolean isNumeric(String string) {
        int intValue;

        if (string == null || string.equals("")) {
            return false;
        }

        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    /**
     * Method checks if String is Alphanumeric.
     *
     * @param string Any string
     * @return True if is alphanumeric, false if not.
     */
    public static boolean isAlphaNumeric(String string) {
        return string != null && string.matches("^[a-žA-Ž0-9]*$");
    }

    public static boolean isJustLetters(String string) {
        return string != null && string.matches("^[a-žA-Ž]*$");
    }

    /**
     * Method checks if String is Alphanumeric and can contain white space.
     *
     * @param string Any string
     * @return True if is alphanumeric, false if not.
     */
    public static boolean isAlphaNumericWithSpace(String string) {
        return string != null && string.matches("^[a-žA-Ž0-9 ]*$");
    }

    public static String sanitizeHTML(String untrustedHTML){
        PolicyFactory policy = new HtmlPolicyBuilder()
                .allowAttributes("src").onElements("img")
                .allowAttributes("href").onElements("a")
                .allowStandardUrlProtocols()
                .allowElements(
                        "a", "img"
                ).toFactory();

        return policy.sanitize(untrustedHTML);
    }

    public void displayError(ArrayList<String> mistakes) throws IOException {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("/fxml/application/infoBox.fxml"));
        StageController.getStageController().mainStage.getChildren().add(gridPane);
        mistakes.forEach(mistake -> {
            Label label = new Label();
            label.setWrapText(true);
            label.setPrefWidth(500);
            label.setText(mistake);
            InfoBoxController.getInfoBoxController().errorBox.getChildren().add(label);
        });
    }

}
