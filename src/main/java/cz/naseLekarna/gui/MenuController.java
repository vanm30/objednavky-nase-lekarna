package cz.naseLekarna.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

/**
 * @author Matěj Vaník
 * @created 09.11.2021
 */
public class MenuController {

    private static MenuController menuController;

    public MenuController(){
        menuController = this;
    }

    public static MenuController getMenuController() {
        return menuController;
    }

    public VBox menuBox;

    public void switchToHome(ActionEvent actionEvent) {
    }

    public void switchToHistory(ActionEvent actionEvent) {
    }

    public void goBack(ActionEvent actionEvent) throws Exception{
        MainController.getMainController().closeMenu();
    }
}
