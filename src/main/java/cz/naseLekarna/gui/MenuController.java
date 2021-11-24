package cz.naseLekarna.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

/**
 * @author Matěj Vaník
 * @created 09.11.2021
 */
public class MenuController {


    private static MenuController menuController;
    @FXML public VBox menuBox;

    /*CONSTRUCTOR, GETTERS, SETTERS*/
    public MenuController(){
        menuController = this;
    }

    public static MenuController getMenuController() {
        return menuController;
    }



    public void switchToHome(ActionEvent actionEvent) {
        /*TODO*/
    }


    public void switchToHistory(ActionEvent actionEvent) {
        /*TODO*/
    }

    /**
     * This method closes popup menu by calling method closeMenu().
     * @param actionEvent
     * @throws Exception
     */
    public void goBack(ActionEvent actionEvent) throws Exception{
        MainController.getMainController().closeMenu();
    }
}
