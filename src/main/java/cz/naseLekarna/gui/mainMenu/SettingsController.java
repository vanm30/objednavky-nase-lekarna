package cz.naseLekarna.gui.mainMenu;

import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.chrono.MinguoChronology;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

/**
 * @author Matěj Vaník
 * @created 02.03.2022
 */
public class SettingsController implements Initializable {

    private static SettingsController settingsController;

    public SettingsController() {
        settingsController = this;
    }

    public static SettingsController getSettingsController() {
        return settingsController;
    }

    @FXML
    public CheckBox autoUserSave;

    Storage storage = Storage.getStorage();
    FirebaseService firebaseService = new FirebaseService();
    MainController mainController = MainController.getMainController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set visibility
        mainController.searchButton.setVisible(false);
        mainController.searchBar.setVisible(false);

        if (storage.user.settings.get(0).equals("1")){
            autoUserSave.setSelected(true);
        }
    }

    public void save(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {

        //TODO - save settings for specific user
        ArrayList<String> settings = new ArrayList<String>();
        if (autoUserSave.isSelected()){
            settings.add(0,"1");
        } else settings.add(0, "0");
        storage.user.settings.clear();
        storage.user.settings = settings;
        Map<String, Object> docData = new HashMap<>();
        docData.put("username",storage.user.userName);
        docData.put("settings",storage.user.settings);

        firebaseService.updateSettings(docData);

        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/homeView.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
        MainController.getMainController().mainLabel.setText("Aktivní objednávky");
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/homeView.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
        MainController.getMainController().mainLabel.setText("Aktivní objednávky");
    }

    public void changePassword(ActionEvent actionEvent) throws IOException {
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/changePassword.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
        MainController.getMainController().mainLabel.setText("Změna hesla");
    }
}
