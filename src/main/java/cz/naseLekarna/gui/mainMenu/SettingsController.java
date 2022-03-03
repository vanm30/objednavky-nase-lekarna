package cz.naseLekarna.gui.mainMenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * @author Matěj Vaník
 * @created 02.03.2022
 */
public class SettingsController {

    private static SettingsController settingsController;

    public SettingsController() {
        settingsController = this;
    }

    public static SettingsController getSettingsController() {
        return settingsController;
    }


    public void save(ActionEvent actionEvent) throws IOException {

        //TODO - save settings for specific user

        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/homeView.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
        MainController.getMainController().mainLabel.setText("Aktivní Objednávky");
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/homeView.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
        MainController.getMainController().mainLabel.setText("Aktivní Objednávky");
    }
}
