package cz.naseLekarna.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * @author Matěj Vaník
 * @created 09.11.2021
 */
public class MenuController {

    private static MenuController menuController;

    @FXML
    public VBox menuBox;


    public MenuController() {
        menuController = this;
    }

    public static MenuController getMenuController() {
        return menuController;
    }

    /**
     * Menu button takes user to Home screen.
     * @param actionEvent
     * @throws IOException
     */
    public void switchToHome(ActionEvent actionEvent) throws IOException {
        MainController.getMainController().closeMenu();
        MainController.getMainController().mainStackPane.getChildren().clear();
        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/homeView.fxml"));
        MainController.getMainController().mainStackPane.getChildren().add(vBox);
        MainController.getMainController().mainLabel.setText("Aktivní Objednávky");
    }


    /**
     * Menu button takes user to Order History screen
     * @param actionEvent
     */
    public void switchToHistory(ActionEvent actionEvent) {
        MainController.getMainController().closeMenu();
        /*TODO*/
    }

    /**
     * This method closes popup menu by calling method closeMenu().
     *
     * @param actionEvent
     * @throws Exception
     */
    public void goBack(ActionEvent actionEvent) throws Exception {
        MainController.getMainController().closeMenu();
    }
}
