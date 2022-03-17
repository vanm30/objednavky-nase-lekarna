package cz.naseLekarna.gui.mainMenu;

import cz.naseLekarna.gui.application.StageController;
import cz.naseLekarna.system.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Matěj Vaník
 * @created 09.11.2021
 */
public class MenuController implements Initializable {

    private static MenuController menuController;

    public MenuController() {
        menuController = this;
    }

    public static MenuController getMenuController() {
        return menuController;
    }

    @FXML
    public VBox menuBox;
    @FXML
    public Label userName;

    Storage storage = Storage.getStorage();
    StageController stage = StageController.getStageController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userName.setText(storage.user.userName);
    }

    /**
     * Menu button takes user to Home screen.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void switchToHome(ActionEvent actionEvent) throws IOException {
        MainController.getMainController().switchToHome();
    }


    /**
     * Menu button takes user to Order History screen
     *
     * @param actionEvent
     */
    public void switchToHistory(ActionEvent actionEvent) throws IOException {
        MainController.getMainController().closeMenu();
        /*TODO*/
    }

    /**
     * Menu button takes user to Settings screen.
     * @param actionEvent
     * @throws IOException
     */
    public void switchToSettings(ActionEvent actionEvent) throws IOException {
        MainController.getMainController().switchToSettings();
    }


    public void logOut(ActionEvent actionEvent) {
        storage.user = null;
        VBox vBox = null;
        try {
            vBox = FXMLLoader.load(getClass().getResource("/fxml/login/logIn.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.mainStage.requestFocus();
        stage.mainStage.getChildren().add(vBox);

    }
}
