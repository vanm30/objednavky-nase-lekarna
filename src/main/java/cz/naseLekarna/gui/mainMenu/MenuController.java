package cz.naseLekarna.gui.mainMenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import org.apache.log4j.chainsaw.Main;

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
        MainController.getMainController().switchToHome();
    }


    /**
     * Menu button takes user to Order History screen
     * @param actionEvent
     */
    public void switchToHistory(ActionEvent actionEvent) throws IOException {
        MainController.getMainController().closeMenu();
        /*TODO*/
    }

    public void switchToSettings(ActionEvent actionEvent) throws IOException {
        MainController.getMainController().switchToSettings();
    }
}
