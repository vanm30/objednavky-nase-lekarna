package cz.naseLekarna.controllers.mainMenu;

import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
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
    @FXML
    public Button userList;
    @FXML
    public VBox adminTab;

    Storage storage = Storage.getStorage();
    FirebaseService firebaseService = new FirebaseService();
    MainController mainController = MainController.getMainController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainController.mainLabel.setText("Nastavení");
        //set visibility
        mainController.searchButton.setVisible(false);
        mainController.searchBar.setVisible(false);

        if (Integer.parseInt(String.valueOf(storage.user.settings.get("autoSave"))) == 1){
            autoUserSave.setSelected(true);
        }
        if (!storage.user.getRole().equals("admin")){
            adminTab.setVisible(false);
            userList.setDisable(true);
        }
    }

    public void save(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {
        Map<String, Object> settings = new HashMap<>();
        if (autoUserSave.isSelected()){
            settings.put("autoSave",1);
        } else settings.put("autoSave",0);
        storage.user.settings.clear();
        storage.user.settings = settings;
        Map<String, Object> docData = new HashMap<>();
        docData.put("username",storage.user.userName);
        docData.put("settings",storage.user.settings);

        firebaseService.updateSettings(docData);

        mainController.changeScene("mainMenu/homeView");
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        mainController.changeScene("mainMenu/homeView");
    }

    public void changePassword(ActionEvent actionEvent) throws IOException {
        mainController.changeScene("mainMenu/changePassword");
    }

    public void usersList(ActionEvent actionEvent) throws IOException {
        mainController.changeScene("lists/userList");
    }
}
